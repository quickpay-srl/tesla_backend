package bo.com.tesla.security.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bo.com.tesla.administracion.dto.RolTransferDto;
import bo.com.tesla.administracion.entity.SegModuloEntity;
import bo.com.tesla.administracion.entity.SegPrivilegioEntity;
import bo.com.tesla.administracion.entity.SegPrivilegioRolEntity;


@Repository
public interface ISegPrivilegiosDao extends JpaRepository<SegPrivilegioEntity, Long>{
	
	@Query("Select p "
			+ " From SegPrivilegioEntity p "
			+ " 	left join SegPrivilegioRolEntity pr on pr.privilegioId.privilegiosId=p.privilegiosId "
			+ " 	left join SegRolEntity r on r.rolId= pr.rolId.rolId "
			+ " 	left join SegUsuarioRolEntity ur on ur.rolId.rolId=r.rolId "
			+ " 	left join SegPrivilegioEntity pp on pp.privilegiosId=p.privilegioPadreId.privilegiosId "
			+ " where "
			+ "  	ur.usuarioId.usuarioId= :usuarioId "
			+ "		and pp.privilegioPadreId is null "
			+ "		and ur.estado='ACTIVO'"
			+ "		and r.estado='ACTIVO'"
			+ "		and p.estado='ACTIVO'"
			+ "		and pr.estado='ACTIVO'"
			+ " order by p.orden asc "
			+ " ")
	public List<SegPrivilegioEntity> getMenuByUserId(@Param("usuarioId") Long usuarioId);
	
	
	@Query("Select distinct  pp "
			+ " From SegPrivilegioEntity p "
			+ " 	left join SegPrivilegioRolEntity pr on pr.privilegioId.privilegiosId=p.privilegiosId "
			+ " 	left join SegRolEntity r on r.rolId= pr.rolId.rolId "
			+ " 	left join SegUsuarioRolEntity ur on ur.rolId.rolId=r.rolId "
			+ " 	left join SegPrivilegioEntity pp on pp.privilegiosId=p.privilegioPadreId.privilegiosId "
			+ " where "
			+ "  	ur.usuarioId.usuarioId= :usuarioId "
			+ "		and pp.privilegioPadreId is not null "
			+ "		and ur.estado='ACTIVO'"
			+ "		and r.estado='ACTIVO'"
			+ "		and p.estado='ACTIVO'"
		
			+ " ")
	public List<SegPrivilegioEntity> getSubMenuByUserId(@Param("usuarioId") Long usuarioId);
	
	
	
	@Query(value=" select pr.estado from "
			+ " tesla.seg_privilegios_roles pr "
			+ " left join tesla.seg_roles r on r.rol_id=pr.rol_id "
			+ " left join tesla.seg_usuarios_roles ur on ur.rol_id=r.rol_id "
			+ " left join tesla.seg_usuarios u on u.usuario_id=ur.usuario_id "
			+ " left join tesla.seg_privilegios p on p.privilegios_id=pr.privilegio_id "
			+ " where u.usuario_id= :usuarioId "
			+ " and privilegio_id= :privilegioId "
			+ " and p.estado='ACTIVO' "
			+ " and pr.estado='ACTIVO' "
			+ " and ur.estado='ACTIVO'", nativeQuery = true)
	public  String getEstadoPrivilegios(@Param("usuarioId") Long usuarioId,@Param("privilegioId") Long privilegioId);
	
	
	@Query(value=" SELECT distinct t.transaccion_id,t.etiqueta,t.imagen,t.orden "
			+ " FROM tesla.seg_transiciones t "
			+ " left join tesla.seg_privilegios_roles_transiciones prt on (prt.tabla_id=t.tabla_id and prt.estado_inicial_id=t.estado_inicial and  prt.transaccion_id=t.transaccion_id) "
			+ " left join tesla.seg_privilegios_roles pr on pr.privilegio_rol_id=prt.privilegio_rol_id "
			+ " left join tesla.seg_roles r on r.rol_id=pr.rol_id "
			+ " left join tesla.seg_usuarios_roles ur on ur.rol_id=r.rol_id "
			+ " left join tesla.seg_usuarios u on u.usuario_id=ur.usuario_id "
			+ " where t.estado='ACTIVO' "
			+ " AND r.estado='ACTIVO' "
			+ " AND u.estado='ACTIVO' "
			+ " AND prt.estado='ACTIVO' "
			+ " AND  u.login= :login "
			+ " AND t.tabla_id= :tabla "
			+ " order by t.orden ASC ", nativeQuery = true)
	public  List<Object[]> getOperaciones(@Param("login") String login,@Param("tabla") String tabla);


	@Query(value="SELECT distinct t.transaccion_id,t.etiqueta,t.imagen,t.orden,t.estado "
			+ " FROM tesla.seg_transiciones t "
			+ " left join tesla.seg_privilegios_roles_transiciones prt on (prt.tabla_id=t.tabla_id and prt.estado_inicial_id=t.estado_inicial and  prt.transaccion_id=t.transaccion_id) "
			+ " left join tesla.seg_privilegios_roles pr on pr.privilegio_rol_id=prt.privilegio_rol_id "
			+ " left join tesla.seg_roles r on r.rol_id=pr.rol_id "
			+ " left join tesla.seg_usuarios_roles ur on ur.rol_id=r.rol_id "
			+ " left join tesla.seg_usuarios u on u.usuario_id=ur.usuario_id "
			+ " where t.estado='ACTIVO' "
			+ " AND r.estado='ACTIVO' "
			+ " AND u.estado='ACTIVO' "
			+ " AND prt.estado='ACTIVO' "
			+ " AND  u.login= :login "
			+ " AND t.tabla_id= :tabla "
			+ " AND t.estado_inicial IN (:estadoInicial) "
			//+ " AND t.estado_inicial IN (:estadoInicial, 'INICIAL') "
			+ " order by t.orden ASC ", nativeQuery = true)
	public  List<Object[]> getOperacionesByEstadoInicial(@Param("login") String login,
														 @Param("tabla") String tabla,
														 @Param("estadoInicial") String estadoInicial);
	
	
	
	
	@Query("Select m "
			+ " from SegModuloEntity m "
			+ " Where m.estado= 'ACTIVO' ")
	public List<SegModuloEntity> findModulos();
	
	
	
