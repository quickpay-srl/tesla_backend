package bo.com.tesla.security.aut;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import bo.com.tesla.security.dto.Credencial;




@Component
public class InfoAdicionarToken implements TokenEnhancer {


	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Credencial usuarioJson = new Credencial();
		usuarioJson.login = authentication.getName();
		HttpEntity<Credencial> request = new HttpEntity<>(usuarioJson, headers);

		Map<String, Object> info = new HashMap<>();	
		
		Map<String, Object> datosUsuario = new HashMap<>();

		datosUsuario.put("usuario", info);

		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(datosUsuario);
		return accessToken;
	}

}
