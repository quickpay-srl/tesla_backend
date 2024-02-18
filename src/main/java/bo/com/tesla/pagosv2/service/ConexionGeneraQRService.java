package bo.com.tesla.pagosv2.service;


import bo.com.tesla.pagosv2.dto.RequestGeneraQrDto;
import bo.com.tesla.pagosv2.dto.ResponseEstadoQrDto;
import bo.com.tesla.pagosv2.dto.ResponseQrDto;
import bo.com.tesla.pagosv2.dto.ResponseSipto;
import bo.com.tesla.useful.dto.ResponseDto;
import bo.com.tesla.useful.utils.FuncionesFechas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ConexionGeneraQRService {


    @Value("${sip.host}")
    private String sipHost;

    @Autowired
    private RestTemplateGeneraQr restTemplateGeneraQr;

    @Autowired
    private ConexionConfirmaPagoQrService conexionConfirmaPagoQrService;

    @Value("${sip.apikey.servicio.login}")
    private String apikeyServicioLogin;
    @Value("${sip.apikey.servicio.genera.qr}")
    private String apikeyServicioGeneraQr;
    @Value("${sip.username}")
    private String username;
    @Value("${sip.password}")
    private String password;




    public ResponseDto  postGeneraToken() throws Exception {
        ResponseDto res = new ResponseDto();
        String url = sipHost+"/autenticacion/v1/generarToken";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey",apikeyServicioLogin);
        Map<String, Object> body = new HashMap<>();
        body.put("username", this.username);
        body.put("password", this.password);
        HttpEntity request = new HttpEntity(body,headers);
        ResponseEntity<ResponseSipto> response = restTemplateGeneraQr.RestTemplateGeneraQr().exchange(
                url,
                HttpMethod.POST,
                request,
                ResponseSipto.class
        );
        if(!response.getBody().getCodigo().equals("OK")){
            res.status = false;
            res.message = response.getBody().getMensaje();
            return res;
        }
        if(response.getStatusCode() != HttpStatus.OK) {
            res.status = false;
            res.message = response.toString();
            return res;
        }
        res.status = true;
        res.result = response.getBody().getObjeto().get("token");
        return  res;

    }

    public ResponseDto generaQr(Map body) throws Exception {
        ResponseDto res = this.postGeneraToken();
        if(!res.status) return res;

        String url = sipHost+"/api/v1/generaQr";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikeyServicio",this.apikeyServicioGeneraQr);
        headers.set("Authorization","Bearer "+res.result.toString());
        HttpEntity request = new HttpEntity(body,headers);
        ResponseEntity<ResponseSipto> response = restTemplateGeneraQr.RestTemplateGeneraQr().exchange(
                url,
                HttpMethod.POST,
                request,
                ResponseSipto.class
        );

        if(!response.getBody().getCodigo().equals("0000")){
            res.status = false;
            res.message = response.getBody().getMensaje()+" "+body.get("alias");
            return res;
        }
        if(response.getStatusCode() != HttpStatus.OK) {
            res.status = false;
            res.message = response.toString() +" "+body.get("alias");
            return res;
        }
        ResponseQrDto qr = new ResponseQrDto();
        qr.setImagenQr("data:image/png;base64," + response.getBody().getObjeto().get("imagenQr")  );
        qr.setIdQr(response.getBody().getObjeto().get("idQr").toString());
        qr.setFechaVencimiento(response.getBody().getObjeto().get("fechaVencimiento").toString());
        qr.setBancoDestino(response.getBody().getObjeto().get("bancoDestino").toString());
        qr.setCuentaDestino(response.getBody().getObjeto().get("cuentaDestino").toString());
        qr.setIdTransaccion(response.getBody().getObjeto().get("idTransaccion").toString());
        qr.setAlias(body.get("alias")+"");
        res.status = true;
        res.result = qr;
        return  res;
    }
    public ResponseDto estadoTransaccionQr(Map objRequest) throws Exception {
        ResponseDto res = this.postGeneraToken();
        if(!res.status) return res;

        String url = sipHost+"/api/v1/estadoTransaccion";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikeyServicio",this.apikeyServicioGeneraQr);
        headers.set("Authorization","Bearer "+res.result.toString());
        Map<String, Object> body = new HashMap<>();
        body.put("alias", objRequest.get("alias"));


        HttpEntity request = new HttpEntity(body,headers);
        ResponseEntity<ResponseSipto> response = restTemplateGeneraQr.RestTemplateGeneraQr().exchange(
                url,
                HttpMethod.POST,
                request,
                ResponseSipto.class
        );

        if(!response.getBody().getCodigo().equals("0000")){
            res.status = false;
            res.message = response.getBody().getMensaje();
            return res;
        }
        if(response.getStatusCode() != HttpStatus.OK) {
            res.status = false;
            res.message = response.toString();
            return res;
        }
        ResponseEstadoQrDto qr = new ResponseEstadoQrDto();
        qr.setAlias(response.getBody().getObjeto().get("alias")+"");
        qr.setEstadoActual(response.getBody().getObjeto().get("estadoActual")+"");
        qr.setFechaProcesamiento(response.getBody().getObjeto().get("fechaProcesamiento")+"");
        qr.setFechaRegistro(response.getBody().getObjeto().get("fechaRegistro")+"");
        qr.setNumeroOrdenOriginante(response.getBody().getObjeto().get("numeroOrdenOriginante")+"");
        qr.setMonto(response.getBody().getObjeto().get("monto")+"");
        qr.setIdQr(response.getBody().getObjeto().get("idQr")+"");
        qr.setMoneda(response.getBody().getObjeto().get("moneda")+"");
        qr.setCuentaCliente(response.getBody().getObjeto().get("cuentaCliente")+"");
        qr.setNombreCliente(response.getBody().getObjeto().get("nombreCliente")+"");
        qr.setDocumentoCliente(response.getBody().getObjeto().get("documentoCliente")+"");
        res.status = true;
        res.result = qr;
        return  res;
    }
}
