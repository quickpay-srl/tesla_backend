package bo.com.tesla.pagos.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bo.com.tesla.administracion.entity.PHistoricoBeneficiariosEntity;
import bo.com.tesla.pagos.dto.PBeneficiarioReporteDto;

@Repository
public interface IPHistoricoBeneficiariosDao extends JpaRepository<PHistoricoBeneficiariosEntity, Long>{
	
	
	
	
	@Modifying
	@Query(value =" Update tesla.p_historicos_beneficiarios  "
			+ " set estado='PAGADO',"
			+ "     usuario_modificacion = :usuarioModificacion, "
			+ "     fecha_modificacion = :fechaModificacion"
			+ " Where "
			+ "     archivo_id= :archivoId "
			+ " 	and codigo_cliente= :codigoCliente "
			+ "     and nro_documento_cliente= :nroDocumentoCliente "
			+ "		and periodo = :periodo ",nativeQuery = true)
	public void updateByArchivoId(
			@Param("archivoId") Long archivoId, 
			@Param("codigoCliente") String codigoCliente,
			@Param("nroDocumentoCliente") String nroDocumentoCliente,
			@Param("usuarioModificacion") Long usuarioModificacion,
			@Param("fechaModificacion") Date fechaModificacion,
			@Param("periodo") String periodo
			);
	
	
	@Query(" Select new bo.com.tesla.pagos.dto.PBeneficiarioReporteDto(hb.codigoCliente, hb.nombreCliente, hb.fechaNacimientoCliente, "
			+ "			hb.nroDocumentoCliente, hb.extencionDocumentoId,  tp.total, "
			+ "			hb.periodo, hb.genero, tp.comisionRecaudacion, tp.comisionExacta, tp.fechaModificacion, "
			+ "			t.nombreCompleto, t.nroDocumento, r.nombre,hb.estado) "
			+ " from PHistoricoBeneficiariosEntity hb "
			+ " left join PTransaccionPagoEntity tp on ( tp.archivoId.archivoId=hb.archivoId.archivoId "
			+ "											 and tp.codigoCliente=hb.codigoCliente "
			+ "											 and tp.periodo=hb.periodo "
			+ "											) "
			+ " left join PTitularPagoEntity t on t.transaccionesPagoId.transaccionPagoId=tp.transaccionPagoId "
			+ " left join RecaudadorEntity r on r.recaudadorId=tp.recaudadorId.recaudadorId "
			+ " left join EntidadEntity e on e.entidadId=tp.entidadId.entidadId "
			+ " left join ArchivoEntity a on a.archivoId=hb.archivoId.archivoId"			
			+ " WHERE "
			+ " 	hb.estado in :estadoList "
			+ " 	and (CAST(tp.fechaModificacion AS date) between CAST(:fechaIni AS date) and CAST(:fechaFin AS date) or tp.fechaModificacion is null)"			
			+ " 	and (tp.recaudadorId.recaudadorId in :recaudadorIdList or tp.recaudadorId.recaudadorId is null)"
			+ " 	and (tp.entidadId.entidadId= :entidadId or tp.entidadId.entidadId is null)"
			+ "     and a.servicioProductoId.servicioProductoId= :servicioProductoId"
			+ " GROUP BY "
			+ " 	hb.codigoCliente,hb.nombreCliente,hb.fechaNacimientoCliente,hb.nroDocumentoCliente,hb.extencionDocumentoId,"
			+ "	    tp.total,hb.periodo,hb.genero, tp.comisionRecaudacion,tp.comisionExacta,tp.fechaModificacion,t.nombreCompleto,"
			+ "		t.nroDocumento,	r.nombre,hb.estado "
			+ " ORDER BY "
			+ "	         hb.estado,r.nombre asc ")
	public List<PBeneficiarioReporteDto> listForReportEntidad(
			@Param("fechaIni") Date fechaIni, 
			@Param("fechaFin") Date fechaFin,
			@Param("estadoList") List<String> estadoList,
			@Param("recaudadorIdList") List<Long> recaudadorIdList,
			@Param("entidadId") Long entidadId,
			@Param("servicioProductoId") Long servicioProductoId		
			);
	
	
	@Query(" Select new bo.com.tesla.pagos.dto.PBeneficiarioReporteDto(hb.codigoCliente, hb.nombreCliente, hb.fechaNacimientoCliente, "
			+ "			hb.nroDocumentoCliente, hb.extencionDocumentoId,  tp.total, "
			+ "			hb.periodo, hb.genero, tp.comisionRecaudacion, tp.comisionExacta, tp.fechaModificacion, "
			+ "			t.nombreCompleto, t.nroDocumento, r.nombre,hb.estado) "
			+ " from PHistoricoBeneficiariosEntity hb "
			+ " left join PTransaccionPagoEntity tp on ( tp.archivoId.archivoId=hb.archivoId.archivoId "
			+ "											 and tp.codigoCliente=hb.codigoCliente "
			+ "											 and tp.periodo=hb.periodo "
			+ "											) "
			+ " left join PTitularPagoEntity t on t.transaccionesPagoId.transaccionPagoId=tp.transaccionPagoId "
			+ " left join RecaudadorEntity r on r.recaudadorId=tp.recaudadorId.recaudadorId "
			+ " left join EntidadEntity e on e.entidadId=tp.entidadId.entidadId "
			+ " left join ArchivoEntity a on a.archivoId=hb.archivoId.archivoId"			
			+ " WHERE "
			+ " 	hb.estado in :estadoList "
			+ " 	and (CAST(tp.fechaModificacion AS date) between CAST(:fechaIni AS date) and CAST(:fechaFin AS date) or tp.fechaModificacion is null)"			
			+ " 	and (tp.recaudadorId.recaudadorId in :recaudadorIdList or tp.recaudadorId.recaudadorId is null)"
			+ " 	and (tp.entidadId.entidadId= :entidadId or tp.entidadId.entidadId is null)"
			+ "     and a.servicioProductoId.servicioProductoId= :servicioProductoId"
			+ " GROUP BY "
			+ " 	hb.codigoCliente,hb.nombreCliente,hb.fechaNacimientoCliente,hb.nroDocumentoCliente,hb.extencionDocumentoId,"
			+ "	    tp.total,hb.periodo,hb.genero, tp.comisionRecaudacion,tp.comisionExacta,tp.fechaModificacion,t.nombreCompleto,"
			+ "		t.nroDocumento,	r.nombre,hb.estado "
			+ " ORDER BY "
			+ "	         hb.estado,r.nombre asc ")
	public Page<PBeneficiarioReporteDto> listForGridEntidad(
			@Param("fechaIni") Date fechaIni, 
			@Param("fechaFin") Date fechaFin,
			@Param("estadoList") List<String> estadoList,
			@Param("recaudadorIdList") List<Long> recaudadorIdList,
			@Param("entidadId") Long entidadId,
			@Param("servicioProductoId") Long servicioProductoId,
			Pageable pageable	
			);
	
