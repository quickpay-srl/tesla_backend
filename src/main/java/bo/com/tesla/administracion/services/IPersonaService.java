package bo.com.tesla.administracion.services;

import java.util.Optional;

import org.springframework.data.domain.Page;

import bo.com.tesla.administracion.dto.PersonaDto;
import bo.com.tesla.administracion.entity.PersonaEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.useful.config.BusinesException;

public interface IPersonaService {
	
	public Optional<PersonaEntity> findById(Long personaId);
	
	public Page<PersonaDto> findPersonasByRecaudadorGrid(String parametro,Long sucursalId, Long recaudadorId,int page,int size);
	public Page<PersonaDto> findPersonasByEntidadesGrid(String parametro,Long entidadId,int page,int size);



	public Page<PersonaDto> findPersonasByAdminGrid(String parametro,int page,int size);

	public PersonaEntity save(PersonaDto entity,SegUsuarioEntity usuario) throws BusinesException;

	
	public PersonaEntity update(PersonaDto personaDto, SegUsuarioEntity usuario);
	
	public PersonaEntity cambiarEstado(PersonaDto personaDto, SegUsuarioEntity usuario);
	
	public SegUsuarioEntity generarCredenciales(Long personaId, SegUsuarioEntity usuarioSession);

}
