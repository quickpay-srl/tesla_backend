package bo.com.tesla.recaudaciones.dao;

import bo.com.tesla.administracion.entity.HistoricoDeudaEntity;
import bo.com.tesla.entidades.dto.ConceptoDto;
import bo.com.tesla.entidades.dto.DeudasClienteDto;
import bo.com.tesla.recaudaciones.dto.DeudasCobradasFacturaDto;
import bo.com.tesla.recaudaciones.dto.EstadoTablasDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface IHistoricoDeudaDao extends JpaRepository<HistoricoDeudaEntity, Long> {

	
	Optional<HistoricoDeudaEntity> findByDeudaClienteId(Long deudaClienteId);

	@Modifying
	@Query(value = "UPDATE HistoricoDeudaEntity h " +
			"SET h.estado = :estado " +
			"where h.deudaClienteId IN :deudaClienteIdLst")
	Integer updateLstEstado(@Param("deudaClienteIdLst") List<Long> deudaClienteIdLst, @Param("estado") String estado);

	/**
	 * Obtiene todas la deudas hitoricas asociadas 
	 * al archivo.
	 * 
	 * @author Adalid Callejas	 
	 * @version 1.0 
	 * @param entidadId 
	 * @return lista de historicos
	 */
	@Query("SELECT new bo.com.tesla.entidades.dto.DeudasClienteDto(d.archivoId.archivoId,d.servicio, d.tipoServicio, d.periodo, d.codigoCliente,sum(d.subTotal),'') "
			+ " FROM HistoricoDeudaEntity d "
			+ " WHERE d.archivoId.archivoId=:archivoId "
			+ "  and  (d.codigoCliente LIKE %:paramBusqueda% "
			+ "  or upper(d.periodo) LIKE  upper(concat('%', :paramBusqueda,'%')) "
			+ "  or upper(d.nombreCliente) LIKE upper(concat('%', :paramBusqueda,'%')) "
			+ "  or upper(d.nroDocumento) LIKE upper(concat('%', :paramBusqueda,'%')) "
			+ "  or upper(d.estado) LIKE upper(concat('%', :paramBusqueda,'%'))) "			
			+ " GROUP BY d.archivoId, d.servicio, d.tipoServicio, d.periodo, d.codigoCliente "
			+ " ORDER BY d.codigoCliente, d.servicio, d.tipoServicio, d.periodo  ASC")
	public Page<DeudasClienteDto> groupByDeudasClientes(@Param("archivoId") Long archivoId ,@Param("paramBusqueda") String paramBusqueda,Pageable pageable );
	
	
	
	@Query("SELECT new  bo.com.tesla.entidades.dto.ConceptoDto(d.nroRegistro, d.nombreCliente, d.nroDocumento, d.direccion, "
			+ "	d.telefono, d.nit, d.tipo, d.concepto, d.montoUnitario, d.cantidad, "
			+ "	d.subTotal, d.datoExtra, d.tipoComprobante, d.periodoCabecera,d.esPostpago) "
			+ " FROM HistoricoDeudaEntity d "
			+ " WHERE d.archivoId.archivoId= :archivoId "
			+ " and d.servicio= :servicio "
			+ " and d.tipoServicio= :tipoServicio "
			+ " and d.periodo= :periodo "
			+ " and d.codigoCliente= :codigoCliente "
			+ " and d.tipo='D' "
			+ " ORDER BY d.nroRegistro ")
	public List<ConceptoDto> findConceptos(
			@Param("archivoId") Long archivoId,
			@Param("servicio") String servicio, 
			@Param("tipoServicio") String tipoServicio, 
			@Param("periodo") String periodo, 
			@Param("codigoCliente") String codigoCliente);
	
	
	/**
	 * Obtiene los estados de la tabla hitorico deudas 
	 * 
	 * 
	 * @author Adalid Callejas	 
	 * @version 1.0
	 * @return lista de EstadoTablasDto
	 */
	
	@Query(" Select new bo.com.tesla.recaudaciones.dto.EstadoTablasDto( e.segEstadoEntityPK.estadoId,   "
			+ " CASE e.segEstadoEntityPK.estadoId "
            + " WHEN 'ACTIVO' THEN 'POR COBRAR' "
            + " WHEN 'COBRADO' THEN 'COBRADOS' "            
            + " WHEN 'ANULADO' THEN 'ANULADOS' "
			+ " WHEN 'ERRONEO' THEN 'ERRONEO' "
            + " END) "
            + " from SegEstadoEntity e "
            + " where e.segEstadoEntityPK.tablaId='HISTORICOS_DEUDAS' and  e.segEstadoEntityPK.estadoId != 'ERRONEO'")
	public List<EstadoTablasDto> findEstadoHistorico();
	
	@Query(" Select new bo.com.tesla.recaudaciones.dto.EstadoTablasDto( e.segEstadoEntityPK.estadoId,   "
			+ " CASE e.segEstadoEntityPK.estadoId "
            + " WHEN 'ACTIVO' THEN 'POR PAGAR' "
            + " WHEN 'PAGADO' THEN 'PAGADO' "            
            + " WHEN 'ANULADO' THEN 'ANULADOS' "
            + " END) "
            + " from SegEstadoEntity e "
            + " where e.segEstadoEntityPK.tablaId='P_HISTORICOS_BENEFICIARIOS' ")
	public List<EstadoTablasDto> findEstadoHistoricoPagos();
	
	
	/**
	 * Obtiene los estados de la tabla hitorico deudas 
	 * 
	 * 
	 * @author Adalid Callejas	 
	 * @version 1.0
	 * @return lista de EstadoTablasDto
	 */
	

	@Query(" select new bo.com.tesla.entidades.dto.DeudasClienteDto( "
			+ " hd.archivoId.archivoId, "
			+ "	hd.codigoCliente, "
			+ " hd.servicio, "
			+ " hd.tipoServicio, "
			+ " hd.periodo, "
			+ " hd.estado,"
			+ " c.fechaCreacion, "
			+ " c.totalDeuda, "
			+ " hd.nombreCliente,"
			+ " r.nombre,"
			+ " c.comision,c.metodoCobro.descripcion )"
			+ " from HistoricoDeudaEntity as hd "
			+ " left join TransaccionCobroEntity c on ( c.archivoId=hd.archivoId "
			+ "											and c.codigoCliente=hd.codigoCliente "
			+ "											and c.servicio=hd.servicio "
			+ "											and c.tipoServicio=hd.tipoServicio "
			+ "											and c.periodo = hd.periodo ) "
			+ "	left join SegUsuarioEntity u on u.usuarioId = c.usuarioCreacion  "
			+ " left join PersonaEntity p on p.personaId=u.personaId.personaId "
			+ " left join EmpleadoEntity e on e.personaId=p "
			+ " left join  RecaudadorEntity r on r.recaudadorId=c.recaudador.recaudadorId "
			+ " Where "
			+ " hd.archivoId.archivoId= :archivoId "
			/*+ " and COALESCE(CAST(r.recaudadorId as string ),'') like :recaudadorId"
			+ " and hd.estado LIKE  :estado "*/		
			+ " group by hd.archivoId ,hd.codigoCliente,hd.servicio,hd.tipoServicio, "
			+ "	hd.periodo ,hd.estado, "
			+ " c.fechaCreacion,c.totalDeuda,hd.nombreCliente,r.nombre,c.comision,c.metodoCobro.descripcion  "
			+ " Order by hd.estado, r.nombre desc ")
	public List<DeudasClienteDto> findDeudasByArchivoIdAndEstadoForEntidad(
			@Param("archivoId") Long archivoId);
	
	
	
	
	@Query(" Select  new bo.com.tesla.recaudaciones.dto.DeudasCobradasFacturaDto(hd.archivoId.archivoId, hd.codigoCliente, hd.nombreCliente, hd.tipoServicio, hd.servicio, "
			+ "			hd.periodo, hd.cantidad, hd.concepto, hd.montoUnitario, hd.nit, "
			+ "			hd.subTotal, tc.totalDeuda) "
			+ " from TransaccionCobroEntity tc "
			+ " inner join HistoricoDeudaEntity hd on ( hd.archivoId.archivoId=tc.archivoId.archivoId "
			+ " 									   and hd.codigoCliente=tc.codigoCliente "
			+ "										   and hd.servicio=tc.servicio "
			+ "										   and hd.tipoServicio=tc.tipoServicio "
			+ "										   and hd.periodo=tc.periodo ) "
			+ " where "
			+ " tc.transaccionCobroId in :transaccionCobroIds"
			+ " and hd.tipo='D' "
			+ " order by hd.archivoId.archivoId, hd.servicio, hd.tipoServicio,hd.periodo")	
	public List<DeudasCobradasFacturaDto> findDeudasCobrasForFactura(
			@Param("transaccionCobroIds") List<Long> transaccionCobroIds
			);
	
	
	
	
	
	
	
	@Query(value = " select  sum(hd.sub_total)as total "
			+ "	from  tesla.historicos_deudas hd "
			+ "	left join tesla.transacciones_cobros c on ( c.archivo_id =hd.archivo_id "
			+ "												 and c.codigo_cliente=hd.codigo_cliente "
			+ "												 and c.servicio=hd.servicio "
			+ "												 and c.tipo_servicio=hd.tipo_servicio "
			+ "												 and c.periodo=hd.periodo "
			+ "												) "
			+ "	left join tesla.seg_usuarios u on u.usuario_id=c.usuario_creacion "
			+ "	left join tesla.personas p on p.persona_id=u.persona_id "
			+ "	left join tesla.empleados e on e.persona_id=p.persona_id "
			+ "	left join tesla.sucursales s on s.sucursal_id=e.sucursal_id "
			+ "	left join tesla.recaudadores r on r.recaudador_id=s.recaudador_id "
			+ "	where hd.archivo_id = :archivoId "
			+ "	and hd.estado='COBRADO' "
			+ "	and r.recaudador_id = :recaudadorId ", nativeQuery = true)
	public BigDecimal getMontoTotalCobrados(
			@Param("archivoId") Long archivoId,
			@Param("recaudadorId") Long recaudadorId
			);
	
	@Query(value = " select deudas.nombre_recaudadora, sum(deudas.total_deuda)as total "
			+ " from ( "
			+ " select  c.total_deuda,r.nombre as nombre_recaudadora "
			+ " from tesla.historicos_deudas hd "
			+ " left join tesla.transacciones_cobros c on ( c.archivo_id =hd.archivo_id "
			+ "											 and c.codigo_cliente=hd.codigo_cliente "
			+ "											 and c.servicio=hd.servicio "
			+ "											 and c.tipo_servicio=hd.tipo_servicio "
			+ "											 and c.periodo=hd.periodo "
			+ "											) "
			+ " left join tesla.seg_usuarios u on u.usuario_id=c.usuario_creacion "
			+ " left join tesla.personas p on p.persona_id=u.persona_id "
			+ " left join tesla.empleados e on e.persona_id=p.persona_id "
			+ " left join tesla.sucursales s on s.sucursal_id=e.sucursal_id "
			+ " left join tesla.recaudadores r on r.recaudador_id=s.recaudador_id "
			+ " where "
			+ " hd.archivo_id = :archivoId "
			+ " and hd.estado ='COBRADO' "			
			+ " group by hd.archivo_id ,hd.codigo_cliente,hd.servicio,hd.tipo_servicio,hd.periodo , c.total_deuda,r.nombre "
			+ " ) as deudas "
			+ " group by deudas.nombre_recaudadora"
			, nativeQuery = true)
	public List<Object[]> getMontoTotalPorRecaudadora(@Param("archivoId") Long archivoId);
	
	
	@Query(" Select hd"
			+ " from HistoricoDeudaEntity hd "
			+ " where hd.archivoId.archivoId= :archivoId ")	
	public List<HistoricoDeudaEntity> findHistoricoDeudasByArchivoId(@Param("archivoId") Long archivoId);


	@Modifying
	@Query("UPDATE HistoricoDeudaEntity h " +
			"SET h.estado = :estado " +
			"where h.historicoDeudaId IN (SELECT h.historicoDeudaId " +
										"FROM HistoricoDeudaEntity " +
										"JOIN CobroClienteEntity c on c.historicoDeuda = h " +
										"WHERE c.transaccionCobro.facturaId in :facturaIdLst)")
	Integer updateLstEstadoByFacturas(@Param("facturaIdLst") List<Long> facturaIdLst, @Param("estado") String estado);

}
