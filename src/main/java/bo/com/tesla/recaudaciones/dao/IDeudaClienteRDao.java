package bo.com.tesla.recaudaciones.dao;

import bo.com.tesla.administracion.entity.DeudaClienteEntity;
import bo.com.tesla.recaudaciones.dto.ClienteDto;
import bo.com.tesla.recaudaciones.dto.DeudaClienteDto;
import bo.com.tesla.recaudaciones.dto.ServicioDeudaDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDeudaClienteRDao extends JpaRepository<DeudaClienteEntity, Long> {

    @Query(value = "select distinct new bo.com.tesla.recaudaciones.dto.ClienteDto(" +
            "d.codigoCliente, d.nombreCliente, d.nroDocumento, d.correoCliente)" +
            "from DeudaClienteEntity d " +
            "where d.archivoId.entidadId.entidadId = :pEntidadId " +
            "and d.archivoId.estado = 'ACTIVO' " +
            "and (upper(d.codigoCliente) like upper(concat('%', :pDatoCliente, '%')) or upper(d.nroDocumento) like upper(concat('%', :pDatoCliente, '%')) or upper(d.nombreCliente) like upper(concat('%', :pDatoCliente, '%')) )")
    List<ClienteDto> findByEntidadAndClienteLike(@Param("pEntidadId") Long pEntidadId, @Param("pDatoCliente") String pDatoCliente);


    @Query("select new bo.com.tesla.recaudaciones.dto.ServicioDeudaDto( "
            + " d.tipoServicio, d.servicio, d.periodo, sum(d.subTotal)) "
            + " from DeudaClienteEntity d "
            + " where d.archivoId.entidadId.entidadId = :entidadId "
            + " and d.archivoId.entidadId.estado = 'ACTIVO' "
            + " and d.archivoId.estado = 'ACTIVO' "
            + " and d.codigoCliente = :codigoCliente "
            + " and d.tipo = 'D' " //puede ser que en cargado envien un tipo diferente
            + " group by d.tipoServicio, d.servicio, d.periodo, d.archivoId.entidadId.entidadId")
    List<ServicioDeudaDto> groupByDeudasClientes(@Param("entidadId") Long entidadId , @Param("codigoCliente") String codigoCliente );

    @Query("select new  bo.com.tesla.recaudaciones.dto.DeudaClienteDto( "
            + " d.deudaClienteId, d.archivoId.archivoId, d.cantidad, d.concepto, d.montoUnitario, d.subTotal, "
            + " d.tipoComprobante, d.periodoCabecera, d.codigoCliente,d.nombreCliente,d.nroDocumento,  d.esPostpago, CASE WHEN d.esPostpago = false AND d.subTotal = 0 AND d.tipo = 'D' THEN true ELSE false END) "
            + " from DeudaClienteEntity d "
            + " where d.archivoId.entidadId.entidadId = :entidadId "
            + " and d.archivoId.estado = 'ACTIVO' "
            + " and d.tipoServicio= :tipoServicio "
            + " and d.servicio= :servicio "
            + " and d.periodo= :periodo"
            + " and d.codigoCliente= :codigoCliente "
            + " and d.tipo = 'D' " +
            " ORDER BY d.deudaClienteId") //Orden del cargado
    List<DeudaClienteDto> findByEntidadByServiciosDeudas(
            @Param("entidadId") Long entidadId,
            @Param("tipoServicio") String tipoServicio,
            @Param("servicio") String servicio,
            @Param("periodo") String periodo,
            @Param("codigoCliente") String codigoCliente);

    @Query("select d "
            + " from DeudaClienteEntity d "
            + " where d.archivoId.entidadId.entidadId = :entidadId "
            + " and d.archivoId.estado = 'ACTIVO' "
            + " and d.tipoServicio= :tipoServicio "
            + " and d.servicio= :servicio "
            + " and d.periodo= :periodo"
            + " and d.codigoCliente= :codigoCliente ")
    List<DeudaClienteEntity> findAllGroupByServicio(
            @Param("entidadId") Long entidadId,
            @Param("tipoServicio") String tipoServicio,
            @Param("servicio") String servicio,
            @Param("periodo") String periodo,
            @Param("codigoCliente") String codigoCliente);


    @Modifying
    Long deleteByDeudaClienteIdIn(List<Long> deudaClienteIdLst);

    @Modifying
    @Query(value = "INSERT INTO tesla.deudas_clientes (archivo_id, nro_registro, codigo_cliente, nombre_cliente, nro_documento, direccion, nit, telefono, servicio, tipo_servicio, periodo, tipo, concepto, cantidad, monto_unitario, sub_total, dato_extras, tipo_comprobante, periodo_cabecera, es_postpago, codigo_actividad_economica, correo_cliente) " +
            "select h.archivo_id, h.nro_registro, h.codigo_cliente, h.nombre_cliente, h.nro_documento, h.direccion, h.nit, h.telefono, h.servicio, h.tipo_servicio, h.periodo, h.tipo, h.concepto, h.cantidad, h.monto_unitario, h.sub_total, h.dato_extra, h.tipo_comprobante, h.periodo_cabecera, h.es_postpago, h.codigo_actividad_economica, h.correo_cliente " +
            "from tesla.historicos_deudas h " +
            "inner join tesla.cobros_clientes c on h.historico_deuda_id = c.historico_deuda_id " +
            "inner join tesla.transacciones_cobros t on t.transaccion_cobro_id = c.transaccion_cobro_id " +
            "where t.factura_id in :facturaIdLst", nativeQuery = true)
    Integer recoverDeudasByFacturas(@Param("facturaIdLst") List<Long> facturaIdLst);

    @Modifying
    @Query(value = "INSERT INTO tesla.deudas_clientes (archivo_id, nro_registro, codigo_cliente, nombre_cliente, nro_documento, direccion, nit, telefono, servicio, tipo_servicio, periodo, tipo, concepto, cantidad, monto_unitario, sub_total, dato_extras, tipo_comprobante, periodo_cabecera, es_postpago, codigo_actividad_economica, correo_cliente) " +
            "select h.archivo_id, h.nro_registro, h.codigo_cliente, h.nombre_cliente, h.nro_documento, h.direccion, h.nit, h.telefono, h.servicio, h.tipo_servicio, h.periodo, h.tipo, h.concepto, h.cantidad, h.monto_unitario, h.sub_total, h.dato_extra, h.tipo_comprobante, h.periodo_cabecera, h.es_postpago, h.codigo_actividad_economica, h.correo_cliente " +
            "from tesla.historicos_deudas h " +
            "inner join tesla.cobros_clientes c on h.historico_deuda_id = c.historico_deuda_id " +
            "inner join tesla.transacciones_cobros t on t.transaccion_cobro_id = c.transaccion_cobro_id " +
            "where t.factura_id = :facturaId", nativeQuery = true)
    Integer recoverDeudasByFactura(@Param("facturaId") Long facturaId);

    @Query("select distinct new bo.com.tesla.recaudaciones.dto.ClienteDto(" +
            "d.codigoCliente, d.nombreCliente, coalesce(d.nroDocumento, d.nit), d.correoCliente)" +
            "from DeudaClienteEntity d " +
            "where d.archivoId.entidadId.entidadId = :entidadId " +
            "and d.archivoId.estado = 'ACTIVO' " +
            "and d.codigoCliente = :codigoCliente")
    List<ClienteDto> findCodigoClienteByEntidad(@Param("entidadId") Long entidadId, @Param("codigoCliente") String codigoCliente);

    @Query(value = "select distinct new bo.com.tesla.recaudaciones.dto.ClienteDto(" +
            "d.codigoCliente, d.nombreCliente, coalesce(d.nroDocumento, d.nit), d.correoCliente)" +
            "from DeudaClienteEntity d " +
            "where d.archivoId.entidadId.entidadId = :entidadId " +
            "and d.archivoId.estado = 'ACTIVO' " +
            "and upper(d.codigoCliente) = upper(:codigoCliente) ")
    List<ClienteDto> findByEntidadAndCodigoCliente(@Param("entidadId") Long entidadId, @Param("codigoCliente") String codigoCliente);

    @Query(value = "select distinct new bo.com.tesla.recaudaciones.dto.ClienteDto(" +
            "d.codigoCliente, d.nombreCliente, coalesce(d.nroDocumento, d.nit), d.correoCliente)" +
            "from DeudaClienteEntity d " +
            "where d.archivoId.entidadId.entidadId = :entidadId " +
            "and d.archivoId.estado = 'ACTIVO' " +
            "and upper(d.nombreCliente) like upper(concat('%', :nombreCliente, '%')) ")
    List<ClienteDto> findByEntidadAndNombreCliente(@Param("entidadId") Long entidadId, @Param("nombreCliente") String nombreCliente);

    @Query(value = "select distinct new bo.com.tesla.recaudaciones.dto.ClienteDto(" +
            "d.codigoCliente, d.nombreCliente, coalesce(d.nroDocumento, d.nit), d.correoCliente)" +
            "from DeudaClienteEntity d " +
            "where d.archivoId.entidadId.entidadId = :entidadId " +
            "and d.archivoId.estado = 'ACTIVO' " +
            "and upper(d.nroDocumento) = upper(:nroDocumento) ")
    List<ClienteDto> findByEntidadAndNroDocumento(@Param("entidadId") Long entidadId, @Param("nroDocumento") String nroDocumento);

    @Query(value = "select distinct new bo.com.tesla.recaudaciones.dto.ClienteDto(" +
            "d.codigoCliente, d.nombreCliente, coalesce(d.nroDocumento, d.nit), d.correoCliente)" +
            "from DeudaClienteEntity d " +
            "where d.archivoId.entidadId.entidadId = :entidadId " +
            "and d.archivoId.estado = 'ACTIVO' " +
            "and upper(d.nit) = upper(:nit) ")
    List<ClienteDto> findByEntidadAndNit(@Param("entidadId") Long entidadId, @Param("nit") String nit);

    @Query(value = "select distinct new bo.com.tesla.recaudaciones.dto.ClienteDto(" +
            "d.codigoCliente, d.nombreCliente, coalesce(d.nroDocumento, d.nit), d.correoCliente)" +
            "from DeudaClienteEntity d " +
            "where d.archivoId.entidadId.entidadId = :entidadId " +
            "and d.archivoId.estado = 'ACTIVO' " +
            "and upper(d.telefono) = upper(:telefono) ")
    List<ClienteDto> findByEntidadAndTelefono(@Param("entidadId") Long entidadId, @Param("telefono") String telefono);

}
