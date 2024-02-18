package bo.com.tesla.security.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.com.tesla.administracion.dao.IEmpleadoDao;
import bo.com.tesla.administracion.dao.IPersonaDao;
import bo.com.tesla.administracion.dto.PersonaDto;
import bo.com.tesla.administracion.entity.PersonaEntity;
import bo.com.tesla.administracion.entity.SegPrivilegioEntity;
import bo.com.tesla.administracion.entity.SegRolEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.administracion.entity.SegUsuarioRolEntity;
import bo.com.tesla.security.dao.ISegPrivilegioRolesDao;
import bo.com.tesla.security.dao.ISegPrivilegiosDao;
import bo.com.tesla.security.dao.ISegRolDao;
import bo.com.tesla.security.dao.ISegUsuarioDao;
import bo.com.tesla.security.dao.ISegUsuarioRolDao;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;

@Service
public class SegUsuarioRolService implements ISegUsuarioRolService {

	@Autowired
	private ISegUsuarioRolDao usuarioRolDao;
	@Autowired
	private ISegRolDao rolDao;

	@Autowired
	private ISegUsuarioDao usuarioDao;

	@Autowired
	private IPersonaDao personaDao;

	@Autowired
	private IEmpleadoDao empleadoDao;

	@Autowired
	private ISegPrivilegiosDao privilegiosDao;

	@Autowired
	private ISegPrivilegioRolesDao privilegioRolesDao;

	@Transactional
	@Override
	public void saveRolesByUsuarioId(PersonaDto personaDto, SegUsuarioEntity usuarioSession) throws BusinesException   {
		SegRolEntity rol = new SegRolEntity();

		try {
			
		
			PersonaEntity persona = this.personaDao.findById(personaDto.personaId).get();
			SegUsuarioEntity usuario = new SegUsuarioEntity();
			
			
			if (!this.usuarioDao.findByPersonaIdAndEstado(persona.getPersonaId()).isPresent()) {
				throw new BusinesException("Antes de registrar los roles debe registrar las credenciales del usuario.");
			} else {
				usuario = this.usuarioDao.findByPersonaIdAndEstado(persona.getPersonaId()).get();
			}

			for (Long privilegioId : personaDto.privilegiosKey) {
			
				
				rol = this.rolDao.findRolByPrivilegioIdAndModuloId(privilegioId, personaDto.moduloId).get();

				Optional<SegUsuarioRolEntity> usuarioRolOptional = this.usuarioRolDao
						.findByRolIdAndUsuarioId(usuario.getUsuarioId(), rol.getRolId());

				if (usuarioRolOptional.isPresent()) {
					SegUsuarioRolEntity usuarioRol = usuarioRolOptional.get();
					usuarioRol.setEstado("ACTIVO");
					usuarioRol.setUsuarioId(usuario);
					usuarioRol.setRolId(rol);
					usuarioRol = this.usuarioRolDao.save(usuarioRol);

				} else {
					SegUsuarioRolEntity usuarioRol = new SegUsuarioRolEntity();
					usuarioRol.setEstado("ACTIVO");
					usuarioRol.setUsuarioId(usuario);
					usuarioRol.setRolId(rol);
					usuarioRol = this.usuarioRolDao.save(usuarioRol);

				}
			}

			List<SegPrivilegioEntity> privilegioUsuarioList = this.privilegiosDao
					.findPrivilegiosByUsuarioId(usuario.getUsuarioId());

			for (SegPrivilegioEntity privilegio : privilegioUsuarioList) {
				Boolean bandera = true;
				for (Long privilegioId : personaDto.privilegiosKey) {

					if (privilegio.getPrivilegiosId().equals(privilegioId)) {
						bandera = false;
					}
				}
				
				if (bandera) {
					
					rol = this.rolDao.findRolByPrivilegioIdAndModuloId(privilegio.getPrivilegiosId(), personaDto.moduloId)
							.get();
					
					SegUsuarioRolEntity usuarioRolOptional = this.usuarioRolDao
							.findByRolIdAndUsuarioId(usuario.getUsuarioId(), rol.getRolId()).get();

					usuarioRolOptional.setEstado("INACTIVO");
					this.usuarioRolDao.save(usuarioRolOptional);
				}
			}
		} catch (BusinesException e) {
			e.printStackTrace();
			throw new BusinesException(e.getMessage());
			
		}catch (Exception e) {
			throw new Technicalexception(e.getMessage(),e.getCause());
		}
		

	}

}
