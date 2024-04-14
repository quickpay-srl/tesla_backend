package bo.com.tesla.externo.sitio.dao;

import bo.com.tesla.administracion.entity.DeudaClienteEntity;
import bo.com.tesla.externo.sitio.dto.SitioDeudaClienteCabDto;
import bo.com.tesla.externo.sitio.dto.SitioDeudaClienteDetDto;
import bo.com.tesla.recaudaciones.dto.ServicioDeudaDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ISitioDeudaClienteDao extends JpaRepository<DeudaClienteEntity,Long> {
    //    String codigoCliente, String nroDocumento, String nombreCliente, String correoCliente
    @Query(value = "select distinct new bo.com.tesla.externo.sitio.dto.SitioDeudaClienteCabDto(" +
            "d.codigoCliente,  coalesce(d.nroDocumento, d.nit),d.nombreCliente, d.correoCliente)" +
            "from DeudaClienteEntity d " +
            "where d.archivoId.entidadId.entidadId = :entidadId " +
            "and d.archivoId.estado = 'ACTIVO' " +
            "and upper(d.codigoCliente) = upper(:codigoCliente) ")
    List<SitioDeudaClienteCabDto> findByEntidadAndCodigoCliente(@Param("entidadId") Long entidadId, @Param("codigoCliente") String codigoCliente);

    //Long deudaClienteId, String tipoServicio, String servicio, String periodo, java.math.BigDecimal cantidad,
    //String concepto, BigDecimal montoUnitario, BigDecimal subTotal
    @Query("select new bo.com.tesla.externo.sitio.dto.SitioDeudaClienteDetDto( "
            + "d.deudaClienteId,d.nroRegistro, d.tipoServicio, d.servicio, d.periodo, d.cantidad,d.concepto,sum(d.montoUnitario), sum(d.subTotal)) "
            + " from DeudaClienteEntity d "
            + " where d.archivoId.entidadId.entidadId = :entidadId "
            + " and d.archivoId.entidadId.estado = 'ACTIVO' "
            + " and d.archivoId.estado = 'ACTIVO' "
            + " and d.codigoCliente = :codigoCliente "
            + " and d.tipo = 'D' " //puede ser que en cargado envien un tipo diferente
            + " group by d.deudaClienteId,d.tipoServicio, d.servicio,d.periodo,d.cantidad,  d.concepto" )
    List<SitioDeudaClienteDetDto> groupByDeudasDetClientes(@Param("entidadId") Long entidadId , @Param("codigoCliente") String codigoCliente );

    @Query("select d "
            + " from DeudaClienteEntity d "
            + " where d.archivoId.estado = 'ACTIVO' "
            + " and d.deudaClienteId =:deudaClienteId ")
    Optional<DeudaClienteEntity> findForDeudaClienteId(@Param("deudaClienteId") Long deudaClienteId);

    @Query("select d "
            + " from DeudaClienteEntity d "
            + " where d.archivoId.estado = 'ACTIVO' "
            + " and d.deudaClienteId IN :deudaClienteId ")
    List<DeudaClienteEntity> findForDeudaClienteId(@Param("deudaClienteId") List<Long> deudaClienteId);


}
