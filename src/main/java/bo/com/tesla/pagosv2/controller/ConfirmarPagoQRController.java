package bo.com.tesla.pagosv2.controller;

import bo.com.tesla.administracion.entity.*;
import bo.com.tesla.pagosv2.dao.ILogPagoDao;
import bo.com.tesla.pagosv2.service.ConfirmarPagoQRService;
import bo.com.tesla.pagosv2.service.QRService;
import bo.com.tesla.recaudaciones.dto.requestGenerarFactura.*;
import bo.com.tesla.security.services.ILogSistemaService;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@RestController
@RequestMapping("sip/endpoint")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ConfirmarPagoQRController {


    @Autowired
    private ConfirmarPagoQRService confirmarPagoQRService;

    @Autowired
    private ILogPagoDao iLogPagoDao;

    @Autowired
    Environment environment;


    @PostMapping("/confirmaPago") // verifica estado de transaccion con qr
    public ResponseEntity<?> estadoTransaccionQr(@RequestBody Map request)  {


        try
        {
            InetAddress address = InetAddress.getLocalHost();
            String port = environment.getProperty("local.server.port");
            // regsitramos log
            LogPagoEntity log=new LogPagoEntity();
            log.setAlias(request.get("alias")+"");
            log.setJson(request+"");
            log.setMensajeUsuario("INGRESO DE PAGO");
            log.setFechaInicio(new Date());
            log.setFechaFin(new Date());
            log.setHost(address+":"+port);
            log.setMetodo("sip/endpoint/confirmaPago");
            log.setTipoLog("CORRECTO");
            log.setEstadoId("ACTIVO");
            iLogPagoDao.save(log);
        }catch (Exception ex){

        }



        // notifica pago
        Long datosconfirmadoQrId=0L;
        try{

            datosconfirmadoQrId = confirmarPagoQRService.notificaClientePago(request);
        }catch (Exception ex){

        }
        Map<String, Object> response = new HashMap<>();
        // registra pago en quickpay

        try{
            Map responseDto  = confirmarPagoQRService.registrarConfirmacion(request,datosconfirmadoQrId);
            if(responseDto.get("codigo").equals("0000")){
                confirmarPagoQRService.registrarFactura(request);
            }
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch (Exception ex){


            response.put("codigo","9999");
            response.put("mensaje",ex.toString());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
