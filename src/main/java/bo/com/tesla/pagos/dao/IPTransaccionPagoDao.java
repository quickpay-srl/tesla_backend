package bo.com.tesla.pagos.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bo.com.tesla.administracion.entity.PPagoClienteEntity;
import bo.com.tesla.administracion.entity.PTransaccionPagoEntity;
import bo.com.tesla.pagos.dto.PPagosDto;
@Repository
public interface IPTransaccionPagoDao  extends JpaRepository<PTransaccionPagoEntity, Long>  {
	
	
	
	@Query(value =" select nextval('tesla.secuencia_transaccion_seq') ",nativeQuery = true)
	public Long getSecuencialTransaccion();
	
	
	@Query("Select new bo.com.tesla.pagos.dto.PPagosDto(max(tp.transaccionPagoId), tp.codigoTransaccion,tp.fechaModificacion,tp.estado,"
			+ "			p.codigoCliente,p.nombreCliente,p.nroDocumentoCliente,p.extencionDocumentoId,ps.descripcion, tp.total)"
			+ " from PTransaccionPagoEntity tp "
			+ " inner join PPagoClienteEntity p on p.transaccionPagoId.transaccionPagoId=tp.transaccionPagoId "
			+ " inner join PServicioProductoEntity ps on ps.servicioProductoId=tp.servicioProductoId.servicioProductoId  "
			+ " where  "
			+ "		tp.usuarioModificacion= :usuarioId	"
			+ "		and tp.servicioProductoId.servicioProductoId= :servicioProductoId "
			+ "		and CAST(tp.fechaModificacion AS date)  between CAST(:fechaIni AS date) and CAST(:fechaFin AS date) "
			+ " 	and ( 	"			
			+ "				p.codigoCliente like :paramBusqueda "
			+ " 			or upper(p.nombreCliente) like upper(concat('%', :paramBusqueda,'%')) "
			+ " 			or p.nroDocumentoCliente like concat(:paramBusqueda,'%') "
			+ "			)"
			+ " GROUP BY tp.codigoTransaccion,tp.fechaModificacion ,tp.estado,"
			+ " p.codigoCliente,p.nombreCliente,p.nroDocumentoCliente,p.extencionDocumentoId,ps.descripcion,tp.total "
			+ " Order by max(tp.transaccionPagoId) desc")
	public Page<PPagosDto>  findTransaccionsByUsuario(
			@Param("fechaIni") Date fechaIni, 
			@Param("fechaFin") Date fechaFin,
			@Param("usuarioId") Long usuarioId,
			@Param("paramBusqueda") String paramBusqueda,
			@Param("servicioProductoId") Long servicioProductoId,
			Pageable pageable
			);
	
	@Query("Select pc "
			+ " From  PPagoClienteEntity pc "
			+ " inner join PTransaccionPagoEntity tp on tp.transaccionPagoId=pc.transaccionPagoId.transaccionPagoId "
			+ " Where tp.codigoTransaccion = :codigoTransaccion")
	public Page<PPagoClienteEntity>  pagosByCodigoTransacciones(			
			@Param("codigoTransaccion") String codigoTransaccion,			
			Pageable pageable
			);
	
	@Query("Select tp "
			+ " From  PTransaccionPagoEntity tp "			
			+ " Where tp.codigoTransaccion = :codigoTransaccion")
	public List<PTransaccionPagoEntity>  transaccionessByCodigoTransacciones(@Param("codigoTransaccion") String codigoTransaccion);

}
