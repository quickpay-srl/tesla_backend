package bo.com.tesla.security.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bo.com.tesla.administracion.dto.RolTransferDto;
import bo.com.tesla.administracion.entity.SegRolEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.security.dao.ISegRolDao;
import bo.com.tesla.security.dto.UsuarioModulosDto;

@Service
public class SegRolService implements ISegRolService {

	@Autowired
	private ISegRolDao segRolDao;

	@Override
	public List<RolTransferDto> findRolesForTransfer(String subModulo, String modulo) {

		return this.segRolDao.findRolesForTransfer(subModulo, modulo);
	}

	@Override
	public List<String> findRolesForTransferByUsuario(String subModulo, String modulo, Long usuarioId) {

		return this.segRolDao.findRolesForTransferByUsuario(subModulo, modulo, usuarioId);
	}

	@Override
	public List<UsuarioModulosDto> getModuloUsuario(Long usuarioId) {

		return this.segRolDao.getModuloUsuario(usuarioId);
	}

	@Override
	public List<SegRolEntity> findRolesByUsuarioLogin(String login) {

		return this.segRolDao.findRolesByUsuarioLogin(login);
	}

	@Override
	public List<SegUsuarioEntity> findUsuarioAdminByEntidadId(Long entidadId) {
		
		return this.segRolDao.findUsuarioAdminByEntidadId(entidadId);
	}

	@Override
	public List<SegUsuarioEntity> findUsuarioAdminByRecaudacionId(Long recaudadorId) {
		
		return this.segRolDao.findUsuarioAdminByRecaudacionId(recaudadorId);
	}
	
	
	

}
