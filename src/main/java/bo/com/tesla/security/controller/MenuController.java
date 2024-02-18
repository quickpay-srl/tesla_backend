package bo.com.tesla.security.controller;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.administracion.entity.SegPrivilegioEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.entidades.services.IEntidadService;
import bo.com.tesla.recaudaciones.services.IRecaudadoraService;
import bo.com.tesla.security.dto.CambiarPasswordDto;
import bo.com.tesla.security.dto.DatosLoginDto;
import bo.com.tesla.security.dto.OperacionesDto;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegPrivilegiosService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;

@RestController
@RequestMapping("api/Menu")
public class MenuController {
	private Logger logger = LoggerFactory.getLogger(MenuController.class);

	@Autowired
	private ISegUsuarioService segUsuarioService;

	@Autowired
	private ISegPrivilegiosService segPrivilegiosService;

	@Autowired
	private IEntidadService entidadService;

	@Autowired
	private IRecaudadoraService recaudadoraService;
	
	@Autowired
    private ILogSistemaService logSistemaService;

	@GetMapping(path = "/menu", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> menu(Authentication authentication) {
		Map<String, Object> response = new HashMap<>();
		List<SegPrivilegioEntity> privilegioList = new ArrayList<>();
		List<SegPrivilegioEntity> privilegioMenu = new ArrayList<>();
		try {
			SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());

			privilegioList = segPrivilegiosService.getMenuByUserId(usuario.getUsuarioId());
			if (privilegioList.isEmpty()) {
				response.put("mensaje",
						"No se encontraron ningún privilegio para el usuario: " + usuario.getLogin() + ".");
				response.put("status", false);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}

			for (SegPrivilegioEntity segPrivilegio : privilegioList) {
				if (segPrivilegio.getSegPrivilegioEntityList().isEmpty()) {
					if (segPrivilegio.getEstado().equals("ACTIVO")) {
						privilegioMenu.add(segPrivilegio);
					}

				}
			}

			response.put("data", privilegioMenu);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());

			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("data", privilegioList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping(path = "/subMenu", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> menuSubMenu(Authentication authentication) {
		Map<String, Object> response = new HashMap<>();
		List<SegPrivilegioEntity> privilegioList = new ArrayList<>();
		List<SegPrivilegioEntity> privilegioMenu = new ArrayList<>();
		try {
			SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());

			privilegioList = segPrivilegiosService.getSubMenuByUserId(usuario.getUsuarioId());
			if (privilegioList.isEmpty()) {
				response.put("mensaje",
						"No se encontraron ningún privilegio para el usuario: " + usuario.getLogin() + ".");
				response.put("status", false);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}

		

			response.put("data", privilegioList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());

			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("data", privilegioList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping(path = "/getOperaciones/{tabla}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getOperaciones(@PathVariable("tabla") String tabla, Authentication authentication) {
		Map<String, Object> response = new HashMap<>();
		List<OperacionesDto> operacionesList = new ArrayList<>();
		try {
			operacionesList = segPrivilegiosService.getOperaciones(authentication.getName(), tabla);
			if (operacionesList.isEmpty()) {
				response.put("data", operacionesList);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("data", operacionesList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping(path = { "/operaciones/{tabla}",
			"/operaciones/{tabla}/{estadoInicial}" }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getOperacionesByEstadoInicial(@PathVariable("tabla") String tabla,
			@PathVariable(name = "estadoInicial", required = false) Optional<String> estadoInicial,
			Authentication authentication) {
		Map<String, Object> response = new HashMap<>();
		List<OperacionesDto> operacionesList = new ArrayList<>();
		try {
			if (estadoInicial.isPresent()) {
				operacionesList = segPrivilegiosService.getOperacionesByEstadoInicial(authentication.getName(), tabla,
						estadoInicial.get());
			} else {
				//operacionesList = segPrivilegiosService.getOperaciones(authentication.getName(), tabla);
				operacionesList = segPrivilegiosService.getOperacionesByEstadoInicial(authentication.getName(), tabla,"INICIAL");
			}
			if (operacionesList.isEmpty()) {
				response.put("data", operacionesList);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("data", operacionesList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping(path = "/getDatosLogin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getDatosLogin(Authentication authentication) {
		Map<String, Object> response = new HashMap<>();
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		EntidadEntity entidad = new EntidadEntity();
		RecaudadorEntity recaudador = new RecaudadorEntity();
		try {
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
			recaudador = this.recaudadoraService.findRecaudadorByUserId(usuario.getUsuarioId());
			DatosLoginDto datos = new DatosLoginDto();
			if(usuario.getPersonaId()!=null) {
				
				if(usuario.getPersonaId().getMaterno()!=null ) {
					datos.nombreUsuario = usuario.getPersonaId().getPaterno() + " " +usuario.getPersonaId().getMaterno() + " "
							+ usuario.getPersonaId().getNombres();
				}else {
					datos.nombreUsuario = usuario.getPersonaId().getPaterno() +  " "
							+ usuario.getPersonaId().getNombres();
				}
				
				datos.nombreUsuario = datos.nombreUsuario.toUpperCase();
				datos.correo = usuario.getPersonaId().getCorreoElectronico();
			}
			if (entidad != null) {
				datos.nombreEntidad = entidad.getNombre();
				datos.modulo="EMPRESA";
			} else if (recaudador != null) {
				datos.nombreEntidad = recaudador.getNombre();
				datos.modulo="SOCIO";
			}else {
				datos.nombreEntidad = "ADMINISTRADOR";
				datos.modulo="ADMINISTRADOR";
			}

			response.put("data", datos);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}
	
	@PostMapping(path = "/cambiarPassword", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> cambiarPassword(@RequestBody CambiarPasswordDto passwords,Authentication authentication) {
		Map<String, Object> response = new HashMap<>();
		SegUsuarioEntity usuario = new SegUsuarioEntity();	
		try {
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			
			this.segUsuarioService.cambiarPassword(passwords, usuario);
			response.put("message","Su contraseña fue modificada con éxito.");
			response.put("status", false);			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (BusinesException e) {
			e.printStackTrace();
			 LogSistemaEntity log=new LogSistemaEntity();
	            log.setModulo("Menu");
	            log.setController("api/Menu/cambiarPassword");
	            if(e.getCause()!=null) {
	            	log.setCausa(e.getCause().getMessage());	
	            }	            
	            log.setMensaje(e.getMessage()+"");
	            log.setUsuarioCreacion(usuario.getUsuarioId());
	            log.setFechaCreacion(new Date());
	            logSistemaService.save(log);
	          
	            response.put("status", false);
	            response.put("result", null);
	            response.put("message", e.getMessage());
	            response.put("code", log.getLogSistemaId()+"");
	            return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (Technicalexception e) {
			e.printStackTrace();
			 LogSistemaEntity log=new LogSistemaEntity();
	            log.setModulo("Menu");
	            log.setController("api/Menu/cambiarPassword");
	            if(e.getCause()!=null) {
	            	log.setCausa(e.getCause().getMessage());	
	            }	            
	            log.setMensaje(e.getMessage()+"");
	            log.setUsuarioCreacion(usuario.getUsuarioId());
	            log.setFechaCreacion(new Date());
	            logSistemaService.save(log);
	          
	            response.put("status", false);
	            response.put("result", null);
	            response.put("message", e.getMessage());
	            response.put("code", log.getLogSistemaId()+"");
	            return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}
	
	
	

}
