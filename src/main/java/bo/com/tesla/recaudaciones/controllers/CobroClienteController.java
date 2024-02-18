package bo.com.tesla.recaudaciones.controllers;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

import bo.com.tesla.administracion.services.IEntidadRecaudadorService;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.administracion.entity.TransaccionCobroEntity;
import bo.com.tesla.recaudaciones.dto.ClienteDto;
import bo.com.tesla.recaudaciones.dto.DeudasCobradasFacturaDto;
import bo.com.tesla.recaudaciones.services.ICobroClienteService;
import bo.com.tesla.recaudaciones.services.IHistoricoDeudaService;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.cross.Util;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.validation.Valid;

@RestController
@RequestMapping("api/cobros")
public class CobroClienteController {

    private Logger logger = LoggerFactory.getLogger(CobroClienteController.class);

    @Autowired
    private ICobroClienteService iCobroClienteService;

    @Autowired
    private ISegUsuarioService segUsuarioService;

    @Autowired
    private ILogSistemaService logSistemaService;
    
    @Autowired
    private IHistoricoDeudaService historicoDeudaService;

    @Autowired
    private IEntidadRecaudadorService entidadRecaudadorService;

	@Value("${tesla.path.files-report}")
	private String filesReport;


	@Secured("ROLE_MCARC")
	@PostMapping("") // genera factura
    public ResponseEntity<?> postCobrarDeudas(@Valid @RequestBody ClienteDto clienteDto, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        if(clienteDto == null || clienteDto.getNombreCliente() == null || clienteDto.getNroDocumento() == null || clienteDto.getCodigoCliente() == null) {
            response.put("status", false);
            response.put("message", "Ocurrió un error en el servidor, por favor verifique selecciona de deudas o datos de clientes");
            response.put("result", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        SegUsuarioEntity usuario = this.segUsuarioService.findByLogin(authentication.getName());
        try {
            entidadRecaudadorService.verificarAccesoUsuarioRecaudador(usuario.getUsuarioId(), clienteDto.getEntidadId());
            ResponseDto res = iCobroClienteService.postCobrarDeudas(clienteDto, usuario.getUsuarioId(), clienteDto.getMetodoCobroId());

            String facturaBase64 = res.result+"";
            byte[] facturaByteArray = Base64.getDecoder().decode(facturaBase64);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(facturaByteArray.length);
            headers.setContentType(MediaType.parseMediaType("application/pdf" ));
            headers.set("Content-Disposition", "inline; filename=report.pdf" );
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return new ResponseEntity<>(facturaByteArray, headers, HttpStatus.OK);



            /*response.put("status", true);
            response.put("message", "Factura Generada correctaente");
            response.put("result", r);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);*/

        } catch (BusinesException e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("RECAUDACION.COBROS");
            log.setController("POST: api/cobros/" + clienteDto.getMetodoCobroId());
            log.setMensaje(e.getMessage());
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            this.logSistemaService.save(log);
            this.logger.error("This is cause " + e.getMessage());
            response.put("status", false);
            response.put("message", e.getMessage());
            response.put("code", log.getLogSistemaId()+"");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Technicalexception e) {
            e.printStackTrace();
            LogSistemaEntity log=new LogSistemaEntity();
            log.setModulo("RECAUDACION.COBROS");
            log.setController("POST: api/cobros/" + clienteDto.getMetodoCobroId());
            log.setCausa(e.getCause() != null ? e.getCause().getCause()+"" : "");
            log.setMensaje(e.getMessage()+"");
            log.setUsuarioCreacion(usuario.getUsuarioId());
            log.setFechaCreacion(new Date());
            logSistemaService.save(log);
            this.logger.error("This is error "+ e.getMessage());
            this.logger.error("This is cause "+ (e.getCause() != null ? e.getCause().getCause()+"" : ""));

            return new ResponseEntity<>("Error " + log.getLogSistemaId() + ": Ocurrio un error en el servidor, por favor intente la operacion mas tarde o consulte con su administrador." , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

 