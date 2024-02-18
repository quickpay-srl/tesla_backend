package bo.com.tesla.entidades.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import bo.com.tesla.administracion.entity.DeudaClienteEntity;
import bo.com.tesla.entidades.dto.ConceptoDto;
import bo.com.tesla.entidades.dto.DeudasClienteDto;

@Repository
public interface IDeudaClienteDao extends JpaRepository<DeudaClienteEntity, Long> {

	@Transactional
	@Modifying
	@Query(value =" delete from "
			+ " tesla.deudas_clientes d "
			+ " where d.archivo_id= :archivoId "
			+ " and d.nro_registro !=0 ",nativeQuery = true)
	public void deletByArchivoId(@Param("archivoId") Long archivoId);
	
	
	@Transactional
	@Modifying
	@Query(value =" UPDATE tesla.historicos_deudas "
			+ " SET estado='HISTORICO' "
			+ " WHERE archivo_id= :archivoId and estado ='ACTIVO' ",nativeQuery = true)
	public void updateHitoricoDeudas(@Param("archivoId") Long archivoId);
	
	@Transactional
	@Modifying
	@Query(value =" UPDATE tesla.deudas_clientes  "
			+ " SET  archivo_id= :archivoId "
			+ " WHERE nro_registro=0 and archivo_id= :archivoPreviusId",nativeQuery = true)
	public void updateDeudasCargadasEndPoint(@Param("archivoId") Long archivoId,@Param("archivoPreviusId") Long archivoPreviusId);

	@Query("SELECT new bo.com.tesla.entidades.dto.DeudasClienteDto(d.archivoId.archivoId,d.servicio, d.tipoServicio, d.periodo, d.codigoCliente, sum(d.subTotal),'') "
			+ " FROM DeudaClienteEntity d "
			+ " WHERE d.archivoId.archivoId=:archivoId "
			+ "  and  (d.codigoCliente LIKE %:paramBusqueda% "
			+ "  or upper(d.periodo) LIKE  upper(concat('%', :paramBusqueda,'%')) "
			+ "  or upper(d.nombreCliente) LIKE upper(concat('%', :paramBusqueda,'%')) "
			+ "  or upper(d.nroDocumento) LIKE upper(concat('%', :paramBusqueda,'%')))"
			+ " GROUP BY d.archivoId, d.servicio,d.tipoServicio,  d.periodo, d.codigoCliente "
			+ " ORDER BY d.codigoCliente, d.servicio, d.tipoServicio, d.periodo  ASC")
	public Page<DeudasClienteDto> groupByDeudasClientes(@Param("archivoId") Long archivoId ,@Param("paramBusqueda") String paramBusqueda,Pageable pageable );
	/*@Query("SELECT new bo.com.tesla.entidades.dto.DeudasClienteDto(d.archivoId.archivoId,d.servicio, d.tipoServicio, d.periodo, d.codigoCliente,'')
			+ " FROM DeudaClienteEntity d "
			+ " WHERE d.archivoId.archivoId=:archivoId and d.subTotal>0"
			//+ " GROUP BY d.archivoId, d.servicio,d.tipoServicio,  d.periodo, d.codigoCliente "
			+ " ORDER BY d.codigoCliente, d.servicio, d.tipoServicio, d.periodo  ASC")
	public Page<DeudasClienteDto> groupByDeudasClientesForArchivoId(@Param("archivoId") Long archivoId ,Pageable pageable );*/


	@Query("SELECT new  bo.com.tesla.entidades.dto.ConceptoDto(d.nroRegistro, d.nombreCliente, d.nroDocumento, d.direccion, "
			+ "	d.telefono, d.nit, d.tipo, d.concepto, d.montoUnitario, d.cantidad, "
			+ "	d.subTotal, d.datoExtras, d.tipoComprobante, d.periodoCabecera,d.esPostpago, d.correoCliente) "
			+ " FROM DeudaClienteEntity d "
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

	@Query("SELECT d"
			+ " FROM DeudaClienteEntity d "
			+ " WHERE d.archivoId.archivoId= :archivoId"
			)
	public List<DeudaClienteEntity> findByArchivoId(@Param("archivoId") Long archivoId	);
	
	
}
