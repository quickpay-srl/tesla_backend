package bo.com.tesla.entidades.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.entidades.dto.BusquedaReportesDto;
import bo.com.tesla.entidades.dto.DeudasClienteDto;
import bo.com.tesla.entidades.services.IEntidadService;
import bo.com.tesla.entidades.services.IReporteEntidadesService;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.cross.Util;

@RestController
@RequestMapping("api/ReportLaRazon")
public class ReportLaRazonController {
	private Logger logger = LoggerFactory.getLogger(ReportLaRazonController.class);
	
	@Autowired
	private ILogSistemaService logSistemaService;
	
	@Autowired
	private ISegUsuarioService segUsuarioService;

	@Autowired
	private IEntidadService entidadService;
	
	@Autowired
	private IReporteEntidadesService reporteEntidadesService;
	
	/*@PostMapping(path = "/findDeudasByParameter")
	public ResponseEntity<?> findDeudasByParameter(@RequestBody BusquedaReportesDto busquedaReportesDto,
			Authentication authentication) {
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		Map<String, Object> response = new HashMap<>();
		try {

			if (busquedaReportesDto.fechaInicio == null) {
				busquedaReportesDto.fechaInicio = Util.stringToDate("01/01/2021");
			}
			if (busquedaReportesDto.fechaFin == null) {
				busquedaReportesDto.fechaFin = Util.stringToDate("01/01/2100");
			}

			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());

			Page<DeudasClienteDto> deudasClienteDtoList = this.reporteEntidadesService.findDeudasByParameter(
					busquedaReportesDto.fechaInicio, busquedaReportesDto.fechaFin, entidad.getEntidadId(),
					busquedaReportesDto.recaudadorArray, busquedaReportesDto.estadoArray,
					busquedaReportesDto.paginacion - 1, 10);

			if (deudasClienteDtoList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("data", deudasClienteDtoList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} catch (Technicalexception e) {

			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES");
			log.setController("api/ReportEntidad/findDeudasByParameter");
			if (e.getCause() != null) {
				log.setCausa(e.getCause().getMessage() + "");
			}
			log.setMensaje(e.getMessage());
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			this.logSistemaService.save(log);
			this.logger.error("This is cause", e.getMessage());

			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("codigo", log.getLogSistemaId() + "");
			response.put("status", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES");
			log.setController("api/ReportEntidad/findDeudasByParameter");
			if (e.getCause() != null) {
				log.setCausa(e.getCause().getMessage() + "");
			}
			log.setMensaje(e.getMessage());
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			this.logSistemaService.save(log);
			this.logger.error("This is cause", e.getMessage());

			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("codigo", log.getLogSistemaId() + "");
			response.put("status", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}*/
	
	@PostMapping("/downloaArchivo")
	public ResponseEntity<?> downloaArchivo(@RequestBody BusquedaReportesDto busquedaReportesDto, Authentication authentication) {
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		Map<String, Object> response = new HashMap<>();
		try {
			if (busquedaReportesDto.fechaInicio == null) {
				busquedaReportesDto.fechaInicio = Util.stringToDate("01/01/2021");
			}
			if (busquedaReportesDto.fechaFin == null) {
				busquedaReportesDto.fechaFin = Util.stringToDate("01/01/2100");
			}
			
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
			busquedaReportesDto.entidadId=entidad.getEntidadId();
			final InputStreamResource resource = new InputStreamResource(this.reporteEntidadesService.load(busquedaReportesDto));

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "archivo.csv")
					.contentType(MediaType.parseMediaType("text/csv")).body(resource);
		} catch (Exception e) {
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES");
			log.setController("api/ReportEntidad/findDeudasByParameter");
			if (e.getCause() != null) {
				log.setCausa(e.getCause().getMessage() + "");
			}
			log.setMensaje(e.getMessage());
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			this.logSistemaService.save(log);
			this.logger.error("This is cause", e.getMessage());

			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("codigo", log.getLogSistemaId() + "");
			response.put("status", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
	
	}

}
