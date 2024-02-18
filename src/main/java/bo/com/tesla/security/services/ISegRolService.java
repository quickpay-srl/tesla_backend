package bo.com.tesla.security.services;

import java.util.List;

import bo.com.tesla.administracion.dto.RolTransferDto;
import bo.com.tesla.administracion.entity.SegRolEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.security.dto.UsuarioModulosDto;

public interface ISegRolService {
	
	public List<RolTransferDto> findRolesForTransfer(
			String subModulo,
			String modulo
			);
	
	public List<String> findRolesForTransferByUsuario(
			String subModulo,
			String modulo,
			Long usuarioId
			);
	
	public List<UsuarioModulosDto> getModuloUsuario(Long usuarioId);	
	
	public List<SegRolEntity> findRolesByUsuarioLogin(String login);
	
	public List<SegUsuarioEntity> findUsuarioAdminByEntidadId(Long entidadId);

	public List<SegUsuarioEntity> findUsuarioAdminByRecaudacionId(Long recaudadorId);
}
