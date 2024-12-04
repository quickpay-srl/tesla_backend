package bo.com.tesla.recaudaciones.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import bo.com.tesla.administracion.dto.DeudasClienteAdmv2Dto;
import bo.com.tesla.administracion.dto.ReporteAdminEmpresaDto;
import bo.com.tesla.administracion.dto.ReporteAdminSocioDto;
import bo.com.tesla.recaudaciones.dto.ReporteCierreCajaDiarioDto;
import bo.com.tesla.recaudaciones.dto.ReporteCobrosSocioDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bo.com.tesla.administracion.dto.DeudasClienteAdmDto;
import bo.com.tesla.administracion.entity.TransaccionCobroEntity;
import bo.com.tesla.entidades.dto.DeudasClienteDto;
import bo.com.tesla.recaudaciones.dto.DeudasClienteRecaudacionDto;
import bo.com.tesla.recaudaciones.dto.TransaccionesCobroApiDto;


@Repository
public interface ITransaccionCobroDao extends JpaRepository<TransaccionCobroEntity, Long> {
	
	@Query("Select new bo.com.tesla.recaudaciones.dto.DeudasClienteRecaudacionDto(hd.archivoId.archivoId, hd.codigoCliente,f.responseNroFactura, hd.servicio, "
			+ " hd.tipoServicio,hd.periodo, hd.estado,hd.nombreCliente, "
			+ "	tc.fechaCreacion,tc.totalDeuda,r.nombre,tc.comision,tc.comisionRecaudacion,p.nombres ||' '||p.paterno||' '||p.materno,e.nombre,tc.metodoCobro.descripcion) "
			+ " from HistoricoDeudaEntity hd "
			+ " left join TransaccionCobroEntity tc on ( tc.archivoId.archivoId=hd.archivoId.archivoId "
			+ "											 and tc.codigoCliente=hd.codigoCliente "
			+ "											 and tc.servicio=hd.servicio "
			+ "											 and tc.tipoServicio=hd.tipoServicio "
			+ "											 and tc.periodo=hd.periodo ) "
			+ " left join RecaudadorEntity r on r.recaudadorId=tc.recaudador.recaudadorId "
			+ " left join ArchivoEntity a on a.archivoId=hd.archivoId.archivoId "
			+ " left join EntidadEntity e on e.entidadId=a.entidadId.entidadId "
			+ " left join SegUsuarioEntity u on u.usuarioId=tc.usuarioCreacion "
			+ " left join PersonaEntity p on p.personaId=u.personaId.personaId "
			+ " left join FacturaEntity f on f.facturaId=tc.facturaId "
			+ " Where "					
			+ " hd.estado in :estado "			
			+ " and (CAST(e.entidadId as string ) in :entidadId and r.recaudadorId = :recaudadorId )  "
			+ " and (  date(tc.fechaCreacion) BETWEEN   date(:fechaInicio) and date(:fechaFin) "
			+ "         or tc.fechaCreacion is null) "
			+ " GROUP BY hd.archivoId,hd.codigoCliente,f.responseNroFactura, hd.tipoServicio,hd.servicio,hd.periodo,hd.nombreCliente,hd.estado, "
			+ " tc.totalDeuda,tc.fechaCreacion,tc.comision,tc.comisionRecaudacion,r.nombre,p.nombres ||' '||p.paterno||' '||p.materno,e.nombre,tc.metodoCobro.descripcion "
			+ " ORDER BY hd.estado,e.nombre,tc.fechaCreacion ASC"
			)
	public Page<DeudasClienteRecaudacionDto>  findDeudasByParameterForRecaudacion(
			@Param("fechaInicio") Date fechaInicio, 
			@Param("fechaFin") Date fechaFin,
			@Param("entidadId") List<String> entidadId,
			@Param("recaudadorId") Long recaudadorId,
			@Param("estado") List<String> estado,
			Pageable pageable
			);
	
	@Query("Select new bo.com.tesla.recaudaciones.dto.DeudasClienteRecaudacionDto(hd.archivoId.archivoId, hd.codigoCliente,f.responseNroFactura,hd.servicio, "
			+ " hd.tipoServicio,hd.periodo, hd.estado,hd.nombreCliente, "
			+ "	tc.fechaCreacion,tc.totalDeuda,r.nombre,tc.comision,tc.comisionRecaudacion,p.nombres ||' '||coalesce(p.paterno,'')||' '||coalesce(p.materno,''),e.nombre,tc.metodoCobro.descripcion) "
			//+ "	tc.fechaCreacion,tc.totalDeuda,r.nombre,tc.comision,concat(p.nombres,' ', p.paterno, ' ', coalesce(p.materno,'')),e.nombre) "
			+ " from HistoricoDeudaEntity hd "
			+ " left join TransaccionCobroEntity tc on ( tc.archivoId.archivoId=hd.archivoId.archivoId "
			+ "											 and tc.codigoCliente=hd.codigoCliente "
			+ "											 and tc.servicio=hd.servicio "
			+ "											 and tc.tipoServicio=hd.tipoServicio "
			+ "											 and tc.periodo=hd.periodo ) "
			+ " left join RecaudadorEntity r on r.recaudadorId=tc.recaudador.recaudadorId "
			+ " left join ArchivoEntity a on a.archivoId=hd.archivoId.archivoId "
			+ " left join EntidadEntity e on e.entidadId=a.entidadId.entidadId "
			+ " left join SegUsuarioEntity u on u.usuarioId=tc.usuarioCreacion "
			+ " left join PersonaEntity p on p.personaId=u.personaId.personaId "
			+ " left join FacturaEntity f on f.facturaId=tc.facturaId "
			+ " Where "					
			+ " hd.estado in :estado "
			+ " and (CAST(e.entidadId as string ) in :entidadId and r.recaudadorId = :recaudadorId)  "
			+ " and (  date(tc.fechaCreacion) BETWEEN   date(:fechaInicio) and date(:fechaFin) "
			+ "         or tc.fechaCreacion is null) "
			+ " GROUP BY hd.archivoId,hd.codigoCliente,f.responseNroFactura,hd.tipoServicio,hd.servicio,hd.periodo,hd.nombreCliente,hd.estado, "
			+ " tc.totalDeuda,tc.fechaCreacion,tc.comision,tc.comisionRecaudacion,r.nombre,p.nombres ||' '||coalesce(p.paterno,'')||' '||coalesce(p.materno,''),e.nombre,tc.metodoCobro.descripcion "
			//+ " tc.totalDeuda,tc.fechaCreacion,tc.comision,r.nombre,concat(p.nombres,' ', p.paterno, ' ', coalesce(p.materno,'')),e.nombre "
			+ " ORDER BY e.nombre,hd.estado,tc.fechaCreacion ASC"
			)
	public List<DeudasClienteRecaudacionDto>  findDeudasByParameterForReportRecaudacion(
			@Param("fechaInicio") Date fechaInicio, 
			@Param("fechaFin") Date fechaFin,
			@Param("entidadId") List<String> entidadId,
			@Param("recaudadorId") Long recaudadorId,
			@Param("estado") List<String> estado		
			);

