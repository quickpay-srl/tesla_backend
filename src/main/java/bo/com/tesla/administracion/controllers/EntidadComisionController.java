package bo.com.tesla.administracion.controllers;


import bo.com.tesla.administracion.dto.EntidadComisionAdmDto;
import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.administracion.services.IEntidadComisionService;
import bo.com.tesla.recaudaciones.controllers.EntidadController;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.config.Technicalexception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/comisionesentidades")
public class EntidadComisionController {
    private Logger logger = LoggerFactory.getLogger(EntidadComisionController.class);

    @Autowired
    private IEntidadComisionService entidadComisionService;

    @Autowired
    private ILogSistemaService logSistemaService;

    @Autowired
    private ISegUsuarioService segUsuarioService;


    // cunado se registra entidad
    /*********************ABM ENTIDADES**************************/
    @PostMapping("")
    public ResponseEntity<?> addUpdateEntidadComision(@RequestBody EntidadComisionAdmDto entidadComisionAdmDto,
                                                      Authentication authentication) {
        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            Boolean esModificacion = entidadComisionAdmDto.entidadComisionId != null;
            entidadComisionAdmDto = entidadComisionService.addUpdateEntidadComision(entidadComisionAdmDto, usuario.getUsuarioId());
            response.put("status", true);
            response.put("message", esModificacion ? "Se realizó la actualización del registro correctamente." : "Se realizó el registro correctamente.");
            response.put("result", entidadComisionAdmDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("ADMINISTRACION.ENTIDADCOMISION");
            log.setController("POST: api/comisionesentidades");
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
        }
    }

    @GetMapping("/{entidadComisionId}")
    public ResponseEntity<?> getEntidadComisionById(@PathVariable Long entidadComisionId,
                                                    Authentication authentication) {
        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            EntidadComisionAdmDto entidadComisionAdmDto = entidadComisionService.getEntidadComisionById(entidadComisionId);
            if(entidadComisionAdmDto != null ) {
                response.put("status", true);
                response.put("message", "El registro fue encontrado.");
                response.put("result", entidadComisionAdmDto);
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
            log.setModulo("ADMINISTRACION.ENTIDADCOMISION");
            log.setController("GET: api/comisionesentidades/" + entidadComisionId);
            log.setCausa(e.getCause() != null ? e.getCause().getCause()+"" : e.getCause()+"");
            log.setMensaje(e.getMessage()+"");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error", e.getMessage());
            this.logger.error("This is cause", e.getCause() != null ? e.getCause().getCause()+"" : e.getCause());
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId()+"");
            return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/entidades/{entidadId}")
    public ResponseEntity<?> getListEntidadesComisionesByEntidad(@PathVariable Long entidadId,
            Authentication authentication) {
        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            List<EntidadComisionAdmDto> entidadComisionAdmDtos = entidadComisionService.getAllEntidadesComisionesByEntidadId(entidadId);
            if(!entidadComisionAdmDtos.isEmpty()) {
                response.put("status", true);
                response.put("message", "El listado fue encontrado.");
                response.put("result", entidadComisionAdmDtos);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", false);
                response.put("message", "El lista   do no fue encontrado.");
                response.put("result", null);
                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
            }
        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("ADMINISTRACION.ENTIDADCOMISION");
            log.setController("GET: api/comisionesentidades");
            log.setCausa(e.getCause() != null ? e.getCause().getCause()+"" : e.getCause()+"");
            log.setMensaje(e.getMessage()+"");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error", e.getMessage());
            this.logger.error("This is cause", e.getCause() != null ? e.getCause().getCause()+"" : e.getCause());
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId()+"");
            return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/comision/activo/{entidadId}")
    public ResponseEntity<?> getEntidadesComisionesActivosByEntidad(@PathVariable Long entidadId,
                                                                 Authentication authentication) {
        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            EntidadComisionAdmDto entidadComisionAdmDtos = entidadComisionService.getEntidadesComisionesActivaByEntidadId(entidadId);
            if(entidadComisionAdmDtos==null) {
                response.put("status", false);
                response.put("message", "No existe comisiones registrado");
                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
            } else {
                response.put("status", true);
                response.put("message", "Comisión encontrado");
                response.put("result", entidadComisionAdmDtos);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("ADMINISTRACION.ENTIDADCOMISION");
            log.setController("GET: api/comisionesentidades");
            log.setCausa(e.getCause() != null ? e.getCause().getCause()+"" : e.getCause()+"");
            log.setMensaje(e.getMessage()+"");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error", e.getMessage());
            this.logger.error("This is cause", e.getCause() != null ? e.getCause().getCause()+"" : e.getCause());
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId()+"");
            return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


}
