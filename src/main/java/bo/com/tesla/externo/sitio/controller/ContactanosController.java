package bo.com.tesla.externo.sitio.controller;

import bo.com.tesla.externo.sitio.service.ContactanosService;
import bo.com.tesla.pagosv2.dto.RequestGeneraQrDto;
import bo.com.tesla.useful.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/sitio/contactanos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ContactanosController {
    @Autowired
    private ContactanosService contactanosService;

    @PostMapping("/envio-correo-contactanos") // genera factura
    public ResponseEntity<?> envioCorreoContactanos(@RequestBody Map request) {
        Map<String, Object> response = new HashMap<>();
        ResponseDto responseDto = contactanosService.envioCorreoContactanos(request);
        response.put("status", responseDto.status);
        response.put("message", responseDto.message);
        response.put("result", responseDto.result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