	/*Long archivoId,
	String codigoCliente,
	String nroFactura,
	String servicio,
	String tipoServicio,
	String periodo,
	String estado,
	String nombreClientePago,
	Date fechaCreacion,
	BigDecimal total,
	String nombreRecaudadora,
	BigDecimal comision,
	String metodoCobro*/


	@Query("Select new bo.com.tesla.entidades.dto.DeudasClienteDto(hd.archivoId.archivoId, hd.codigoCliente, f.responseNroFactura, hd.servicio, "
			+ " hd.tipoServicio,hd.periodo, hd.estado,hd.nombreCliente, "
			+ "	tc.fechaCreacion,tc.totalDeuda,r.nombre, tc.comision,tc.metodoCobro.descripcion) "
			+ " from HistoricoDeudaEntity hd "
			+ " left join TransaccionCobroEntity tc on ( tc.archivoId.archivoId=hd.archivoId.archivoId "
			+ "											 and tc.codigoCliente=hd.codigoCliente "
			+ "											 and tc.servicio=hd.servicio "
			+ "											 and tc.tipoServicio=hd.tipoServicio "
			+ "											 and tc.periodo=hd.periodo ) "
			+ " left join RecaudadorEntity r on r.recaudadorId=tc.recaudador.recaudadorId "
			+ " left join FacturaEntity f on f.facturaId=tc.facturaId "
			+ " inner join ArchivoEntity a on a.archivoId=hd.archivoId.archivoId "
			+ " inner join EntidadEntity e on e.entidadId=a.entidadId.entidadId "
			+ " Where "			
			+ " e.entidadId= :entidadId "		
			+ " and (COALESCE(CAST(r.recaudadorId as string ),'') in :recaudadorId or r.recaudadorId = null ) "
			+ " and hd.estado in :estado  "
			+ " and (  date(tc.fechaCreacion) BETWEEN   date(:fechaInicio) and date(:fechaFin) "
			+ "         or tc.fechaCreacion is null) "
			+ " GROUP BY hd.archivoId,hd.codigoCliente,f.responseNroFactura,hd.servicio,hd.tipoServicio,hd.periodo,hd.estado,hd.nombreCliente,"
			+ " tc.totalDeuda,r.nombre,tc.fechaCreacion ,tc.comision,tc.metodoCobro.descripcion"
			+ " ORDER BY hd.estado,r.nombre,tc.fechaCreacion ASC "
			)
	public Page<DeudasClienteDto>  findDeudasByParameterForEntidad(
			@Param("fechaInicio") Date fechaInicio, 
			@Param("fechaFin") Date fechaFin,
			@Param("entidadId") Long entidadId,
			@Param("recaudadorId") List<String> recaudadorId,
			@Param("estado") List<String> estado,
			Pageable pageable
			);
	
	@Query("Select new bo.com.tesla.entidades.dto.DeudasClienteDto(hd.archivoId.archivoId, hd.codigoCliente,f.responseNroFactura, hd.servicio, "
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
			+ " left join FacturaEntity f on f.facturaId=tc.facturaId "
			+ " Where "			
			+ " e.entidadId= :entidadId "
			+ " and (COALESCE(CAST(r.recaudadorId as string ),'') in :recaudadorId or r.recaudadorId = null )"
			+ " and hd.estado in :estado"
			+ " and (  date(tc.fechaCreacion) BETWEEN   date(:fechaInicio) and date(:fechaFin) "
			+ "         or tc.fechaCreacion is null) "
			+ " GROUP BY hd.archivoId,hd.codigoCliente,f.responseNroFactura,hd.tipoServicio,hd.servicio,hd.periodo,hd.nombreCliente,hd.estado, "
			+ " tc.totalDeuda,tc.fechaCreacion,tc.comision,r.nombre,tc.metodoCobro.descripcion "
			+ " ORDER BY hd.estado,r.nombre,tc.fechaCreacion ASC "
			)
	public List<DeudasClienteDto>  findDeudasByParameterForReportEntidades(
			@Param("fechaInicio") Date fechaInicio, 
			@Param("fechaFin") Date fechaFin,
			@Param("entidadId") Long entidadId,
			@Param("recaudadorId") List<String> recaudadorId,
			@Param("estado") List<String> estado			
			);


