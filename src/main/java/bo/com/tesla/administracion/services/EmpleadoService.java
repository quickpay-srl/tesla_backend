package bo.com.tesla.administracion.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bo.com.tesla.administracion.dao.IEmpleadoDao;
import bo.com.tesla.administracion.entity.EmpleadoEntity;

@Service
public class EmpleadoService implements IEmpleadoService{
	@Autowired
	private IEmpleadoDao empleadoDao;

	@Override
	public EmpleadoEntity save(EmpleadoEntity entity) {
		
		return this.empleadoDao.save(entity);
	}

	@Override
	public Optional<EmpleadoEntity> findByPersonaIdAndSucursalIdSucursalId(Long personaId, Long sucursalId) {
	
		return this.empleadoDao.findByPersonaIdAndSucursalIdSucursalId(personaId, sucursalId);
	}

	@Override
	public Optional<EmpleadoEntity> findEmpleadosByPersonaId(Long personaId) {
		
		return this.empleadoDao.findEmpleadosByPersonaId(personaId);
	}

}
