package bo.com.tesla.administracion.controllers;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import bo.com.tesla.administracion.dto.*;
import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.entidades.services.IEntidadService;
import bo.com.tesla.recaudaciones.dto.EstadoTablasDto;
import bo.com.tesla.recaudaciones.dto.ReporteCobrosSocioDto;
import bo.com.tesla.recaudaciones.services.IHistoricoDeudaService;
import bo.com.tesla.recaudaciones.services.IRecaudadoraService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.administracion.services.IReporteAdminService;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.cross.Util;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@RestController
@RequestMapping("api/ReportAdmin")
public class ReportAdminController {
	private Logger logger = LoggerFactory.getLogger(ReportAdminController.class);

	@Autowired
	private IReporteAdminService reporteAdminService;

	@Autowired
	private ISegUsuarioService segUsuarioService;
	
	@Autowired
	private ILogSistemaService logSistemaService;


	@Autowired
	private IEntidadService entidadService;

	@Autowired
	private IRecaudadoraService recaudadoraService;

	@Autowired
	private IHistoricoDeudaService historicoDeudaService;

	@Value("${tesla.path.files-report}")
	private String filesReport;

	@PostMapping(path = "/findDeudasByParameter")
	public ResponseEntity<?> findDeudasByParameter(@RequestBody BusquedaReportesAdmDto busquedaReportesDto,
			Authentication authentication) throws Exception {
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		Map<String, Object> response = new HashMap<>();
		try {
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			if (busquedaReportesDto.fechaInicio == null) {
				busquedaReportesDto.fechaInicio = Util.stringToDate("01/01/2021");
			}
			if (busquedaReportesDto.fechaFin == null) {
				busquedaReportesDto.fechaFin = Util.stringToDate("01/01/2100");
			}
			if (busquedaReportesDto.entidadId.equals("All") || busquedaReportesDto.entidadId == null) {
				busquedaReportesDto.entidadId = "%";
			}
			if (busquedaReportesDto.recaudadorId.equals("All") || busquedaReportesDto.entidadId == null) {
				busquedaReportesDto.recaudadorId = "%";
			}
			if (busquedaReportesDto.estado.equals("All") || busquedaReportesDto.estado == null) {
				busquedaReportesDto.estado = "%";
			}			
			
			Page<DeudasClienteAdmDto> deudasClienteDtoList = this.reporteAdminService.findDeudasByParameter(
					busquedaReportesDto.fechaInicio, busquedaReportesDto.fechaFin, busquedaReportesDto.entidadId,
					busquedaReportesDto.recaudadorId, busquedaReportesDto.estadoArray, busquedaReportesDto.paginacion - 1,
					10);

			if (deudasClienteDtoList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("data", deudasClienteDtoList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} catch (Exception e) {
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES");
			log.setController("api/ReportAdmin/findDeudasByParameter");
			log.setMensaje(e.getMessage());
			log.setCausa(e.getCause().toString());
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
	@PostMapping(path = "/findDeudasByParameterv2")
	public ResponseEntity<?> findDeudasByParameterv2(@RequestBody BusquedaReportesAdmv2Dto busquedaReportesDto,
												   Authentication authentication) throws Exception {
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		Map<String, Object> response = new HashMap<>();
        boolean tieneEntidad = true;
        boolean tieneRecaudador = true;
        boolean tieneEstado = true;
		try {

			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			if (busquedaReportesDto.getFechaInicio() == null) {
				busquedaReportesDto.setFechaInicio(Util.stringToDate("01/01/2021"));
			}
			if (busquedaReportesDto.getFechaFin() == null) {
				busquedaReportesDto.setFechaFin(Util.stringToDate("01/01/2100"));
			}
			if(busquedaReportesDto.getFiltroRangoFechas()==true){
				busquedaReportesDto.setDia(0);
				busquedaReportesDto.setMes(0);
			}
			if(!busquedaReportesDto.getEntidadIdArray().stream().filter(r -> r==0).collect(Collectors.toList()).isEmpty()){
				List<EntidadEntity> lstEntidad = entidadService.findAllEntidades();
				List<Long> lstEntidad2 = new ArrayList<>();
				for (EntidadEntity objEntida :lstEntidad) {
					lstEntidad2.add(objEntida.getEntidadId());
				}
				busquedaReportesDto.setEntidadIdArray(lstEntidad2);
			}else if (busquedaReportesDto.getEntidadIdArray().isEmpty()){
                tieneEntidad = false;
            }
			if(!busquedaReportesDto.getRecaudadorIdArray().stream().filter(r -> r==0).collect(Collectors.toList()).isEmpty()){
				List<RecaudadorEntity> lstRecaudador = recaudadoraService.findAllRecaudadora();
				List<Long> lstRecaudador2 = new ArrayList<>();
				for (RecaudadorEntity objRecaudador :lstRecaudador) {
					lstRecaudador2.add(objRecaudador.getRecaudadorId());
				}
				busquedaReportesDto.setRecaudadorIdArray(lstRecaudador2);
			}else if (busquedaReportesDto.getRecaudadorIdArray().isEmpty()){
                tieneRecaudador =false;
            }
			if(!busquedaReportesDto.getEstadoArray().stream().filter(r -> r.equals("0")).collect(Collectors.toList()).isEmpty()){
				List<EstadoTablasDto> lstEstadoHistorico = historicoDeudaService.findEstadoHistorico();
				List<String> lstEstado = new ArrayList<>();
				for (EstadoTablasDto objEstado :lstEstadoHistorico) {
					lstEstado.add(objEstado.estadoId);
				}
				busquedaReportesDto.setEstadoArray(lstEstado);
			}else if (busquedaReportesDto.getEstadoArray().isEmpty()){
                tieneEstado = false;
            }

			Page<DeudasClienteAdmv2Dto> deudasClienteDtoList = this.reporteAdminService.findDeudasByParameterv2(
					busquedaReportesDto.getFechaInicio(), busquedaReportesDto.getFechaFin(), busquedaReportesDto.getEntidadIdArray(),
					busquedaReportesDto.getRecaudadorIdArray(), busquedaReportesDto.getEstadoArray(), busquedaReportesDto.getPaginacion() - 1,
					10,   tieneEntidad,  tieneRecaudador,tieneEstado, busquedaReportesDto.getFiltroRangoFechas (),
					busquedaReportesDto.getMes(), busquedaReportesDto.getDia());
			if (deudasClienteDtoList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("data", deudasClienteDtoList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} catch (Exception e) {
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES");
			log.setController("api/ReportAdmin/findDeudasByParameter");
			log.setMensaje(e.getMessage());
			log.setCausa(e.getCause().toString());
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			this.logSistemaService.save(log);
			this.logger.error("This is cause", e.getMessage());

			response.put("mensaje", "Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("codigo", log.getLogSistemaId() + "");
			response.put("status", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}

	@PostMapping(path = "/findReporteCobroSocio")
	public ResponseEntity<?> findReporteCobroSocio(@RequestBody BusquedaReportesAdmv2Dto busquedaReportesDto, Authentication authentication) throws Exception {
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		Map<String, Object> response = new HashMap<>();
		boolean tieneEntidad = true;
		boolean tieneRecaudador = true;
		boolean tieneEstado = true;
		try {

			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			if (busquedaReportesDto.getFechaInicio() == null) {
				busquedaReportesDto.setFechaInicio(Util.stringToDate("01/01/2021"));
			}
			if (busquedaReportesDto.getFechaFin() == null) {
				busquedaReportesDto.setFechaFin(Util.stringToDate("01/01/2100"));
			}
			if(busquedaReportesDto.getFiltroRangoFechas()==true){
				busquedaReportesDto.setDia(0);
				busquedaReportesDto.setMes(0);
				busquedaReportesDto.setAnio(0);
			}
			if(!busquedaReportesDto.getEntidadIdArray().stream().filter(r -> r==0).collect(Collectors.toList()).isEmpty()){
				List<EntidadEntity> lstEntidad = entidadService.findAllEntidades();
				List<Long> lstEntidad2 = new ArrayList<>();
				for (EntidadEntity objEntida :lstEntidad) {
					lstEntidad2.add(objEntida.getEntidadId());
				}
				busquedaReportesDto.setEntidadIdArray(lstEntidad2);
			}else if (busquedaReportesDto.getEntidadIdArray().isEmpty()){
				tieneEntidad = false;
			}
			if(!busquedaReportesDto.getRecaudadorIdArray().stream().filter(r -> r==0).collect(Collectors.toList()).isEmpty()){
				List<RecaudadorEntity> lstRecaudador = recaudadoraService.findAllRecaudadora();
				List<Long> lstRecaudador2 = new ArrayList<>();
				for (RecaudadorEntity objRecaudador :lstRecaudador) {
					lstRecaudador2.add(objRecaudador.getRecaudadorId());
				}
				busquedaReportesDto.setRecaudadorIdArray(lstRecaudador2);
			}else if (busquedaReportesDto.getRecaudadorIdArray().isEmpty()){
				tieneRecaudador =false;
			}
			if(!busquedaReportesDto.getEstadoArray().stream().filter(r -> r.equals("0")).collect(Collectors.toList()).isEmpty()){
				List<EstadoTablasDto> lstEstadoHistorico = historicoDeudaService.findEstadoHistorico();
				List<String> lstEstado = new ArrayList<>();
				for (EstadoTablasDto objEstado :lstEstadoHistorico) {
					lstEstado.add(objEstado.estadoId);
				}
				busquedaReportesDto.setEstadoArray(lstEstado);
			}else if (busquedaReportesDto.getEstadoArray().isEmpty()){
				tieneEstado = false;
			}

			Page<ReporteAdminSocioDto> deudasClienteDtoList = this.reporteAdminService.findReporteAdminCobroSocio(
					busquedaReportesDto.getFechaInicio(), busquedaReportesDto.getFechaFin(), busquedaReportesDto.getEntidadIdArray(),
					busquedaReportesDto.getRecaudadorIdArray(), busquedaReportesDto.getEstadoArray(), busquedaReportesDto.getPaginacion() - 1,
					10,   tieneEntidad,  tieneRecaudador,tieneEstado, busquedaReportesDto.getFiltroRangoFechas (),
					busquedaReportesDto.getAnio(),busquedaReportesDto.getMes(), busquedaReportesDto.getDia());
			if (deudasClienteDtoList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("data", deudasClienteDtoList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} catch (Exception e) {
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES");
			log.setController("api/ReportAdmin/findReporteCobroSocio");
			log.setMensaje(e.getMessage());
			log.setCausa(e.getCause().toString());
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			this.logSistemaService.save(log);
			this.logger.error("This is cause", e.getMessage());

			response.put("mensaje", "Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("codigo", log.getLogSistemaId() + "");
			response.put("status", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}
	@PostMapping(path = "/findReporteCobroEmpresa")
	public ResponseEntity<?> findReporteCobroEmpresa(@RequestBody BusquedaReportesAdmv2Dto busquedaReportesDto, Authentication authentication) throws Exception {
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		Map<String, Object> response = new HashMap<>();
		boolean tieneEntidad = true;
		boolean tieneRecaudador = true;
		boolean tieneEstado = true;
		try {

			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			if (busquedaReportesDto.getFechaInicio() == null) {
				busquedaReportesDto.setFechaInicio(Util.stringToDate("01/01/2021"));
			}
			if (busquedaReportesDto.getFechaFin() == null) {
				busquedaReportesDto.setFechaFin(Util.stringToDate("01/01/2100"));
			}
			if(busquedaReportesDto.getFiltroRangoFechas()==true){
				busquedaReportesDto.setDia(0);
				busquedaReportesDto.setMes(0);
				busquedaReportesDto.setAnio(0);
			}
			if(!busquedaReportesDto.getEntidadIdArray().stream().filter(r -> r==0).collect(Collectors.toList()).isEmpty()){
				List<EntidadEntity> lstEntidad = entidadService.findAllEntidades();
				List<Long> lstEntidad2 = new ArrayList<>();
				for (EntidadEntity objEntida :lstEntidad) {
					lstEntidad2.add(objEntida.getEntidadId());
				}
				busquedaReportesDto.setEntidadIdArray(lstEntidad2);
			}else if (busquedaReportesDto.getEntidadIdArray().isEmpty()){
				tieneEntidad = false;
			}
			if(!busquedaReportesDto.getRecaudadorIdArray().stream().filter(r -> r==0).collect(Collectors.toList()).isEmpty()){
				List<RecaudadorEntity> lstRecaudador = recaudadoraService.findAllRecaudadora();
				List<Long> lstRecaudador2 = new ArrayList<>();
				for (RecaudadorEntity objRecaudador :lstRecaudador) {
					lstRecaudador2.add(objRecaudador.getRecaudadorId());
				}
				busquedaReportesDto.setRecaudadorIdArray(lstRecaudador2);
			}else if (busquedaReportesDto.getRecaudadorIdArray().isEmpty()){
				tieneRecaudador =false;
			}
			if(!busquedaReportesDto.getEstadoArray().stream().filter(r -> r.equals("0")).collect(Collectors.toList()).isEmpty()){
				List<EstadoTablasDto> lstEstadoHistorico = historicoDeudaService.findEstadoHistorico();
				List<String> lstEstado = new ArrayList<>();
				for (EstadoTablasDto objEstado :lstEstadoHistorico) {
					lstEstado.add(objEstado.estadoId);
				}
				busquedaReportesDto.setEstadoArray(lstEstado);
			}else if (busquedaReportesDto.getEstadoArray().isEmpty()){
				tieneEstado = false;
			}

			Page<ReporteAdminEmpresaDto> deudasClienteDtoList = this.reporteAdminService.findReporteAdminCobroEmpresa(
					busquedaReportesDto.getFechaInicio(), busquedaReportesDto.getFechaFin(), busquedaReportesDto.getEntidadIdArray(),
					busquedaReportesDto.getRecaudadorIdArray(), busquedaReportesDto.getEstadoArray(), busquedaReportesDto.getPaginacion() - 1,
					10,   tieneEntidad,  tieneRecaudador,tieneEstado, busquedaReportesDto.getFiltroRangoFechas (),
					busquedaReportesDto.getAnio(),busquedaReportesDto.getMes(), busquedaReportesDto.getDia());
			if (deudasClienteDtoList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("data", deudasClienteDtoList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} catch (Exception e) {
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES");
			log.setController("api/ReportAdmin/findDeudasByParameter");
			log.setMensaje(e.getMessage());
			log.setCausa(e.getCause().toString());
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			this.logSistemaService.save(log);
			this.logger.error("This is cause", e.getMessage());

			response.put("mensaje", "Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("codigo", log.getLogSistemaId() + "");
			response.put("status", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}
	/*@PostMapping(path = "/findDeudasByParameterForReport")
	public ResponseEntity<?> findDeudasByParameterForReport(@RequestBody BusquedaReportesAdmv2Dto busquedaReportesDto,
			Authentication authentication) throws Exception {
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		boolean tieneEntidad = true;
		boolean tieneRecaudador = true;
		boolean tieneEstado = true;
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> parameters = new HashMap<>();
		try {
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			if (busquedaReportesDto.getFechaInicio() == null && busquedaReportesDto.getFechaFin() == null) {
				parameters.put("tituloRangoFechas", "");
			} else if (busquedaReportesDto.getFechaInicio() == null && busquedaReportesDto.getFechaFin() != null) {
				parameters.put("tituloRangoFechas","REPORTE GENERADO HASTA LA FECHA : " + Util.dateToStringFormat(busquedaReportesDto.getFechaFin()));
			} else if (busquedaReportesDto.getFechaInicio() != null && busquedaReportesDto.getFechaFin() == null) {
				parameters.put("tituloRangoFechas", "REPORTE GENERADO A PARTIR DE LA FECHA : "+ Util.dateToStringFormat(busquedaReportesDto.getFechaFin()));
			} else {
				parameters.put("tituloRangoFechas","REPORTE GENERADO EN EL RANGO DE FECHAS : "+ Util.dateToStringFormat(busquedaReportesDto.getFechaInicio()) + "-" + Util.dateToStringFormat(busquedaReportesDto.getFechaFin()));
			}

			if (busquedaReportesDto.getFechaInicio() == null) {
				busquedaReportesDto.setFechaInicio(Util.stringToDate("01/01/2021"));
			}
			if (busquedaReportesDto.getFechaFin() == null) {
				busquedaReportesDto.setFechaFin(Util.stringToDate("01/01/2100"));
			}
            if(busquedaReportesDto.getFiltroRangoFechas()==true){
                busquedaReportesDto.setDia(0);
                busquedaReportesDto.setMes(0);
            }
			if(!busquedaReportesDto.getEntidadIdArray().stream().filter(r -> r==0).collect(Collectors.toList()).isEmpty()){
				List<EntidadEntity> lstEntidad = entidadService.findAllEntidades();
				List<Long> lstEntidad2 = new ArrayList<>();
				for (EntidadEntity objEntida :lstEntidad) {
					lstEntidad2.add(objEntida.getEntidadId());
				}
				busquedaReportesDto.setEntidadIdArray(lstEntidad2);
			}else if (busquedaReportesDto.getEntidadIdArray().isEmpty()){
				tieneEntidad = false;
			}
			if(!busquedaReportesDto.getRecaudadorIdArray().stream().filter(r -> r==0).collect(Collectors.toList()).isEmpty()){
				List<RecaudadorEntity> lstRecaudador = recaudadoraService.findAllRecaudadora();
				List<Long> lstRecaudador2 = new ArrayList<>();
				for (RecaudadorEntity objRecaudador :lstRecaudador) {
					lstRecaudador2.add(objRecaudador.getRecaudadorId());
				}
				busquedaReportesDto.setRecaudadorIdArray(lstRecaudador2);
			}else if (busquedaReportesDto.getRecaudadorIdArray().isEmpty()){
				tieneRecaudador =false;
			}

			parameters.put("tituloReporte", " REPORTE GENERAL DE DEUDAS");
			if(!busquedaReportesDto.getEstadoArray().stream().filter(r -> r.equals("0")).collect(Collectors.toList()).isEmpty()){
				List<EstadoTablasDto> lstEstadoHistorico = historicoDeudaService.findEstadoHistorico();
				List<String> lstEstado = new ArrayList<>();
				for (EstadoTablasDto objEstado :lstEstadoHistorico) {
					lstEstado.add(objEstado.estadoId);
				}
				busquedaReportesDto.setEstadoArray(lstEstado);
				parameters.put("tituloReporte", " REPORTE GENERAL DE DEUDAS");
			}else if (busquedaReportesDto.getEstadoArray().isEmpty()){
				tieneEstado = false;
			}

			parameters.put("logoTesla", filesReport + "/img/quickpaypng.png");

			if (busquedaReportesDto.getExport().equals("msexcel")) {
				parameters.put("IS_IGNORE_PAGINATION", true);
			}
			Page<DeudasClienteAdmv2Dto> deudasClienteDtoList = this.reporteAdminService.findDeudasByParameterv2(
					busquedaReportesDto.getFechaInicio(), busquedaReportesDto.getFechaFin(), busquedaReportesDto.getEntidadIdArray(),
					busquedaReportesDto.getRecaudadorIdArray(), busquedaReportesDto.getEstadoArray(), busquedaReportesDto.getPaginacion() - 1,
					10,   tieneEntidad,  tieneRecaudador,tieneEstado, busquedaReportesDto.getFiltroRangoFechas (),
					busquedaReportesDto.getMes(), busquedaReportesDto.getDia());


			if (deudasClienteDtoList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
			}
			File file = ResourceUtils.getFile(filesReport + "/report_jrxml/reportes/administracion/ListaDeudasAdmin.jrxml");
			JasperReport jasper = JasperCompileManager.compileReport(file.getAbsolutePath());
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(deudasClienteDtoList.stream().toList());
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, parameters,ds);

			byte[] report = Util.jasperExportFormat(jasperPrint, busquedaReportesDto.getExport(), filesReport);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentLength(report.length);
			headers.setContentType(MediaType.parseMediaType("application/" + busquedaReportesDto.getExport()));
			headers.set("Content-Disposition", "inline; filename=report." + busquedaReportesDto.getExport());
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			return new ResponseEntity<byte[]>(report, headers, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES");
			log.setController("api/ReportAdmin/findDeudasByParameterForReport");
			log.setMensaje(e.getMessage());
			log.setCausa(e.getCause().toString());
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			log=this.logSistemaService.save(log);
			this.logger.error("This is cause", e.getMessage());

			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("codigo", log.getLogSistemaId() + "");
			response.put("status", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
	}*/
	@PostMapping(path = "/findCobrosByParameterForReportSocioAdmin")
	public ResponseEntity<?> findCobrosByParameterForReportSocioAdmin(@RequestBody BusquedaReportesAdmv2Dto busquedaReportesDto,
															Authentication authentication) throws Exception {
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		boolean tieneEntidad = true;
		boolean tieneRecaudador = true;
		boolean tieneEstado = true;
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> parameters = new HashMap<>();
		try {
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			if (busquedaReportesDto.getFechaInicio() == null && busquedaReportesDto.getFechaFin() == null) {
				parameters.put("tituloRangoFechas", "");
			} else if (busquedaReportesDto.getFechaInicio() == null && busquedaReportesDto.getFechaFin() != null) {
				parameters.put("tituloRangoFechas","REPORTE GENERADO HASTA LA FECHA : " + Util.dateToStringFormat(busquedaReportesDto.getFechaFin()));
			} else if (busquedaReportesDto.getFechaInicio() != null && busquedaReportesDto.getFechaFin() == null) {
				parameters.put("tituloRangoFechas", "REPORTE GENERADO A PARTIR DE LA FECHA : "+ Util.dateToStringFormat(busquedaReportesDto.getFechaFin()));
			} else {
				parameters.put("tituloRangoFechas","REPORTE GENERADO EN EL RANGO DE FECHAS : "+ Util.dateToStringFormat(busquedaReportesDto.getFechaInicio()) + "-" + Util.dateToStringFormat(busquedaReportesDto.getFechaFin()));
			}

			if (busquedaReportesDto.getFechaInicio() == null) {
				busquedaReportesDto.setFechaInicio(Util.stringToDate("01/01/2021"));
			}
			if (busquedaReportesDto.getFechaFin() == null) {
				busquedaReportesDto.setFechaFin(Util.stringToDate("01/01/2100"));
			}
			if(busquedaReportesDto.getFiltroRangoFechas()==true){
				busquedaReportesDto.setDia(0);
				busquedaReportesDto.setMes(0);
			}
			if(!busquedaReportesDto.getEntidadIdArray().stream().filter(r -> r==0).collect(Collectors.toList()).isEmpty()){
				List<EntidadEntity> lstEntidad = entidadService.findAllEntidades();
				List<Long> lstEntidad2 = new ArrayList<>();
				for (EntidadEntity objEntida :lstEntidad) {
					lstEntidad2.add(objEntida.getEntidadId());
				}
				busquedaReportesDto.setEntidadIdArray(lstEntidad2);
			}else if (busquedaReportesDto.getEntidadIdArray().isEmpty()){
				tieneEntidad = false;
			}
			if(!busquedaReportesDto.getRecaudadorIdArray().stream().filter(r -> r==0).collect(Collectors.toList()).isEmpty()){
				List<RecaudadorEntity> lstRecaudador = recaudadoraService.findAllRecaudadora();
				List<Long> lstRecaudador2 = new ArrayList<>();
				for (RecaudadorEntity objRecaudador :lstRecaudador) {
					lstRecaudador2.add(objRecaudador.getRecaudadorId());
				}
				busquedaReportesDto.setRecaudadorIdArray(lstRecaudador2);
			}else if (busquedaReportesDto.getRecaudadorIdArray().isEmpty()){
				tieneRecaudador =false;
			}

			parameters.put("tituloReporte", " REPORTE GENERAL DE COBROS");
			if(!busquedaReportesDto.getEstadoArray().stream().filter(r -> r.equals("0")).collect(Collectors.toList()).isEmpty()){
				List<EstadoTablasDto> lstEstadoHistorico = historicoDeudaService.findEstadoHistorico();
				List<String> lstEstado = new ArrayList<>();
				for (EstadoTablasDto objEstado :lstEstadoHistorico) {
					lstEstado.add(objEstado.estadoId);
				}
				busquedaReportesDto.setEstadoArray(lstEstado);
				parameters.put("tituloReporte", " REPORTE GENERAL DE COBROS");
			}else if (busquedaReportesDto.getEstadoArray().isEmpty()){
				tieneEstado = false;
			}
			parameters.put("logoTesla", filesReport + "/img/logo_fondo_blanco.jpg");

			if (busquedaReportesDto.getExport().equals("msexcel")) {
				parameters.put("IS_IGNORE_PAGINATION", true);
			}
			List<ReporteAdminSocioDto> deudasClienteDtoList = this.reporteAdminService.findReporteAdminCobroSocioJasper(
					busquedaReportesDto.getFechaInicio(), busquedaReportesDto.getFechaFin(), busquedaReportesDto.getEntidadIdArray(),
					busquedaReportesDto.getRecaudadorIdArray(), busquedaReportesDto.getEstadoArray(),  tieneEntidad,  tieneRecaudador,
					tieneEstado, busquedaReportesDto.getFiltroRangoFechas (),
					busquedaReportesDto.getAnio(),busquedaReportesDto.getMes(), busquedaReportesDto.getDia());


			if (deudasClienteDtoList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
			}



			File file = ResourceUtils.getFile(filesReport + "/report_jrxml/reportes/administracion/CobrosSocioAdmin.jrxml");
			JasperReport jasper = JasperCompileManager.compileReport(file.getAbsolutePath());
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(deudasClienteDtoList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, parameters,ds);

			byte[] report = Util.jasperExportFormat(jasperPrint, busquedaReportesDto.getExport(), filesReport);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentLength(report.length);
			headers.setContentType(MediaType.parseMediaType("application/" + busquedaReportesDto.getExport()));
			headers.set("Content-Disposition", "inline; filename=report." + busquedaReportesDto.getExport());
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			return new ResponseEntity<byte[]>(report, headers, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES");
			log.setController("api/ReportAdmin/findCobrosByParameterForReportSocioAdmin");
			log.setMensaje(e.getMessage());
			log.setCausa(e.getCause().toString());
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			log=this.logSistemaService.save(log);
			this.logger.error("This is cause", e.getMessage());

			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("codigo", log.getLogSistemaId() + "");
			response.put("status", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}

	@PostMapping(path = "/findCobrosByParameterForReportEmpresaAdmin")
	public ResponseEntity<?> findCobrosByParameterForReportEmpresaAdmin(@RequestBody BusquedaReportesAdmv2Dto busquedaReportesDto,
																	  Authentication authentication) throws Exception {
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		boolean tieneEntidad = true;
		boolean tieneRecaudador = true;
		boolean tieneEstado = true;
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> parameters = new HashMap<>();
		try {
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			if (busquedaReportesDto.getFechaInicio() == null && busquedaReportesDto.getFechaFin() == null) {
				parameters.put("tituloRangoFechas", "");
			} else if (busquedaReportesDto.getFechaInicio() == null && busquedaReportesDto.getFechaFin() != null) {
				parameters.put("tituloRangoFechas","REPORTE GENERADO HASTA LA FECHA : " + Util.dateToStringFormat(busquedaReportesDto.getFechaFin()));
			} else if (busquedaReportesDto.getFechaInicio() != null && busquedaReportesDto.getFechaFin() == null) {
				parameters.put("tituloRangoFechas", "REPORTE GENERADO A PARTIR DE LA FECHA : "+ Util.dateToStringFormat(busquedaReportesDto.getFechaFin()));
			} else {
				parameters.put("tituloRangoFechas","REPORTE GENERADO EN EL RANGO DE FECHAS : "+ Util.dateToStringFormat(busquedaReportesDto.getFechaInicio()) + " - " + Util.dateToStringFormat(busquedaReportesDto.getFechaFin()));
			}

			if (busquedaReportesDto.getFechaInicio() == null) {
				busquedaReportesDto.setFechaInicio(Util.stringToDate("01/01/2021"));
			}
			if (busquedaReportesDto.getFechaFin() == null) {
				busquedaReportesDto.setFechaFin(Util.stringToDate("01/01/2100"));
			}
			if(busquedaReportesDto.getFiltroRangoFechas()==true){
				busquedaReportesDto.setDia(0);
				busquedaReportesDto.setMes(0);
			}
			if(!busquedaReportesDto.getEntidadIdArray().stream().filter(r -> r==0).collect(Collectors.toList()).isEmpty()){
				List<EntidadEntity> lstEntidad = entidadService.findAllEntidades();
				List<Long> lstEntidad2 = new ArrayList<>();
				for (EntidadEntity objEntida :lstEntidad) {
					lstEntidad2.add(objEntida.getEntidadId());
				}
				busquedaReportesDto.setEntidadIdArray(lstEntidad2);
			}else if (busquedaReportesDto.getEntidadIdArray().isEmpty()){
				tieneEntidad = false;
			}
			if(!busquedaReportesDto.getRecaudadorIdArray().stream().filter(r -> r==0).collect(Collectors.toList()).isEmpty()){
				List<RecaudadorEntity> lstRecaudador = recaudadoraService.findAllRecaudadora();
				List<Long> lstRecaudador2 = new ArrayList<>();
				for (RecaudadorEntity objRecaudador :lstRecaudador) {
					lstRecaudador2.add(objRecaudador.getRecaudadorId());
				}
				busquedaReportesDto.setRecaudadorIdArray(lstRecaudador2);
			}else if (busquedaReportesDto.getRecaudadorIdArray().isEmpty()){
				tieneRecaudador =false;
			}

			parameters.put("tituloReporte", " REPORTE GENERAL DE COBROS");
			if(!busquedaReportesDto.getEstadoArray().stream().filter(r -> r.equals("0")).collect(Collectors.toList()).isEmpty()){
				List<EstadoTablasDto> lstEstadoHistorico = historicoDeudaService.findEstadoHistorico();
				List<String> lstEstado = new ArrayList<>();
				for (EstadoTablasDto objEstado :lstEstadoHistorico) {
					lstEstado.add(objEstado.estadoId);
				}
				busquedaReportesDto.setEstadoArray(lstEstado);
			}else if (busquedaReportesDto.getEstadoArray().isEmpty()){
				tieneEstado = false;
			}
			parameters.put("logoTesla", filesReport + "/img/logo_fondo_blanco.jpg");

			if (busquedaReportesDto.getExport().equals("msexcel")) {
				parameters.put("IS_IGNORE_PAGINATION", true);
			}

			 List<ReporteAdminEmpresaDto> deudasClienteDtoList = this.reporteAdminService.findReporteAdminCobroEmpresaJasper(
					busquedaReportesDto.getFechaInicio(), busquedaReportesDto.getFechaFin(), busquedaReportesDto.getEntidadIdArray(),
					busquedaReportesDto.getRecaudadorIdArray(), busquedaReportesDto.getEstadoArray(),
					  tieneEntidad,  tieneRecaudador,tieneEstado, busquedaReportesDto.getFiltroRangoFechas (), busquedaReportesDto.getAnio(),
					busquedaReportesDto.getMes(), busquedaReportesDto.getDia());


			if (deudasClienteDtoList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
			}



			File file = ResourceUtils.getFile(filesReport + "/report_jrxml/reportes/administracion/CobrosEmpresaAdmin.jrxml");
			JasperReport jasper = JasperCompileManager.compileReport(file.getAbsolutePath());



			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(deudasClienteDtoList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, parameters,ds);

			byte[] report = Util.jasperExportFormat(jasperPrint, busquedaReportesDto.getExport(), filesReport);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentLength(report.length);
			headers.setContentType(MediaType.parseMediaType("application/" + busquedaReportesDto.getExport()));
			headers.set("Content-Disposition", "inline; filename=report." + busquedaReportesDto.getExport());
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			return new ResponseEntity<byte[]>(report, headers, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES");
			log.setController("api/ReportAdmin/findDeudasByParameterForReportEmpresaAdmin");
			log.setMensaje(e.getMessage());
			log.setCausa(e.getCause().toString());
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			log=this.logSistemaService.save(log);
			this.logger.error("This is cause", e.getMessage());

			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("codigo", log.getLogSistemaId() + "");
			response.put("status", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}

}