	@Query(" Select new  bo.com.tesla.entidades.dto.DeudasClienteDto( "
			+ "	c.servicio,c.tipoServicio,c.periodo,c.fechaCreacion,CONCAT(p.nombres,' ',p.paterno,' ',p.materno) , "
			+ " c.nombreClientePago,c.totalDeuda,c.metodoCobro.descripcion)"
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
	
	
	
	@Query("Select new bo.com.tesla.administracion.dto.DeudasClienteAdmDto(hd.archivoId.archivoId, hd.codigoCliente,hd.servicio, "
			+ " hd.tipoServicio,hd.periodo, hd.estado,hd.nombreCliente, "
			+ "	tc.fechaCreacion,tc.totalDeuda,r.nombre,tc.comision,p.nombres ||' '||COALESCE(p.paterno,'')||' '||COALESCE(p.materno,''),e.nombre) "
			+ " from HistoricoDeudaEntity hd "
			+ " left join TransaccionCobroEntity tc on ( tc.archivoId.archivoId=hd.archivoId.archivoId "
			+ "											 and tc.codigoCliente=hd.codigoCliente "
			+ "											 and tc.servicio=hd.servicio "
			+ "											 and tc.tipoServicio=hd.tipoServicio "
			+ "											 and tc.periodo=hd.periodo ) "
			+ " left join RecaudadorEntity r on r.recaudadorId=tc.recaudador.recaudadorId "
			+ " left join ArchivoEntity a on a.archivoId=hd.archivoId.archivoId "
			+ " left join EntidadEntity e on e.entidadId=a.entidadId.entidadId "
			+ " left join SegUsuarioEntity u on u.usuarioId=tc.usuarioCreacion "
			+ " left join PersonaEntity p on p.personaId=u.personaId.personaId "
			+ " Where "			
			+ " COALESCE(CAST(e.entidadId as string ),'') like :entidadId "
			+ " and COALESCE(CAST(r.recaudadorId as string ),'') like :recaudadorId "			
			+ " and hd.estado in :estado"
			+ " and (  date(tc.fechaCreacion) BETWEEN   date(:fechaInicio) and date(:fechaFin) "
			+ "         or tc.fechaCreacion is null) "
			+ " GROUP BY hd.archivoId,hd.codigoCliente,hd.tipoServicio,hd.servicio,hd.periodo,hd.nombreCliente,hd.estado, "
			+ " tc.totalDeuda,tc.fechaCreacion,tc.comision,r.nombre,p.nombres ||' '||COALESCE(p.paterno,'')||' '||COALESCE(p.materno,''),e.nombre "
			+ " ORDER BY tc.fechaCreacion ASC"
			)
	public Page<DeudasClienteAdmDto>  findDeudasByParameterForAdmin(
			@Param("fechaInicio") Date fechaInicio, 
			@Param("fechaFin") Date fechaFin,
			@Param("entidadId") String entidadId,
			@Param("recaudadorId") String recaudadorId,
			@Param("estado") List<String> estado,
			Pageable pageable
			);




	@Query(value="SELECT hd.archivo_id AS archivoId, \n" +
			"       hd.codigo_cliente AS codigoCliente,\n" +
			"       hd.servicio AS servicio,\n" +
			"       hd.tipo_servicio AS tipoServicio,\n" +
			"       hd.periodo AS periodo,\n" +
			"       hd.estado AS estado,\n" +
			"       hd.nombre_cliente AS nombreCliente,\n" +
			"       tc.fecha_creacion AS fechaCreacion,\n" +
			"       tc.total_deuda AS total,\n" +
			"       r.nombre AS nombreRecaudadora,\n" +
			"       tc.comision AS comision,\n" +
			"       (p.nombres||' '||coalesce(p.paterno, '')||' '||coalesce(p.materno, '')) AS cajero, \n" +
			"       e.nombre AS nombre \n" +
			" FROM tesla.historicos_deudas hd \n" +
			" LEFT OUTER JOIN tesla.transacciones_cobros tc ON (tc.archivo_id=hd.archivo_id \n" +
			"                                                  AND tc.codigo_cliente=hd.codigo_cliente \n" +
			"                                                  AND tc.servicio=hd.servicio \n" +
			"                                                  AND tc.tipo_servicio=hd.tipo_servicio \n" +
			"                                                  AND tc.periodo=hd.periodo) \n" +
			" LEFT OUTER JOIN tesla.recaudadores r ON (r.recaudador_id=tc.recaudador_id) \n" +
			" LEFT OUTER JOIN tesla.archivos a ON (a.archivo_id=hd.archivo_id) \n" +
			" LEFT OUTER JOIN tesla.entidades e ON (e.entidad_id=a.entidad_id) \n" +
			" LEFT OUTER JOIN tesla.seg_usuarios u ON (u.usuario_id=tc.usuario_creacion) \n" +
			" LEFT OUTER JOIN tesla.personas p ON (p.persona_id=u.persona_id) \n" +
			" WHERE \n" +
			" (CASE  \n" +
			" WHEN :tieneEstado = TRUE THEN --tieneEstado \n" +
			"  hd.estado in (:estado) " +
			" END) and " +
			" (CASE  " +
			" WHEN :tieneEntidad = TRUE THEN --tieneEntidad \n" +
			"  e.entidad_id in (:entidadId) \n" +
			" END) and\n" +
			" (CASE  \n" +
			" WHEN :tieneRecaudador = TRUE THEN --tieneSocio \n" +
			"  r.recaudador_id in (:recaudadorId) \n" +
			" END) and \n" +
			" (CASE  \n" +
			" WHEN :filtroRangoFechas = TRUE THEN --tieneFiltroPorRangoFechas \n" +
			" date(tc.fecha_creacion) between date(:fechaInicio) and date(:fechaFin) \n " +
			" else " +
			" EXTRACT( MONTH FROM tc.fecha_creacion )  = :mes and EXTRACT( day  FROM tc.fecha_creacion ) =:dia \n" +
			" END)", nativeQuery = true)

	public Page<DeudasClienteAdmv2Dto> findDeudasByParameterForAdminv2(
			@Param("fechaInicio") Date fechaInicio,
			@Param("fechaFin") Date fechaFin,
			@Param("entidadId") List<Long> entidadId,
			@Param("recaudadorId") List<Long> recaudadorId,
			@Param("estado") List<String> estado,
			@Param("tieneEntidad") boolean tieneEntidad,
			@Param("tieneRecaudador") boolean tieneRecaudador,
			@Param("tieneEstado") boolean tieneEstado,
			@Param("filtroRangoFechas") Boolean filtroRangoFechas,
			@Param("mes") Integer mes,
			@Param("dia") Integer dia,
			Pageable pageable
	);



	@Query("Select new bo.com.tesla.administracion.dto.DeudasClienteAdmDto(hd.archivoId.archivoId, hd.codigoCliente,hd.servicio, "
			+ " hd.tipoServicio,hd.periodo, hd.estado,hd.nombreCliente, "
			+ "	tc.fechaCreacion,tc.totalDeuda,r.nombre,tc.comision, "
			+ " p.nombres ||' '||COALESCE(p.paterno,'')||' '||COALESCE(p.materno,''),e.nombre) "
			+ " from HistoricoDeudaEntity hd "
			+ " left join TransaccionCobroEntity tc on ( tc.archivoId.archivoId=hd.archivoId.archivoId "
			+ "											 and tc.codigoCliente=hd.codigoCliente "
			+ "											 and tc.servicio=hd.servicio "
			+ "											 and tc.tipoServicio=hd.tipoServicio "
			+ "											 and tc.periodo=hd.periodo ) "
			+ " left join RecaudadorEntity r on r.recaudadorId=tc.recaudador.recaudadorId "
			+ " left join ArchivoEntity a on a.archivoId=hd.archivoId.archivoId "
			+ " left join EntidadEntity e on e.entidadId=a.entidadId.entidadId "
			+ " left join SegUsuarioEntity u on u.usuarioId=tc.usuarioCreacion "
			+ " left join PersonaEntity p on p.personaId=u.personaId.personaId "
			+ " Where "			
			+ " COALESCE(CAST(e.entidadId as string ),'') like :entidadId "
			+ " and COALESCE(CAST(r.recaudadorId as string ),'') like :recaudadorId "		
			+ " and hd.estado in :estado "
			+ " and (  date(tc.fechaCreacion) BETWEEN   date(:fechaInicio) and date(:fechaFin) "
			+ "         or tc.fechaCreacion is null) "
			+ " GROUP BY hd.archivoId,hd.codigoCliente,hd.tipoServicio,hd.servicio,hd.periodo,hd.nombreCliente,hd.estado, "
			+ " tc.totalDeuda,tc.fechaCreacion,tc.comision,r.nombre,p.nombres ||' '||COALESCE(p.paterno,'')||' '||COALESCE(p.materno,''),e.nombre "
			+ " ORDER BY hd.estado,e.nombre,r.nombre, tc.fechaCreacion ASC"
			)
	public List<DeudasClienteAdmDto>  findDeudasByParameterForReportAdmin(
			@Param("fechaInicio") Date fechaInicio, 
			@Param("fechaFin") Date fechaFin,
			@Param("entidadId") String entidadId,
			@Param("recaudadorId") String recaudadorId,
			@Param("estado") List<String> estado			
			);

	
	
	@Query("Select tc "
			+ " from TransaccionCobroEntity tc "
			+ " where date(tc.fechaCreacion) = date(:fechaSeleccionada) "
			+ " or tc.usuarioCreacion= :usuarioCreacion "
			+ " or tc.estado='COBRADO' "
			+ " or tc.entidadId.entidadId= :entidadId "
			)
	public List<TransaccionCobroEntity>  findDeudasCobradasByUsuarioCreacionForGrid(			
			@Param("usuarioCreacion") Long usuarioCreacion,
			@Param("fechaSeleccionada") Date fechaSeleccionada,
			@Param("entidadId") Long entidadId
			);


	@Modifying
	@Query(value = "UPDATE TransaccionCobroEntity t " +
			"SET t.transaccion = :transaccion, t.usuarioModificacion = :usuarioModificacionId, t.fechaModificacion = CURRENT_TIMESTAMP " +
			"WHERE t.facturaId in :facturaIdLst " +
			//"AND t.modalidadFacturacion.dominioId = :modalidadFacturacionId " +
			"AND (SELECT a.estado " +
				"FROM ArchivoEntity a " +
				"WHERE a.archivoId = t.archivoId.archivoId) = 'ACTIVO'")//Permite controlar que no se actualice con un cargado nuevo
	Integer updateLstTransaccionByFacturas(@Param("facturaIdLst") List<Long> facturaIdLst,
												//@Param("modalidadFacturacionId") Long modalidadFacturacionId,
												@Param("transaccion") String transaccion,
												@Param("usuarioModificacionId") Long usuarioModificacionId);

	@Query(value = "SELECT count(t) " +
			"FROM TransaccionCobroEntity t " +
			"WHERE t.facturaId in :facturaIdLst " +
			"AND t.modalidadFacturacion.dominioId = :modalidadFacturacionId " +
			"AND (SELECT a.estado " +
				"FROM ArchivoEntity a " +
				"WHERE a.archivoId = t.archivoId.archivoId) != 'ACTIVO'")
	Integer countArchivosNoActivosPorListaFacturas(@Param("facturaIdLst") List<Long> facturaIdLst,
										   @Param("modalidadFacturacionId") Long modalidadFacturacionId);

	@Modifying
	@Query("UPDATE TransaccionCobroEntity t " +
			"SET t.facturaId = :facturaId, t.transaccion = 'COBRAR' " +
			"WHERE t.transaccionCobroId in :transaccionCobroIdLst ")
	Integer updateFacturaTransaccion(@Param("transaccionCobroIdLst") List<Long> transaccionCobroIdLst,
						  @Param("facturaId") Long facturaId);

	List<TransaccionCobroEntity> findByFacturaIdAndEstado(Long facturaId, String estado);

	@Query("SELECT distinct t.facturaId " +
			"FROM TransaccionCobroEntity t " +
			"WHERE t.modalidadFacturacion.dominioId = :modalidadFacturacionId " +
			"AND t.entidadId.entidadId = :entidadId " +
			"AND (:recaudadorId is null OR (t.recaudador.recaudadorId = :recaudadorId)) " +
			"ORDER BY t.facturaId")
	List<Long> findFacturasByModalidadAndEntidadAndRecaudador(@Param("modalidadFacturacionId") Long modalidadFacturacionId,
															  @Param("entidadId") Long entidadId,
															  @Param("recaudadorId") Long recaudadorId);

	
	@Query(	"Select new bo.com.tesla.recaudaciones.dto.TransaccionesCobroApiDto( "
			+ " tc.tipoServicio, "
			+ " tc.servicio, "
			+ " tc.periodo, "
			+ " tc.codigoCliente,"
			+ "	tc.totalDeuda, "
			+ " tc.nombreClienteArchivo, "
			+ " tc.nroDocumentoClienteArchivo,"
			+ "	r.nombre, "
			+ " s.nombre, "
			+ " tc.fechaCreacion ) "			
					+ " from  TransaccionCobroEntity tc "					
					+ " left join RecaudadorEntity r on r.recaudadorId=tc.recaudador.recaudadorId "	
					+ " left join SegUsuarioEntity u on u.usuarioId=tc.usuarioCreacion "
					+ " left join PersonaEntity p on p.personaId=u.personaId.personaId "
					+ " left join EmpleadoEntity e on e.personaId.personaId=p.personaId "
					+ " left join SucursalEntity s on s.sucursalId=e.sucursalId.sucursalId "					
					+ " Where "
					+ " tc.archivoId = :archivoId "
					+ " and tc.codigoCliente= :codigoCliente "
					+ " and tc.servicio= :servicio "
					+ " and tc.tipoServicio = :tipoServicio "
					+ " and tc.periodo= :periodo "
					+ " and tc.estado ='COBRADO' "
			
			)
	public TransaccionesCobroApiDto  getTransaccionCobroForEntidad(			
			@Param("archivoId") Long archivoId,
			@Param("codigoCliente") String codigoCliente,
			@Param("servicio") String servicio,
			@Param("tipoServicio") String tipoServicio,
			@Param("periodo") String periodo
			
			);

	@Query("SELECT t " +
			"FROM TransaccionCobroEntity t " +
			"WHERE t.modalidadFacturacion.dominioId = :modalidadFacturacionId " +
			"AND t.facturaId = :facturaId " +
			"AND t.entidadId.entidadId = :entidadId " +
			"AND (:recaudadorId is null OR (t.recaudador.recaudadorId = :recaudadorId))")
	List<TransaccionCobroEntity> findByEntAndRecAndFac(@Param("entidadId") Long entidadId,
												 @Param("recaudadorId") Long recaudadorId,
												 @Param("modalidadFacturacionId") Long modalidadFacturacionId,
												 @Param("facturaId") Long facturaId);





	@Query(value="SELECT " +
			" r.nombre AS nombreSocio," +
			" hd.servicio AS servicio," +
			" e.nombre AS nombreEmpresa, " +
			" hd.nombre_cliente AS nombreClienteFinal," +
			" tc.fecha_creacion AS fechaCobro," +
			" tc.total_deuda AS montoCobro," +
			" tc.comision_recaudacion as montoComision," +
			" (tc.total_deuda - tc.comision_recaudacion) as saldo," +
			" hd.estado AS estado " +

			" FROM tesla.historicos_deudas hd " +
			" LEFT OUTER JOIN tesla.transacciones_cobros tc ON (tc.archivo_id=hd.archivo_id AND tc.codigo_cliente=hd.codigo_cliente  AND tc.servicio=hd.servicio AND tc.tipo_servicio=hd.tipo_servicio AND tc.periodo=hd.periodo) " +
			" LEFT OUTER JOIN tesla.recaudadores r ON (r.recaudador_id=tc.recaudador_id) " +
			" LEFT OUTER JOIN tesla.archivos a ON (a.archivo_id=hd.archivo_id) " +
			" LEFT OUTER JOIN tesla.entidades e ON (e.entidad_id=a.entidad_id) " +
			" LEFT OUTER JOIN tesla.seg_usuarios u ON (u.usuario_id=tc.usuario_creacion) " +
			" LEFT OUTER JOIN tesla.personas p ON (p.persona_id=u.persona_id) " +
			" left join tesla.recaudadores_comisiones rc on rc.recaudador_comision_id  = tc.recaudador_comision_id " +
			" left join tesla.dominios d on d.dominio_id  = rc.tipo_comision_id " +
			" WHERE " +
			" ( " +
			" CASE  " +
			"    WHEN :tieneEstado = true THEN " +
			"    hd.estado in (:estado) " +
			" end " +
			" ) " +
			" and  " +
			" ( " +
			" CASE   " +
			"   WHEN :tieneEntidad = true THEN " +
			"   e.entidad_id in (:entidadId) " +
			" end " +
			" ) " +
			" and " +
			" ( " +
			" CASE  " +
			" WHEN :tieneRecaudador = true THEN " +
			" r.recaudador_id in (:recaudadorId) " +
			" end " +
			" ) " +
			" and " +
			" ( " +
			" CASE  " +
			"    WHeN :filtroRangoFechas = TRUE THEN " +
			"    date(tc.fecha_creacion) between date(:fechaInicio) and date(:fechaFin) " +
			"    else " +
			"    EXTRACT( YEAR FROM tc.fecha_creacion )  = :anio and EXTRACT( MONTH FROM tc.fecha_creacion )  = :mes and EXTRACT( day  FROM tc.fecha_creacion ) = :dia " +
			" end " +
			" )", nativeQuery = true)

	  Page<ReporteCobrosSocioDto> findReporteCobroSocio(
			@Param("fechaInicio") Date fechaInicio,
			@Param("fechaFin") Date fechaFin,
			@Param("entidadId") List<Long> entidadId,
			@Param("recaudadorId") List<Long> recaudadorId,
			@Param("estado") List<String> estado,
			@Param("tieneEntidad") boolean tieneEntidad,
			@Param("tieneRecaudador") boolean tieneRecaudador,
			@Param("tieneEstado") boolean tieneEstado,
			@Param("filtroRangoFechas") Boolean filtroRangoFechas,
			@Param("anio") Integer anio,
			@Param("mes") Integer mes,
			@Param("dia") Integer dia,
			Pageable pageable
	);

	// reporte cobros por empresa
	@Query(value="SELECT \n" +
			"e.nombre AS nombreEmpresa,\n" +
			"r.nombre AS nombreSocio,\n" +
			"hd.tipo_servicio  as tipoServicio,\n" +
			"hd.servicio AS servicio,\n" +
			"hd.codigo_cliente  AS codEstudiante,\n" +
			"factura.response_nro_factura  AS nroFactura,\n" +
			"tc.fecha_creacion AS fechaCobro,\n" +
			"domDepto.descripcion as deptoBanco,\n" +
			"s.nombre_sucursal as sucursalEmpresa,\n" +
			"tc.total_deuda AS montoCobro,\n" +
			"tc.comision as comisionEmpresa,\n" +
			"(tc.total_deuda - tc.comision) as montoTransferir,\n" +
			"metCobro.descripcion as metodoCobro,\n" +
			"hd.estado AS estado \n" +
			"FROM tesla.historicos_deudas hd \n" +
			"LEFT  JOIN tesla.transacciones_cobros tc ON (tc.archivo_id=hd.archivo_id AND tc.codigo_cliente=hd.codigo_cliente  AND tc.servicio=hd.servicio AND tc.tipo_servicio=hd.tipo_servicio AND tc.periodo=hd.periodo)\n" +
			"LEFT  JOIN tesla.recaudadores r ON (r.recaudador_id=tc.recaudador_id)\n" +
			"LEFT  JOIN tesla.entidades e ON (e.entidad_id=tc.entidad_id)\n" +
			"left join tesla.sucursales_entidades s on s.entidad_id  = e.entidad_id\n" +
			"left join tesla.dominios domDepto on domdepto.dominio_id  = s.departamento_id\n" +
			"left join tesla.dominios metCobro on metCobro.dominio_id  = tc.metodo_cobro_id\n" +
			"left join tesla.facturas factura on factura.factura_id  = tc.factura_id\n" +
			"WHERE\n" +
			"(\n" +
			"\tCASE \n" +
			"\tWHEN :tieneEstado = true THEN \n" +
			"\thd.estado in (:estado)\n" +
			"\tend\n" +
			") \n" +
			"and\n" +
			"(\n" +
			"\tCASE\n" +
			"\tWHEN :tieneEntidad = true THEN\n" +
			"\te.entidad_id in (:entidadId)\n" +
			"\tend\n" +
			")\n" +
			"and\n" +
			"(\n" +
			"\tCASE\n" +
			"\tWHEN :tieneRecaudador = true THEN\n" +
			"\tr.recaudador_id in (:recaudadorId)\n" +
			"\tend\n" +
			")\n" +
			"and\n" +
			"(\n" +
			"\tCASE\n" +
			"\tWHeN :filtroRangoFechas = TRUE THEN\n" +
			"\tdate(tc.fecha_creacion) between date(:fechaInicio) and date(:fechaFin)\n" +
			"\telse\n" +
			"\tEXTRACT( YEAR FROM tc.fecha_creacion )  = :anio and EXTRACT( MONTH FROM tc.fecha_creacion )  = :mes and EXTRACT( day  FROM tc.fecha_creacion ) = :dia \n" +
			"\tend\n" +
			")\n", nativeQuery = true)

	Page<ReporteAdminEmpresaDto> findReporteAdminCobroEmpresa(
			@Param("fechaInicio") Date fechaInicio,
			@Param("fechaFin") Date fechaFin,
			@Param("entidadId") List<Long> entidadId,
			@Param("recaudadorId") List<Long> recaudadorId,
			@Param("estado") List<String> estado,
			@Param("tieneEntidad") boolean tieneEntidad,
			@Param("tieneRecaudador") boolean tieneRecaudador,
			@Param("tieneEstado") boolean tieneEstado,
			@Param("filtroRangoFechas") Boolean filtroRangoFechas,
			@Param("anio") Integer anio,
			@Param("mes") Integer mes,
			@Param("dia") Integer dia,
			Pageable pageable
	);
	@Query(value="SELECT \n" +
			"e.nombre AS nombreEmpresa,\n" +
			"r.nombre AS nombreSocio,\n" +
			"hd.tipo_servicio  as tipoServicio,\n" +
			"hd.servicio AS servicio,\n" +
			"hd.codigo_cliente  AS codEstudiante,\n" +
			"tc.fecha_creacion AS fechaCobro,\n" +
			"domDepto.descripcion as deptoBanco,\n" +
			"s.nombre_sucursal as sucursalEmpresa,\n" +
			"tc.total_deuda AS montoCobro,\n" +
			"tc.comision as comisionEmpresa,\n" +
			"(tc.total_deuda - tc.comision) as montoTransferir,\n" +
			"hd.estado AS estado \n" +
			"FROM tesla.historicos_deudas hd \n" +
			"LEFT  JOIN tesla.transacciones_cobros tc ON (tc.archivo_id=hd.archivo_id AND tc.codigo_cliente=hd.codigo_cliente  AND tc.servicio=hd.servicio AND tc.tipo_servicio=hd.tipo_servicio AND tc.periodo=hd.periodo)\n" +
			"LEFT  JOIN tesla.recaudadores r ON (r.recaudador_id=tc.recaudador_id)\n" +
			"LEFT  JOIN tesla.entidades e ON (e.entidad_id=tc.entidad_id)\n" +
			"left join tesla.sucursales_entidades s on s.entidad_id  = e.entidad_id  \n" +
			"left join tesla.dominios domDepto on domdepto.dominio_id  = s.departamento_id\n" +
			"WHERE\n" +
			"(\n" +
			"\tCASE \n" +
			"\tWHEN :tieneEstado = true THEN \n" +
			"\thd.estado in (:estado)\n" +
			"\tend\n" +
			") \n" +
			"and\n" +
			"(\n" +
			"\tCASE\n" +
			"\tWHEN :tieneEntidad = true THEN\n" +
			"\te.entidad_id in (:entidadId)\n" +
			"\tend\n" +
			")\n" +
			"and\n" +
			"(\n" +
			"\tCASE\n" +
			"\tWHEN :tieneRecaudador = true THEN\n" +
			"\tr.recaudador_id in (:recaudadorId)\n" +
			"\tend\n" +
			")\n" +
			"and\n" +
			"(\n" +
			"\tCASE\n" +
			"\tWHeN :filtroRangoFechas = TRUE THEN\n" +
			"\tdate(tc.fecha_creacion) between date(:fechaInicio) and date(:fechaFin)\n" +
			"\telse\n" +
			"\tEXTRACT( YEAR FROM tc.fecha_creacion )  = :anio and EXTRACT( MONTH FROM tc.fecha_creacion )  = :mes and EXTRACT( day  FROM tc.fecha_creacion ) = :dia \n" +
			"\tend\n" +
			")\n", nativeQuery = true)

	List<ReporteAdminEmpresaDto> findReporteAdminCobroEmpresaJasper(
			@Param("fechaInicio") Date fechaInicio,
			@Param("fechaFin") Date fechaFin,
			@Param("entidadId") List<Long> entidadId,
			@Param("recaudadorId") List<Long> recaudadorId,
			@Param("estado") List<String> estado,
			@Param("tieneEntidad") boolean tieneEntidad,
			@Param("tieneRecaudador") boolean tieneRecaudador,
			@Param("tieneEstado") boolean tieneEstado,
			@Param("filtroRangoFechas") Boolean filtroRangoFechas,
			@Param("anio") Integer anio,
			@Param("mes") Integer mes,
			@Param("dia") Integer dia
	);
	// ===================

	// reprote cobros por socio
	@Query(value="SELECT\n" +
			"r.nombre AS nombreSocio,\n" +
			"e.nombre AS nombreEmpresa,\n" +
			"domDepto.descripcion as deptoBanco,\n" +
			"s.nombre as sucursalBanco,\n" +
			"(p.nombres ||' '|| p.paterno ||' '|| p.materno) as cajero,\n" +
			"hd.tipo_servicio  as tipoServicio,\n" +
			"hd.servicio AS servicio,\n" +
			"hd.codigo_cliente  AS codEstudiante,\n" +
			"tc.fecha_creacion AS fechaCobro,\n" +
			"tc.total_deuda AS montoCobro,\n" +
			"tc.comision_recaudacion as comisionSocio,\n" +
			"hd.estado AS estado\n" +
			"FROM tesla.historicos_deudas hd\n" +
			"LEFT  JOIN tesla.transacciones_cobros tc ON (tc.archivo_id=hd.archivo_id AND tc.codigo_cliente=hd.codigo_cliente  AND tc.servicio=hd.servicio AND tc.tipo_servicio=hd.tipo_servicio AND tc.periodo=hd.periodo)\n" +
			"left join tesla.seg_usuarios u on tc.usuario_creacion = u.usuario_id\n" +
			"left join tesla.personas p on p.persona_id = u.persona_id\n" +
			"LEFT  JOIN tesla.recaudadores r ON (r.recaudador_id=tc.recaudador_id)\n" +
			"LEFT  JOIN tesla.entidades e ON (e.entidad_id=tc.entidad_id)\n" +
			"left join tesla.sucursales s on s.recaudador_id  = r.recaudador_id\n" +
			"left join tesla.dominios domDepto on domdepto.dominio_id  = s.departamento_id\n" +
			"WHERE\n" +
			"(\n" +
			"\tCASE\n" +
			"\tWHEN :tieneEstado = true THEN\n" +
			"\thd.estado in (:estado)\n" +
			"\tend\n" +
			")\n" +
			"and\n" +
			"(\n" +
			"\tCASE\n" +
			"\tWHEN :tieneEntidad = true THEN\n" +
			"\te.entidad_id in (:entidadId)\n" +
			"\tend\n" +
			")\n" +
			"and\n" +
			"(\n" +
			"\tCASE\n" +
			"\tWHEN :tieneRecaudador = true THEN\n" +
			"\tr.recaudador_id in (:recaudadorId)\n" +
			"\tend\n" +
			")\n" +
			"and\n" +
			"(\n" +
			"\tCASE\n" +
			"\tWHeN :filtroRangoFechas = TRUE THEN\n" +
			"\tdate(tc.fecha_creacion) between date(:fechaInicio) and date(:fechaFin)\n" +
			"\telse\n" +
			"\tEXTRACT( YEAR FROM tc.fecha_creacion )  = :anio and EXTRACT( MONTH FROM tc.fecha_creacion )  = :mes and EXTRACT( day  FROM tc.fecha_creacion ) = :dia\n" +
			"\tend\n" +
			")", nativeQuery = true)

	Page<ReporteAdminSocioDto> findReporteAdminCobroSocio(
			@Param("fechaInicio") Date fechaInicio,
			@Param("fechaFin") Date fechaFin,
			@Param("entidadId") List<Long> entidadId,
			@Param("recaudadorId") List<Long> recaudadorId,
			@Param("estado") List<String> estado,
			@Param("tieneEntidad") boolean tieneEntidad,
			@Param("tieneRecaudador") boolean tieneRecaudador,
			@Param("tieneEstado") boolean tieneEstado,
			@Param("filtroRangoFechas") Boolean filtroRangoFechas,
			@Param("anio") Integer anio,
			@Param("mes") Integer mes,
			@Param("dia") Integer dia,
			Pageable pageable
	);
	@Query(value="SELECT\n" +
			"r.nombre AS nombreSocio,\n" +
			"e.nombre AS nombreEmpresa,\n" +
			"domDepto.descripcion as deptoBanco,\n" +
			"s.nombre as sucursalBanco,\n" +
			"(p.nombres ||' '|| p.paterno ||' '|| p.materno) as cajero,\n" +
			"hd.tipo_servicio  as tipoServicio,\n" +
			"hd.servicio AS servicio,\n" +
			"hd.codigo_cliente  AS codEstudiante,\n" +
			"tc.fecha_creacion AS fechaCobro,\n" +
			"tc.total_deuda AS montoCobro,\n" +
			"tc.comision_recaudacion as comisionSocio,\n" +
			"hd.estado AS estado\n" +
			"FROM tesla.historicos_deudas hd\n" +
			"LEFT  JOIN tesla.transacciones_cobros tc ON (tc.archivo_id=hd.archivo_id AND tc.codigo_cliente=hd.codigo_cliente  AND tc.servicio=hd.servicio AND tc.tipo_servicio=hd.tipo_servicio AND tc.periodo=hd.periodo)\n" +
			"left join tesla.seg_usuarios u on tc.usuario_creacion = u.usuario_id\n" +
			"left join tesla.personas p on p.persona_id = u.persona_id\n" +
			"LEFT  JOIN tesla.recaudadores r ON (r.recaudador_id=tc.recaudador_id)\n" +
			"LEFT  JOIN tesla.entidades e ON (e.entidad_id=tc.entidad_id)\n" +
			"left join tesla.sucursales s on s.recaudador_id  = r.recaudador_id\n" +
			"left join tesla.dominios domDepto on domdepto.dominio_id  = s.departamento_id\n" +
			"WHERE\n" +
			"(\n" +
			"\tCASE\n" +
			"\tWHEN :tieneEstado = true THEN\n" +
			"\thd.estado in (:estado)\n" +
			"\tend\n" +
			")\n" +
			"and\n" +
			"(\n" +
			"\tCASE\n" +
			"\tWHEN :tieneEntidad = true THEN\n" +
			"\te.entidad_id in (:entidadId)\n" +
			"\tend\n" +
			")\n" +
			"and\n" +
			"(\n" +
			"\tCASE\n" +
			"\tWHEN :tieneRecaudador = true THEN\n" +
			"\tr.recaudador_id in (:recaudadorId)\n" +
			"\tend\n" +
			")\n" +
			"and\n" +
			"(\n" +
			"\tCASE\n" +
			"\tWHeN :filtroRangoFechas = TRUE THEN\n" +
			"\tdate(tc.fecha_creacion) between date(:fechaInicio) and date(:fechaFin)\n" +
			"\telse\n" +
			"\tEXTRACT( YEAR FROM tc.fecha_creacion )  = :anio and EXTRACT( MONTH FROM tc.fecha_creacion )  = :mes and EXTRACT( day  FROM tc.fecha_creacion ) = :dia\n" +
			"\tend\n" +
			")", nativeQuery = true)

	List<ReporteAdminSocioDto> findReporteAdminCobroSocioJasper(
			@Param("fechaInicio") Date fechaInicio,
			@Param("fechaFin") Date fechaFin,
			@Param("entidadId") List<Long> entidadId,
			@Param("recaudadorId") List<Long> recaudadorId,
			@Param("estado") List<String> estado,
			@Param("tieneEntidad") boolean tieneEntidad,
			@Param("tieneRecaudador") boolean tieneRecaudador,
			@Param("tieneEstado") boolean tieneEstado,
			@Param("filtroRangoFechas") Boolean filtroRangoFechas,
			@Param("anio") Integer anio,
			@Param("mes") Integer mes,
			@Param("dia") Integer dia
	);
	// ====================================

	@Query(value = "select  e.nombre as nombreEntidad, t.tipo_servicio as tipoServicio,t.servicio,t.periodo,t.codigo_cliente as codigoCliente,t.fecha_creacion as fechaCreacion,\n" +
			" t.total_deuda as totalDeuda, t.nombre_cliente_archivo as nombreClienteArchivo , t.nro_documento_cliente_archivo as nroDocumentoClienteArchivo \n" +
			" from tesla.transacciones_cobros t inner join tesla.entidades e on e.entidad_id = t.entidad_id and e.estado = 'ACTIVO'\n" +
			" where t.fecha_creacion = date(:fechaSeleccionada) or t.usuario_creacion = :usuarioCreacion and t.estado = 'COBRADO' " +
			" or t.entidad_id in (:entidadId)", nativeQuery = true)
	List<ReporteCierreCajaDiarioDto> findDeudasCobradasByUsuarioCreacionForJasper(
			@Param("usuarioCreacion") Long usuarioCreacion,
			@Param("fechaSeleccionada") Date fechaSeleccionada,
			@Param("entidadId") List<Long> entidadId
	);
	@Query(value = "select  e.nombre as nombreEntidad, t.tipo_servicio as tipoServicio,t.servicio,t.periodo,t.codigo_cliente as codigoCliente,t.fecha_creacion as fechaCreacion,\n" +
			" t.total_deuda as totalDeuda, t.nombre_cliente_archivo as nombreClienteArchivo , t.nro_documento_cliente_archivo as nroDocumentoClienteArchivo \n" +
			" from tesla.transacciones_cobros t inner join tesla.entidades e on e.entidad_id = t.entidad_id and e.estado = 'ACTIVO'\n" +
			" where date(t.fecha_creacion)= date(now()) and t.usuario_creacion = :usuarioCreacion and t.estado = 'COBRADO'" +
			" and t.entidad_id in (:entidadId)", nativeQuery = true)
	List<ReporteCierreCajaDiarioDto> findCierreCajaDiarioForJasper(
			@Param("usuarioCreacion") Long usuarioCreacion,
			@Param("entidadId") List<Long> entidadId
	);
	@Query("SELECT t " +
			"FROM TransaccionCobroEntity t " +
			"WHERE t.deudaClienteId = :deudaClienteId " +
			"AND t.estado ='COBRADO'")
	Optional<TransaccionCobroEntity> findByDeudaClienteId(@Param("deudaClienteId") Long deudaClienteId);

	@Query("SELECT t " +
			"FROM TransaccionCobroEntity t " +
			"WHERE t.deudaClienteId = :deudaClienteId " +
			"AND t.estado =:estado")
	Optional<TransaccionCobroEntity> findByDeudaClienteIdAndEstado(@Param("deudaClienteId") Long deudaClienteId,@Param("estado") String estado);

	@Query("SELECT t " +
			"FROM TransaccionCobroEntity t " +
			"WHERE t.datosconfirmadoQrId = :datosConfirmadoQrId " +
			"AND t.estado ='COBRADO'")
	List<TransaccionCobroEntity> findByDatosConfirmadoQrId(@Param("datosConfirmadoQrId") Long datosConfirmadoQrId);


}
