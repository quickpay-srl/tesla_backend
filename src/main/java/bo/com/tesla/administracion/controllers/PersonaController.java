package bo.com.tesla.administracion.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.com.tesla.administracion.dao.IEmpleadoDao;
import bo.com.tesla.administracion.dto.PersonaDto;
import bo.com.tesla.administracion.dto.RolTransferDto;
import bo.com.tesla.administracion.entity.DominioEntity;
import bo.com.tesla.administracion.entity.EmpleadoEntity;
import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.PersonaEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.administracion.entity.SegPrivilegioEntity;
import bo.com.tesla.administracion.entity.SegRolEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.administracion.entity.SucursalEntity;
import bo.com.tesla.administracion.services.IDominioService;
import bo.com.tesla.administracion.services.IEmpleadoService;
import bo.com.tesla.administracion.services.IPersonaService;
import bo.com.tesla.administracion.services.IRecaudadorService;
import bo.com.tesla.administracion.services.ISucursalService;
import bo.com.tesla.entidades.services.IEntidadService;
import bo.com.tesla.recaudaciones.dto.DominioDto;
import bo.com.tesla.recaudaciones.services.IEntidadRService;
import bo.com.tesla.recaudaciones.services.IRecaudadoraService;
import bo.com.tesla.security.dao.ISegRolDao;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegPrivilegiosService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.cross.SendEmail;

@RestController
@RequestMapping("api/personas")
public class PersonaController {
	private Logger logger = LoggerFactory.getLogger(DominioController.class);

	@Autowired
	private ILogSistemaService logSistemaService;

	@Autowired
	private ISegUsuarioService segUsuarioService;

	@Autowired
	private IPersonaService personaService;

	@Autowired
	private IEntidadService entidadService;

	@Autowired
	private IRecaudadoraService recaudadorService;

	@Autowired
	private ISegRolDao rolDao;

	@Autowired
	private IEmpleadoService empleadoService;
	
	@Autowired
	private ISegPrivilegiosService privilegiosService;



