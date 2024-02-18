package bo.com.tesla.pagos.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bo.com.tesla.administracion.entity.PServicioProductoEntity;

@Repository
public interface IPServicioProductosDao extends JpaRepository<PServicioProductoEntity, Long>{
	
	@Query(" Select s "
			+ " from PServicioProductoEntity s "
			+ " where s.entidadId.entidadId= :entidadId "
			+ " and s.estado= 'ACTIVO' ")
	public  List<PServicioProductoEntity> findByEntidadId(@Param("entidadId") Long entidadId );
	
	@Query(" Select s "
			+ " from PServicioProductoEntity s "
			+ " where s.entidadId.entidadId= :entidadId "
			+ " and s.estado= 'ACTIVO' "
			+ " and s.productoServicioPadreId is null")
	public  List<PServicioProductoEntity> findByEntidadIdForSelect(@Param("entidadId") Long entidadId );
	
	
	
	@Query(" Select s "
			+ " from PServicioProductoEntity s "
			+ " where s.entidadId.entidadId in :entidadIdList "
			+ " and upper(s.descripcion) LIKE  upper(concat('%', :paramBusqueda,'%'))"
			+ " and s.estado= 'ACTIVO' "
			+ " and s.productoServicioPadreId is not  null"			
			+ " and COALESCE(CAST(s.entidadId.tipoEntidad.dominioId  as string ),'') like :tipoEntidadId")
	public  List<PServicioProductoEntity> findByProductos(
			@Param("entidadIdList") List<Long> entidadIdList,  
			@Param("paramBusqueda") String paramBusqueda,
			@Param("tipoEntidadId") String tipoEntidadId
			);
	
	@Query("Select sp "
			+ " from PServicioProductoEntity sp "
			+ " 	left join EntidadEntity e on e.entidadId=sp.entidadId.entidadId "
			+ " 	left join EntidadRecaudadorEntity er on er.entidad.entidadId=e.entidadId "
			+ " Where "
			+ "		er.recaudador.recaudadorId= :recaudadorId "
			+ " 	and sp.estado='ACTIVO'")
	public  List<PServicioProductoEntity> findServiciosForRecaudadorId(			  
			@Param("recaudadorId") Long recaudadorId
			);
	
	
	@Query(" Select s "
			+ " from PServicioProductoEntity s "
			+ " where "
			+ " s.estado= 'ACTIVO' "
			+ " and s.productoServicioPadreId is null")
	public  List<PServicioProductoEntity> findForSelect();
	
	

}
