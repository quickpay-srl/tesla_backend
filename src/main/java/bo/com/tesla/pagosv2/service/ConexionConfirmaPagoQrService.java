package bo.com.tesla.pagosv2.service;

import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

@Service
public class ConexionConfirmaPagoQrService {
    @Value("${host.notifica.pago}")
    private String hostNotificaPago;

    @Autowired
    private RestTemplateGeneraQr restTemplateGeneraQr;

    public ResponseDto getResponseMethodGet(String endPoint) throws Exception {
        String url = hostNotificaPago+endPoint;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<ResponseDto> response = restTemplateGeneraQr.RestTemplateGeneraQr().exchange(
                url,
                HttpMethod.GET,
                request,
                ResponseDto.class
        );

        if(response.getStatusCode() != HttpStatus.OK) {
            throw new Technicalexception(response.getBody().message);
        }
        return response.getBody();
    }

}
