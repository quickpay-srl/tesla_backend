package bo.com.tesla.security.aut;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.security.services.ISegUsuarioService;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher{
	 private Logger logger = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);
	
	@Autowired
	private ISegUsuarioService usuarioService;

	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		try {
			this.logger.info("USUARIO SE AUTENTICO CORECTAMENTE : "+authentication.getName());
			SegUsuarioEntity usuario=this.usuarioService.findByLogin(authentication.getName());
			if(usuario.getIntentos()!=null && usuario.getIntentos()>0) 
			{
				usuario.setIntentos(0);
				this.usuarioService.save(usuario);
			}
			
		} catch (Exception e) {
			this.logger.error("El Usuario '"+authentication.getName()+ "' no se encuentra en el sistema");
		}		
	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {	
		
		try {
			this.logger.error("USUARIO SE AUTENTICO INCORECTAMENTE : "+authentication.getName());
			SegUsuarioEntity usuario=this.usuarioService.findByLogin(authentication.getName());
			if(usuario.getIntentos() == null) {
				usuario.setIntentos(0);
			}
			usuario.setIntentos(usuario.getIntentos()+1);
			if(usuario.getIntentos()>=5) {
				this.logger.error("El usuario : '"+authentication.getName()+"' se deshabilito por mas de 5 intentos");
				usuario.setBloqueado(true);
			}
			this.usuarioService.save(usuario);
			
		} catch (Exception e) {
			this.logger.error("El Usuario '"+authentication.getName()+ "' no se encuentra en el sistema");
		}
	}

}
