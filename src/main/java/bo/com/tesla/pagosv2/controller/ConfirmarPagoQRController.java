package bo.com.tesla.pagosv2.controller;

import bo.com.tesla.pagosv2.service.ConfirmarPagoQRService;
import bo.com.tesla.pagosv2.service.QRService;
import bo.com.tesla.useful.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("sip/endpoint")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ConfirmarPagoQRController {


    @Autowired
    private ConfirmarPagoQRService confirmarPagoQRService;

    @PostMapping("/confirmaPago") // verifica estado de transaccion con qr
    public ResponseEntity<?> estadoTransaccionQr(@RequestBody Map request) {
        try{
            confirmarPagoQRService.notificaClientePago(request.get("monto")+"",request.get("moneda")+"",request.get("alias")+"");
        }catch (Exception ex){}

        try{
            Map responseDto = confirmarPagoQRService.registrarConfirmacion(request);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch (Exception ex){
            Map<String, Object> response = new HashMap<>();
            response.put("codigo","9999");
            response.put("mensaje",ex.toString());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
