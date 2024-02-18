package bo.com.tesla.entidades.dao;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bo.com.tesla.administracion.entity.ArchivoEntity;
import bo.com.tesla.entidades.dto.ArchivoDto;

@Repository
public interface IArchivoDao extends JpaRepository<ArchivoEntity, Long> {

	@Query("Select a from ArchivoEntity a Where a.estado= :estado  and a.entidadId.entidadId= :entidadId ")
	public ArchivoEntity findByEstado(@Param("estado") String estado, @Param("entidadId") Long entidadId);

	@Query(" Select a from ArchivoEntity a "
			+ " Where a.estado= :estado  "
			+ " and a.entidadId.entidadId= :entidadId "
			+ " and a.servicioProductoId.servicioProductoId = :servicioProductoId")
	public ArchivoEntity findByEstadoAndEntidadAndServicio(
			@Param("estado") String estado, 
			@Param("entidadId") Long entidadId,
			@Param("servicioProductoId") Long servicioProductoId
			);
	
	@Query(" Select  new bo.com.tesla.entidades.dto.ArchivoDto( "
			+ " a.archivoId, a.nombre, p.nombres ||' '|| p.paterno||' '||p.materno , a.fechaCreacion, a.nroRegistros,a.estado)  "
			+ " from ArchivoEntity a "
			+ " inner join SegUsuarioEntity u on u.usuarioId=a.usuarioCreacion "
			+ " inner join PersonaEntity p on p.personaId=u.personaId.personaId "			 
			+ " Where a.entidadId.entidadId= :entidadId "
			+ " and a.estado!='CREADO' "
			+ " and upper(a.estado) LIKE  upper(concat( :estado,'%')) "
			+ " and CAST(a.fechaCreacion AS date) >= CAST(:fechaIni AS date) "
			+ " and CAST(a.fechaCreacion AS date) <= CAST(:fechaFin AS date)  "
			+ " order by a.fechaCreacion desc ")
	public Page<ArchivoDto> findByEntidadIdAndFechaIniAndFechaFin(
			@Param("entidadId") Long entidadId,
			@Param("fechaIni") Date fechaIni, 
			@Param("fechaFin") Date fechaFin, 
			@Param("estado") String estado,
			Pageable pageable);
	
	@Query(" Select  new bo.com.tesla.entidades.dto.ArchivoDto( "
			+ " a.archivoId, a.nombre, p.nombres ||' '|| p.paterno||' '||p.materno , a.fechaCreacion, a.nroRegistros,a.estado)  "
			+ " from ArchivoEntity a "
			+ " inner join SegUsuarioEntity u on u.usuarioId=a.usuarioCreacion "
			+ " inner join PersonaEntity p on p.personaId=u.personaId.personaId "			 
			+ " Where a.entidadId.entidadId= :entidadId "
			+ " and a.estado!='CREADO'  "
			+ " and a.estado!='FALLIDO'"
			+ " and a.estado LIKE  :estado "
			+ " and CAST(a.fechaCreacion AS date) >= CAST(:fechaIni AS date) "
			+ " and CAST(a.fechaCreacion AS date) <= CAST(:fechaFin AS date)  "
			+ " order by a.fechaCreacion desc ")
	public Page<ArchivoDto> findByEntidadIdAndFechaIniAndFechaFinForEntidades(
			@Param("entidadId") Long entidadId,
			@Param("fechaIni") Date fechaIni, 
			@Param("fechaFin") Date fechaFin, 
			@Param("estado") String estado,
			Pageable pageable);
	
	
	
	@Query(" Select a from ArchivoEntity a "
			+ " Where a.estado= :estado  "
			+ " and a.entidadId.entidadId= :entidadId ")
	public ArchivoEntity findByEstadoAndEntidad(
			@Param("estado") String estado, 
			@Param("entidadId") Long entidadId			
			);

}
