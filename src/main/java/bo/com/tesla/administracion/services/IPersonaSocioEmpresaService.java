package bo.com.tesla.administracion.services;

import bo.com.tesla.administracion.dto.PersonaDto;
import bo.com.tesla.administracion.entity.PersonaEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.useful.config.BusinesException;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IPersonaSocioEmpresaService {
	

	public PersonaEntity guardarSocioEmpresa(PersonaDto entity,Long tipoUsuarioId,SegUsuarioEntity usuario) throws BusinesException;

	
	public PersonaEntity modificarSocioEmpresa(PersonaDto personaDto, Long tipoUsuarioId,SegUsuarioEntity usuario);

	public Page<PersonaDto> findPersonasSocioEntidad(String parametro, Long pTipo, int page, int size);


}
