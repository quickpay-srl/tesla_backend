package bo.com.tesla.externo.sitio.controller;

import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.externo.sitio.dto.SitioDatosCobroDto;
import bo.com.tesla.externo.sitio.service.SitioDeudaClienteService;
import bo.com.tesla.recaudaciones.dto.ClienteDto;
import bo.com.tesla.recaudaciones.dto.DeudaClienteDto;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("api/sitio/deuda-cliente")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SitioDeudaClienteController {

    @Autowired
    private SitioDeudaClienteService sitioDeudaClienteService;

    @GetMapping("/obtener-cliente/{pEntidadId}/{pCodCliente}")
    public ResponseEntity<?> getClienteColegiosTodos(@PathVariable Long pEntidadId, @PathVariable String pCodCliente){
        Map<String, Object> response = new HashMap<>();
        ResponseDto responseDto =  sitioDeudaClienteService.clienteByEntidadIdAndCodigoCliente(pEntidadId,pCodCliente);
        response.put("status", responseDto.status);
        response.put("message", responseDto.message);
        response.put("result", responseDto.result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/obtener-deuda-cliente/{pEntidadId}/{pCodCliente}")
    public ResponseEntity<?> getCobroCliente(@PathVariable Long pEntidadId, @PathVariable String pCodCliente){
        Map<String, Object> response = new HashMap<>();
        ResponseDto responseDto =  sitioDeudaClienteService.deudaClienteByEntidadIdAndCodigoCliente(pEntidadId,pCodCliente);
        response.put("status", responseDto.status);
        response.put("message", responseDto.message);
        response.put("result", responseDto.result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/descarga-factura/{alias}") // genera factura
    public ResponseEntity<?> generaFactura(@PathVariable String alias){
        Map<String, Object> response = new HashMap<>();
        ResponseDto responseDto =  sitioDeudaClienteService.generaFactura(alias);
        response.put("status", responseDto.status);
        response.put("message", responseDto.message);
        response.put("result", responseDto.result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
