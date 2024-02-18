package bo.com.tesla.pagos.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import bo.com.tesla.administracion.entity.ArchivoEntity;
import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.administracion.entity.PServicioProductoEntity;
import bo.com.tesla.entidades.services.IArchivoService;
import bo.com.tesla.entidades.services.IEntidadService;
import bo.com.tesla.pagos.dto.PPagosDto;
import bo.com.tesla.pagos.services.IPBeneficiariosService;
import bo.com.tesla.pagos.services.IPServicioProductosService;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.cross.HandlingFiles;
import bo.com.tesla.useful.cross.Util;


@RestController
@RequestMapping("api/entidadPago")
public class EntidadPagoController {
	
	private Logger logger = LoggerFactory.getLogger(EntidadPagoController.class);
	
	@Autowired
	private ISegUsuarioService segUsuarioService;
	@Autowired
	private IEntidadService entidadService;
	@Autowired
	private IPServicioProductosService servicioProductosService;	
	@Autowired
	private IArchivoService archivoService;
	@Autowired
	private ILogSistemaService logSistemaService;
	@Autowired
	private IPBeneficiariosService abonoClienteService;
	
	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	@Qualifier("pagoClienteJob")
	Job job;
	
	
	@Value("${tesla.path.files-payments}")
	private String filesBets;
	
