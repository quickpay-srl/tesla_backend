package bo.com.tesla.security.services;

import java.util.List;
import java.util.Optional;

import bo.com.tesla.administracion.dto.RolTransferDto;
import bo.com.tesla.administracion.entity.SegModuloEntity;
import bo.com.tesla.administracion.entity.SegPrivilegioEntity;
import bo.com.tesla.administracion.entity.SegPrivilegioRolEntity;
import bo.com.tesla.security.dto.OperacionesDto;

public interface ISegPrivilegiosService {
	
	public List<SegPrivilegioEntity> getMenuByUserId(Long usuarioId);
	
	public List<SegPrivilegioEntity> getSubMenuByUserId(Long usuarioId);
	
	public  List<OperacionesDto> getOperaciones(String login, String tabla);

	public List<OperacionesDto> getOperacionesByEstadoInicial(String login, String tabla, String estadoInicial);
	
	public List<SegModuloEntity> findModulos();
	
	public List<RolTransferDto> findPrivilegiosByModuloId(Long moduloId);
	
	public List<String> findPrivilegiosByUsuario(Long usuarioId);
	
	public SegModuloEntity findModuloByUsuarioId( Long usuarioId);
	
	public Optional<SegPrivilegioRolEntity> findByPrivilegioIdAndRolId(Long privilegioId, Long rolId);
	
	public List<SegPrivilegioEntity> findPrivilegiosByUsuarioId(Long usuarioId);
	
	public List<RolTransferDto> findPrivilegiosByUsuarioIdForTransfer( Long usuarioId);
	
	

}
