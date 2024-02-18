package bo.com.tesla.pagos.services;

import java.util.List;
import java.util.Optional;

import bo.com.tesla.administracion.entity.PServicioProductoEntity;

public interface IPServicioProductosService {

	public List<PServicioProductoEntity> findByEntidadId(Long entidadId);

	public Optional<PServicioProductoEntity> findById(Long servicioProductoId);

	public List<PServicioProductoEntity> findByEntidadIdForSelect(Long entidadId);

	public List<PServicioProductoEntity> findByProductos(List<Long> entidadIdList, String parametros,
			String tipoEntidadId);

	public List<PServicioProductoEntity> findServiciosForRecaudadorId(Long recaudadorId);
	
	public  List<PServicioProductoEntity> findForSelect();

}
