package bo.com.tesla.facturaciones.computarizada.controllers;

import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.entidades.services.IEntidadService;
import bo.com.tesla.facturaciones.computarizada.services.IActividadEconomicaService;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/actividadeseconomicas")
public class ActividadEconomicaController {

    private Logger logger = LoggerFactory.getLogger(ActividadEconomicaController.class);

    @Autowired
    private ILogSistemaService logSistemaService;

    @Autowired
    private ISegUsuarioService segUsuarioService;

    @Autowired
    private IActividadEconomicaService actividadEconomicaService;

    @Autowired
    private IEntidadService entidadService;

    @Secured( "ROLE_MCED" )
    @GetMapping("/{codigoActividadEconomica}")
    public ResponseEntity<?> getActividadesEconomicasByCodigo(@PathVariable String codigoActividadEconomica,
                                                            Authentication authentication) {

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
            if(entidad == null) {
                throw new BusinesException("El usuario debe pertenecer a una Entidad.");
            }
            ResponseDto responseDto = actividadEconomicaService.getByCodigo(entidad.getEntidadId(), codigoActividadEconomica);

            response.put("status", responseDto.status);
            response.put("message", responseDto.message);
            response.put("result", responseDto.result);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("ACTIVIDADECONOMICA");
            log.setController("api/actividadeseconomicas/" + codigoActividadEconomica);
            log.setCausa(e.getCause() + "");
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
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BusinesException e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("ACTIVIDADECONOMICA");
            log.setController("api/actividadeseconomicas/" + codigoActividadEconomica);
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

    @Secured( { "ROLE_MCARF", "ROLE_MCRA", "ROLE_MCRRA",   "ROLE_MCLV", "ROLE_MCERF" } )
    @GetMapping(path = {"", "/entidades/{entidadId}" }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getActividadesEconomicas(@PathVariable Optional<Long> entidadId,
            Authentication authentication) {

        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            ResponseDto responseDto = new ResponseDto();
            if(!entidadId.isPresent()) {
                EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
                if(entidad == null) {
                    throw new BusinesException("El usuario debe pertenecer a una Entidad.");
                }
                responseDto = actividadEconomicaService.getActividadesEconomicas(entidad.getEntidadId());
            } else {
                responseDto = actividadEconomicaService.getActividadesEconomicas(entidadId.get());
            }

            response.put("status", responseDto.status);
            response.put("message", responseDto.message);
            response.put("result", responseDto.result);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("ACTIVIDADECONOMICA");
            log.setController("api/actividadeseconomicas/entidades");
            log.setCausa(e.getCause() + "");
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
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BusinesException e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("ACTIVIDADECONOMICA");
            log.setController("api/actividadeseconomicas/entidades");
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
}
