package bo.com.tesla.entidades.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.entidades.dao.SelectDto;
import bo.com.tesla.entidades.services.IEntidadService;
import bo.com.tesla.recaudaciones.services.IRecaudadoraService;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegUsuarioService;

@RestController
@RequestMapping("api/entidades")
public class EntidadesController {
	private Logger logger = LoggerFactory.getLogger(EntidadesController.class);

	@Autowired
	private IEntidadService entidadService;

	@Autowired
	private IRecaudadoraService recaudadoraService;

	@Autowired
	private ISegUsuarioService segUsuarioService;
	
	@Autowired
	private ILogSistemaService logSistemaService;

	@GetMapping(path = "/findEntidadByRecaudacionId")
	public ResponseEntity<?> findEntidadByRecaudacionId(Authentication authentication)  {

		Map<String, Object> response = new HashMap<>();
		List<EntidadEntity> entidadList = new ArrayList<>();
		List<SelectDto> selectList=new ArrayList<>();
		SegUsuarioEntity usuario=new SegUsuarioEntity();
		try {

			 usuario = this.segUsuarioService.findByLogin(authentication.getName());

			RecaudadorEntity recaudador = this.recaudadoraService.findRecaudadorByUserId(usuario.getUsuarioId());

			entidadList = this.entidadService.findEntidadByRecaudacionId(recaudador.getRecaudadorId());
			if (entidadList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			
			for (EntidadEntity entidad : entidadList) {
				SelectDto  select = new SelectDto(entidad.getNombre(),  entidad.getEntidadId()+""); 
				selectList.add(select);
			}

			response.put("data", selectList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			LogSistemaEntity log=new LogSistemaEntity();
			log.setModulo("ENTIDADES");
			log.setController("api/entidades/findEntidadByRecaudacionId/");			
			if(e.getCause()!=null) {
				log.setCausa(e.getCause().getMessage());
			}
			log.setUsuarioCreacion(usuario.getUsuarioId());
			log.setFechaCreacion(new Date());			
			this.logSistemaService.save(log);
			
			response.put("mensaje", e.getMessage());
			response.put("codigo", log.getLogSistemaId()+"");
			response.put("status", false);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping(path = "/findAllEntidad")
	public ResponseEntity<?> findAllEntidad(Authentication authentication)  {

		Map<String, Object> response = new HashMap<>();
		List<EntidadEntity> entidadList = new ArrayList<>();

		try {
			entidadList = this.entidadService.findAllEntidades();
			if (entidadList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}

			response.put("data", entidadList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}

}
