package bo.com.tesla.pagos.controllers;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.PBeneficiariosEntity;
import bo.com.tesla.administracion.entity.PTransaccionPagoEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.pagos.dao.IPTransaccionPagoDao;
import bo.com.tesla.pagos.dto.PPagosDto;
import bo.com.tesla.pagos.services.IPBeneficiariosService;
import bo.com.tesla.pagos.services.IPPagoClienteService;
import bo.com.tesla.pagos.services.IPTransaccionPagoService;
import bo.com.tesla.recaudaciones.services.IRecaudadoraService;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.cross.Util;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@RestController
@RequestMapping("api/recaudadoraPagos")
public class RecaudadoraPagosController {
	private Logger logger = LoggerFactory.getLogger(RecaudadoraPagosController.class);

	@Autowired
	private ISegUsuarioService segUsuarioService;

	@Autowired
	private IRecaudadoraService recaudadoraService;

	@Autowired
	private IPBeneficiariosService abonoClienteService;

	@Autowired
	private IPPagoClienteService pagoClienteService;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private ILogSistemaService logSistemaService;

	@Autowired
	private IPTransaccionPagoService transaccionPagoService;

	@Value("${tesla.path.files-report}")
	private String filesReport;

	@GetMapping(path = { "/getBeneficiariosParaPagar/{servicioProductoId}/",
			"/getBeneficiariosParaPagar/{servicioProductoId}/{paramBusqueda}", }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getAbonosParaPagar(@PathVariable("servicioProductoId") Long servicioProductoId,
			@PathVariable("paramBusqueda") Optional<String> paramBusqueda, Authentication authentication) {
		Map<String, Object> response = new HashMap<>();
		List<PPagosDto> abonosClientesList = new ArrayList<>();
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		String newParamBusqueda = "";
		try {
			if (paramBusqueda.isPresent()) {
				newParamBusqueda = paramBusqueda.get();
			}

			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			RecaudadorEntity recaudador = this.recaudadoraService.findRecaudadorByUserId(usuario.getUsuarioId());
			abonosClientesList = this.abonoClienteService.getAbonosParaPagar(servicioProductoId,
					recaudador.getRecaudadorId(), newParamBusqueda);
			if (abonosClientesList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("data", abonosClientesList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (Technicalexception e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("RECAUDADORA PAGOS");
			log.setController(
					"api/recaudadoraPagos/getBeneficiariosParaPagar/" + servicioProductoId + "/" + paramBusqueda);
			if (e.getCause() != null) {
				log.setCausa(e.getCause().getMessage() + "");
			}
			log.setCausa(e.getCause() + "");
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

	@GetMapping(path = "/getBeneficiario/{archivoId}/{codigoCliente}/{nroDocumentoCliente}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getAbonado(@PathVariable("archivoId") Long archivoId,
			@PathVariable("codigoCliente") String codigoCliente,
			@PathVariable("nroDocumentoCliente") String nroDocumentoCliente, Authentication authentication) {
		Map<String, Object> response = new HashMap<>();
		List<PPagosDto> abonosClientesList = new ArrayList<>();
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		try {
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			abonosClientesList = this.abonoClienteService.getBeneficiarioPagos(archivoId, codigoCliente,
					nroDocumentoCliente);
			if (abonosClientesList == null) {
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("data", abonosClientesList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (Technicalexception e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("RECAUDADORA PAGOS");
			log.setController("api/recaudadoraPagos/getBeneficiario/" + archivoId + "/" + codigoCliente + "/"
					+ nroDocumentoCliente);
			if (e.getCause() != null) {
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

	@PostMapping(path = "/realizarPago")
	public ResponseEntity<?> realizarPago(@RequestBody List<PPagosDto> abonoClienteList,
			Authentication authentication) {
		Map<String, Object> parameters = new HashMap<>();
		Map<String, Object> response = new HashMap<>();
		SegUsuarioEntity usuario = new SegUsuarioEntity();

		List<PTransaccionPagoEntity> pagoClienteList = new ArrayList<>();

		BigDecimal totalPagado = new BigDecimal(0);
		String codigoTransaccion = null;
		try {
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			List<PTransaccionPagoEntity> transaccionPagoList = this.pagoClienteService.realizarPago(abonoClienteList,
					usuario.getUsuarioId());

			Connection conection = null;

			for (PTransaccionPagoEntity transaccion : transaccionPagoList) {
				totalPagado = totalPagado.add(transaccion.getTotal());
				codigoTransaccion = transaccion.getCodigoTransaccion();
			}

			parameters.put("montoLiteral", Util.translate(totalPagado + ""));
			parameters.put("codigoTransaccion", codigoTransaccion);

			File file = ResourceUtils
					.getFile(filesReport + "/report_jrxml/reportes/recaudador/comprobanteMaestro.jrxml");

			JasperReport jasper = JasperCompileManager.compileReport(file.getAbsolutePath());
			conection = dataSource.getConnection();
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, parameters, conection);
			conection.close();
			byte[] report = Util.jasperExportFormat(jasperPrint, "pdf", filesReport);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentLength(report.length);
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
			headers.set("Content-Disposition", "inline; filename=report.pdf");
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			return new ResponseEntity<byte[]>(report, headers, HttpStatus.OK);

		} catch (Technicalexception e) {

			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("RECAUDADORA PAGOS");
			log.setController("api/recaudadoraPagos/realizarPago/");
			if (e.getCause() != null) {
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
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("RECAUDADORA PAGOS");
			log.setController("api/recaudadoraPagos/realizarPago/");
			if (e.getCause() != null) {
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
			// response.put("codigo", log.getLogSistemaId() + "");
			response.put("status", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping(path = "/verificarPrelacion/{archivoId}/{codigoCliente}/{nroRegistro}")
	public ResponseEntity<?> verificarPrelacion(@PathVariable("archivoId") Long archivoId,
			@PathVariable("codigoCliente") String codigoCliente, @PathVariable("nroRegistro") List<Integer> nroRegistro,
			Authentication authentication) {

		Map<String, Object> response = new HashMap<>();
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		List<PBeneficiariosEntity> beneficiarioList = new ArrayList<>();
		try {
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			beneficiarioList = this.abonoClienteService.verificarPrelacion(archivoId, codigoCliente, nroRegistro);

			response.put("data", beneficiarioList);
			if (beneficiarioList.isEmpty()) {
				response.put("prelacion", true);
			} else {
				response.put("prelacion", false);
			}

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} catch (Technicalexception e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("RECAUDADORA PAGOS");
			log.setController("api/recaudadoraPagos/realizarPago/");
			if (e.getCause() != null) {
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

	@PostMapping(path = "/reImpresionGrid", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> reImpresionGrid(@RequestBody PPagosDto beneficiario, Authentication authentication) {
		Map<String, Object> response = new HashMap<>();
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		Page<PPagosDto> transaccionPagoList;
		try {
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			if (beneficiario.fechaInicio == null) {
				beneficiario.fechaInicio = Util.stringToDate("01/01/2021");
			}
			if (beneficiario.fechaFin == null) {
				beneficiario.fechaFin = Util.stringToDate("01/01/2100");
			}
			if (beneficiario.parameter == null || beneficiario.parameter == "") {
				beneficiario.parameter = "%";
			}
			transaccionPagoList = this.transaccionPagoService.findTransaccionsByUsuario(beneficiario.fechaInicio,
					beneficiario.fechaFin, usuario.getUsuarioId(), beneficiario.parameter,
					beneficiario.servicioProductoId, beneficiario.paginacion - 1, 10);

			response.put("data", transaccionPagoList);
			if (transaccionPagoList.isEmpty()) {
				response.put("mensaje", "Informacion encontrada");
				response.put("status", true);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			} else {
				response.put("mensaje", "No se encontro Informacinón");
				response.put("status", false);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
			}

		} catch (Technicalexception e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("RECAUDADORA PAGOS");
			log.setController("api/recaudadoraPagos/realizarPago/");
			if (e.getCause() != null) {
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

	@GetMapping(path = "/imprimirComprobantePago/{codigoTransaccion}")
	public ResponseEntity<?> imprimirComprobantePago(@PathVariable("codigoTransaccion") String codigoTransaccion,
			Authentication authentication) {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> parameters = new HashMap<>();
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		List<PTransaccionPagoEntity> transaccionesPagoList = new ArrayList<>();
		BigDecimal totalPagado = new BigDecimal(0);
		Connection conection = null;
		try {
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			transaccionesPagoList = this.transaccionPagoService.transaccionessByCodigoTransacciones(codigoTransaccion);
			for (PTransaccionPagoEntity transaccion : transaccionesPagoList) {
				totalPagado = totalPagado.add(transaccion.getTotal());
			}
			parameters.put("montoLiteral", Util.translate(totalPagado + ""));
			parameters.put("codigoTransaccion", codigoTransaccion);
			File file = ResourceUtils
					.getFile(filesReport + "/report_jrxml/reportes/recaudador/comprobanteMaestro.jrxml");
			JasperReport jasper = JasperCompileManager.compileReport(file.getAbsolutePath());
			conection = dataSource.getConnection();
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, parameters, conection);
			conection.close();
			byte[] report = Util.jasperExportFormat(jasperPrint, "pdf", filesReport);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentLength(report.length);
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
			headers.set("Content-Disposition", "inline; filename=report.pdf");
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			return new ResponseEntity<byte[]>(report, headers, HttpStatus.OK);

		} catch (Technicalexception e) {
			try {
				if (conection != null) {
					conection.close();
				}
			} catch (SQLException e2) {
				this.logger.error("This is error", e2.getMessage());
				this.logger.error("This is cause", e2.getCause());
			}

			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("RECAUDADORA PAGOS");
			log.setController("api/recaudadoraPagos/realizarPago/");
			if (e.getCause() != null) {
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
		} catch (Exception e) {
			try {
				if (conection != null) {
					conection.close();
				}
			} catch (SQLException e2) {
				this.logger.error("This is error", e2.getMessage());
				this.logger.error("This is cause", e2.getCause());
			}
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("RECAUDADORA PAGOS");
			log.setController("api/recaudadoraPagos/realizarPago/");
			if (e.getCause() != null) {
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
