package bo.com.tesla.administracion.controllers;

import bo.com.tesla.administracion.entity.DominioEntity;
import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.administracion.services.IDominioService;
import bo.com.tesla.recaudaciones.dto.DominioDto;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.config.Technicalexception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/dominios")
public class DominioController {
    private Logger logger = LoggerFactory.getLogger(DominioController.class);

    @Autowired
    private ILogSistemaService logSistemaService;

    @Autowired
    private ISegUsuarioService segUsuarioService;

    @Autowired
    private IDominioService iDominioService;



    @GetMapping("/{dominio}")
    public ResponseEntity<?> getListSucursalesEntidades(@PathVariable String dominio,
                                                        Authentication authentication) {

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            List<DominioDto> dominioDtoList = iDominioService.getListDominios(dominio);
            if (!dominioDtoList.isEmpty()) {
                response.put("status", true);
                response.put("message", "El listado fue encontrado.");
                response.put("result", dominioDtoList);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", false);
                response.put("message", "El listado no fue encontrado.");
                response.put("result", null);
                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
            }
        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("ADMINISTRACION.DOMINIOS");
            log.setController("api/dominios/" + dominio);
            if(e.getCause()!=null) {
				log.setCausa(e.getCause().getMessage());
			}
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error", e.getMessage());
            this.logger.error("This is cause", e.getCause());
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/agrupadores/{agrupadorId}")
    public ResponseEntity<?> getListSucursalesEntidades(@PathVariable Long agrupadorId,
                                                        Authentication authentication) {

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            List<DominioDto> dominioDtoList = iDominioService.getLstDominiosByAgrupador(agrupadorId);
            if (!dominioDtoList.isEmpty()) {
                response.put("status", true);
                response.put("message", "El listado fue encontrado.");
                response.put("result", dominioDtoList);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", false);
                response.put("message", "El listado no fue encontrado.");
                response.put("result", null);
                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
            }
        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("ADMINISTRACION.DOMINIOS");
            log.setController("api/dominios/agrupadores/" + agrupadorId);
            if(e.getCause()!=null) {
				log.setCausa(e.getCause().getMessage());
			}
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error", e.getMessage());
            this.logger.error("This is cause", e.getCause());
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping(path = "/findByDominio/{dominio}")
	public ResponseEntity<?> findByDominio( @PathVariable("dominio") String dominio,
			Authentication authentication) throws Exception {

		Map<String, Object> response = new HashMap<>();
		List<DominioEntity> dominioList=new ArrayList<>();
		
		try {
			dominioList=this.iDominioService.findByDominio(dominio);
			if(dominioList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}
			
			response.put("data", dominioList);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

	}
}
