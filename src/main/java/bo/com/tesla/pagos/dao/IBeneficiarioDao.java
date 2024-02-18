package bo.com.tesla.pagos.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import bo.com.tesla.administracion.entity.PBeneficiariosEntity;
import bo.com.tesla.administracion.entity.PPagoClienteEntity;
import bo.com.tesla.pagos.dto.PPagosDto;

@Repository
public interface IBeneficiarioDao extends JpaRepository<PBeneficiariosEntity, Long> {
	
	@Query(" Select new bo.com.tesla.pagos.dto.PPagosDto(ac.codigoCliente,ac.periodo,sum(ac.montoUnitario * ac.cantidad)) "
			+ " from PBeneficiariosEntity ac "
			+ " Where ac.archivoId.archivoId= :archivoId "
			+ " and ( "
			+ "		ac.codigoCliente LIKE concat(:paramBusqueda,'%') "
			+ "		or upper(ac.nombreCliente) LIKE upper(concat('%', :paramBusqueda,'%')) "
			+ "		or upper(ac.nroDocumentoCliente) LIKE upper(concat('%', :paramBusqueda,'%')) "
			+ ") "
			+ " GROUP BY ac.codigoCliente,ac.periodo "
			+ " Order by ac.codigoCliente asc "
			)
	public Page<PPagosDto>  groupByAbonosClientes(
			@Param("archivoId") Long archivoId, 
			@Param("paramBusqueda") String paramBusqueda,
			Pageable pageable			
			);
	
	
	@Query(" Select new bo.com.tesla.pagos.dto.PPagosDto(ac.archivoId.archivoId,ac.nroRegistro, ac.codigoCliente, ac.nombreCliente, ac.fechaNacimientoCliente, "
			+ "			ac.genero, ac.nroDocumentoCliente, ac.extencionDocumentoId, ac.tipoDocumentoId, ac.cantidad, "
			+ "			ac.montoUnitario, ac.periodo,ac.concepto) "
			+ " from PBeneficiariosEntity ac "
			+ " Where ac.archivoId.archivoId= :archivoId "
			+ " and ac.codigoCliente= :codigoCliente "
			+ " and ac.periodo= :periodo "		
			+ " Order by ac.nroRegistro asc "
			)
	public List<PPagosDto>  findByCodigoAndArchivoId(
			@Param("archivoId") Long archivoId, 
			@Param("codigoCliente") String codigoCliente,
			@Param("periodo") String periodo		
			);
	
	
	
	@Transactional
	@Modifying
	@Query(value =" delete from "
			+ " tesla.p_beneficiarios ac "
			+ " where ac.archivo_id= :archivoId ",nativeQuery = true)
	public void deletByArchivoId(@Param("archivoId") Long archivoId);
	
	
	
	@Query(" Select new bo.com.tesla.pagos.dto.PPagosDto(ac.archivoId.archivoId, ac.codigoCliente, ac.nroDocumentoCliente, ac.nombreCliente) "
			
			+ " from ArchivoEntity a "
			+ " inner join EntidadEntity e on e.entidadId=a.entidadId.entidadId "
			+ " inner join PBeneficiariosEntity ac on ac.archivoId.archivoId=a.archivoId "
			+ " Where "
			+ " a.servicioProductoId.servicioProductoId= :servicioProductoId "
			+ " and a.estado = 'ACTIVO' "
			+ " and e.entidadId in :entidadIdList "
			+ " and ( ac.codigoCliente LIKE  concat('%',:paramBusqueda,'%')"
			+ "		  or ac.nroDocumentoCliente LIKE concat('%',:paramBusqueda,'%') "
			+ " 	  or upper(ac.nombreCliente) LIKE upper(concat('%',:paramBusqueda,'%')) " 
			+ "     )"
			+ " GROUP BY ac.archivoId.archivoId, ac.codigoCliente, ac.nombreCliente, ac.nroDocumentoCliente "
			+ "")
	public List<PPagosDto>  getAbonosParaPagar(
			@Param("servicioProductoId") Long servicioProductoId, 
			@Param("entidadIdList") List<Long> 	entidadIdList,
			@Param("paramBusqueda") String 	paramBusqueda	
			);
	
	
	
	
	@Query(" Select new bo.com.tesla.pagos.dto.PPagosDto(ac.codigoCliente, ac.nroDocumentoCliente, ac.nombreCliente,ac.periodo, sum(ac.montoUnitario * ac.cantidad)) "
			+ " From  PBeneficiariosEntity   ac "
			+ " Where "
			+ " ac.archivoId.archivoId= :archivoId"
			+ "	and ac.codigoCliente = :codigoCliente "
			+ " and ac.nroDocumentoCliente= :nroDocumentoCliente " 
			+ " GROUP BY ac.codigoCliente, ac.nroDocumentoCliente, ac.nombreCliente,ac.periodo ")
	public List<PPagosDto>  getBeneficiario(
			@Param("archivoId") Long archivoId, 
			@Param("codigoCliente") String codigoCliente, 			
			@Param("nroDocumentoCliente") String nroDocumentoCliente	
			
			);
	
