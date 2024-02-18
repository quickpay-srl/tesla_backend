package bo.com.tesla.entidades.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.entidades.dao.IEntidadDao;
import bo.com.tesla.security.dao.ISegRolDao;


@Service
public class EntidadService implements IEntidadService {
	
	@Autowired
	private IEntidadDao entidadDao;

	@Transactional(readOnly = true)
	@Override
	public EntidadEntity findEntidadByUserId(Long usuarioId) {
		return this.entidadDao.findEntidadByUserId(usuarioId);		
	}

	@Override
	public List<EntidadEntity> findEntidadByRecaudacionId(Long recaudadorId) {
	
		return entidadDao.findEntidadByRecaudacionId(recaudadorId);
	}

	@Override
	public List<EntidadEntity> findAllEntidades() {
	
		return this.entidadDao.findAllEntidades();
	}

	@Override
	public Optional<EntidadEntity> findEntidadById(Long entidadId) {
		
		return this.entidadDao.findEntidadById(entidadId);
	}

}
