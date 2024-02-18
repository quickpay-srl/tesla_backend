package bo.com.tesla.recaudaciones.services;

import java.util.List;

import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.useful.config.BusinesException;

public interface IRecaudadoraService {
	
	public RecaudadorEntity findRecaudadorByUserId(Long usuarioId);
	
	public RecaudadorEntity findByRecaudadorId(Long recaudadorId);

	public List<RecaudadorEntity> findRecaudadoresByEntidadId(Long entidadId);
	
	public List<RecaudadorEntity> findAllRecaudadora();

}