	@Query(" Select new bo.com.tesla.pagos.dto.PPagosDto(ac.archivoId.archivoId,ac.nroRegistro, ac.codigoCliente, ac.nombreCliente, ac.fechaNacimientoCliente, "
			+ "					ac.genero, ac.nroDocumentoCliente, ac.extencionDocumentoId, ac.tipoDocumentoId, ac.cantidad, "
			+ "					ac.montoUnitario, ac.periodo,ac.concepto) "
			+ " From  PBeneficiariosEntity   ac "
			+ " Where  ac.archivoId.archivoId= :archivoId "
			+ "      and  ac.codigoCliente = :codigoCliente "
			+ "      and  ac.nroDocumentoCliente= :nroDocumentoCliente "
			+ "		 and  ac.periodo= :periodo " 
			+ " Order by ac.nroRegistro,ac.beneficiarioId asc ")
	public List<PPagosDto>  getBeneficiarioDetalle(
			@Param("archivoId") Long archivoId,
			@Param("codigoCliente") String codigoCliente, 			
			@Param("nroDocumentoCliente") String nroDocumentoCliente,
			@Param("periodo") String periodo	
			);
	
	
	
	@Query(" Select new bo.com.tesla.pagos.dto.PPagosDto(ac.codigoCliente, ac.nroDocumentoCliente, ac.nombreCliente,sum(ac.montoUnitario * ac.cantidad)) "
			+ " From  PBeneficiariosEntity   ac "
			+ " Where  ac.archivoId.archivoId= :archivoId "
			+ "      and  ac.codigoCliente = :codigoCliente "
			+ "      and  ac.nroDocumentoCliente= :nroDocumentoCliente "
			+ "		 and  ac.periodo in :periodoList "
			+ " GROUP BY ac.archivoId.archivoId, ac.codigoCliente, ac.nroDocumentoCliente, ac.nombreCliente " 
			+ " ")
	public PPagosDto  getBeneficiarioAndMontoToal(
			@Param("archivoId") Long archivoId,
			@Param("codigoCliente") String codigoCliente, 			
			@Param("nroDocumentoCliente") String nroDocumentoCliente,
			@Param("periodoList") List<String> periodoList	
			);
	
	
	
	@Transactional
	@Modifying
	@Query(value =" Delete from tesla.p_beneficiarios ac "
			+ " Where ac.archivo_id= :archivoId "
			+ " and ac.codigo_cliente= :codigoCliente"
			+ " and ac.nro_documento_cliente= :nroDocumentoCliente"
			+ " and ac.periodo= :periodo",nativeQuery = true)
	public void deleteAbonados(
			@Param("archivoId") Long archivoId,
			@Param("codigoCliente") String codigoCliente, 			
			@Param("nroDocumentoCliente") String nroDocumentoCliente,
			@Param("periodo") String periodo);
	
	
	@Query(" Select  b "
			+ " from PBeneficiariosEntity b "
			+ " where "
			+ " b.archivoId.archivoId= :archivoId "
			+ " and b.codigoCliente= :codigoCliente "						
			+ " and b.nroRegistro not in :nroRegistro "
			+ " and b.nroRegistro < ( Select max( b.nroRegistro) "
			+ " 						from PBeneficiariosEntity b "
			+ " 						where "
			+ " 						b.archivoId.archivoId= :archivoId "
			+ " 						and b.codigoCliente= :codigoCliente "						
			+ " 						and b.nroRegistro in :nroRegistro )")
	public List<PBeneficiariosEntity>  verificarPrelacion(
			@Param("archivoId") Long archivoId,
			@Param("codigoCliente") String codigoCliente,			
			@Param("nroRegistro") List<Integer> nroRegistro			
			);
	
	
}
