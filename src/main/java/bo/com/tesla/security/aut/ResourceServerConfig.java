package bo.com.tesla.security.aut;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Value("${proy.allowed.origins}")
	private String url;

	@Override
	public void configure(HttpSecurity http) throws Exception {


		http.authorizeRequests()
		 		.antMatchers("/api/sitio/**").permitAll()
				.antMatchers("/api/prueba-guitarra/**").permitAll()
				.antMatchers("/api/prueba-cliente/**").permitAll()

				.antMatchers("/sip/**").permitAll()
				.antMatchers("/api/**").access("#oauth2.hasScope('resource')")
				.antMatchers("/ext/**").access("#oauth2.hasScope('external')")
				.antMatchers("/rec/**").access("#oauth2.hasScope('collection')")
				.anyRequest().authenticated();


	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();	
		//config.addAllowedOrigin(url);		
		//config.setAllowCredentials(true);			
		
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.setAllowedOrigins(Arrays.asList("*"));
		config.setAllowedMethods(Arrays.asList("*"));
		config.setAllowedHeaders(Collections.singletonList("*"));
			
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter(){
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;	
	}

}
