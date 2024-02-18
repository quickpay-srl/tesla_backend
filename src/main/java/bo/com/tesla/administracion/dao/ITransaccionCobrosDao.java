package bo.com.tesla.administracion.dao;

import java.util.Date;
import java.util.List;

import bo.com.tesla.administracion.dto.DashboardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bo.com.tesla.administracion.entity.TransaccionCobroEntity;
import bo.com.tesla.entidades.dto.DeudasClienteDto;

@Repository
public interface ITransaccionCobrosDao  extends JpaRepository<TransaccionCobroEntity, Long>{
	
	@Query(value=" select TO_CHAR(date(c.fecha_creacion),'dd/mm/yyyy'),count(*) "
			+ " from  tesla.transacciones_cobros c "
			+ " left join tesla.seg_usuarios u on u.usuario_id=c.usuario_creacion "
			+ " left join tesla.personas p on p.persona_id=u.persona_id "
			+ " left join tesla.empleados e on e.persona_id=p.persona_id "
			+ " left join tesla.sucursales s on s.sucursal_id=e.sucursal_id "
			+ " left join tesla.recaudadores r on r.recaudador_id=s.recaudador_id "
			+ " where  "
			+ " c.entidad_id= :entidadId "
			+ " and cast(s.recaudador_id as VARCHAR) like :recaudadorId "
			+ " and c.estado LIKE concat(:estado,'%') "
			+ " and date(c.fecha_creacion)>=date(:fechaInicio) "
			+ " and date(c.fecha_creacion)<=date(:fechaFin) "
			+ " GROUP BY TO_CHAR(date(c.fecha_creacion),'dd/mm/yyyy') "
			+ " ORDER BY TO_CHAR(date(c.fecha_creacion),'dd/mm/yyyy') ASC", nativeQuery = true)
	public  List<Object[]> getDeudasforDate(
			@Param("entidadId") Long entidadId,
			@Param("recaudadorId") String recaudadorId,
			@Param("estado") String estado,
			@Param("fechaInicio") Date fechaInicio,
			@Param("fechaFin") Date fechaFin
			);
	
	@Query(value=" select c.servicio, count(*) "
			+ " from  tesla.transacciones_cobros c "
			+ " left join tesla.seg_usuarios u on u.usuario_id=c.usuario_creacion "
			+ " left join tesla.personas p on p.persona_id=u.persona_id "
			+ " left join tesla.empleados e on e.persona_id=p.persona_id "
			+ " left join tesla.sucursales s on s.sucursal_id=e.sucursal_id "
			+ " left join tesla.recaudadores r on r.recaudador_id=s.recaudador_id "
			+ " where  "
			+ " c.entidad_id= :entidadId "
			+ " and cast(s.recaudador_id as VARCHAR) like :recaudadorId "
			+ " and c.estado LIKE concat(:estado,'%') "
			+ " and date(c.fecha_creacion)>=date(:fechaInicio) "
			+ " and date(c.fecha_creacion)<=date(:fechaFin) "
			+ " GROUP BY c.servicio "
			+ " ", nativeQuery = true)
	public  List<Object[]> getDeudasforServicio(
			@Param("entidadId") Long entidadId,
			@Param("recaudadorId") String recaudadorId,
			@Param("estado") String estado,
			@Param("fechaInicio") Date fechaInicio,
			@Param("fechaFin") Date fechaFin
			);
	
	@Query(value= " select c.tipo_servicio,count(c.tipo_servicio) as cantidad "
			+ " from  tesla.transacciones_cobros c "
			+ " left join tesla.seg_usuarios u on u.usuario_id=c.usuario_creacion "
			+ " left join tesla.personas p on p.persona_id=u.persona_id "
			+ " left join tesla.empleados e on e.persona_id=p.persona_id "
			+ " left join tesla.sucursales s on s.sucursal_id=e.sucursal_id "
			+ " left join tesla.recaudadores r on r.recaudador_id=s.recaudador_id "
			+ " where  "
			+ " c.entidad_id=1 "
			+ " and cast(s.recaudador_id as VARCHAR) like '%' "
			+ " and c.estado LIKE concat('COBRADO','%')  "
			+ " and date(c.fecha_creacion)>=date('2021-01-01 18:11:05.028') "
			+ " and date(c.fecha_creacion)<=date('2021-08-25 18:11:05.028') "
			+ " GROUP BY c.tipo_servicio "
			, nativeQuery = true)
	public  List<Object[]> getDeudasforTipoServicio(
			@Param("entidadId") Long entidadId,
			@Param("recaudadorId") String recaudadorId,
			@Param("estado") String estado,
			@Param("fechaInicio") Date fechaInicio,
			@Param("fechaFin") Date fechaFin
			);
	
	
	@Query(" Select new  bo.com.tesla.entidades.dto.DeudasClienteDto( "
			+ "	c.servicio,c.tipoServicio,c.periodo,c.fechaCreacion,CONCAT(p.nombres,' ',p.paterno,' ',p.materno)  , "
			+ " c.nombreClientePago,c.totalDeuda,c.metodoCobro.descripcion )"
			+ " from TransaccionCobroEntity c "
			+ " left join SegUsuarioEntity u on u.usuarioId=c.usuarioCreacion "
			+ " left join PersonaEntity p on p.personaId=u.personaId.personaId "
			+ " left join EmpleadoEntity e on e.personaId.personaId=p.personaId "
			+ " left join SucursalEntity s on s.sucursalId=e.sucursalId.sucursalId "
			+ " left join RecaudadorEntity r on r.recaudadorId=s.recaudador.recaudadorId "
			+ " where c.entidadId.entidadId= :entidadId "
			+ " and CAST(r.recaudadorId as string ) like concat( :recaudadorId,'%') "
			+ " and c.estado like concat(:estado,'%') "
			+ " and CAST(c.fechaCreacion AS date) >= CAST(:fechaIni AS date) "
			+ " and CAST(c.fechaCreacion AS date) <= CAST(:fechaFin AS date)  "
			+ " ORDER BY c.fechaCreacion asc ")
	public List<DeudasClienteDto>  findDeudasPagadasByParameter(
			@Param("entidadId") Long entidadId, 
			@Param("recaudadorId") String recaudadorId,
			@Param("estado") String estado,
			@Param("fechaIni") Date fechaIni,
			@Param("fechaFin") Date fechaFin
			);
	
