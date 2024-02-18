package bo.com.tesla.administracion.controllers;

import bo.com.tesla.administracion.dto.CredencialFacturacionDto;
import bo.com.tesla.administracion.dto.SucursalEntidadAdmDto;
import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.administracion.services.ISucursalEntidadService;
import bo.com.tesla.entidades.services.IEntidadService;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("api/sucursalesentidades")
public class SucursalEntidadController {
	private Logger logger = LoggerFactory.getLogger(SucursalEntidadController.class);

	@Autowired
	private ISucursalEntidadService iSucursalEntidadService;

	@Autowired
	private ILogSistemaService logSistemaService;

	@Autowired
	private ISegUsuarioService segUsuarioService;

	@Autowired
	private IEntidadService entidadService;

	/*********************ABM**************************/

	@PostMapping("")
	public ResponseEntity<?> addUpdateSucursalEntidad(@Valid @RequestBody SucursalEntidadAdmDto sucursalEntidadAdmDto,
													  Authentication authentication) {
		SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
		Map<String, Object> response = new HashMap<>();
		try {
			Boolean esModificacion = sucursalEntidadAdmDto.sucursalEntidadId != null;
			sucursalEntidadAdmDto = iSucursalEntidadService.addUpdateSucursalEntidad(sucursalEntidadAdmDto, usuario.getUsuarioId());
			response.put("status", true);
			response.put("message", esModificacion ? "Se realizó la actualización del registro correctamente." : "Se realizó el registro correctamente");
			response.put("result", sucursalEntidadAdmDto);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Technicalexception e) {
			e.printStackTrace();
			LogSistemaEntity log=new LogSistemaEntity();
			log.setModulo("ADMINISTRACION.SUCURSALENTIDAD");
			log.setController("api/adm/sucursalesentidades");
			log.setCausa(e.getCause() != null ? e.getCause().getCause()+"" : e.getCause()+"");
			log.setMensaje(e.getMessage()+"");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause() != null ? e.getCause().getCause()+"" : e.getCause()+"");
			response.put("status", false);
			response.put("result", null);
			response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("code", log.getLogSistemaId()+"");
			return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (BusinesException e) {
			LogSistemaEntity log=new LogSistemaEntity();
			log.setModulo("FACTURAS");
			log.setController("api/facturas/filters");
			log.setMensaje(e.getMessage());
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			this.logSistemaService.save(log);
			this.logger.error("This is cause", e.getMessage());
			response.put("status", false);
			response.put("message", e.getMessage());
			response.put("code", log.getLogSistemaId()+"");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/{sucursalEntidadId}/{transaccion}")
	public ResponseEntity<?> setTransaccion(@PathVariable Long sucursalEntidadId,
											@PathVariable String transaccion,
											Authentication authentication) {
		SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
		Map<String, Object> response = new HashMap<>();
		try {
			iSucursalEntidadService.setTransaccionSucursalEntidad(sucursalEntidadId, transaccion, usuario.getUsuarioId());
			response.put("status", true);
			response.put("message", "Se realizó la actualización de la transacción correctamente.");
			response.put("result", true);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Technicalexception e) {
			e.printStackTrace();
			LogSistemaEntity log=new LogSistemaEntity();
			log.setModulo("ADMINISTRACION.SUCURSALENTIDAD");
			log.setController("api/sucursalesentidades/" + sucursalEntidadId + "/" + transaccion);
			log.setCausa(e.getCause()+"");
			log.setMensaje(e.getMessage()+"");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("status", false);
			response.put("result", null);
			response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("code", log.getLogSistemaId()+"");
			return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/listas/{transaccion}")
	public ResponseEntity<?> setTransaccion(@RequestBody List<Long> sucursalEntidadIdLst,
											@PathVariable String transaccion,
											Authentication authentication) {
		SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
		Map<String, Object> response = new HashMap<>();
		try {
			iSucursalEntidadService.setLstTransaccion(sucursalEntidadIdLst, transaccion, usuario.getUsuarioId());
			response.put("status", true);
			response.put("message", "Se realizó la actualización de la transacción correctamente.");
			response.put("result", true);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Technicalexception e) {
			e.printStackTrace();
			LogSistemaEntity log=new LogSistemaEntity();
			log.setModulo("ADMINISTRACION.SUCURSALENTIDAD");
			log.setController("api/sucursalesentidades/listas/"+ transaccion);
			log.setCausa(e.getCause()+"");
			log.setMensaje(e.getMessage()+"");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("status", false);
			response.put("result", null);
			response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("code", log.getLogSistemaId()+"");
			return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/{sucursalEntidadId}")
	public ResponseEntity<?> getSucursalEntidadById(@PathVariable Long sucursalEntidadId,
													Authentication authentication) {
		SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
		Map<String, Object> response = new HashMap<>();
		try {
			SucursalEntidadAdmDto sucursalEntidadAdmDto = iSucursalEntidadService.getSucursalEntidadById(sucursalEntidadId);
			if(sucursalEntidadAdmDto != null) {
				response.put("status", true);
				response.put("message", "El registro fue encontrado.");
				response.put("result", sucursalEntidadAdmDto);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("status", false);
				response.put("message", "El registro no fue encontrado.");
				response.put("result", null);
				return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
			}

		} catch (Technicalexception e) {
			e.printStackTrace();
			LogSistemaEntity log=new LogSistemaEntity();
			log.setModulo("ADMINISTRACION.SUCURSALENDTIDAD");
			log.setController("api/sucursalesentidades/" + sucursalEntidadId);
			log.setCausa(e.getCause()+"");
			log.setMensaje(e.getMessage()+"");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("status", false);
			response.put("result", null);
			response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("code", log.getLogSistemaId()+"");
			return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("")
	public ResponseEntity<?> getListSucursalesEntidades(Authentication authentication) {
		SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
		Map<String, Object> response = new HashMap<>();
		try {
			List<SucursalEntidadAdmDto> sucursalEntidadAdmDtos = iSucursalEntidadService.getAllSucursalEntidades();
			if(!sucursalEntidadAdmDtos.isEmpty()) {
				response.put("status", true);
				response.put("message", "El listado fue encontrado.");
				response.put("result", sucursalEntidadAdmDtos);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("status", false);
				response.put("message", "El listado no fue encontrado.");
				response.put("result", null);
				return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
			}
		} catch (Technicalexception e) {
			e.printStackTrace();
			LogSistemaEntity log=new LogSistemaEntity();
			log.setModulo("ADMINISTRACION.SUCURSALENTIDAD");
			log.setController("api/sucursalentidades");
			log.setCausa(e.getCause()+"");
			log.setMensaje(e.getMessage()+"");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("status", false);
			response.put("result", null);
			response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("code", log.getLogSistemaId()+"");
			return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/entidades/{entidadId}")
	public ResponseEntity<?> getListSucursalesEntidades(@PathVariable Long entidadId,
														Authentication authentication) {
		SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
		Map<String, Object> response = new HashMap<>();
		try {
			List<SucursalEntidadAdmDto> sucursalEntidadAdmDtos = iSucursalEntidadService.getLisSucursalEntidadesByEntidadId(entidadId);
			if(!sucursalEntidadAdmDtos.isEmpty()) {
				response.put("status", true);
				response.put("message", "El listado fue encontrado.");
				response.put("result", sucursalEntidadAdmDtos);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("status", false);
				response.put("message", "El listado no fue encontrado.");
				response.put("result", null);
				return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
			}
		} catch (Technicalexception e) {
			e.printStackTrace();
			LogSistemaEntity log=new LogSistemaEntity();
			log.setModulo("ADMINISTRACION.SUCURSALENTIDAD");
			log.setController("api/sucursalentidades");
			log.setCausa(e.getCause()+"");
			log.setMensaje(e.getMessage()+"");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("status", false);
			response.put("result", null);
			response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("code", log.getLogSistemaId()+"");
			return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}


	@GetMapping(path = {"/entidades/{entidadId}", "/entidades"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getListSucursalesEntidades(@PathVariable(name = "entidadId", required = false) Optional<Long> entidadId,
														Authentication authentication) {
		SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
		Map<String, Object> response = new HashMap<>();
		try {
			List<SucursalEntidadAdmDto> sucursalEntidadAdmDtos = new ArrayList<>();
			if(!entidadId.isPresent()) {
				EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
				if(entidad == null) {
					throw new BusinesException("El usuario debe pertenecer a una Entidad.");
				}
				sucursalEntidadAdmDtos = iSucursalEntidadService.getLisSucursalEntidadesByEntidadId(entidad.getEntidadId());

			} else {
				sucursalEntidadAdmDtos = iSucursalEntidadService.getLisSucursalEntidadesByEntidadId(entidadId.get());
			}
			if(!sucursalEntidadAdmDtos.isEmpty()) {
				response.put("status", true);
				response.put("message", "El listado fue encontrado.");
				response.put("result", sucursalEntidadAdmDtos);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("status", false);
				response.put("message", "El listado no fue encontrado.");
				response.put("result", null);
				return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
			}
		} catch (Technicalexception e) {
			e.printStackTrace();
			LogSistemaEntity log=new LogSistemaEntity();
			log.setModulo("ADMINISTRACION.SUCURSALENTIDAD");
			log.setController("api/sucursalentidades");
			log.setCausa(e.getCause()+"");
			log.setMensaje(e.getMessage()+"");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("status", false);
			response.put("result", null);
			response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("code", log.getLogSistemaId()+"");
			return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (BusinesException e) {
			e.printStackTrace();
			LogSistemaEntity log=new LogSistemaEntity();
			log.setModulo("FACTURAS");
			log.setController("api/facturas/filters");
			log.setMensaje(e.getMessage());
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			this.logSistemaService.save(log);
			this.logger.error("This is cause", e.getMessage());
			response.put("status", false);
			response.put("message", e.getMessage());
			response.put("code", log.getLogSistemaId()+"");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}
	@GetMapping(path = {"/entidadesForAddUser/{entidadId}"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getListSucursalesEntidadesForAddUser(@PathVariable(name = "entidadId", required = true) Long entidadId,
														Authentication authentication) {
		SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
		Map<String, Object> response = new HashMap<>();
		try {
			List<SucursalEntidadAdmDto> sucursalEntidadAdmDtos = new ArrayList<>();
			sucursalEntidadAdmDtos = iSucursalEntidadService.getLisSucursalEntidadesForAddUserByEntidadId( entidadId );
			if(!sucursalEntidadAdmDtos.isEmpty()) {
				response.put("status", true);
				response.put("message", "El listado fue encontrado.");
				response.put("result", sucursalEntidadAdmDtos);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("status", false);
				response.put("message", "El listado no fue encontrado.");
				response.put("result", null);
				return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
			}
		} catch (Exception ex){
			return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}
	@GetMapping("/entidades")
	public ResponseEntity<?> getListSucursalesEntidadesByUsuario(Authentication authentication) {
		SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
		Map<String, Object> response = new HashMap<>();
		try {
			EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
			if(entidad == null) {
				throw new Technicalexception("El usuario debe pertenecer a una Entidad.");
			}

			List<SucursalEntidadAdmDto> sucursalEntidadAdmDtos = iSucursalEntidadService.getLisSucursalEntidadesByEntidadIdActivos(entidad.getEntidadId());
			if(!sucursalEntidadAdmDtos.isEmpty()) {
				response.put("status", true);
				response.put("message", "El listado fue encontrado.");
				response.put("result", sucursalEntidadAdmDtos);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("status", false);
				response.put("message", "El listado no fue encontrado.");
				response.put("result", null);
				return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogSistemaEntity log=new LogSistemaEntity();
			log.setModulo("ADMINISTRACION.SUCURSALENTIDAD");
			log.setController("api/sucursalentidades");
			log.setCausa(e.getCause()+"");
			log.setMensaje(e.getMessage()+"");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("status", false);
			response.put("result", null);
			response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("code", log.getLogSistemaId()+"");
			return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/emisionesfacturas")
	public ResponseEntity<?> getSucursalEmtidadEmiteFacturaTesla(Authentication authentication) {
		SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
		Map<String, Object> response = new HashMap<>();
		try {
			EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
			if(entidad == null) {
				throw new Technicalexception("El usuario debe pertenecer a una Entidad.");
			}

			Optional<SucursalEntidadAdmDto> sucursalEntidadAdmDtoOptional = iSucursalEntidadService.findsucursalEmtidadEmiteFacturaTesla(entidad.getEntidadId());
			if(!sucursalEntidadAdmDtoOptional.isPresent()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			response.put("status", true);
			response.put("message", "El registro fue encontrado.");
			response.put("result", sucursalEntidadAdmDtoOptional.get());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			LogSistemaEntity log=new LogSistemaEntity();
			log.setModulo("ADMINISTRACION.SUCURSALENTIDAD");
			log.setController("api/sucursalentidades/emisionesfacturas");
			log.setCausa(e.getCause()+"");
			log.setMensaje(e.getMessage()+"");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("status", false);
			response.put("result", null);
			response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("code", log.getLogSistemaId()+"");
			return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/credenciales")
	public ResponseEntity<?> putCredencialesFacturacion(@Valid @RequestBody CredencialFacturacionDto credencialFacturacionDto,
														Authentication authentication) {
		SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
		Map<String, Object> response = new HashMap<>();
		try {
            /*EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
            if(entidad == null) {
                throw new Technicalexception("El usuario debe pertenecer a una Entidad.");
            }*/

			iSucursalEntidadService.updateCredencialesFacturacion(credencialFacturacionDto, usuario);
			response.put("status", true);
			response.put("message", "Las credenciales fueron registradas.");
			response.put("result", true);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			LogSistemaEntity log=new LogSistemaEntity();
			log.setModulo("ADMINISTRACION.SUCURSALENTIDAD");
			log.setController("api/sucursalentidades/credenciales");
			log.setCausa(e.getCause()+"");
			log.setMensaje(e.getMessage()+"");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			logSistemaService.save(log);
			this.logger.error("This is error: " + e.getMessage());
			this.logger.error("This is cause: " + e.getCause());
			response.put("status", false);
			response.put("result", null);
			response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("code", log.getLogSistemaId()+"");
			return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/credenciales/{sucursalEntidadId}")
	public ResponseEntity<?> getCredencialFacturacion(@PathVariable Long sucursalEntidadId,
													  Authentication authentication) {
		SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
		Map<String, Object> response = new HashMap<>();
		try {
			Optional<CredencialFacturacionDto> credencialFacturacionDtoOptional = iSucursalEntidadService.findCredencialFacturacion(sucursalEntidadId);
			if(!credencialFacturacionDtoOptional.isPresent()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			response.put("status", true);
			response.put("message", "Las credenciales fueron registradas.");
			response.put("result", credencialFacturacionDtoOptional.get());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			LogSistemaEntity log=new LogSistemaEntity();
			log.setModulo("ADMINISTRACION.SUCURSALENTIDAD");
			log.setController("api/sucursalentidades/credenciales/" + sucursalEntidadId);
			log.setCausa(e.getCause()+"");
			log.setMensaje(e.getMessage()+"");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("status", false);
			response.put("result", null);
			response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("code", log.getLogSistemaId()+"");
			return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}





}
