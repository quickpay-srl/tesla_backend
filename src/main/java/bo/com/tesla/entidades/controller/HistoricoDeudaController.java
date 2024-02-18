package bo.com.tesla.entidades.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
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
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.entidades.dao.SelectDto;
import bo.com.tesla.entidades.dto.ArchivoDto;
import bo.com.tesla.entidades.dto.DeudasClienteDto;
import bo.com.tesla.entidades.services.IArchivoService;
import bo.com.tesla.entidades.services.IEntidadService;
import bo.com.tesla.recaudaciones.dto.EstadoTablasDto;
import bo.com.tesla.recaudaciones.services.IHistoricoDeudaService;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.config.Technicalexception;

@RestController
@RequestMapping("api/historicoDeuda")
public class HistoricoDeudaController {

	private Logger logger = LoggerFactory.getLogger(HistoricoDeudaController.class);

	@Autowired
	private IArchivoService archivoService;
	@Autowired
	private IHistoricoDeudaService historicoDeudaService;

	@Autowired
	private ILogSistemaService logSistemaService;

	@Autowired
	private ISegUsuarioService segUsuarioService;

	@Autowired
	private IEntidadService entidadService;

	@GetMapping(path = { "/findArchivos/{paginacion}",
			"/findArchivos/{paginacion}/{fechaInicio}/{fechaFin}/{estado}" }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> findArchivos(

			@PathVariable("paginacion") int paginacion,
			@PathVariable(name = "fechaInicio", required = false) Optional<Date> fechaInicio,
			@PathVariable(name = "fechaFin", required = false) Optional<Date> fechaFin,
			@PathVariable(name = "estado", required = false) Optional<String> estado, Authentication authentication) {
		System.out.println("Ingreso a controlador " + paginacion);
		Map<String, Object> response = new HashMap<>();
		Page<ArchivoDto> archivosList;
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		EntidadEntity entidad = new EntidadEntity();

		Date dateFechaIni = new Date();
		Date dateFechaFin = new Date();
		String newEstado = "";
		try {
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());

			if (!estado.get().equals("null")) {
				newEstado = estado.get();
			} else {
				newEstado = "";
			}

			archivosList = this.archivoService.findByEntidadIdAndFechaIniAndFechaFin(entidad.getEntidadId(),
					fechaInicio.get(), fechaFin.get(), newEstado, paginacion - 1, 5);

			if (archivosList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("status", "true");
			response.put("data", archivosList);

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} catch (Technicalexception e) {

			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES");
			log.setController("api/historicoDeuda/getOperaciones/" + entidad.getEntidadId() + "/" + paginacion + "/"
					+ dateFechaIni + "/" + dateFechaFin + "");
			if(e.getCause()!=null) {
				log.setCausa(e.getCause().getMessage());
			}
			log.setMensaje(e.getMessage());
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping(path = { "/groupByDeudasClientes/{archivoId}/{paginacion}",
			"/groupByDeudasClientes/{archivoId}/{paginacion}/{paramBusqueda}" }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> groupByDeudasClientes(@PathVariable("archivoId") Long archivoId,
			@PathVariable("paginacion") int paginacion,
			@PathVariable(name = "paramBusqueda", required = false) Optional<String> paramBusqueda,
			Authentication authentication) {
		Map<String, Object> response = new HashMap<>();
		Page<DeudasClienteDto> historicoDeudaList;
		String newParamBusqueda = "";
		SegUsuarioEntity usuario = new SegUsuarioEntity();

		if (paramBusqueda.isPresent()) {
			newParamBusqueda = paramBusqueda.get();
			paginacion = 1;
		} else {
			newParamBusqueda = "";
		}
		try {
			historicoDeudaList = this.historicoDeudaService.groupByDeudasClientes(archivoId, newParamBusqueda,
					paginacion - 1, 5);

			if (historicoDeudaList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("status", "true");
			response.put("data", historicoDeudaList);

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} catch (Technicalexception e) {

			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES");
			log.setController("api/historicoDeuda/groupByDeudasClientes/" + archivoId + "/" + paginacion + "/"
					+ newParamBusqueda + "");
			if(e.getCause()!=null) {
				log.setCausa(e.getCause().getMessage());
			}
			log.setMensaje(e.getMessage());
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping(path = "/findEstadoHistorico")
	public ResponseEntity<?> findEstadoHistorico(Authentication authentication)  {

		Map<String, Object> response = new HashMap<>();
		List<EstadoTablasDto> estadosList = new ArrayList<>();
		List<SelectDto> selectDtoList = new ArrayList<>();

		try {
			estadosList = this.historicoDeudaService.findEstadoHistorico();
				if (estadosList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			for (EstadoTablasDto estados : estadosList) {
				SelectDto select = new SelectDto(estados.estado, estados.estadoId);
				selectDtoList.add(select);
			}

			response.put("data", selectDtoList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("/getCsv/{archivoId}")
	public ResponseEntity<?> getCsv(@PathVariable("archivoId") Long archivoId, Authentication authentication) {

		final InputStreamResource resource = new InputStreamResource(historicoDeudaService.load(archivoId));

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "archivo")
				.contentType(MediaType.parseMediaType("text/csv")).body(resource);
	}

	@GetMapping(path = "/findEstadoHistoricoPagos")
	public ResponseEntity<?> findEstadoHistoricoPagos(Authentication authentication) {

		Map<String, Object> response = new HashMap<>();
		List<EstadoTablasDto> estadosList = new ArrayList<>();
		List<SelectDto> selectDtoList = new ArrayList<>();

		try {

			estadosList = this.historicoDeudaService.findEstadoHistoricoPagos();
			if (estadosList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			for (EstadoTablasDto estados : estadosList) {
				SelectDto select = new SelectDto(estados.estado, estados.estadoId);
				selectDtoList.add(select);
			}

			response.put("data", selectDtoList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}

}