	@PostMapping("/findPersonas")
	public ResponseEntity<?> findPersonas(@RequestBody PersonaDto personaDto, Authentication authentication) {

		Map<String, Object> response = new HashMap<>();
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		EntidadEntity entidad = new EntidadEntity();
		RecaudadorEntity recaudador = new RecaudadorEntity();
		Page<PersonaDto> personaDtoList = null;
		Long entidadId = 0L;
		Long recaudadorId = 0L;
		try {
			usuario = this.segUsuarioService.findByLogin(authentication.getName());

			if (personaDto.parametro == null || personaDto.parametro == "") {
				personaDto.parametro = "%";
			}

			switch (personaDto.subModulo) {
			case "ADMIN": // actualmente este controller ya esta en otro lugar
				personaDtoList = this.personaService.findPersonasByAdminGrid(personaDto.parametro, personaDto.page - 1,
						10);
				break;
			case "ADM_ENTIDADES":
				entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
				entidadId = entidad.getEntidadId();
				personaDtoList = this.personaService.findPersonasByEntidadesGrid(personaDto.parametro, entidadId,
						personaDto.page - 1, 10);

				break;
			case "ADM_RECAUDACION":
				recaudador = this.recaudadorService.findRecaudadorByUserId(usuario.getUsuarioId());
				recaudadorId = recaudador.getRecaudadorId();				
				personaDtoList = this.personaService.findPersonasByRecaudadorGrid(personaDto.parametro,personaDto.sucursalId, recaudadorId,
						personaDto.page - 1, 10);
				break;
			}

			for (PersonaDto persona : personaDtoList) {
				
				
				List<SegPrivilegioEntity> privilegiosList= this.privilegiosService.findPrivilegiosByUsuarioId(persona.usuarioId);
				
				persona.privilegioEntities = privilegiosList;

				if (this.empleadoService.findEmpleadosByPersonaId(persona.personaId).isPresent()) {
					EmpleadoEntity empleado = this.empleadoService.findEmpleadosByPersonaId(persona.personaId).get();
					if (empleado.getSucursalId() != null) {
						persona.nombreSucursal = empleado.getSucursalId().getNombre();
					}

				}

			}

			if (!personaDtoList.isEmpty()) {
				response.put("status", true);
				response.put("message", "Se encontro los siguientes registros.");
				response.put("data", personaDtoList);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("status", false);
				response.put("message", "No se encontro registros para mostrar.");
				response.put("data", null);
				return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("PERSONAS");
			log.setController("api/personas/");
			if (e.getCause() != null) {
				log.setCausa(e.getCause() + "");
			}
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			log = this.logSistemaService.save(log);
			response.put("status", false);
			response.put("result", null);
			response.put("message", " Código de  Error : " + log.getLogSistemaId()
					+ " \n Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");

			response.put("code", log.getLogSistemaId() + "");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/savePersona")
	public ResponseEntity<?> save(@Valid @RequestBody PersonaDto personaDto, BindingResult result,
			Authentication authentication) {

		Map<String, Object> response = new HashMap<>();
		PersonaEntity persona = new PersonaEntity();
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			persona = personaService.save(personaDto, usuario);
			response.put("status", true);
			response.put("message", "Los datos fueron guardados con éxito.");
			response.put("data", persona);
			return new ResponseEntity<>(response, HttpStatus.OK);

		}catch (BusinesException e) {

			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("PERSONAS");
			log.setController("api/personas/");
			if (e.getCause() != null) {
				log.setCausa(e.getCause() + "");
			}
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			log = this.logSistemaService.save(log);

			response.put("status", false);
			response.put("result", null);
			response.put("message", "Código Error : " + log.getLogSistemaId()
					+ " "+ e.getMessage() );

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} 
		catch (Technicalexception e) {
			
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("PERSONAS");
			log.setController("api/personas/");
			if (e.getCause() != null) {
				log.setCausa(e.getCause() + "");
			}
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			log = this.logSistemaService.save(log);

			response.put("status", false);
			response.put("result", null);
			response.put("message", "Código Error : " + log.getLogSistemaId()
					+ " "+ e.getMessage());

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("PERSONAS");
			log.setController("api/personas/");
			if (e.getCause() != null) {
				log.setCausa(e.getCause() + "");
			}
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			log = this.logSistemaService.save(log);
			response.put("status", false);
			response.put("result", null);
			response.put("message", " Código de  Error : " + log.getLogSistemaId()
					+ " \n Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");

			response.put("code", log.getLogSistemaId() + "");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/updatePersona")
	public ResponseEntity<?> update(@Valid @RequestBody PersonaDto personaDto, BindingResult result,
			Authentication authentication) {

		Map<String, Object> response = new HashMap<>();
		PersonaEntity persona = new PersonaEntity();
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			persona = personaService.update(personaDto, usuario);
			response.put("status", true);
			response.put("message", "Los datos fueron modificados con éxito.");
			response.put("data", persona);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Technicalexception e) {

			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("PERSONAS");
			log.setController("api/personas/");
			if (e.getCause() != null) {
				log.setCausa(e.getCause() + "");
			}
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			log = this.logSistemaService.save(log);
			response.put("status", false);
			response.put("result", null);
			response.put("message", " Código de  Error : " + log.getLogSistemaId()
					+ " \n Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");

			response.put("code", log.getLogSistemaId() + "");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (Exception e) {

			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("PERSONAS");
			log.setController("api/personas/");
			if (e.getCause() != null) {
				log.setCausa(e.getCause() + "");
			}
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			log = this.logSistemaService.save(log);

			response.put("status", false);
			response.put("result", null);
			response.put("message", " Código de  Error : " + log.getLogSistemaId()
					+ " \n Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");

			response.put("code", log.getLogSistemaId() + "");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/cambiarEstado")
	public ResponseEntity<?> cambiarEstado(@Valid @RequestBody PersonaDto personaDto, BindingResult result,
			Authentication authentication) {

		Map<String, Object> response = new HashMap<>();
		PersonaEntity persona = new PersonaEntity();
		SegUsuarioEntity usuario = new SegUsuarioEntity();

		try {
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			persona = this.personaService.cambiarEstado(personaDto, usuario);
			response.put("status", true);
			response.put("message", "La operación fue ejecutada con éxito.");
			response.put("data", persona);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Technicalexception e) {

			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("PERSONAS");
			log.setController("api/personas/");
			if (e.getCause() != null) {
				log.setCausa(e.getCause() + "");
			}
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			log = this.logSistemaService.save(log);

			response.put("status", false);
			response.put("result", null);
			response.put("message", " Código de  Error : " + log.getLogSistemaId()
					+ " \n Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");

			response.put("code", log.getLogSistemaId() + "");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (Exception e) {

			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("PERSONAS");
			log.setController("api/personas/");
			if (e.getCause() != null) {
				log.setCausa(e.getCause() + "");
			}
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			log = this.logSistemaService.save(log);
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());
			response.put("status", false);
			response.put("result", null);
			response.put("message", " Código de  Error : " + log.getLogSistemaId()
					+ " \n Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("code", log.getLogSistemaId() + "");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/generarCredenciales/{personaId}")
	public ResponseEntity<?> generarCredenciales(
			@PathVariable("personaId") Long personaId,
			Authentication authentication) {
		Map<String, Object> response = new HashMap<>();
		SegUsuarioEntity usuarioSession = new SegUsuarioEntity();
		try {
			usuarioSession = this.segUsuarioService.findByLogin(authentication.getName());
			SegUsuarioEntity usuario = this.personaService.generarCredenciales(personaId, usuarioSession);
			response.put("status", true);
			response.put("data", usuario);
			response.put("message",
					"Sus credenciales fueron enviadas al correo electrónico registrado en el formulario.");
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Technicalexception e) {

			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("PERSONAS");
			log.setController("api/personas/generarCredenciales/" + personaId);
			if (e.getCause() != null) {
				log.setCausa(e.getCause() + "");
			}
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuarioSession.getUsuarioId());
			log.setFechaCreacion(new Date());
			log = this.logSistemaService.save(log);			
			response.put("status", false);
			response.put("result", null);
			response.put("message", " Código de  Error : " + log.getLogSistemaId()
					+ " \n Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (Exception e) {

			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("PERSONAS");
			log.setController("api/personas/generarCredenciales/" + personaId);
			if (e.getCause() != null) {
				log.setCausa(e.getCause() + "");
			}
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuarioSession.getUsuarioId());
			log.setFechaCreacion(new Date());
			log = this.logSistemaService.save(log);

			response.put("status", false);
			response.put("result", null);
			response.put("message", " Código de  Error : " + log.getLogSistemaId()
					+ " \n Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");

			response.put("code", log.getLogSistemaId() + "");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}
	
	@PostMapping("/toUnlock/{personaId}")
	public ResponseEntity<?> toUnlock(@PathVariable("personaId") Long personaId,Authentication authentication) {
		Map<String, Object> response = new HashMap<>();
		SegUsuarioEntity usuarioSession = new SegUsuarioEntity();
		try {
			usuarioSession = this.segUsuarioService.findByLogin(authentication.getName());
			SegUsuarioEntity usuario = this.segUsuarioService.findByPersonaIdAndEstado(personaId).get();
			
			this.personaService.generarCredenciales(usuario.getPersonaId().getPersonaId(), usuarioSession);
			
			response.put("status", true);
			//response.put("data", usuario);
			response.put("message",
					"Sus credenciales fueron enviadas al correo electrónico registrado.");
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Technicalexception e) {

			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("PERSONAS");
			log.setController("api/toUnlock/" + personaId);
			if (e.getCause() != null) {
				log.setCausa(e.getCause() + "");
			}
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuarioSession.getUsuarioId());
			log.setFechaCreacion(new Date());
			log = this.logSistemaService.save(log);			
			response.put("status", false);
			response.put("result", null);
			response.put("message", " Código de  Error : " + log.getLogSistemaId()
					+ " \n Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (Exception e) {

			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("PERSONAS");
			log.setController("api/toUnlock/" + personaId);
			if (e.getCause() != null) {
				log.setCausa(e.getCause() + "");
			}
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuarioSession.getUsuarioId());
			log.setFechaCreacion(new Date());
			log = this.logSistemaService.save(log);

			response.put("status", false);
			response.put("result", null);
			response.put("message", " Código de  Error : " + log.getLogSistemaId()
					+ " \n Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");

			response.put("code", log.getLogSistemaId() + "");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}

}
