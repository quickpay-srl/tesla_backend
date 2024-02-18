package bo.com.tesla.recaudaciones.services;

import bo.com.tesla.administracion.entity.AccionEntity;
import bo.com.tesla.administracion.entity.DeudaClienteEntity;
import bo.com.tesla.administracion.entity.HistoricoDeudaEntity;
import bo.com.tesla.recaudaciones.dao.IAccionDao;
import bo.com.tesla.recaudaciones.dao.IHistoricoDeudaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccionService implements IAccionService {

	@Autowired
	private IAccionDao iAccionDao;

	@Autowired
	private IHistoricoDeudaDao iHistoricoDeudaDao;

	@Override
	public List<AccionEntity> saveAllAcciones(List<AccionEntity> accionEntities) {
		return iAccionDao.saveAll(accionEntities);
	}

	@Override
	public AccionEntity loadAccion(DeudaClienteEntity deudaClienteEntity, String estado, Long usuarioId) {

		System.out.println("-----------------" + deudaClienteEntity.getDeudaClienteId());
		
		Optional<HistoricoDeudaEntity> optionalHistoricoDeudaEntity = this.iHistoricoDeudaDao.findByDeudaClienteId(deudaClienteEntity.getDeudaClienteId());

		System.out.println("-------------- paso");
		if (!optionalHistoricoDeudaEntity.isPresent()) {
			return null;
		}
		AccionEntity accionEntity = new AccionEntity();
		accionEntity.setHistoricoDeudaId(optionalHistoricoDeudaEntity.get());
		accionEntity.setEstado(estado);
		accionEntity.setFechaEstado(new Date());
		accionEntity.setUsuario(usuarioId);
		return accionEntity;

	}
}
