package bo.com.tesla.facturaciones.computarizada.controllers;

import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.entidades.services.IEntidadService;
import bo.com.tesla.facturaciones.computarizada.dto.DosificacionDto;
import bo.com.tesla.facturaciones.computarizada.services.IDosificacionService;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/dosificaciones")
public class DosificacionController {

    private Logger logger = LoggerFactory.getLogger(DosificacionController.class);

    @Autowired
    private ILogSistemaService logSistemaService;

    @Autowired
    private ISegUsuarioService segUsuarioService;

    @Autowired
    private IDosificacionService dosificacionService;

    @Autowired
    private IEntidadService entidadService;

    @Secured( "ROLE_MCED" )
    @PostMapping("")
    public ResponseEntity<?> postDosificacion(@RequestBody DosificacionDto dosificacionDto,
                                              Authentication authentication)  {

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
            if(entidad == null) {
                throw new BusinesException("El usuario debe pertenecer a una Entidad.");
            }

            ResponseDto responseDto = dosificacionService.saveDosificacion(entidad.getEntidadId(),dosificacionDto);
            response.put("status", responseDto.status);
            response.put("message", responseDto.message);
            response.put("result", responseDto.result);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("DOSIFICACION");
            log.setController("api/dosificaciones");
            log.setCausa(e.getCause() != null ? e.getCause().getCause()+"" : "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error: " + e.getMessage());
            this.logger.error("This is cause: " + (e.getCause() != null ? e.getCause().getCause()+"" : ""));
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BusinesException e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("FACTURAS");
            log.setController("api/facturas/filters");
            log.setMensaje(e.getMessage());
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            this.logSistemaService.save(log);
            this.logger.error("This is cause: " + e.getMessage());
            response.put("status", false);
            response.put("message", e.getMessage());
            response.put("code", log.getLogSistemaId()+"");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Secured( "ROLE_MCED" )
    @GetMapping("")
    public ResponseEntity<?> getDosificacionesLst(Authentication authentication)  {

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
            if(entidad == null) {
                throw new BusinesException("El usuario debe pertenecer a una Entidad.");
            }
            ResponseDto responseDto = dosificacionService.getDosificacionesLst(entidad.getEntidadId());
            if(((List<DosificacionDto>)responseDto.result).size() == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            response.put("status", responseDto.status);
            response.put("message", responseDto.message);
            response.put("result", responseDto.result);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("DOSIFICACION");
            log.setController("api/dosificaciones");
            log.setCausa(e.getCause() != null ? e.getCause().getCause()+"" : "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error: " + e.getMessage());
            this.logger.error("This is cause: " + (e.getCause() != null ? e.getCause().getCause()+"" : ""));response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
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

    @Secured( "ROLE_MCED" )
    @GetMapping("/{dosificacionId}")
    public ResponseEntity<?> getDosificacionById(@PathVariable Long dosificacionId,
                                                  Authentication authentication)  {

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
            if(entidad == null) {
                throw new BusinesException("El usuario debe pertenecer a una Entidad.");
            }
            ResponseDto responseDto = dosificacionService.getDosificacionById(entidad.getEntidadId(),dosificacionId);

            response.put("status", responseDto.status);
            response.put("message", responseDto.message);
            response.put("result", responseDto.result);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("DOSIFICACION");
            log.setController("api/dosificaciones");
            log.setCausa(e.getCause() != null ? e.getCause().getCause()+"" : "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error: " + e.getMessage());
            this.logger.error("This is cause: " + (e.getCause() != null ? e.getCause().getCause()+"" : ""));
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
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

    @Secured( "ROLE_MCED" )
    @PutMapping("/{dosificacionId}/transacciones/{transaccion}")
    public ResponseEntity<?> putTransaccion(@PathVariable Long dosificacionId,
                                               @PathVariable String transaccion,
                                               Authentication authentication) {
        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
            if(entidad == null) {
                throw new BusinesException("El usuario debe pertenecer a una Entidad.");
            }
            ResponseDto responseDto = dosificacionService.putTransaccion(entidad.getEntidadId(),dosificacionId, transaccion);

            response.put("status", responseDto.status);
            response.put("message", responseDto.message);
            response.put("result", responseDto.result);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("ADMINISTRACION.SUCURSAL");
            log.setController("PUT: api/sucursales/listas/" + transaccion);
            log.setCausa(e.getCause() != null ? e.getCause().getCause()+"" : e.getCause()+"");
            log.setMensaje(e.getMessage()+"");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error: " + e.getMessage());
            this.logger.error("This is cause: " + (e.getCause() != null ? e.getCause().getCause()+"" : ""));
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId()+"");
            return  new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
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

    //Por el momento no debe considerar @Secured
    @GetMapping("/alertas")
    public ResponseEntity<?> getDosificacionesLstAlertas(Authentication authentication)  {

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
            /*if(entidad == null) {
                throw new BusinesException("El usuario debe pertenecer a una Entidad.");
            }*/
            if(entidad != null) {
                ResponseDto responseDto = dosificacionService.getDosificacionesLstAlerta(entidad.getEntidadId());
                if (((List<DosificacionDto>) responseDto.result).size() == 0) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                response.put("status", responseDto.status);
                response.put("message", responseDto.message);
                response.put("result", responseDto.result);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", null);
                response.put("message", null);
                response.put("result", null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("DOSIFICACION");
            log.setController("api/dosificaciones");
            log.setCausa(e.getCause() != null ? e.getCause().getCause()+"" : "");
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save( log);
            this.logger.error("This is error: " + e.getMessage());
            this.logger.error("This is cause: " + (e.getCause() != null ? e.getCause().getCause()+"" : ""));
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema (notificaciones/alertas) en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
