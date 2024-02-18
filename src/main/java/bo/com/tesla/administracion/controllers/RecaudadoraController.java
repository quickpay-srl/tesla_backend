package bo.com.tesla.administracion.controllers;

import bo.com.tesla.administracion.dto.RecaudadorAdmDto;
import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.PServicioProductoEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.administracion.services.IRecaudadorService;
import bo.com.tesla.pagos.services.IPServicioProductosService;
import bo.com.tesla.recaudaciones.services.IRecaudadoraService;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.config.Technicalexception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/recaudadores")
public class RecaudadoraController {
    private Logger logger = LoggerFactory.getLogger(RecaudadoraController.class);

    @Autowired
    private ILogSistemaService logSistemaService;

    @Autowired
    private ISegUsuarioService segUsuarioService;

    @Autowired
    private IRecaudadorService recaudadorService;
    
    @Autowired
    private IRecaudadoraService recaudadoraService;
    
    @Autowired
    private IPServicioProductosService servicioProductosService;

    /*********************ABM RECAUDADORES**************************/
    @PostMapping("")
    public ResponseEntity<?> addUpdateRecaudador(@RequestBody RecaudadorAdmDto recaudadorAdmDto,
                                                Authentication authentication) {
        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            Boolean esModificacion = recaudadorAdmDto.recaudadorId != null;
            recaudadorAdmDto = recaudadorService.addUpdateRecaudador(recaudadorAdmDto, usuario.getUsuarioId());
            response.put("status", true);
            response.put("message", esModificacion ? "Se realizó la actualización del registro correctamente." : "Se realizó el registro correctamente.");
            response.put("result", recaudadorAdmDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("ADMINISTRACION.RECAUDADOR");
            log.setController("POST: api/recaudadores");
            log.setCausa(e.getCause() != null ? e.getCause().getCause()+"" : e.getCause()+"");
            log.setMensaje(e.getMessage()+"");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause "+ e.getCause() != null ? e.getCause().getCause()+"" : e.getCause()+"");
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId()+"");
            return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{recaudadorId}/{transaccion}")
    public ResponseEntity<?> setTransaccion(@PathVariable Long recaudadorId,
                                            @PathVariable String transaccion,
                                            Authentication authentication) {
        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            recaudadorService.setTransaccion(recaudadorId, transaccion, usuario.getUsuarioId());
            response.put("status", true);
            response.put("message", "Se realizó la actualización de la transacción correctamente.");
            response.put("result", true);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("ADMINISTRACION.RECAUDADOR");
            log.setController("PUT: api/recaudadores/" + recaudadorId + "/" + transaccion);
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

    @PutMapping("/listas/{transaccion}")
    public ResponseEntity<?> setLstTransaccion(@RequestBody List<Long> recaudadorIdLst,
                                               @PathVariable String transaccion,
                                               Authentication authentication) {
        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            recaudadorService.setLstTransaccion(recaudadorIdLst, transaccion, usuario.getUsuarioId());
            response.put("status", true);
            response.put("message", "Se realizó la actualización de registro(s) con el nuevo estado.");
            response.put("result", true);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("ADMINISTRACION.RECAUDADOR");
            log.setController("PUT: api/recaudadores/listas/" + transaccion);
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

    @GetMapping("/{recaudadorId}")
    public ResponseEntity<?> getRecaudadoraById(@PathVariable Long recaudadorId,
                                            Authentication authentication) {
        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            RecaudadorAdmDto recaudadorAdmDto = recaudadorService.getRecaudadoraById(recaudadorId);
            if(recaudadorAdmDto != null ) {
                response.put("status", true);
                response.put("message", "El registro fue encontrado.");
                response.put("result", recaudadorAdmDto);
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
            log.setModulo("ADMINISTRACION.RECAUDADOR");
            log.setController("GET: api/recaudadores/" + recaudadorId);
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

    @GetMapping("")
    public ResponseEntity<?> getListRecaudadores(Authentication authentication) {
        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            List<RecaudadorAdmDto> recaudadorAdmDtoList = recaudadorService.getAllRecaudadoras();
            if(!recaudadorAdmDtoList.isEmpty()) {
                response.put("status", true);
                response.put("message", "El listado fue encontrado.");
                response.put("result", recaudadorAdmDtoList);
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
            log.setModulo("ADMINISTRACION.RECAUDADOR");
            log.setController("GET: api/recaudadores");
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
    @GetMapping(path="/findRecaudadoresByservicioProductoId/{servicioProductoId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> findRecaudadoresByservicioProductoId(@PathVariable("servicioProductoId")Long servicioProductoId, Authentication authentication) {
        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
        	
        	PServicioProductoEntity servicioProducto=this.servicioProductosService.findById(servicioProductoId).get();
        	
            List<RecaudadorEntity> recaudadorAdmDtoList = this.recaudadoraService.findRecaudadoresByEntidadId(servicioProducto.getEntidadId().getEntidadId());
            		
            if(!recaudadorAdmDtoList.isEmpty()) {
                response.put("status", true);
                response.put("message", "El listado fue encontrado.");
                response.put("data", recaudadorAdmDtoList);
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
            log.setModulo("ADMINISTRACION.RECAUDADOR");
            log.setController("GET: api/recaudadores");
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

    
    
    
    @GetMapping("/findAllRecaudadoras")
    public ResponseEntity<?> findAll(Authentication authentication) {
    	SegUsuarioEntity usuario =new SegUsuarioEntity();
        Map<String, Object> response = new HashMap<>();
        try {
        	usuario = this.segUsuarioService.findByLogin(authentication.getName());
            List<RecaudadorEntity> recaudadorAdmDtoList = recaudadorService.findAll();
            if(!recaudadorAdmDtoList.isEmpty()) {
                response.put("status", true);
                response.put("message", "El listado fue encontrado.");
                response.put("data", recaudadorAdmDtoList);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", false);
                response.put("message", "El listado no fue encontrado.");
                response.put("data", null);
                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
            }
        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("ADMINISTRACION.RECAUDADOR");
            log.setController("GET: api/recaudadores");
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
