package bo.com.tesla.pagos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bo.com.tesla.administracion.entity.PServicioProductoEntity;
import bo.com.tesla.pagos.dao.IPServicioProductosDao;

@Service
public class PServicioProductosService implements IPServicioProductosService {

	@Autowired
	private IPServicioProductosDao servicioProductosDao;

	@Override
	public List<PServicioProductoEntity> findByEntidadId(Long entidadId) {
		return this.servicioProductosDao.findByEntidadId(entidadId);
	}

	@Override
	public Optional<PServicioProductoEntity> findById(Long servicioProductoId) {
		return this.servicioProductosDao.findById(servicioProductoId);
	}

	@Override
	public List<PServicioProductoEntity> findByEntidadIdForSelect(Long entidadId) {
		return this.servicioProductosDao.findByEntidadIdForSelect(entidadId);
	}

	@Override
	public List<PServicioProductoEntity> findByProductos(List<Long> entidadIdList, String parametros,
			String tipoEntidadId) {
		return this.servicioProductosDao.findByProductos(entidadIdList, parametros, tipoEntidadId);
	}

	@Override
	public List<PServicioProductoEntity> findServiciosForRecaudadorId(Long recaudadorId) {
		return this.servicioProductosDao.findServiciosForRecaudadorId(recaudadorId);
	}

	@Override
	public List<PServicioProductoEntity> findForSelect() {		
		return this.servicioProductosDao.findForSelect();
	}

}
