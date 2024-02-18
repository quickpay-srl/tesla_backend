package bo.com.tesla.entidades.services;

import java.util.List;
import java.util.Optional;

import bo.com.tesla.administracion.entity.EntidadEntity;

public interface IEntidadService {
	
	public EntidadEntity findEntidadByUserId(Long usuarioId);
	
	public List<EntidadEntity> findEntidadByRecaudacionId(Long recaudadorId);
	
	public List<EntidadEntity> findAllEntidades();
	
	public Optional<EntidadEntity> findEntidadById(Long entidadId);

}
