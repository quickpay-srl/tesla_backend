package bo.com.tesla.pagos.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.administracion.entity.PServicioProductoEntity;
import bo.com.tesla.entidades.services.IEntidadService;
import bo.com.tesla.pagos.services.IPServicioProductosService;
import bo.com.tesla.recaudaciones.services.IRecaudadoraService;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.cross.Util;

@RestController
@RequestMapping("api/servicioProductos")
public class ServicioProductoController {
	private Logger logger = LoggerFactory.getLogger(ServicioProductoController.class);

	@Autowired
	private ISegUsuarioService segUsuarioService;

	@Autowired
	private IPServicioProductosService servicioProductosService;

	@Autowired
	private IEntidadService entidadService;

	@Autowired
	private ILogSistemaService logSistemaService;

	@Autowired
	private IRecaudadoraService recaudadoraService;

	@Value("${tesla.path.logos.abonos}")
	private String fileAbonosLogo;

	@GetMapping("/findById/{servicioProductoId}")
	public ResponseEntity<?> findById(@PathVariable("servicioProductoId") Long servicioProductoId,
			Authentication authentication) {
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		Optional<PServicioProductoEntity> servicioProducto;
		Map<String, Object> response = new HashMap<>();
		try {
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			servicioProducto = this.servicioProductosService.findById(servicioProductoId);
			if (servicioProducto.isPresent()) {
				response.put("status", true);
				response.put("message", "El listado fue encontrado.");
				response.put("data", servicioProducto);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("status", false);
				response.put("message", "El listado no fue encontrado.");
				response.put("data", null);
				return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
			}
		} catch (Technicalexception e) {
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ADMINISTRACION");
			log.setController("api/servicioProductos"+servicioProductoId);
			log.setCausa(e.getCause() + "");
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("status", false);
			response.put("result", null);
			response.put("message",
					"Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("code", log.getLogSistemaId() + "");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/findByEntidadIdForSelect")
	public ResponseEntity<?> findByEntidadIdForSelect(Authentication authentication) {

		SegUsuarioEntity usuario = new SegUsuarioEntity();
		EntidadEntity entidad = new EntidadEntity();
		Map<String, Object> response = new HashMap<>();
		try {
			
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
			List<PServicioProductoEntity> servicioProductosList = this.servicioProductosService
					.findByEntidadIdForSelect(entidad.getEntidadId());
			if (!servicioProductosList.isEmpty()) {
				response.put("status", true);
				response.put("message", "El listado fue encontrado.");
				response.put("data", servicioProductosList);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("status", false);
				response.put("message", "El listado no fue encontrado.");
				response.put("data", null);
				return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
			}
		} catch (Technicalexception e) {
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ADMINISTRACION");
			log.setController("api/findByEntidadIdForSelect");
			log.setCausa(e.getCause() + "");
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("status", false);
			response.put("result", null);
			response.put("message",
					"Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("code", log.getLogSistemaId() + "");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(path = { "/findByProductos/tipo/{tipoEntidad}", "/findByProductos/busqueda/{paramBusqueda}",
			"/findByProductos/", }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> findByProductos(@PathVariable("paramBusqueda") Optional<String> paramBusqueda,
			@PathVariable("tipoEntidad") Optional<String> tipoEntidad, Authentication authentication) {

		SegUsuarioEntity usuario = new SegUsuarioEntity();
		List<EntidadEntity> entidadList = new ArrayList<>();
		RecaudadorEntity recaudador = new RecaudadorEntity();
		List<Long> entidadIdList = new ArrayList<>();
		Map<String, Object> response = new HashMap<>();
		String newParamBusqueda = "";
		String newTipoEntidad = "";
		try {
			if (paramBusqueda.isPresent()) {
				newParamBusqueda = paramBusqueda.get();
			}
			if (tipoEntidad.isPresent()) {
				newTipoEntidad = tipoEntidad.get();
			} else {
				newTipoEntidad = "%";
			}

			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			recaudador = this.recaudadoraService.findRecaudadorByUserId(usuario.getUsuarioId());
			entidadList = this.entidadService.findEntidadByRecaudacionId(recaudador.getRecaudadorId());

			for (EntidadEntity entidadEntity : entidadList) {
				entidadIdList.add(entidadEntity.getEntidadId());
			}

			List<PServicioProductoEntity> servicioProductosList = this.servicioProductosService
					.findByProductos(entidadIdList, newParamBusqueda, newTipoEntidad);

			for (PServicioProductoEntity servicioProductoEntity : servicioProductosList) {

				servicioProductoEntity
						.setImagenBase64(Util.imgToText(this.fileAbonosLogo + servicioProductoEntity.getImagen()));
			}
			if (!servicioProductosList.isEmpty()) {
				response.put("status", true);
				response.put("message", "El listado fue encontrado.");
				response.put("data", servicioProductosList);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("status", false);
				response.put("message", "El listado no fue encontrado.");
				response.put("data", null);
				return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
			}
		} catch (Technicalexception e) {
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ADMINISTRACION");
			log.setController("api/findByEntidadIdForSelect");
			log.setCausa(e.getCause() + "");
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("status", false);
			response.put("result", null);
			response.put("message",
					"Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("code", log.getLogSistemaId() + "");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/findServiciosForRecaudadorId")
	public ResponseEntity<?> findServiciosForRecaudadorId(Authentication authentication) {

		SegUsuarioEntity usuario = new SegUsuarioEntity();		
		RecaudadorEntity recaudador=new RecaudadorEntity();
		Map<String, Object> response = new HashMap<>();
		try {
			usuario = this.segUsuarioService.findByLogin(authentication.getName());			
			recaudador = this.recaudadoraService.findRecaudadorByUserId(usuario.getUsuarioId());			
			List<PServicioProductoEntity> servicioProductosList = this.servicioProductosService
					.findServiciosForRecaudadorId(recaudador.getRecaudadorId());
			if (!servicioProductosList.isEmpty()) {
				response.put("status", true);
				response.put("message", "El listado fue encontrado.");
				response.put("data", servicioProductosList);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("status", false);
				response.put("message", "El listado no fue encontrado.");
				response.put("data", null);
				return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
			}
		} catch (Technicalexception e) {
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ADMINISTRACION");
			log.setController("api/findByEntidadIdForSelect");
			if(e.getCause()!=null) {
				log.setCausa(e.getCause().getMessage() + "");	
			}
		
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("status", false);
			response.put("result", null);
			response.put("message",
					"Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("code", log.getLogSistemaId() + "");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/findForSelect")
	public ResponseEntity<?> findForSelect(Authentication authentication) {

		SegUsuarioEntity usuario = new SegUsuarioEntity();
		EntidadEntity entidad = new EntidadEntity();

		Map<String, Object> response = new HashMap<>();
		try {
			
			List<PServicioProductoEntity> servicioProductosList = this.servicioProductosService
					.findForSelect();
			if (!servicioProductosList.isEmpty()) {
				response.put("status", true);
				response.put("message", "El listado fue encontrado.");
				response.put("data", servicioProductosList);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("status", false);
				response.put("message", "El listado no fue encontrado.");
				response.put("data", null);
				return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
			}
		} catch (Technicalexception e) {
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ADMINISTRACION");
			log.setController("api/findByEntidadIdForSelect");
			log.setCausa(e.getCause() + "");
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("status", false);
			response.put("result", null);
			response.put("message",
					"Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("code", log.getLogSistemaId() + "");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

}
