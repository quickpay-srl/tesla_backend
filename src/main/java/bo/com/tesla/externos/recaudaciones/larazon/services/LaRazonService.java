package bo.com.tesla.externos.recaudaciones.larazon.services;

import bo.com.tesla.administracion.entity.EndPointEntidadEntity;
import bo.com.tesla.administracion.entity.TransaccionCobroEntity;
import bo.com.tesla.externos.recaudaciones.larazon.dto.CobroEntidadDto;
import bo.com.tesla.externos.recaudaciones.larazon.dto.ExceptionDto;
import bo.com.tesla.useful.cross.Util;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class LaRazonService implements ILaRazonService{

    @Autowired
    private IEndPointTransaccionCobroService endPointTransaccionCobroService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void updateCobroLaRazon(EndPointEntidadEntity endPoint,
                                   List<TransaccionCobroEntity> transaccionCobroEntityList) {
        //Recorrer las n transacciones
        for(TransaccionCobroEntity transaccion : transaccionCobroEntityList) {
            CobroEntidadDto cobro = new CobroEntidadDto(
                    transaccion.getCodigoCliente(),
                    transaccion.getFechaCreacion()
            );
            responseCobro(transaccion,
                    endPoint,
                    cobro);
        }
    }

    private void responseCobro(TransaccionCobroEntity transaccion,
                               EndPointEntidadEntity endPoint,
                               CobroEntidadDto cobro) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
            body.add("id_aviso", cobro.idAviso);
            body.add("fecha", Util.formatDateLaRazon(cobro.fecha));

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(endPoint.getRuta().trim(),
                    request,
                    String.class);

            //Verificar respuesta caso la Razon
            if(response.getStatusCode() == HttpStatus.OK) {
                JSONObject jsonObject = new JSONObject(response.getBody());
                boolean resp = (boolean)jsonObject.get("resp");

                if(!resp) {
                    ExceptionDto exceptionDto = new ExceptionDto(
                            null,
                            "Respuesta de api falsa (resp = false)"
                    );
                    endPointTransaccionCobroService.addEnPointTransaccionCobro(endPoint,
                            transaccion,
                            exceptionDto);
                }
            }
        } catch (HttpStatusCodeException exception) {
            int statusCode = exception.getStatusCode().value();
            ExceptionDto exceptionDto = new ExceptionDto(
                    statusCode,
                    exception.getMessage()
            );
            endPointTransaccionCobroService.addEnPointTransaccionCobro(endPoint,
                    transaccion,
                    exceptionDto);
        } catch (Exception ex) {
            ExceptionDto exceptionDto = new ExceptionDto(
                    null,
                    ex.getMessage()
            );
            endPointTransaccionCobroService.addEnPointTransaccionCobro(endPoint,
                    transaccion,
                    exceptionDto);
        }
    }

}
