package bo.com.tesla.entidades.controller;


import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.entidades.dto.DeudasClienteRestDto;
import bo.com.tesla.entidades.services.IDeudaClienteService;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.config.BusinesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
@RequestMapping("ext/deudas")
public class DeudaEntidadController {
    private Logger logger = LoggerFactory.getLogger(DeudaEntidadController.class);

    @Autowired
    private ISegUsuarioService segUsuarioService;

    @Autowired
    private IDeudaClienteService deudaClienteService;

    @Autowired
    private ILogSistemaService logSistemaService;

    @Secured("ROLE_MCAECA")
    //@CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(path ="", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postCustomerDebt(@Valid @RequestBody DeudasClienteRestDto deuda,
                                              BindingResult result,
                                              Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        SegUsuarioEntity usuario=new SegUsuarioEntity();

        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                .stream()
                .map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
                .collect(Collectors.toList());

            response.put("errors", errors);
            response.put("status", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            usuario=this.segUsuarioService.findByLogin(authentication.getName());
            String codigo=this.deudaClienteService.saveCustomerDebt(deuda, usuario);

            response.put("status", "true");
            response.put("message",	"La operación fue registrada con éxito.");
            response.put("data", codigo);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BusinesException e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("EXTERNO DEUDAS");
            log.setController("ext/deudas");
            if(e.getCause()!=null) {
                log.setCausa(e.getCause().getMessage());
            }
            log.setMensaje(e.getMessage());
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            log=this.logSistemaService.save(log);
            response.put("mesagge",e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {

            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("EXTERNO DEUDAS");
            log.setController("ext/deudas");
            if(e.getCause()!=null) {
                log.setCausa(e.getCause().getMessage());
            }
            log.setMensaje(e.getMessage());
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            log=this.logSistemaService.save(log);
            response.put("message",
                    "Error : "+log.getLogSistemaId()+"\n Ocurrió un error en el servidor, por favor intente la operación más tarde o consulte con su administrador.");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

    }
}
