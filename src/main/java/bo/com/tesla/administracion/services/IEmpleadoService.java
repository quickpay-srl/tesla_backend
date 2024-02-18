package bo.com.tesla.administracion.services;

import java.util.Optional;

import bo.com.tesla.administracion.entity.EmpleadoEntity;

public interface IEmpleadoService {
	
	public EmpleadoEntity save(EmpleadoEntity entity);
	
	public Optional<EmpleadoEntity>  findByPersonaIdAndSucursalIdSucursalId(Long personaId,Long sucursalId);
	
	public Optional<EmpleadoEntity>  findEmpleadosByPersonaId(Long personaId);

}
