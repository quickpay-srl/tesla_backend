package bo.com.tesla.administracion.controllers;

import bo.com.tesla.administracion.dto.PersonaDto;
import bo.com.tesla.administracion.entity.*;
import bo.com.tesla.administracion.services.IEmpleadoService;
import bo.com.tesla.administracion.services.IPersonaService;
import bo.com.tesla.administracion.services.IPersonaSocioEmpresaService;
import bo.com.tesla.entidades.services.IEntidadService;
import bo.com.tesla.recaudaciones.services.IRecaudadoraService;
import bo.com.tesla.security.dao.ISegRolDao;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegPrivilegiosService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/personasSocioEmpresa")
public class PersonaSocioEmpresaController {
	private Logger logger = LoggerFactory.getLogger(DominioController.class);

	@Autowired
	private ILogSistemaService logSistemaService;

	@Autowired
	private ISegUsuarioService segUsuarioService;

	@Autowired
	private IPersonaSocioEmpresaService personaSocioEmpresaService;

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




	@PostMapping("/savePersonaSocioEmpresa/{tipoUsuarioId}")
	public ResponseEntity<?> save(@PathVariable("tipoUsuarioId") Long tipoUsuarioId, @Valid @RequestBody PersonaDto personaDto, BindingResult result,
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
			persona = personaSocioEmpresaService.guardarSocioEmpresa(personaDto,tipoUsuarioId, usuario);
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
	@PostMapping("/updatePersonaSocioEmpresa/{tipoUsuarioId}")
	public ResponseEntity<?> update(@Valid @RequestBody PersonaDto personaDto, @PathVariable("tipoUsuarioId") Long tipoUsuarioId,BindingResult result,
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
			persona = personaSocioEmpresaService.modificarSocioEmpresa(personaDto, tipoUsuarioId,usuario);
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
	@PostMapping("/findPersonasByTipo/{pTipo}")
	public ResponseEntity<?> findPersonas(@RequestBody PersonaDto personaDto,@PathVariable("pTipo") Long pTipo, Authentication authentication) {

		Map<String, Object> response = new HashMap<>();
		SegUsuarioEntity usuario = new SegUsuarioEntity();
		Page<PersonaDto> personaDtoList = null;
		try {
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			if (personaDto.parametro == null || personaDto.parametro == "") {
				personaDto.parametro = "%";
			}
			personaDtoList = this.personaSocioEmpresaService.findPersonasSocioEntidad(personaDto.parametro, pTipo,personaDto.page - 1, 10);

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
}
