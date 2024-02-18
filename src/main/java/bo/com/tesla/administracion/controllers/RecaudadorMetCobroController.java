package bo.com.tesla.administracion.controllers;

import bo.com.tesla.administracion.dto.RecaudadorMetodoCobroDto;
import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.administracion.services.RecaudadorMetCobroService;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.SegUsuarioService;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/rec-metodos-cobros")
public class RecaudadorMetCobroController {
    private Logger logger = LoggerFactory.getLogger(RecaudadoraController.class);

    @Autowired
    private SegUsuarioService segUsuarioService;

    @Autowired
    private RecaudadorMetCobroService recaudadorMetCobroService;

    @Autowired
    private ILogSistemaService logSistemaService;

    @GetMapping("")
    public ResponseEntity<?> getLstMetCobroByLogin(Authentication authentication) {

        SegUsuarioEntity usuario = segUsuarioService.findByLogin(authentication.getName());
        Map<String, Object> response = new HashMap<>();
        try {
            List<RecaudadorMetodoCobroDto> recaudadorLst = recaudadorMetCobroService.findRecMetCobDtoBRecaudador(usuario.getUsuarioId());
            if (recaudadorLst.size() > 0) {
                response.put("status", true);
                response.put("message", "El listado fue encontrado.");
                response.put("result", recaudadorLst);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (BusinesException e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("REC-MET-COBROS");
            log.setController("GET api/rec-metodos-cobros");
            log.setMensaje(e.getMessage());
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            this.logSistemaService.save(log);
            this.logger.error("This is cause", e.getMessage());

            response.put("message", e.getMessage());
            response.put("code", log.getLogSistemaId() + "");
            response.put("status", false);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log = new LogSistemaEntity();
            log.setModulo("REC-MET-COBROS");
            log.setController("GET api/rec-metodos-cobros");
            if(e.getCause()!=null) {
                log.setCausa(e.getCause().getMessage());
            }
            log.setMensaje(e.getMessage() + "");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error " + e.getMessage());
            this.logger.error("This is cause " + e.getCause());
            response.put("status", false);
            response.put("result", null);
            response.put("message", "Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            response.put("code", log.getLogSistemaId() + "");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
