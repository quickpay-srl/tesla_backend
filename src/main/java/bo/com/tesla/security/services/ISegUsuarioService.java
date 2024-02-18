package bo.com.tesla.security.services;

import java.util.Optional;

import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.security.dto.CambiarPasswordDto;
import bo.com.tesla.useful.config.BusinesException;

public interface ISegUsuarioService {
	
	
	public SegUsuarioEntity save(SegUsuarioEntity entity);
	public SegUsuarioEntity findByLogin(String login);
	public SegUsuarioEntity findById(Long usuarioId);
	public Optional<SegUsuarioEntity> findByPersonaIdAndEstado( Long personaId);		
	public void cambiarPassword(CambiarPasswordDto passwords, SegUsuarioEntity usuario) throws BusinesException;	
	public void  toUnlock(String login, SegUsuarioEntity usuarioSession);

}
