package bo.com.tesla.security.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import bo.com.tesla.administracion.dto.EntidadComisionAdmDto;
import bo.com.tesla.administracion.dto.PersonaDto;
import bo.com.tesla.administracion.dto.RolTransferDto;
import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.PersonaEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.administracion.entity.SegModuloEntity;
import bo.com.tesla.administracion.entity.SegRolEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.administracion.services.IPersonaService;
import bo.com.tesla.entidades.services.IEntidadService;
import bo.com.tesla.recaudaciones.services.IRecaudadoraService;
import bo.com.tesla.security.dto.UsuarioModulosDto;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegPrivilegiosService;
import bo.com.tesla.security.services.ISegRolService;
import bo.com.tesla.security.services.ISegUsuarioRolService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;

@RestController
@RequestMapping("api/Rol")
public class SegRolController {
	private Logger logger = LoggerFactory.getLogger(SegRolController.class);

	@Autowired
	private ISegRolService segRolService;

	@Autowired
	private ISegUsuarioService segUsuarioService;
	
	@Autowired
	private IPersonaService personaService;
	
	@Autowired
	private ISegUsuarioRolService segUsuarioRolService;
	
	@Autowired
	private ILogSistemaService logSistemaService;
	
	@Autowired
	private ISegPrivilegiosService privilegioService;
	
	@Autowired
	private IEntidadService entidadService;
	
	@Autowired
	private IRecaudadoraService recaudadoraService;
	
