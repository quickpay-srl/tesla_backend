package bo.com.tesla.administracion.dao;

import bo.com.tesla.administracion.dto.CredencialFacturacionDto;
import bo.com.tesla.administracion.dto.SucursalEntidadAdmDto;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.administracion.entity.SucursalEntidadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISucursalEntidadDao extends JpaRepository<SucursalEntidadEntity, Long> {

    /************************ABM****************************/

    @Modifying
    @Query(value = "UPDATE SucursalEntidadEntity se " +
            "SET se.transaccion = :transaccion, se.usuarioModificacion = :usuarioModificacion, se.fechaModificacion = CURRENT_TIMESTAMP " +
            "WHERE se.sucursalEntidadId = :sucursalEntidadId ")
    Integer updateTransaccionSucursalEntidad(@Param("sucursalEntidadId") Long sucursalEntidadId,
                                           @Param("transaccion") String transaccion,
                                           @Param("usuarioModificacion") Long usuarioModificacion);

    @Modifying
    @Query(value = "UPDATE SucursalEntidadEntity se " +
            "SET se.transaccion = :transaccion, se.usuarioModificacion = :usuarioModificacion, se.fechaModificacion = CURRENT_TIMESTAMP " +
            "WHERE se.sucursalEntidadId IN :sucursalEntidadLstId ")
    Integer updateLstTransaccionSucursalEntidad(@Param("sucursalEntidadLstId") List<Long> sucursalEntidadLstId,
                                             @Param("transaccion") String transaccion,
                                             @Param("usuarioModificacion") Long usuarioModificacion);


    @Query(value = "SELECT new bo.com.tesla.administracion.dto.SucursalEntidadAdmDto( " +
            "se.sucursalEntidadId, se.entidad.entidadId, se.nombreSucursal,se.direccion, se.telefono, u.login, se.fechaCreacion, se.estado, " +
            "se.email, se.numeroSucursalSin, se.emiteFacturaTesla, " +
            "se.departamentoId.dominioId, se.departamentoId.descripcion, se.municipioId.dominioId, se.municipioId.descripcion) " +
            "FROM SucursalEntidadEntity se " +
            "INNER JOIN SegUsuarioEntity u ON se.usuarioCreacion = u.usuarioId " +
            "WHERE se.estado <> 'ELIMINADO'")
    List<SucursalEntidadAdmDto> findSucursalesEntidadesDtoAll();

    @Query(value = "SELECT new bo.com.tesla.administracion.dto.SucursalEntidadAdmDto( " +
            "se.sucursalEntidadId, se.entidad.entidadId, se.nombreSucursal,se.direccion, se.telefono, u.login, se.fechaCreacion, se.estado," +
            "se.email, se.numeroSucursalSin, se.emiteFacturaTesla, " +
            "se.departamentoId.dominioId, se.departamentoId.descripcion, se.municipioId.dominioId, se.municipioId.descripcion) " +
            "FROM SucursalEntidadEntity se " +
            "INNER JOIN SegUsuarioEntity u ON se.usuarioCreacion = u.usuarioId " +
            "WHERE se.sucursalEntidadId = :sucursalEntidadId")
    Optional<SucursalEntidadAdmDto> findSucursalEntidadDtoById(@Param("sucursalEntidadId") Long sucursalEntidadId);

    @Query(value = "SELECT new bo.com.tesla.administracion.dto.SucursalEntidadAdmDto( " +
            "se.sucursalEntidadId, se.entidad.entidadId, se.nombreSucursal,se.direccion, se.telefono, u.login, se.fechaCreacion, se.estado, " +
            "se.email, se.numeroSucursalSin, se.emiteFacturaTesla, " +
            "se.departamentoId.dominioId, se.departamentoId.descripcion, se.municipioId.dominioId, se.municipioId.descripcion) " +
            "FROM SucursalEntidadEntity se " +
            "INNER JOIN SegUsuarioEntity u ON se.usuarioCreacion = u.usuarioId " +
            "WHERE se.entidad.entidadId = :entidadId " +
            "AND se.estado <> 'ELIMINADO'")
    List<SucursalEntidadAdmDto> findSucursalesEntidadesDtoByEntidadId(@Param("entidadId") Long entidadId);

    @Query(value = "SELECT new bo.com.tesla.administracion.dto.SucursalEntidadAdmDto( " +
            "se.sucursalEntidadId, se.entidad.entidadId, se.nombreSucursal,se.direccion, se.telefono, u.login, se.fechaCreacion, se.estado, " +
            "se.email, se.numeroSucursalSin, se.emiteFacturaTesla, " +
            "se.departamentoId.dominioId, se.departamentoId.descripcion, se.municipioId.dominioId, se.municipioId.descripcion) " +
            "FROM SucursalEntidadEntity se " +
            "INNER JOIN SegUsuarioEntity u ON se.usuarioCreacion = u.usuarioId " +
            "WHERE (se.entidad.entidadId = :entidadId or se.sucursalEntidadId=43)" +
            "AND se.estado <> 'ELIMINADO'")
    List<SucursalEntidadAdmDto> findSucursalesEntidadesDtoForAddUserByEntidadId(@Param("entidadId") Long entidadId);
    
    
    
    @Query(value = "Select se "
    		+ " from SucursalEntidadEntity se "
    		+ " where se.entidad.entidadId= :entidadId"
    		+ " and se.estado= 'ACTIVO'")
    public List<SucursalEntidadEntity> findSucursalByEntidadId(@Param("entidadId") Long entidadId);

    @Query("SELECT new bo.com.tesla.administracion.dto.SucursalEntidadAdmDto( " +
            "se.sucursalEntidadId, se.entidad.entidadId, se.nombreSucursal,se.direccion, se.telefono, u.login, se.fechaCreacion, se.estado, " +
            "se.email, se.numeroSucursalSin, se.emiteFacturaTesla, " +
            "se.departamentoId.dominioId, se.departamentoId.descripcion, se.municipioId.dominioId, se.municipioId.descripcion) " +
            "FROM SucursalEntidadEntity se " +
            "INNER JOIN SegUsuarioEntity u ON se.usuarioCreacion = u.usuarioId " +
            "WHERE se.entidad.entidadId = :entidadId " +
            "AND se.estado = 'ACTIVO' " +
            "AND se.entidad.estado = 'ACTIVO' AND se.entidad.nombre != '-' ")
    List<SucursalEntidadAdmDto> findSucursalesEntidadesDtoByEntidadIdActivos(@Param("entidadId") Long entidadId);


    /************************FACTURACION****************************/


    @Query("SELECT s " +
            "FROM SucursalEntidadEntity s " +
            "WHERE s.entidad.entidadId = :entidadId " +
            "AND s.entidad.estado = 'ACTIVO' " +
            "AND s.estado = 'ACTIVO' " +
            "AND s.emiteFacturaTesla = true " +
            "AND s.usuarioFacturacion is not null " +
            "AND s.passwordFacturacion is not null")
    Optional<SucursalEntidadEntity> findByEmiteFacturaTesla(@Param("entidadId") Long entidadId);

    @Query("SELECT new bo.com.tesla.administracion.dto.SucursalEntidadAdmDto( " +
            "se.sucursalEntidadId, se.entidad.entidadId, se.nombreSucursal,se.direccion, se.telefono, u.login, se.fechaCreacion, se.estado, " +
            "se.email, se.numeroSucursalSin, se.emiteFacturaTesla, " +
            "se.departamentoId.dominioId, se.departamentoId.descripcion, se.municipioId.dominioId, se.municipioId.descripcion) " +
            "FROM SucursalEntidadEntity se " +
            "INNER JOIN SegUsuarioEntity u ON se.usuarioCreacion = u.usuarioId " +
            "WHERE se.entidad.entidadId = :entidadId " +
            "AND se.entidad.estado = 'ACTIVO' " +
            "AND se.estado = 'ACTIVO' " +
            "AND se.emiteFacturaTesla = true " +
            "AND se.usuarioFacturacion is not null " +
            "AND se.passwordFacturacion is not null")
    Optional<SucursalEntidadAdmDto> findDtoByEmiteFacturaTesla(@Param("entidadId") Long entidadId);

    @Query("SELECT count(s) " +
            "FROM SucursalEntidadEntity s " +
            "WHERE s.entidad.entidadId = :entidadId " +
            "AND s.entidad.estado IN ('ACTIVO', 'CREADO') " +
            "AND s.estado IN ('ACTIVO', 'CREADO') " +
            "AND s.emiteFacturaTesla = true " +
            "AND (:sucursalEntidadId is null OR (s.sucursalEntidadId <> :sucursalEntidadId))")
    Long countEmiteFacturaTesla(@Param("entidadId") Long entidadId,
                                @Param("sucursalEntidadId") Long sucursalEntidadId);

    @Modifying
    @Query("UPDATE SucursalEntidadEntity se " +
            "SET se.usuarioFacturacion = :login, se.passwordFacturacion = :password," +
            "se.transaccion = 'MODIFICAR', se.usuarioModificacion = :usuarioId, se.fechaModificacion = CURRENT_TIMESTAMP " +
            "WHERE se.sucursalEntidadId = :sucursalEntidadId")
    int updateCredencialesFacturacion(@Param("sucursalEntidadId") Long sucursalEntidadId,
                                      @Param("login") String login,
                                      @Param("password") String password,
                                      @Param("usuarioId") Long usuarioId);

    @Query("SELECT new bo.com.tesla.administracion.dto.CredencialFacturacionDto( " +
            "se.entidad.entidadId, se.sucursalEntidadId, se.usuarioFacturacion, se.passwordFacturacion) " +
            "FROM SucursalEntidadEntity se " +
            "WHERE se.sucursalEntidadId = :sucursalEntidadId")
    Optional<CredencialFacturacionDto> findCredencialFacturacion(@Param("sucursalEntidadId") Long sucursalEntidadId);
}
