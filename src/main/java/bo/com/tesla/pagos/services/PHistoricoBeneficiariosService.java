package bo.com.tesla.pagos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bo.com.tesla.administracion.entity.PHistoricoBeneficiariosEntity;
import bo.com.tesla.pagos.dao.IPHistoricoBeneficiariosDao;

@Service
public class PHistoricoBeneficiariosService implements IPHistoricoBeneficiariosService {
	
	@Autowired
	private IPHistoricoBeneficiariosDao  historicoAbonoClienteDao;

	@Override
	public PHistoricoBeneficiariosEntity save(PHistoricoBeneficiariosEntity entity) {		
		return this.historicoAbonoClienteDao.save(entity);
	}

}