	@Query(" Select new bo.com.tesla.pagos.dto.PBeneficiarioReporteDto(hb.codigoCliente, hb.nombreCliente, hb.fechaNacimientoCliente, "
			+ "			hb.nroDocumentoCliente, hb.extencionDocumentoId,  tp.total, "
			+ "			hb.periodo, hb.genero, tp.comisionRecaudacion, tp.comisionExacta, tp.fechaModificacion, "
			+ "			t.nombreCompleto, t.nroDocumento, r.nombre,hb.estado) "
			+ " from PHistoricoBeneficiariosEntity hb "
			+ " left join PTransaccionPagoEntity tp on ( tp.archivoId.archivoId=hb.archivoId.archivoId "
			+ "											 and tp.codigoCliente=hb.codigoCliente "
			+ "											 and tp.periodo=hb.periodo "
			+ "											) "
			+ " left join PTitularPagoEntity t on t.transaccionesPagoId.transaccionPagoId=tp.transaccionPagoId "
			+ " left join RecaudadorEntity r on r.recaudadorId=tp.recaudadorId.recaudadorId "
			+ " left join EntidadEntity e on e.entidadId=tp.entidadId.entidadId "
			+ " left join ArchivoEntity a on a.archivoId=hb.archivoId.archivoId"			
			+ " WHERE "
			+ " 	hb.estado in :estadoList "
			+ " 	and (CAST(tp.fechaModificacion AS date) between CAST(:fechaIni AS date) and CAST(:fechaFin AS date) or tp.fechaModificacion is null)"			
			+ " 	and (tp.recaudadorId.recaudadorId= :recaudadorId or tp.recaudadorId.recaudadorId is null )"			
			+ "     and a.servicioProductoId.servicioProductoId= :servicioProductoId "
			+ " GROUP BY "
			+ " 	hb.codigoCliente,hb.nombreCliente,hb.fechaNacimientoCliente,hb.nroDocumentoCliente,hb.extencionDocumentoId,"
			+ "	    tp.total,hb.periodo,hb.genero, tp.comisionRecaudacion,tp.comisionExacta,tp.fechaModificacion,t.nombreCompleto,"
			+ "		t.nroDocumento,	r.nombre,hb.estado "
			+ " ORDER BY "
			+ "	         hb.estado,r.nombre asc ")
	public Page<PBeneficiarioReporteDto> listForGridRecaudacion(
			@Param("fechaIni") Date fechaIni, 
			@Param("fechaFin") Date fechaFin,
			@Param("estadoList") List<String> estadoList,
			@Param("recaudadorId") Long recaudadorId,		
			@Param("servicioProductoId") Long servicioProductoId,
			Pageable pageable	
			);
	
	
	@Query(" Select new bo.com.tesla.pagos.dto.PBeneficiarioReporteDto(hb.codigoCliente, hb.nombreCliente, hb.fechaNacimientoCliente, "
			+ "			hb.nroDocumentoCliente, hb.extencionDocumentoId,  tp.total, "
			+ "			hb.periodo, hb.genero, tp.comisionRecaudacion, tp.comisionExacta, tp.fechaModificacion, "
			+ "			t.nombreCompleto, t.nroDocumento, r.nombre,hb.estado) "
			+ " from PHistoricoBeneficiariosEntity hb "
			+ " left join PTransaccionPagoEntity tp on ( tp.archivoId.archivoId=hb.archivoId.archivoId "
			+ "											 and tp.codigoCliente=hb.codigoCliente "
			+ "											 and tp.periodo=hb.periodo "
			+ "											) "
			+ " left join PTitularPagoEntity t on t.transaccionesPagoId.transaccionPagoId=tp.transaccionPagoId "
			+ " left join RecaudadorEntity r on r.recaudadorId=tp.recaudadorId.recaudadorId "
			+ " left join EntidadEntity e on e.entidadId=tp.entidadId.entidadId "
			+ " left join ArchivoEntity a on a.archivoId=hb.archivoId.archivoId"			
			+ " WHERE "
			+ " 	hb.estado in :estadoList "
			+ " 	and (CAST(tp.fechaModificacion AS date) between CAST(:fechaIni AS date) and CAST(:fechaFin AS date) or tp.fechaModificacion is null)"			
			+ " 	and (tp.recaudadorId.recaudadorId= :recaudadorId or tp.recaudadorId.recaudadorId is null )"					
			+ "     and a.servicioProductoId.servicioProductoId= :servicioProductoId "
			+ " GROUP BY "
			+ " 	hb.codigoCliente,hb.nombreCliente,hb.fechaNacimientoCliente,hb.nroDocumentoCliente,hb.extencionDocumentoId,"
			+ "	    tp.total,hb.periodo,hb.genero, tp.comisionRecaudacion,tp.comisionExacta,tp.fechaModificacion,t.nombreCompleto,"
			+ "		t.nroDocumento,	r.nombre,hb.estado "
			+ " ORDER BY "
			+ "	         hb.estado,r.nombre asc ")
	public List<PBeneficiarioReporteDto> listForReportRecaudacion(
			@Param("fechaIni") Date fechaIni, 
			@Param("fechaFin") Date fechaFin,
			@Param("estadoList") List<String> estadoList,
			@Param("recaudadorId") Long recaudadorId,		
			@Param("servicioProductoId") Long servicioProductoId			
			);
	
