package bo.com.tesla.facturaciones.computarizada.services;

import bo.com.tesla.administracion.dao.ISucursalEntidadDao;
import bo.com.tesla.administracion.entity.SucursalEntidadEntity;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.dto.ResponseDto;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Optional;

@Service
public class ConexionService implements IConexionService {

    @Value("${host.facturacion.electronica}")
    private String hostFacturacion;

    @Value("${user.facturacion.computarizada}")
    private String userFacturacion;

    @Value("${password.facturacion.computarizada}")
    private String passwordFacturacion;

    @Autowired
    private ISucursalEntidadDao sucursalEntidadDao;

    @Autowired
    private RestTemplateFacturacionComputarizada restTemplate;

    //Caso establecido que para TESLA solo se registrará una sucursal de entidad (CASA MATRIZ)
    @Override
    public HashMap<String, String> getCredencialesByEntidadId(Long entidadId) {
        //Encontrar sucursal
        Optional<SucursalEntidadEntity> sucursalEntidadEntityOptional  = sucursalEntidadDao.findByEmiteFacturaTesla(entidadId);
        if(!sucursalEntidadEntityOptional.isPresent()) {
            throw new Technicalexception("No se ha encontrado las credenciales o no se encuentra registrada la sucursal en la entidadId" + entidadId);
        }

        HashMap<String, String> credenciales = new HashMap<>();
        String login = sucursalEntidadEntityOptional.get().getUsuarioFacturacion();
        String password = sucursalEntidadEntityOptional.get().getPasswordFacturacion();
        credenciales.put("login", login);
        credenciales.put("password", password);

        return credenciales;
    }

    @Override
    public HttpHeaders getHeaderToken(Long entidadId) {
        try {
            HashMap<String, String> credencialEntidad = getCredencialesByEntidadId(entidadId);

            String url = hostFacturacion + "/oauth/token";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(userFacturacion, passwordFacturacion);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
            body.add("username", credencialEntidad.get("login"));
            body.add("password", credencialEntidad.get("password"));
            body.add("grant_type", "password");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(body, headers);
            ResponseEntity<String> response = restTemplate.restTemplate().exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if(response.getStatusCode() != HttpStatus.OK) {
                throw new Technicalexception("Error al recuperar token de FC con credenciales otorgadas");
            }
            JSONObject jsonObject = new JSONObject(response.getBody());
            String token = jsonObject.getString("access_token");
            HttpHeaders headersToken = new HttpHeaders();
            headersToken.setBearerAuth(token);
            headersToken.setContentType(MediaType.APPLICATION_JSON);

            return headersToken;

        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }

    @Override
    public <T> ResponseDto getResponseMethodPost(Long entidadId, T body, String url) throws Exception {
        HttpHeaders headers = getHeaderToken(entidadId);
        HttpEntity<T> request = new HttpEntity<T>(body, headers);
        ResponseEntity<ResponseDto> response = restTemplate.restTemplate().exchange(
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

    @Override
    public <T> ResponseDto getResponseMethodPostParameter(Long entidadId, T body, UriComponentsBuilder uriComponentsBuilder) {
        try {
            HttpHeaders headers = getHeaderToken(entidadId);

            HttpEntity<T> request = new HttpEntity<T>(body, headers);
            ResponseEntity<ResponseDto> response = restTemplate.restTemplate().exchange(
                    uriComponentsBuilder.toUriString(),
                    HttpMethod.POST,
                    request,
                    ResponseDto.class
            );

            if(response.getStatusCode() == HttpStatus.NO_CONTENT) {
                return response.getBody();
            }

            if(response.getStatusCode() != HttpStatus.OK) {
                throw new Technicalexception(response.getBody().message);
            }

            return response.getBody();
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }

    @Override
    public ResponseDto getResponseMethodGet(Long entidadId, String url) throws Exception {
        HttpHeaders headers = getHeaderToken(entidadId);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<ResponseDto> response = restTemplate.restTemplate().exchange(
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

    @Override
    public ResponseDto getResponseMethodGetParams(Long entidadId, UriComponentsBuilder uriComponentsBuilder) throws Exception {
        HttpHeaders headers = getHeaderToken(entidadId);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<ResponseDto> response = restTemplate.restTemplate().exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.GET,
                request,
                ResponseDto.class
        );

        if(response.getStatusCode() != HttpStatus.OK) {
            throw new Technicalexception(response.getBody().message);
        }
        return response.getBody();
    }

    @Override
    public <T> ResponseDto getResponseMethodPut(Long entidadId, T body, String url) throws Exception {
        HttpHeaders headers = getHeaderToken(entidadId);

        HttpEntity<T> request = new HttpEntity<T>(body, headers);
        ResponseEntity<ResponseDto> response = restTemplate.restTemplate().exchange(
                url,
                HttpMethod.PUT,
                request,
                ResponseDto.class
        );

        if(response.getStatusCode() != HttpStatus.OK) {
            throw new Technicalexception(response.getBody().message);
        }

        return response.getBody();
    }

    @Override
    public <T> ResponseDto getResponseMethodPutParameter(Long entidadId, T body, UriComponentsBuilder uriComponentsBuilder) throws Exception {
        HttpHeaders headers = getHeaderToken(entidadId);

        HttpEntity<T> request = new HttpEntity<T>(body, headers);
        ResponseEntity<ResponseDto> response = restTemplate.restTemplate().exchange(
                uriComponentsBuilder.toUriString(),
                HttpMethod.PUT,
                request,
                ResponseDto.class
        );

        if(response.getStatusCode() != HttpStatus.OK) {
            throw new Technicalexception(response.getBody().message);
        }

        return response.getBody();
    }


}
