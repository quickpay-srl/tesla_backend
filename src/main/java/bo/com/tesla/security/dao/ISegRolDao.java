package bo.com.tesla.security.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import bo.com.tesla.administracion.dto.RolTransferDto;
import bo.com.tesla.administracion.entity.SegRolEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.security.dto.UsuarioModulosDto;

public interface ISegRolDao extends JpaRepository<SegRolEntity, Long> {

	@Query("Select ur.rolId " 
			+ " from SegUsuarioRolEntity ur "
			+ " where ur.usuarioId.login=:login "
			+ " AND ur.usuarioId.estado='ACTIVO' "
			+ " AND ur.estado = 'ACTIVO' "
			+ " and ur.rolId.estado='ACTIVO' ")
	public List<SegRolEntity> findRolesByUsuarioLogin(@Param("login") String login);
	
	
	@Query("Select new bo.com.tesla.administracion.dto.RolTransferDto(r.rolId, r.descripcion, r.descripcion) " 
			+ " from SegRolEntity r "
			+ " where r.subModulo= :subModulo " 
			+ " and r.modulo= :modulo "
			+ " and r.estado='ACTIVO' ")
	public List<RolTransferDto> findRolesForTransfer(
			@Param("subModulo") String subModulo,
			@Param("modulo") String modulo
			);
	
	
	@Query("Select ur.rolId.rolId " 
			+ " from SegUsuarioRolEntity ur "			
			+ " where ur.rolId.subModulo= :subModulo " 
			+ " and ur.rolId.modulo= :modulo "
			+ " and ur.rolId.estado='ACTIVO' "
			+ " and ur.usuarioId.usuarioId= :usuarioId "
			+ " and ur.estado='ACTIVO' ")
	public List<String> findRolesForTransferByUsuario(
			@Param("subModulo") String subModulo,
			@Param("modulo") String modulo,
			@Param("usuarioId") Long usuarioId
			);
	
	
	@Query("Select DISTINCT new bo.com.tesla.security.dto.UsuarioModulosDto(ur.rolId.modulo,ur.rolId.subModulo ) " 
			+ " from SegUsuarioRolEntity ur "
			+ " Where "
			+ " ur.usuarioId.usuarioId= :usuarioId "
			+ " and ur.estado='ACTIVO' ")
	public List<UsuarioModulosDto> getModuloUsuario(			
			@Param("usuarioId") Long usuarioId
			);
	
	
	@Query("Select new bo.com.tesla.administracion.dto.RolTransferDto(ur.rolId.descripcion) " 
			+ " from SegUsuarioRolEntity ur "
			+ " where ur.usuarioId.usuarioId=:usuarioId AND ur.usuarioId.estado='ACTIVO' " 
			+ " and ur.rolId.estado='ACTIVO' "
			+ " and ur.estado='ACTIVO'")
	public List<RolTransferDto> findNombreRolesByUsuarioId(@Param("usuarioId") Long usuarioId);
	
	@Query("Select pr.rolId "
			+ " from SegPrivilegioRolEntity pr "
			+ " where "
			+ "   pr.privilegioId.privilegiosId = :privilegiosId "
			+ "   and pr.privilegioId.moduloId.moduloId= :moduloId"
			+ "   and pr.rolId.moduloId.moduloId = :moduloId")
	public Optional<SegRolEntity> findRolByPrivilegioIdAndModuloId(
							@Param("privilegiosId") Long privilegiosId,
							@Param("moduloId") Long moduloId);
	
	@Query("Select ur.rolId " 
			+ " from SegUsuarioRolEntity ur "
			+ " where ur.usuarioId.usuarioId=:usuarioId " 
			)
	public List<SegRolEntity> findRolesByUsuarioId(@Param("usuarioId") Long usuarioId);
	
	
	
	@Query(" Select u "
			+ " from EmpleadoEntity e "
			+ " inner join PersonaEntity p on p.personaId=e.personaId.personaId "
			+ " inner join EntidadEntity et on et.entidadId= e.entidadId.entidadId "
			+ " inner join SegUsuarioEntity u on u.personaId.personaId=p.personaId "
			+ " Where p.admin = true "
			+ " and   et.entidadId = :entidadId "
			+ " and   u.estado='ACTIVO' "
			+ " and  p.estado='ACTIVO' "
			+ " order by u.usuarioId asc ")
	public List<SegUsuarioEntity> findUsuarioAdminByEntidadId(@Param("entidadId") Long entidadId);
	
	
	@Query(" Select u "
			+ " from EmpleadoEntity e "
			+ " inner join PersonaEntity p on p.personaId=e.personaId.personaId "
			+ " inner join SucursalEntity s on s.sucursalId=e.sucursalId.sucursalId "
			+ " inner join RecaudadorEntity r on r.recaudadorId=s.recaudador.recaudadorId"
			+ " inner join SegUsuarioEntity u on u.personaId.personaId=p.personaId "
			+ " Where p.admin = true "
			+ " and  r.recaudadorId = :recaudadorId "
			+ " and u.estado= 'ACTIVO' "
			+ " and p.estado= 'ACTIVO' "
			+ " order by u.usuarioId asc ")
	public List<SegUsuarioEntity> findUsuarioAdminByRecaudacionId(@Param("recaudadorId") Long recaudadorId);
	
	

}
