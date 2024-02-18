package bo.com.tesla.entidades.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.entidades.dao.SelectDto;
import bo.com.tesla.entidades.services.IEntidadService;
import bo.com.tesla.recaudaciones.services.IRecaudadoraService;
import bo.com.tesla.security.services.ISegUsuarioService;

@RestController
@RequestMapping("api/recaudadores")
public class RecaudadoresController {
	@Autowired
	private IRecaudadoraService recaudadoraService;
	@Autowired
	private ISegUsuarioService segUsuarioService;

	@Autowired
	private IEntidadService entidadService;

	@GetMapping(path = "/findRecaudadoresByEntidadId")
	public ResponseEntity<?> findRecaudadoresByEntidadId(			
			Authentication authentication) {

		Map<String, Object> response = new HashMap<>();
		SegUsuarioEntity usuario=new SegUsuarioEntity();
		List<SelectDto> selectDtoList=new ArrayList<>();
		try {
			
			usuario = this.segUsuarioService.findByLogin(authentication.getName());
			EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
			
			List<RecaudadorEntity> recaudadorList = this.recaudadoraService.findRecaudadoresByEntidadId(entidad.getEntidadId());
			if (recaudadorList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			
			
			for (RecaudadorEntity recaudadorEntity : recaudadorList) {
				SelectDto select=new SelectDto(recaudadorEntity.getNombre(),recaudadorEntity.getRecaudadorId()+"");
				selectDtoList.add(select);
			}
			
			response.put("data", selectDtoList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}
	@GetMapping(path = "/findAllRecaudadora")
	public ResponseEntity<?> findAllRecaudadora(			
			Authentication authentication) {

		Map<String, Object> response = new HashMap<>();		
		try {
			
			List<RecaudadorEntity> recaudadorList = this.recaudadoraService.findAllRecaudadora();
			if (recaudadorList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			response.put("data", recaudadorList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}
}