	@Query("Select new bo.com.tesla.entidades.dto.DeudasClienteDto(hd.archivoId.archivoId, hd.codigoCliente,'-',hd.servicio, "
			+ " hd.tipoServicio,hd.periodo, hd.estado,hd.nombreCliente, "
			+ "	tc.fechaCreacion,tc.totalDeuda,r.nombre,tc.comision,tc.metodoCobro.descripcion) "
			+ " from HistoricoDeudaEntity hd "
			+ " left join TransaccionCobroEntity tc on ( tc.archivoId.archivoId=hd.archivoId.archivoId "
			+ "											 and tc.codigoCliente=hd.codigoCliente "
			+ "											 and tc.servicio=hd.servicio "
			+ "											 and tc.tipoServicio=hd.tipoServicio "
			+ "											 and tc.periodo=hd.periodo ) "
			+ " left join RecaudadorEntity r on r.recaudadorId=tc.recaudador.recaudadorId "
			+ " inner join ArchivoEntity a on a.archivoId=hd.archivoId.archivoId "
			+ " inner join EntidadEntity e on e.entidadId=a.entidadId.entidadId "
			+ " Where "			
			+ " e.entidadId= :entidadId "
			+ " and COALESCE(CAST(r.recaudadorId as string ),'') like :recaudadorId "
			+ " and hd.estado like :estado"
			+ " and (  date(tc.fechaCreacion) BETWEEN   date(:fechaInicio) and date(:fechaFin) "
			+ "         or tc.fechaCreacion is null) "
			+ " GROUP BY hd.archivoId,hd.codigoCliente,hd.servicio,hd.tipoServicio,hd.periodo,hd.nombreCliente,hd.estado, "
			+ " tc.totalDeuda,tc.fechaCreacion,tc.comision,r.nombre,tc.metodoCobro.descripcion "
			+ " ORDER BY tc.fechaCreacion ASC"
			)
	public Page<DeudasClienteDto>  findDeudasByParameter(
			@Param("fechaInicio") Date fechaInicio, 
			@Param("fechaFin") Date fechaFin,
			@Param("entidadId") Long entidadId,
			@Param("recaudadorId") String recaudadorId,
			@Param("estado") String estado,
			Pageable pageable
			);
	
	@Query("Select new bo.com.tesla.entidades.dto.DeudasClienteDto(hd.archivoId.archivoId, hd.codigoCliente,'-',hd.servicio, "
			+ " hd.tipoServicio,hd.periodo, hd.estado,hd.nombreCliente, "
			+ "	tc.fechaCreacion,tc.totalDeuda,r.nombre,tc.comision,tc.metodoCobro.descripcion) "
			+ " from HistoricoDeudaEntity hd "
			+ " left join TransaccionCobroEntity tc on ( tc.archivoId.archivoId=hd.archivoId.archivoId "
			+ "											 and tc.codigoCliente=hd.codigoCliente "
			+ "											 and tc.servicio=hd.servicio "
			+ "											 and tc.tipoServicio=hd.tipoServicio "
			+ "											 and tc.periodo=hd.periodo ) "
			+ " left join RecaudadorEntity r on r.recaudadorId=tc.recaudador.recaudadorId "
			+ " inner join ArchivoEntity a on a.archivoId=hd.archivoId.archivoId "
			+ " inner join EntidadEntity e on e.entidadId=a.entidadId.entidadId "
			+ " Where "			
			+ " e.entidadId= :entidadId "
			+ " and COALESCE(CAST(r.recaudadorId as string ),'') like :recaudadorId "
			+ " and hd.estado like :estado"
			+ " and (  date(tc.fechaCreacion) BETWEEN   date(:fechaInicio) and date(:fechaFin) "
			+ "         or tc.fechaCreacion is null) "
			+ " GROUP BY hd.archivoId,hd.codigoCliente,hd.servicio,hd.tipoServicio,hd.periodo,hd.nombreCliente,hd.estado, "
			+ " tc.totalDeuda,tc.fechaCreacion,tc.comision,r.nombre,tc.metodoCobro.descripcion "
			+ " ORDER BY hd.estado,tc.fechaCreacion ASC"
			)
	public List<DeudasClienteDto>  findDeudasByParameterForReport(
			@Param("fechaInicio") Date fechaInicio, 
			@Param("fechaFin") Date fechaFin,
			@Param("entidadId") Long entidadId,
			@Param("recaudadorId") String recaudadorId,
			@Param("estado") String estado			
			);


}
