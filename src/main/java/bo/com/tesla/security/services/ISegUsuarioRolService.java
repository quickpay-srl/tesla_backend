package bo.com.tesla.security.services;

import bo.com.tesla.administracion.dto.PersonaDto;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.useful.config.BusinesException;

public interface ISegUsuarioRolService {
	
	public void saveRolesByUsuarioId(PersonaDto personaDto, SegUsuarioEntity usuarioSession) throws BusinesException;

}