	@GetMapping(path = "/findRolesForTransfer/{subModulo}/{modulo}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> findRolesForTransfer(@PathVariable("subModulo") String subModulo,
			@PathVariable("modulo") String modulo, Authentication authentication) {
		Map<String, Object> response = new HashMap<>();
		List<RolTransferDto> rolList = new ArrayList<>();

		try {
			rolList = this.segRolService.findRolesForTransfer(subModulo, modulo);
			if (rolList.isEmpty()) {
				response.put("mensaje", "No se encontraron ningún registro.");
				response.put("status", false);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("status", true);
			response.put("data", rolList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());

			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("data", rolList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}
	
	@GetMapping(path = "/findRolesForTransferByUsuario/{subModulo}/{modulo}/{usuarioId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> findRolesForTransferByUsuario(
			@PathVariable("subModulo") String subModulo,
			@PathVariable("modulo") String modulo, 
			@PathVariable("usuarioId") Long usuarioId,
			Authentication authentication) {
		Map<String, Object> response = new HashMap<>();
		List<String> rolList = new ArrayList<>();
		try {
				
			rolList = this.segRolService.findRolesForTransferByUsuario(subModulo, modulo, usuarioId);
			if (rolList.isEmpty()) {
				response.put("mensaje", "No se encontraron ningún registro.");
				response.put("status", false);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("status", true);
			response.put("data", rolList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			

			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("data", rolList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}
	
	@GetMapping(path = "/getModuloUsuario", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getModuloUsuario(Authentication authentication) {
		SegUsuarioEntity usuario=new SegUsuarioEntity();
		Map<String, Object> response = new HashMap<>();
		List<UsuarioModulosDto> modulosList = new ArrayList<>();
		try {
			
			usuario=this.segUsuarioService.findByLogin(authentication.getName());
			modulosList = this.segRolService.getModuloUsuario(usuario.getUsuarioId());
			if (modulosList.isEmpty()) {
				response.put("mensaje", "No se encontraron ningún registro.");
				response.put("status", false);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("status", true);
			response.put("data", modulosList.get(0));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			

			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("data", modulosList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}
	
	@PostMapping(path = "/savePrivilegio", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> savePrivilegio(
			@RequestBody PersonaDto personaDto,
			Authentication authentication) {
		
		Map<String, Object> response = new HashMap<>();		
		SegUsuarioEntity usuario=new SegUsuarioEntity();
		try {
			usuario=this.segUsuarioService.findByLogin(authentication.getName());
			this.segUsuarioRolService.saveRolesByUsuarioId(personaDto,usuario);
			
			response.put("message", "LOS ROLES PARA EL USUARIO FUERON REGISTRADOS CON EXITO");
			response.put("status", true);
		
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
			
		}
		catch (BusinesException e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("SEGROL");
			log.setController("api/savePrivilegio/");
			if (e.getCause() != null) {
				log.setCausa(e.getCause() + "");
			}
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			log=logSistemaService.save(log);
		
			response.put("status", false);
			response.put("result", null);
			response.put("message", e.getMessage());
			response.put("message",
					"Error : "+log.getLogSistemaId()+" "+e.getMessage());
			response.put("code", log.getLogSistemaId() + "");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		catch (Technicalexception e) {
			e.printStackTrace();
			LogSistemaEntity log = new LogSistemaEntity();
			log.setModulo("SEGROL");
			log.setController("api/savePrivilegio/");
			if (e.getCause() != null) {
				log.setCausa(e.getCause() + "");
			}
			log.setMensaje(e.getMessage() + "");
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());
			log=logSistemaService.save(log);
		
			response.put("status", false);
			response.put("result", null);
		
			response.put("message",
					"Error: "+ log.getLogSistemaId()+" \n. Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("code", log.getLogSistemaId() + "");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}
	
	
	
	
	@GetMapping(path = "/findModulos", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> findModulos(Authentication authentication) {
		SegUsuarioEntity usuario=new SegUsuarioEntity();
		Map<String, Object> response = new HashMap<>();
		List<SegModuloEntity> modulosList = new ArrayList<>();
		try {
			
			usuario=this.segUsuarioService.findByLogin(authentication.getName());
			modulosList = this.privilegioService.findModulos();
			if (modulosList.isEmpty()) {
				response.put("mensaje", "No se encontraron ningún registro.");
				response.put("status", false);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("status", true);
			response.put("data", modulosList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());

			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("data", modulosList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}
	
	
	@GetMapping(path = "/findPrivilegiosByModuloId/{moduloId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> findPrivilegiosByModuloId(
			@PathVariable("moduloId") Long moduloId,
			Authentication authentication) {
		SegUsuarioEntity usuario=new SegUsuarioEntity();
		Map<String, Object> response = new HashMap<>();
		List<RolTransferDto> rolTransfer = new ArrayList<>();
		try {
			
			usuario=this.segUsuarioService.findByLogin(authentication.getName());
			rolTransfer = this.privilegioService.findPrivilegiosByModuloId(moduloId);
			if (rolTransfer.isEmpty()) {
				response.put("mensaje", "No se encontraron ningún registro.");
				response.put("status", false);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("status", true);
			response.put("data", rolTransfer);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());

			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("data", rolTransfer);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}
	
	@GetMapping(path = "/findPrivilegiosByUsuarioId/{usuarioId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> findPrivilegiosByUsuarioId(
			@PathVariable("usuarioId") Long usuarioId,
			Authentication authentication) {
		SegUsuarioEntity usuario=new SegUsuarioEntity();
		Map<String, Object> response = new HashMap<>();
		List<String> privilegioList = new ArrayList<>();
		SegRolEntity rol=new SegRolEntity();
		try {
			
			usuario=this.segUsuarioService.findById(usuarioId);
			List<SegRolEntity> rolesList=this.segRolService.findRolesByUsuarioLogin(usuario.getLogin());
			if(!rolesList.isEmpty()) {
				rol= rolesList.get(0);
			}
			
			
			privilegioList = this.privilegioService.findPrivilegiosByUsuario(usuarioId);
			if (privilegioList.isEmpty()) {
				response.put("mensaje", "No se encontraron ningún registro.");
				response.put("status", false);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("status", true);
			response.put("data", privilegioList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());

			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("data", privilegioList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}
	
	@GetMapping(path = "/findModuloByUsuarioId/{usuarioId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> findModuloByUsuarioId(
			@PathVariable("usuarioId") Long usuarioId,
			Authentication authentication) {
		SegUsuarioEntity usuario=new SegUsuarioEntity();
		Map<String, Object> response = new HashMap<>();
		SegModuloEntity modulo = new SegModuloEntity();
		SegRolEntity rol=new SegRolEntity();
		try {
			
			modulo = this.privilegioService.findModuloByUsuarioId(usuarioId);
			if (modulo==null) {
				response.put("mensaje", "No se encontraron ningún registro.");
				response.put("status", false);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("status", true);
			response.put("data", modulo);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());

			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("data", modulo);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}
	
	@GetMapping(path = "/findModuloByUsuarioSession", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> findModuloByUsuarioSession(			
			Authentication authentication) {
		SegUsuarioEntity usuario=new SegUsuarioEntity();
		Map<String, Object> response = new HashMap<>();
		SegModuloEntity modulo = new SegModuloEntity();
		SegRolEntity rol=new SegRolEntity();
		try {
			
			usuario=this.segUsuarioService.findByLogin(authentication.getName());
			modulo = this.privilegioService.findModuloByUsuarioId(usuario.getUsuarioId());
			if (modulo==null) {
				response.put("mensaje", "No se encontraron ningún registro.");
				response.put("status", false);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("status", true);
			response.put("data", modulo);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());

			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("data", modulo);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}
	
	
	@GetMapping(path = "/findPrivilegiosByUsuarioIdForTransfer/{subModulo}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> findPrivilegiosByUsuarioIdForTransfer(		
			@PathVariable("subModulo") String subModulo,
			Authentication authentication) {
		SegUsuarioEntity usuario=new SegUsuarioEntity();
		SegUsuarioEntity usuarioSession=new SegUsuarioEntity();
		Map<String, Object> response = new HashMap<>();
		List<RolTransferDto> rolTransfer = new ArrayList<>();
		try {
			
			usuarioSession=this.segUsuarioService.findByLogin(authentication.getName());
			
			if(subModulo.equals("ADM_ENTIDADES")) {
				EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuarioSession.getUsuarioId());
				usuario=this.segRolService.findUsuarioAdminByEntidadId(entidad.getEntidadId()).get(0);
				
			}else if(subModulo.equals("ADM_RECAUDACION")) {
				RecaudadorEntity recaudador = this.recaudadoraService.findRecaudadorByUserId(usuarioSession.getUsuarioId());
				
				usuario=this.segRolService.findUsuarioAdminByRecaudacionId(recaudador.getRecaudadorId()).get(0);
			}
			
			
			
			rolTransfer = this.privilegioService.findPrivilegiosByUsuarioIdForTransfer(usuario.getUsuarioId());
			if (rolTransfer.isEmpty()) {
				response.put("mensaje", "No se encontraron ningún registro.");
				response.put("status", false);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("status", true);
			response.put("data", rolTransfer);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("This is error", e.getMessage());
			this.logger.error("This is cause", e.getCause());

			response.put("mensaje",
					"Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
			response.put("data", rolTransfer);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}


}
