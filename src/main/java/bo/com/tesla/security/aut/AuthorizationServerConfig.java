package bo.com.tesla.security.aut;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;

	@Autowired
	private InfoAdicionarToken infoAdicionarToken;

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {		
		security.tokenKeyAccess("permitAll()")
		.checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		
		clients.inMemory()
		.withClient("exacta.tesla.resource")
		.secret(passwordEncoder.encode("6pqn@Ean%3peC%o09.12zx_nWAS#"))
		.scopes("resource")//"read", "write")
		//.authorizedGrantTypes("password", "refresh_token")
		.authorizedGrantTypes("password")
		.accessTokenValiditySeconds(43200)
		.refreshTokenValiditySeconds(43200)
				.and()
				.withClient("exacta.tesla")
				.secret(passwordEncoder.encode("$3Xacta.T3sla.C0m.B0.2021@$"))
				.scopes("external")
				.authorizedGrantTypes("password")
				.accessTokenValiditySeconds(43200)
				.refreshTokenValiditySeconds(43200)
				.and()
				.withClient("exacta.recaudaciones")
				.secret(passwordEncoder.encode("X65Ta_rC5%99$as.0!O3mDq"))
				.scopes("collection")
				.authorizedGrantTypes("password")
				.accessTokenValiditySeconds(43200)
				.refreshTokenValiditySeconds(43200);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(infoAdicionarToken, accessTokenConverter()));
		endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore())
				.accessTokenConverter(accessTokenConverter()).tokenEnhancer(tokenEnhancerChain);
	}

	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setSigningKey("alguna.clave.secreta.12345678");
		
		//jwtAccessTokenConverter.setSigningKey(JwtConfig.RSA_PRIVATE);		
		//jwtAccessTokenConverter.setVerifierKey(JwtConfig.RSA_PUBLIC);
		return jwtAccessTokenConverter;
	}

}
