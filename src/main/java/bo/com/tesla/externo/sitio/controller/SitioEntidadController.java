package bo.com.tesla.externo.sitio.controller;

import bo.com.tesla.administracion.services.IDominioService;
import bo.com.tesla.externo.sitio.service.SitioEntidadService;
import bo.com.tesla.recaudaciones.dto.DominioDto;
import bo.com.tesla.useful.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("api/sitio/entidad")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SitioEntidadController {

    @Autowired
    private SitioEntidadService entidadService;

    @Autowired
    private IDominioService iDominioService;

    @GetMapping("/colegio-todos")
    public ResponseEntity<?> getColegiosTodos(){
        Map<String, Object> response = new HashMap<>();
        ResponseDto responseDto =  entidadService.entidadByTipoEntidadId(31L);
        response.put("status", responseDto.status);
        response.put("message", responseDto.message);
        response.put("result", responseDto.result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/tipo-entidades/{pDominio}")
    public ResponseEntity<?> getTipoEntidades(@PathVariable String pDominio){
        Map<String, Object> response = new HashMap<>();
        ResponseDto responseDto = iDominioService.getDominios(pDominio);
        response.put("status", responseDto.status);
        response.put("message", responseDto.message);
        response.put("result", responseDto.result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/for-tipo/{pTipoEntidad}")
    public ResponseEntity<?> getForTipo(@PathVariable Long pTipoEntidad){
        Map<String, Object> response = new HashMap<>();
        ResponseDto responseDto = entidadService.getEntidadForTipo(pTipoEntidad);
        response.put("status", responseDto.status);
        response.put("message", responseDto.message);
        response.put("result", responseDto.result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/for-entidad-id/{pEntidadId}")
    public ResponseEntity<?> getForEntidadId(@PathVariable Long pEntidadId){
        Map<String, Object> response = new HashMap<>();
        ResponseDto responseDto = entidadService.getEntidadForEntidadId(pEntidadId);
        response.put("status", responseDto.status);
        response.put("message", responseDto.message);
        response.put("result", responseDto.result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/for-subdominio/{pSubdominioEmpresa}")
    public ResponseEntity<?> getForSubDominio(@PathVariable String pSubdominioEmpresa){
        Map<String, Object> response = new HashMap<>();
        ResponseDto responseDto = entidadService.getEntidadForSubDominioEmpresa(pSubdominioEmpresa);
        response.put("status", responseDto.status);
        response.put("message", responseDto.message);
        response.put("result", responseDto.result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