	@Query("Select new bo.com.tesla.administracion.dto.RolTransferDto(p.privilegiosId, p.descripcion, p.descripcion)  "
			+ " from SegPrivilegioEntity p "
			+ " where p.moduloId.moduloId= :moduloId "
			+ " and p.estado= 'ACTIVO' "
			+ " and (p.link is not null or p.link !='')"
			+ " ")
	public List<RolTransferDto> findPrivilegiosByModuloId(@Param("moduloId") Long moduloId);
	
	
	@Query("Select  p.privilegiosId "
			+ " from SegPrivilegioRolEntity pr "
			+ " inner join SegPrivilegioEntity p on p.privilegiosId= pr.privilegioId.privilegiosId "
			+ " inner join SegRolEntity r on r.rolId=pr.rolId.rolId "
			+ " inner join SegUsuarioRolEntity ur on ur.rolId.rolId=r.rolId "
			+ " inner join SegUsuarioEntity u on u.usuarioId=ur.usuarioId.usuarioId "
			+ " Where u.usuarioId= :usuarioId "
			+ " and p.estado='ACTIVO' "
			+ " and pr.estado='ACTIVO'"
			+ " and ur.estado='ACTIVO'")
	public List<String> findPrivilegiosByUsuario(@Param("usuarioId") Long usuarioId);
	
	@Query("Select DISTINCT p.moduloId "
			+ " from SegPrivilegioRolEntity pr "
			+ " inner join SegPrivilegioEntity p on p.privilegiosId= pr.privilegioId.privilegiosId "
			+ " inner join SegRolEntity r on r.rolId=pr.rolId.rolId "
			+ " inner join SegUsuarioRolEntity ur on ur.rolId.rolId=r.rolId "
			+ " inner join SegUsuarioEntity u on u.usuarioId=ur.usuarioId.usuarioId "
			+ " Where u.usuarioId= :usuarioId "
			+ " ")
	public SegModuloEntity findModuloByUsuarioId(@Param("usuarioId") Long usuarioId);
	
	
	@Query("Select pr "
			+ " from SegPrivilegioRolEntity pr "			
			+ " where pr.rolId.rolId= :rolId "
			+ " and pr.privilegioId.privilegiosId= :privilegioId "			
			+ " and pr.privilegioId.estado='ACTIVO' ")
	public Optional<SegPrivilegioRolEntity> findByPrivilegioIdAndRolId(@Param("privilegioId") Long privilegioId,@Param("rolId") Long rolId);
	
	
	@Query("Select pr "
			+ " from SegPrivilegioRolEntity pr "			
			+ " where pr.rolId.rolId= :rolId "			
			+ " and pr.estado='ACTIVO' "
			+ " and pr.privilegioId.estado='ACTIVO' ")
	public List<SegPrivilegioRolEntity> findByRolId(@Param("rolId") Long rolId);
	
	
	@Query("Select  p "
			+ " from SegPrivilegioRolEntity pr "
			+ " inner join SegPrivilegioEntity p on p.privilegiosId= pr.privilegioId.privilegiosId "
			+ " inner join SegRolEntity r on r.rolId=pr.rolId.rolId "
			+ " inner join SegUsuarioRolEntity ur on ur.rolId.rolId=r.rolId "
			+ " inner join SegUsuarioEntity u on u.usuarioId=ur.usuarioId.usuarioId "
			+ " Where u.usuarioId= :usuarioId "
			+ " and p.estado='ACTIVO' "
			//+ " and pr.estado='ACTIVO' "
			+ " and ur.estado='ACTIVO'")
	public List<SegPrivilegioEntity> findPrivilegiosByUsuarioId(@Param("usuarioId") Long usuarioId);
	
	
	
	@Query("Select new bo.com.tesla.administracion.dto.RolTransferDto(p.privilegiosId, p.descripcion, p.descripcion)  "
			+ " from SegPrivilegioRolEntity pr "
			+ " inner join SegPrivilegioEntity p on p.privilegiosId= pr.privilegioId.privilegiosId "
			+ " inner join SegRolEntity r on r.rolId=pr.rolId.rolId "
			+ " inner join SegUsuarioRolEntity ur on ur.rolId.rolId=r.rolId "
			+ " inner join SegUsuarioEntity u on u.usuarioId=ur.usuarioId.usuarioId "
			+ " Where u.usuarioId= :usuarioId "
			+ " and p.estado='ACTIVO' "
			//+ " and pr.estado='ACTIVO' "
			+ " and ur.estado='ACTIVO'")
	public List<RolTransferDto> findPrivilegiosByUsuarioIdForTransfer(@Param("usuarioId") Long usuarioId);
}
