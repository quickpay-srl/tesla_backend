package bo.com.tesla.pagos.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.com.tesla.administracion.entity.DominioEntity;
import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.PServicioProductoEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.administracion.services.IRecaudadorService;
import bo.com.tesla.entidades.services.IEntidadService;
import bo.com.tesla.pagos.dao.IPServicioProductosDao;
import bo.com.tesla.pagos.dto.PPagosDto;
import bo.com.tesla.pagos.dto.PBeneficiarioReporteDto;
import bo.com.tesla.pagos.services.IPServicioProductosService;
import bo.com.tesla.pagos.services.IReportesPagoService;
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
@RequestMapping("api/reportePagos")
public class ReportesPagoController {
	private Logger logger = LoggerFactory.getLogger(ReportesPagoController.class);
	
	@Autowired
	private IReportesPagoService reportesPagoService;
	@Autowired
	private ISegUsuarioService segUsuarioService;
	@Autowired
	private IEntidadService entidadService;	
	@Autowired
	private IPServicioProductosService  servicioProductosService;
	@Autowired
	private IRecaudadoraService recaudadorService;
	@Autowired
	private ILogSistemaService logSistemaService;
	
	
	
	@Value("${tesla.path.files-report}")
	private String filesReport;
	
	@PostMapping(path = "/listForReportEntidad")
	public ResponseEntity<?> listForReportEntidad( @RequestBody PBeneficiarioReporteDto abonoCliente,
			Authentication authentication) throws Exception {

		Map<String, Object> response = new HashMap<>();
		List<PBeneficiarioReporteDto> beneficiarioList=new ArrayList<>();
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		Map<String, Object> parameters = new HashMap<>();
	
		try {
			if (abonoCliente.fechaIni == null && abonoCliente.fechaFin == null) {
				parameters.put("tituloRangoFechas", "");
			} else if (abonoCliente.fechaIni == null && abonoCliente.fechaFin != null) {
				parameters.put("tituloRangoFechas",
						"REPORTE GENERADO HASTA LA FECHA : " + Util.dateToStringFormat(abonoCliente.fechaFin));
			} else if (abonoCliente.fechaIni != null && abonoCliente.fechaFin == null) {
				parameters.put("tituloRangoFechas", "REPORTE GENERADO A PARTIR DE LA FECHA : "
						+ Util.dateToStringFormat(abonoCliente.fechaIni));
			} else {
				parameters.put("tituloRangoFechas",
						"REPORTE GENERADO EN EL RANGO DE FECHAS : "
								+ Util.dateToStringFormat(abonoCliente.fechaIni) + " al "
								+ Util.dateToStringFormat(abonoCliente.fechaFin));
			}
			if (abonoCliente.fechaIni == null) {
				abonoCliente.fechaIni = Util.stringToDate("01/01/2021");
			}
			if (abonoCliente.fechaFin == null) {
				abonoCliente.fechaFin = Util.stringToDate("01/01/2100");
			}
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
			PServicioProductoEntity servicio= this.servicioProductosService.findById(abonoCliente.servicioProductoId).get();
			
			parameters.put("tituloReporte", "REPORTE "+servicio.getDescripcion().toUpperCase());
			
			parameters.put("tituloEntidad", entidad.getNombre());
			parameters.put("logoTesla", filesReport + "/img/teslapng.png");
			if (abonoCliente.export.equals("msexcel")) {
				parameters.put("IS_IGNORE_PAGINATION", true);
			}
			
			beneficiarioList=	this.reportesPagoService.
					listForReportEntidad(
							abonoCliente.fechaIni, 
							abonoCliente.fechaFin, 
							abonoCliente.estadoList, 
							abonoCliente.recaudadorIdList, 
							entidad.getEntidadId(),
							abonoCliente.servicioProductoId);
			File file = ResourceUtils.getFile(filesReport + "/report_jrxml/reportes/entidades/pagosEntidadGeneral.jrxml");

			JasperReport jasper = JasperCompileManager.compileReport(file.getAbsolutePath());
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(beneficiarioList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, parameters, ds);

			byte[] report = Util.jasperExportFormat(jasperPrint, abonoCliente.export, filesReport);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentLength(report.length);
			headers.setContentType(MediaType.parseMediaType("application/" + abonoCliente.export));
			headers.set("Content-Disposition", "inline; filename=report." + abonoCliente.export);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			return new ResponseEntity<byte[]>(report, headers, HttpStatus.OK);
			
		} catch (Technicalexception e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("REPORTE PAGOS");
			log.setController("api/reportePagos/listForReportEntidad/");
			if(e.getCause()!=null) {
				log.setCausa(e.getCause().getMessage() + "");	
			}	
			
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			this.logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("codigo", log.getLogSistemaId() + "");
			response.put("status", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	@PostMapping(path = "/listForGridEntidad")
	public ResponseEntity<?> listForGridEntidad( @RequestBody PBeneficiarioReporteDto abonoCliente,
			Authentication authentication) throws Exception {

		Map<String, Object> response = new HashMap<>();
		Page<PBeneficiarioReporteDto> beneficiarioReporteDto;
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		try {
			if (abonoCliente.fechaIni == null) {
				abonoCliente.fechaIni = Util.stringToDate("01/01/2021");
			}
			if (abonoCliente.fechaFin == null) {
				abonoCliente.fechaFin = Util.stringToDate("01/01/2100");
			}
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
			beneficiarioReporteDto=	this.reportesPagoService.
					listForGridEntidad(
							abonoCliente.fechaIni, 
							abonoCliente.fechaFin, 
							abonoCliente.estadoList, 
							abonoCliente.recaudadorIdList, 
							entidad.getEntidadId(),
							abonoCliente.servicioProductoId,
							abonoCliente.paginacion - 1, 10);
			
			response.put("data", beneficiarioReporteDto);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
			
		} catch (Technicalexception e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("REPORTE PAGOS");
			log.setController("api/reportePagos/listForGridEntidad/");
			if(e.getCause()!=null) {
				log.setCausa(e.getCause().getMessage() + "");	
			}	
			
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			this.logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("codigo", log.getLogSistemaId() + "");
			response.put("status", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@PostMapping(path = "/listForGridRecaudacion")
	public ResponseEntity<?> listForGridRecaudacion( @RequestBody PBeneficiarioReporteDto abonoCliente,
			Authentication authentication) throws Exception {

		Map<String, Object> response = new HashMap<>();
		Page<PBeneficiarioReporteDto> beneficiarioReporteDto;
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		try {
			if (abonoCliente.fechaIni == null) {
				abonoCliente.fechaIni = Util.stringToDate("01/01/2021");
			}
			if (abonoCliente.fechaFin == null) {
				abonoCliente.fechaFin = Util.stringToDate("01/01/2100");
			}
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			RecaudadorEntity recaudador= this.recaudadorService.findRecaudadorByUserId(usuario.getUsuarioId());	
					
			beneficiarioReporteDto=	this.reportesPagoService.
					listForGridRecaudacion(
							abonoCliente.fechaIni, 
							abonoCliente.fechaFin, 
							abonoCliente.estadoList, 
							recaudador.getRecaudadorId(), 
							abonoCliente.servicioProductoId,
							abonoCliente.paginacion - 1, 10);
					
			
			response.put("data", beneficiarioReporteDto);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
			
		} catch (Technicalexception e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("REPORTE PAGOS");
			log.setController("api/reportePagos/listForGridRecaudacion/");
			if(e.getCause()!=null) {
				log.setCausa(e.getCause().getMessage() + "");	
			}	
			
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			this.logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("codigo", log.getLogSistemaId() + "");
			response.put("status", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@PostMapping(path = "/listForReportRecaudacion")
	public ResponseEntity<?> listForReportRecaudacion( @RequestBody PBeneficiarioReporteDto abonoCliente,
			Authentication authentication) throws Exception {

		Map<String, Object> response = new HashMap<>();
		List<PBeneficiarioReporteDto> beneficiarioList=new ArrayList<>();
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		Map<String, Object> parameters = new HashMap<>();
	
		try {
			if (abonoCliente.fechaIni == null && abonoCliente.fechaFin == null) {
				parameters.put("tituloRangoFechas", "");
			} else if (abonoCliente.fechaIni == null && abonoCliente.fechaFin != null) {
				parameters.put("tituloRangoFechas",
						"REPORTE GENERADO HASTA LA FECHA : " + Util.dateToStringFormat(abonoCliente.fechaFin));
			} else if (abonoCliente.fechaIni != null && abonoCliente.fechaFin == null) {
				parameters.put("tituloRangoFechas", "REPORTE GENERADO A PARTIR DE LA FECHA : "
						+ Util.dateToStringFormat(abonoCliente.fechaIni));
			} else {
				parameters.put("tituloRangoFechas",
						"REPORTE GENERADO EN EL RANGO DE FECHAS : "
								+ Util.dateToStringFormat(abonoCliente.fechaIni) + " al "
								+ Util.dateToStringFormat(abonoCliente.fechaFin));
			}
			if (abonoCliente.fechaIni == null) {
				abonoCliente.fechaIni = Util.stringToDate("01/01/2021");
			}
			if (abonoCliente.fechaFin == null) {
				abonoCliente.fechaFin = Util.stringToDate("01/01/2100");
			}
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			RecaudadorEntity recaudador= this.recaudadorService.findRecaudadorByUserId(usuario.getUsuarioId());
			PServicioProductoEntity servicio= this.servicioProductosService.findById(abonoCliente.servicioProductoId).get();
			
			parameters.put("tituloReporte", "REPORTE "+servicio.getDescripcion().toUpperCase());
			
			parameters.put("tituloEntidad", recaudador.getNombre());
			parameters.put("logoTesla", filesReport + "/img/teslapng.png");
			if (abonoCliente.export.equals("msexcel")) {
				parameters.put("IS_IGNORE_PAGINATION", true);
			}	
			beneficiarioList=	this.reportesPagoService.listForReportRecaudacion(
					abonoCliente.fechaIni, 
					abonoCliente.fechaFin, 
					abonoCliente.estadoList,
					recaudador.getRecaudadorId(),
					abonoCliente.servicioProductoId);
			File file = ResourceUtils.getFile(filesReport + "/report_jrxml/reportes/recaudador/pagosRecaudadorGeneral.jrxml");

			JasperReport jasper = JasperCompileManager.compileReport(file.getAbsolutePath());
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(beneficiarioList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, parameters, ds);

			byte[] report = Util.jasperExportFormat(jasperPrint, abonoCliente.export, filesReport);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentLength(report.length);
			headers.setContentType(MediaType.parseMediaType("application/" + abonoCliente.export));
			headers.set("Content-Disposition", "inline; filename=report." + abonoCliente.export);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			return new ResponseEntity<byte[]>(report, headers, HttpStatus.OK);
			
		} catch (Technicalexception e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("REPORTE PAGOS");
			log.setController("api/reportePagos/listForReportRecaudacion/");
			if(e.getCause()!=null) {
				log.setCausa(e.getCause().getMessage() + "");	
			}	
			
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			this.logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("codigo", log.getLogSistemaId() + "");
			response.put("status", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@PostMapping(path = "/listForReportAdministracion")
	public ResponseEntity<?> listForReportAdministracion( @RequestBody PBeneficiarioReporteDto abonoCliente,
			Authentication authentication) throws Exception {

		Map<String, Object> response = new HashMap<>();
		List<PBeneficiarioReporteDto> beneficiarioList=new ArrayList<>();
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		Map<String, Object> parameters = new HashMap<>();
	
		try {
			if (abonoCliente.fechaIni == null && abonoCliente.fechaFin == null) {
				parameters.put("tituloRangoFechas", "");
			} else if (abonoCliente.fechaIni == null && abonoCliente.fechaFin != null) {
				parameters.put("tituloRangoFechas",
						"REPORTE GENERADO HASTA LA FECHA : " + Util.dateToStringFormat(abonoCliente.fechaFin));
			} else if (abonoCliente.fechaIni != null && abonoCliente.fechaFin == null) {
				parameters.put("tituloRangoFechas", "REPORTE GENERADO A PARTIR DE LA FECHA : "
						+ Util.dateToStringFormat(abonoCliente.fechaIni));
			} else {
				parameters.put("tituloRangoFechas",
						"REPORTE GENERADO EN EL RANGO DE FECHAS : "
								+ Util.dateToStringFormat(abonoCliente.fechaIni) + " al "
								+ Util.dateToStringFormat(abonoCliente.fechaFin));
			}
			if (abonoCliente.fechaIni == null) {
				abonoCliente.fechaIni = Util.stringToDate("01/01/2021");
			}
			if (abonoCliente.fechaFin == null) {
				abonoCliente.fechaFin = Util.stringToDate("01/01/2100");
			}
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
			PServicioProductoEntity servicio= this.servicioProductosService.findById(abonoCliente.servicioProductoId).get();			
			parameters.put("tituloReporte", "REPORTE "+servicio.getDescripcion().toUpperCase());			
			parameters.put("tituloEntidad", "EXACTA");
			parameters.put("logoTesla", filesReport + "/img/teslapng.png");
			if (abonoCliente.export.equals("msexcel")) {
				parameters.put("IS_IGNORE_PAGINATION", true);
			}
			
			beneficiarioList=	this.reportesPagoService.
					listForReportAdministracion(
							abonoCliente.fechaIni, 
							abonoCliente.fechaFin, 
							abonoCliente.estadoList, 
							abonoCliente.recaudadorIdList,
							abonoCliente.servicioProductoId);
			File file = ResourceUtils.getFile(filesReport + "/report_jrxml/reportes/administracion/pagosAdministracionGeneral.jrxml");

			JasperReport jasper = JasperCompileManager.compileReport(file.getAbsolutePath());
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(beneficiarioList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, parameters, ds);

			byte[] report = Util.jasperExportFormat(jasperPrint, abonoCliente.export, filesReport);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentLength(report.length);
			headers.setContentType(MediaType.parseMediaType("application/" + abonoCliente.export));
			headers.set("Content-Disposition", "inline; filename=report." + abonoCliente.export);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			return new ResponseEntity<byte[]>(report, headers, HttpStatus.OK);
			
		} catch (Technicalexception e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("REPORTE PAGOS");
			log.setController("api/reportePagos/listForReportAdministracion/");
			if(e.getCause()!=null) {
				log.setCausa(e.getCause().getMessage() + "");	
			}	
			
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			this.logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("codigo", log.getLogSistemaId() + "");
			response.put("status", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	@PostMapping(path = "/listForGridAdministracion")
	public ResponseEntity<?> listForGridAdministracion( @RequestBody PBeneficiarioReporteDto abonoCliente,
			Authentication authentication) throws Exception {

		Map<String, Object> response = new HashMap<>();
		Page<PBeneficiarioReporteDto> beneficiarioReporteDto;
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		try {
			if (abonoCliente.fechaIni == null) {
				abonoCliente.fechaIni = Util.stringToDate("01/01/2021");
			}
			if (abonoCliente.fechaFin == null) {
				abonoCliente.fechaFin = Util.stringToDate("01/01/2100");
			}
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			
			beneficiarioReporteDto=	this.reportesPagoService.
					listForGridAdministracion(
							abonoCliente.fechaIni, 
							abonoCliente.fechaFin, 
							abonoCliente.estadoList, 
							abonoCliente.recaudadorIdList,							
							abonoCliente.servicioProductoId,
							abonoCliente.paginacion - 1, 10);
			
			response.put("data", beneficiarioReporteDto);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
			
		} catch (Technicalexception e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("REPORTE PAGOS");
			log.setController("api/reportePagos/listForGridAdministracion/");
			if(e.getCause()!=null) {
				log.setCausa(e.getCause().getMessage() + "");	
			}	
			
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			this.logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("codigo", log.getLogSistemaId() + "");
			response.put("status", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	

}
