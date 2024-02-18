package bo.com.tesla.facturacion.electronica.services;


import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

@Service
public class ConexionFacElectronicaService  {


    @Value("${host.facturacion.electronica}")
    private String hostFacElectronico;

    @Autowired
    private RestTemplateFacturacionElectronica restTemplate;


    public <T> ResponseDto getResponseMethodPost(T body, String endPoint) throws Exception {
        String url = hostFacElectronico+endPoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<T> request = new HttpEntity<T>(body,headers);
        ResponseEntity<ResponseDto> response = restTemplate.RestTemplateFacturacionElectronica().exchange(
                url,
                HttpMethod.POST,
                request,
                ResponseDto.class
        );

        if(response.getStatusCode() != HttpStatus.OK) {
            throw new Technicalexception(response.getBody().message);
        }
        return response.getBody();
    }

    public ResponseDto getResponseMethodGet(String endPoint) throws Exception {
        String url = hostFacElectronico+endPoint;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<ResponseDto> response = restTemplate.RestTemplateFacturacionElectronica().exchange(
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
