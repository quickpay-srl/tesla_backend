package bo.com.tesla.entidades.controller;

import java.io.File;
import java.math.BigDecimal;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.com.tesla.administracion.entity.ArchivoEntity;
import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.entidades.dto.ArchivoDto;
import bo.com.tesla.entidades.dto.BusquedaReportesDto;
import bo.com.tesla.entidades.dto.DeudasClienteDto;
import bo.com.tesla.entidades.services.IArchivoService;
import bo.com.tesla.entidades.services.IEntidadService;
import bo.com.tesla.entidades.services.IReporteEntidadesService;
import bo.com.tesla.recaudaciones.dto.RecaudadoraDto;
import bo.com.tesla.recaudaciones.services.IHistoricoDeudaService;
import bo.com.tesla.recaudaciones.services.IRecaudadoraService;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegUsuarioService;

import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.cross.Util;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@RestController
@RequestMapping("api/ReportEntidad")
public class ReportEntidadController {
	private Logger logger = LoggerFactory.getLogger(ReportEntidadController.class);

	@Autowired
	private IReporteEntidadesService reporteEntidadesService;

	@Autowired
	private IRecaudadoraService recaudadoraService;

	@Autowired
	private ILogSistemaService logSistemaService;

	@Autowired
	private ISegUsuarioService segUsuarioService;

	@Autowired
	private IEntidadService entidadService;

	@Autowired
	private IArchivoService archivoService;

	@Value("${tesla.path.files-report}")
	private String filesReport;

