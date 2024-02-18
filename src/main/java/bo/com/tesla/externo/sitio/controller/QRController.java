package bo.com.tesla.externo.sitio.controller;

import bo.com.tesla.externo.sitio.dto.SitioDatosCobroDto;
import bo.com.tesla.pagosv2.dto.RequestGeneraQrDto;
import bo.com.tesla.pagosv2.service.QRService;
import bo.com.tesla.useful.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/sitio/qr")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class QRController {

    @Autowired
    private QRService qrService;

    @PostMapping("/genera-qr") // genera factura
    public ResponseEntity<?> postCobrarDeudas(@RequestBody RequestGeneraQrDto request) {
        Map<String, Object> response = new HashMap<>();
        ResponseDto responseDto = qrService.generarQr(request);
        response.put("status", responseDto.status);
        response.put("message", responseDto.message);
        response.put("result", responseDto.result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/estado-transaccion-qr/{alias}") // verifica estado de transaccion con qr
    public ResponseEntity<?> estadoTransaccionQr(@PathVariable String alias) {
        Map<String, Object> response = new HashMap<>();
        ResponseDto responseDto = qrService.estadoTransaccionQr(alias);
        response.put("status", responseDto.status);
        response.put("message", responseDto.message);
        response.put("result", responseDto.result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