	@PostMapping(path = "/upload/{servicioProductosId}")
	public ResponseEntity<?> upload(@PathVariable("servicioProductosId") Long servicioProductosId,
			@RequestParam("file") MultipartFile file, Authentication authentication)  {

		Map<String, Object> response = new HashMap<>();
		ArchivoEntity archivo = new ArchivoEntity();
		String path = null;
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		PServicioProductoEntity servicioProductos = new PServicioProductoEntity();
		try {
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
			servicioProductos = this.servicioProductosService.findById(servicioProductosId).get();
			path = HandlingFiles.saveFileToDisc(file, entidad.getNombre(), filesBets);

			Long cantidadRegistros = Util.fileDataValidatePagos(path);

			archivo.setEntidadId(entidad);
			archivo.setNombre(file.getOriginalFilename());
			archivo.setPath(path);
			archivo.setUsuarioCreacion(usuario.getUsuarioId());
			archivo.setFechaCreacion(new Date());
			archivo.setNroRegistros(cantidadRegistros);
			archivo.setServicioProductoId(servicioProductos);
			archivo.setTransaccion("CREAR");
			archivo = this.archivoService.save(archivo);
			response.put("mensaje",
					"Los registros encontrados en este archivo fueron "
							+ cantidadRegistros + ". Presione en el botón 'Procesar Cargado de Datos' para cargar a los abonados.");
			response.put("archivo", archivo);
			response.put("status", true);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (BusinesException e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES PAGOS");
			log.setController("api/pagoCliente/upload/" + servicioProductosId);
			if(e.getCause()!=null) {
				log.setCausa(e.getCause().getMessage() + "");	
			}	
			log.setMensaje(e.getMessage());
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			this.logSistemaService.save(log);
			this.logger.error("This is cause", e.getMessage());

			response.put("mensaje", e.getMessage());
			response.put("codigo", log.getLogSistemaId() + "");
			response.put("status", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);

		} catch (Technicalexception e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES PAGOS");
			log.setController("api/pagoCliente/upload/" + servicioProductosId);
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
		}catch (Exception e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES PAGOS");
			log.setController("api/pagoCliente/upload/" + servicioProductosId);
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

	@PostMapping(path = "/processFile/{archivoId}")
	public ResponseEntity<?> processFile(@PathVariable("archivoId") Long archivoId, Authentication authentication)
			 {
		Map<String, Object> response = new HashMap<>();
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		ArchivoEntity archivo = new ArchivoEntity();
		if (archivoId <= 0 || archivoId.toString().isEmpty()) {
			response.put("mensaje", "Ocurrió un error en el servidor, por favor verifique los parametros de ingreso.");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {

			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			archivo = this.archivoService.findById(archivoId);
			

			JobParameters jobParameters = new JobParametersBuilder().addString("pathToFile", archivo.getPath())
					.addLong("archivoId", archivo.getArchivoId()).addLong("time", System.currentTimeMillis())
					.toJobParameters();
			JobExecution execution = jobLauncher.run(job, jobParameters);
			if (execution.getStatus() != BatchStatus.COMPLETED) {
				this.abonoClienteService.deletByArchivoId(archivo.getArchivoId());
				List<Throwable> throwList = execution.getAllFailureExceptions();
				for (Throwable throwable : throwList) {
					LogSistemaEntity log = new LogSistemaEntity();
					log.setModulo("ENTIDADES PAGO");
					log.setController("api/deudaCliente/processFile/" + archivoId);
					log.setCausa(Util.causeRow(throwable.getCause() + "") + "");
					log.setMensaje(Util.mensajeRow(throwable.getMessage() + "") + "");
					log.setUsuarioCreacion(usuario.getUsuarioId());
					log.setFechaCreacion(new Date());
					logSistemaService.save(log);
					archivo.setTransaccion("FALLAR");
					this.archivoService.save(archivo);
					this.logger.error("This is error", throwable.getMessage());
					this.logger.error("This is cause", throwable.getCause());
					response.put("mensaje", Util.mensajeRow(throwable.getMessage() + "") + "");
					response.put("detalle", Util.causeRow(throwable.getCause() + "") + "");
					response.put("codigo", log.getLogSistemaId());
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
				}
			}				
			ArchivoEntity archivoPrevious = this.archivoService.findByEstadoAndEntidadAndServicio("ACTIVO",
					archivo.getEntidadId().getEntidadId(), archivo.getServicioProductoId().getServicioProductoId());

			archivo.setFechaModificacion(new Date());
			archivo.setUsuarioModificacion(usuario.getUsuarioId());
			archivo.setTransaccion("PROCESAR");
			archivo = this.archivoService.save(archivo);

			if (archivoPrevious != null) {
				this.abonoClienteService.deletByArchivoId(archivoPrevious.getArchivoId());
				archivoPrevious.setFechaModificacion(new Date());
				archivoPrevious.setUsuarioModificacion(usuario.getUsuarioId());
				archivoPrevious.setTransaccion("DESACTIVAR");
				this.archivoService.save(archivoPrevious);
			}

			response.put("mensaje", "El archivo fue procesado con éxito ");
			response.put("archivo", archivo);
			response.put("estado", true);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (Technicalexception e) {
			
			this.abonoClienteService.deletByArchivoId(archivoId);
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADES PAGO");
			log.setController("api/pagoCliente/processFile/" + archivoId);
			if(e.getCause()!=null) {
				log.setCausa(e.getCause().getMessage() + "");	
			}			
			log.setMensaje(e.getMessage());
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			this.logSistemaService.save(log);
			archivo.setTransaccion("FALLAR");
			this.archivoService.save(archivo);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("estado", false);
			response.put("causa", "O");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);

		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			
			this.abonoClienteService.deletByArchivoId(archivoId);
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADE PAGO");
			log.setController("api/pagosCliente/processFile/" + archivoId);
			if(e.getCause()!=null) {
				log.setCausa(e.getCause().getMessage() + "");	
			}			
			log.setMensaje(e.getMessage());
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			this.logSistemaService.save(log);

			archivo.setTransaccion("FALLAR");
			this.archivoService.save(archivo);

			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("estado", false);
			response.put("causa", "O");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);

		}
	}

	@GetMapping(path = { "/findBeneficiarios/{archivoId}/{paginacion}/",
			"/findBeneficiarios/{archivoId}/{paginacion}/{paramBusqueda}", }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> findBeneficiarios(@PathVariable("archivoId") Long archivoId,
			@PathVariable("paginacion") int paginacion, @PathVariable("paramBusqueda") Optional<String> paramBusqueda,
			Authentication authentication) {
		Map<String, Object> response = new HashMap<>();
		Page<PPagosDto> beneficiarioList;
		String newParamBusqueda = "";
		SegUsuarioEntity usuario = new SegUsuarioEntity();

		if (paramBusqueda.isPresent()) {
			newParamBusqueda = paramBusqueda.get();
			paginacion = 1;
		} else {
			newParamBusqueda = "";
		}

		try {

			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			beneficiarioList = this.abonoClienteService.groupByAbonosClientes(archivoId, newParamBusqueda,
					paginacion - 1, 10);

			if (beneficiarioList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("status", "true");
			response.put("data", beneficiarioList);

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} catch (Technicalexception e) {

			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("ENTIDADE PAGO");
			log.setController(
					"api/pagosCliente/findAbonosClientes/" + archivoId + "/" + paginacion + "/" + paramBusqueda);
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

}