	@PostMapping(path = "/deudasPagadas")
	public ResponseEntity<?> deudasPagadas(@RequestBody BusquedaReportesDto busquedaReportesDto,
			Authentication authentication) {
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		Map<String, Object> parameters = new HashMap<>();
		Map<String, Object> response = new HashMap<>();

		try {

			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
			if (busquedaReportesDto.recaudadorId == null || busquedaReportesDto.recaudadorId == "") {
				busquedaReportesDto.recaudadorId = "%";
			} else {
				RecaudadorEntity recaudador = this.recaudadoraService
						.findByRecaudadorId(new Long(busquedaReportesDto.recaudadorId));
				parameters.put("recaudadora", recaudador.getNombre());
			}
			parameters.put("fechaInicio", Util.dateToStringFormat(busquedaReportesDto.fechaInicio));
			parameters.put("fechaFin", Util.dateToStringFormat(busquedaReportesDto.fechaFin));
			if (busquedaReportesDto.export.equals("msexcel")) {
				parameters.put("IS_IGNORE_PAGINATION", true);
			}
			List<DeudasClienteDto> linealChart = this.reporteEntidadesService.findDeudasPagadasByParameter(
					entidad.getEntidadId(), busquedaReportesDto.recaudadorId, busquedaReportesDto.estado,
					busquedaReportesDto.fechaInicio, busquedaReportesDto.fechaFin);
			if (linealChart.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}

			File file = ResourceUtils.getFile("classpath:reportes_entidad/deudas_pagadas.jrxml");
			JasperReport jasper = JasperCompileManager.compileReport(file.getAbsolutePath());
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(linealChart);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, parameters, ds);

			byte[] report = Util.jasperExportFormat(jasperPrint, busquedaReportesDto.export, filesReport);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentLength(report.length);
			headers.setContentType(MediaType.parseMediaType("application/" + busquedaReportesDto.export));
			headers.set("Content-Disposition", "inline; filename=report.pdf");
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			return new ResponseEntity<byte[]>(report, headers, HttpStatus.OK);

		} catch (Technicalexception e) {

			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES");
			log.setController("api/ReportEntidad/deudasPagadas");
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
			log.setController("api/ReportEntidad/deudasPagadas");
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

	@Secured("ROLE_MCAERG")
	@PostMapping(path = "/findDeudasByParameter")
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

	}
	@Secured("ROLE_MCAERG")
	@PostMapping(path = "/findDeudasByParameterForReport")
	public ResponseEntity<?> findDeudasByParameterForReport(@RequestBody BusquedaReportesDto busquedaReportesDto,
			Authentication authentication) {

		Map<String, Object> parameters = new HashMap<>();
		Map<String, Object> response = new HashMap<>();
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		try {
			
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());

			if (busquedaReportesDto.fechaInicio == null && busquedaReportesDto.fechaFin == null) {
				parameters.put("tituloRangoFechas", "NO SE ESTABLECIÓ UN RANGO DE FECHAS");
			} else if (busquedaReportesDto.fechaInicio == null && busquedaReportesDto.fechaFin != null) {
				parameters.put("tituloRangoFechas",
						"REPORTE GENERADO HASTA LA FECHA : " + Util.dateToStringFormat(busquedaReportesDto.fechaFin));
			} else if (busquedaReportesDto.fechaInicio != null && busquedaReportesDto.fechaFin == null) {
				parameters.put("tituloRangoFechas", "REPORTE GENERADO A PARTIR DE LA FECHA : "
						+ Util.dateToStringFormat(busquedaReportesDto.fechaInicio));
			} else {
				parameters.put("tituloRangoFechas",
						"REPORTE GENERADO EN EL RANGO DE FECHAS : "
								+ Util.dateToStringFormat(busquedaReportesDto.fechaInicio) + "-"
								+ Util.dateToStringFormat(busquedaReportesDto.fechaFin));
			}

			if (busquedaReportesDto.fechaInicio == null) {
				busquedaReportesDto.fechaInicio = Util.stringToDate("01/01/2021");
			}
			if (busquedaReportesDto.fechaFin == null) {
				busquedaReportesDto.fechaFin = Util.stringToDate("01/01/2100");
			}

			parameters.put("tituloEntidad", entidad.getNombre().toUpperCase());
			parameters.put("tituloReporte", "INFORMACIÓN DE DEUDAS GENERALES");
			parameters.put("logoTesla", filesReport + "/img/logo_fondo_blanco.jpg");
			if (busquedaReportesDto.export.equals("msexcel")) {
				parameters.put("IS_IGNORE_PAGINATION", true);
			}

			List<DeudasClienteDto> deudasClienteDtoList = this.reporteEntidadesService.findDeudasByParameterForReport(
					busquedaReportesDto.fechaInicio, busquedaReportesDto.fechaFin, entidad.getEntidadId(),
					busquedaReportesDto.recaudadorArray, busquedaReportesDto.estadoArray);

			if (deudasClienteDtoList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
			}

			File file = ResourceUtils.getFile(filesReport + "/report_jrxml/reportes/entidades/ListaDeudasEntidad.jrxml");

			JasperReport jasper = JasperCompileManager.compileReport(file.getAbsolutePath());
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(deudasClienteDtoList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, parameters, ds);

			byte[] report = Util.jasperExportFormat(jasperPrint, busquedaReportesDto.export, filesReport);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentLength(report.length);
			headers.setContentType(MediaType.parseMediaType("application/" + busquedaReportesDto.export));
			headers.set("Content-Disposition", "inline; filename=report." + busquedaReportesDto.export);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			return new ResponseEntity<byte[]>(report, headers, HttpStatus.OK);

		} catch (Technicalexception e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES");
			log.setController("api/ReportEntidad/findDeudasByParameterForReport");
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
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES");
			log.setController("api/ReportEntidad/findDeudasByParameterForReport");
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

	@Secured({"ROLE_MCAERG","ROLE_MCAERA"})
	@GetMapping(path = { "/findArchivos/{paginacion}",
			"/findArchivos/{paginacion}/{fechaInicio}/{fechaFin}/{estado}" }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> findArchivos(

			@PathVariable("paginacion") int paginacion,
			@PathVariable(name = "fechaInicio", required = false) Optional<Date> fechaInicio,
			@PathVariable(name = "fechaFin", required = false) Optional<Date> fechaFin,
			@PathVariable(name = "estado", required = false) Optional<String> estado, Authentication authentication)
			 {
		
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

			if (!estado.get().equals("All")) {
				newEstado = estado.get();
			} else {
				newEstado = "%";
			}

			archivosList = this.reporteEntidadesService.findByEntidadIdAndFechaIniAndFechaFin(entidad.getEntidadId(),
					fechaInicio.get(), fechaFin.get(), newEstado, paginacion - 1, 5);

			if (archivosList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("status", "true");
			response.put("data", archivosList);

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} catch(Technicalexception e) {
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES");
			log.setController("api/ReportEntidad/findArchivos/" + entidad.getEntidadId() + "/" + paginacion + "/"
					+ dateFechaIni + "/" + dateFechaFin + "");
			if(e.getCause()!=null) {
				log.setCausa(e.getCause().getMessage() + "");	
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
		catch (Exception e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES");
			log.setController("api/ReportEntidad/findArchivos/" + entidad.getEntidadId() + "/" + paginacion + "/"
					+ dateFechaIni + "/" + dateFechaFin + "");
			if(e.getCause()!=null) {
				log.setCausa(e.getCause().getMessage() + "");	
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

	@Secured("ROLE_MCAERA")
	@GetMapping(path = "/findDeudasByArchivoIdAndEstado/{archivoId}/{export}")
	public ResponseEntity<?> findDeudasByArchivoIdAndEstado(@PathVariable("archivoId") Long archivoId,
			@PathVariable("export") String export, Authentication authentication) {
		Map<String, Object> parameters = new HashMap<>();
		Map<String, Object> response = new HashMap<>();

		ArchivoEntity archivo = new ArchivoEntity();
		List<DeudasClienteDto> deudasClienteList = new ArrayList<>();
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		try {
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			archivo = this.archivoService.findById(archivoId);
			parameters.put("tituloFecha", "Información del archivo enviado en fecha :" + Util.dateToStringFormat(archivo.getFechaCreacion()));

			deudasClienteList = this.reporteEntidadesService.findDeudasByArchivoIdAndEstado(archivoId);

			if (deudasClienteList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}

			parameters.put("logoTesla", filesReport + "/img/logo_fondo_blanco.jpg");
			if (export.equals("msexcel")) {
				parameters.put("IS_IGNORE_PAGINATION", true);
			}
			File file = ResourceUtils.getFile(filesReport + "/report_jrxml/reportes/entidades/PorArchivos.jrxml");
			JasperReport jasper = JasperCompileManager.compileReport(file.getAbsolutePath());
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(deudasClienteList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, parameters, ds);
			byte[] report = Util.jasperExportFormat(jasperPrint, export, filesReport);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentLength(report.length);
			headers.setContentType(MediaType.parseMediaType("application/" + export));
			headers.set("Content-Disposition", "inline; filename=report." + export);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			return new ResponseEntity<byte[]>(report, headers, HttpStatus.OK);
		}catch(Technicalexception e) {
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES");
			log.setController("api/ReportEntidad/findDeudasByArchivoIdAndEstado/" + archivoId + "/" + export);
			if (e.getCause() != null) {
				log.setCausa(e.getCause().getMessage() + "");
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
		catch (Exception e) {
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES");
			log.setController("api/ReportEntidad/findDeudasByArchivoIdAndEstado/" + archivoId + "/" + export);
			if (e.getCause() != null) {
				log.setCausa(e.getCause().getMessage() + "");
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

}