	@Query(" Select new bo.com.tesla.pagos.dto.PBeneficiarioReporteDto(hb.codigoCliente, hb.nombreCliente, hb.fechaNacimientoCliente, "
			+ "			hb.nroDocumentoCliente, hb.extencionDocumentoId,  tp.total, "
			+ "			hb.periodo, hb.genero, tp.comisionRecaudacion, tp.comisionExacta, tp.fechaModificacion, "
			+ "			t.nombreCompleto, t.nroDocumento, r.nombre,hb.estado) "
			+ " from PHistoricoBeneficiariosEntity hb "
			+ " left join PTransaccionPagoEntity tp on ( tp.archivoId.archivoId=hb.archivoId.archivoId "
			+ "											 and tp.codigoCliente=hb.codigoCliente "
			+ "											 and tp.periodo=hb.periodo "
			+ "											) "
			+ " left join PTitularPagoEntity t on t.transaccionesPagoId.transaccionPagoId=tp.transaccionPagoId "
			+ " left join RecaudadorEntity r on r.recaudadorId=tp.recaudadorId.recaudadorId "
			+ " left join EntidadEntity e on e.entidadId=tp.entidadId.entidadId "
			+ " left join ArchivoEntity a on a.archivoId=hb.archivoId.archivoId"			
			+ " WHERE "
			+ " 	hb.estado in :estadoList "
			+ " 	and (CAST(tp.fechaModificacion AS date) between CAST(:fechaIni AS date) and CAST(:fechaFin AS date) or tp.fechaModificacion is null)"			
			+ " 	and (tp.recaudadorId.recaudadorId in :recaudadorIdList or tp.recaudadorId.recaudadorId is null)"		
			+ "     and a.servicioProductoId.servicioProductoId= :servicioProductoId"
			+ " GROUP BY "
			+ " 	hb.codigoCliente,hb.nombreCliente,hb.fechaNacimientoCliente,hb.nroDocumentoCliente,hb.extencionDocumentoId,"
			+ "	    tp.total,hb.periodo,hb.genero, tp.comisionRecaudacion,tp.comisionExacta,tp.fechaModificacion,t.nombreCompleto,"
			+ "		t.nroDocumento,	r.nombre,hb.estado "
			+ " ORDER BY "
			+ "	         hb.estado,r.nombre asc ")
	public List<PBeneficiarioReporteDto> listForReportAdministracion(
			@Param("fechaIni") Date fechaIni, 
			@Param("fechaFin") Date fechaFin,
			@Param("estadoList") List<String> estadoList,
			@Param("recaudadorIdList") List<Long> recaudadorIdList,			
			@Param("servicioProductoId") Long servicioProductoId		
			);
	
	
	@Query(" Select new bo.com.tesla.pagos.dto.PBeneficiarioReporteDto(hb.codigoCliente, hb.nombreCliente, hb.fechaNacimientoCliente, "
			+ "			hb.nroDocumentoCliente, hb.extencionDocumentoId,  tp.total, "
			+ "			hb.periodo, hb.genero, tp.comisionRecaudacion, tp.comisionExacta, tp.fechaModificacion, "
			+ "			t.nombreCompleto, t.nroDocumento, r.nombre,hb.estado) "
			+ " from PHistoricoBeneficiariosEntity hb "
			+ " left join PTransaccionPagoEntity tp on ( tp.archivoId.archivoId=hb.archivoId.archivoId "
			+ "											 and tp.codigoCliente=hb.codigoCliente "
			+ "											 and tp.periodo=hb.periodo "
			+ "											) "
			+ " left join PTitularPagoEntity t on t.transaccionesPagoId.transaccionPagoId=tp.transaccionPagoId "
			+ " left join RecaudadorEntity r on r.recaudadorId=tp.recaudadorId.recaudadorId "
			+ " left join EntidadEntity e on e.entidadId=tp.entidadId.entidadId "
			+ " left join ArchivoEntity a on a.archivoId=hb.archivoId.archivoId"			
			+ " WHERE "
			+ " 	hb.estado in :estadoList "
			+ " 	and (CAST(tp.fechaModificacion AS date) between CAST(:fechaIni AS date) and CAST(:fechaFin AS date) or tp.fechaModificacion is null)"			
			+ " 	and (tp.recaudadorId.recaudadorId in :recaudadorIdList or tp.recaudadorId.recaudadorId is null)"			
			+ "     and a.servicioProductoId.servicioProductoId= :servicioProductoId"
			+ " GROUP BY "
			+ " 	hb.codigoCliente,hb.nombreCliente,hb.fechaNacimientoCliente,hb.nroDocumentoCliente,hb.extencionDocumentoId,"
			+ "	    tp.total,hb.periodo,hb.genero, tp.comisionRecaudacion,tp.comisionExacta,tp.fechaModificacion,t.nombreCompleto,"
			+ "		t.nroDocumento,	r.nombre,hb.estado "
			+ " ORDER BY "
			+ "	         hb.estado,r.nombre asc ")
	public Page<PBeneficiarioReporteDto> listForGridAdministracion(
			@Param("fechaIni") Date fechaIni, 
			@Param("fechaFin") Date fechaFin,
			@Param("estadoList") List<String> estadoList,
			@Param("recaudadorIdList") List<Long> recaudadorIdList,			
			@Param("servicioProductoId") Long servicioProductoId,
			Pageable pageable	
			);
	
	

}
