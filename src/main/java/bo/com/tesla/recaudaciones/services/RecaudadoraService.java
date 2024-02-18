package bo.com.tesla.recaudaciones.services;

import java.util.List;
import java.util.Optional;

import bo.com.tesla.useful.config.BusinesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.recaudaciones.dao.IRecaudadorDao;

@Service
public class RecaudadoraService implements IRecaudadoraService {
	
	@Autowired
	private IRecaudadorDao recaudadorDao;

	@Override
	public RecaudadorEntity findRecaudadorByUserId(Long usuarioId) {
		Optional<RecaudadorEntity>  recaudador=this.recaudadorDao.findRecaudadorByUserId(usuarioId);		
		if(recaudador.isPresent()) {
			return this.recaudadorDao.findRecaudadorByUserId(usuarioId).get();	
		}else {
			return null;
		}	
	}

	@Override
	public RecaudadorEntity findByRecaudadorId(Long recaudadorId) {	
		return this.recaudadorDao.findByRecaudadorId(recaudadorId);
	}
	@Override
	public List<RecaudadorEntity> findRecaudadoresByEntidadId(Long entidadId) {		
		return this.recaudadorDao.findRecaudadoresByEntidadId(entidadId);
	}
	@Override
	public List<RecaudadorEntity> findAllRecaudadora() {
	
		return this.recaudadorDao.findAllRecaudadora();
	}
}
