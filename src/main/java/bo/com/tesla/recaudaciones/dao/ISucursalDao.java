package bo.com.tesla.recaudaciones.dao;

import bo.com.tesla.administracion.dto.RecaudadorAdmDto;
import bo.com.tesla.administracion.dto.SucursalAdmDto;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.administracion.entity.SucursalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISucursalDao extends JpaRepository<SucursalEntity, Long> {

    /************************ABM****************************/

    @Modifying
    @Query(value = "UPDATE SucursalEntity e " +
            "SET e.transaccion = :transaccion, e.usuarioModificacion = :usuarioModificacion, e.fechaModificacion = current_timestamp " +
            "WHERE e.sucursalId = :sucursalId")
    Integer updateTransaccionSucursal(@Param("sucursalId") Long sucursalId,
                                         @Param("transaccion") String transaccion,
                                         @Param("usuarioModificacion") Long usuarioModificacion);

    @Modifying
    @Query(value = "UPDATE SucursalEntity e " +
            "SET e.transaccion = :transaccion, e.usuarioModificacion = :usuarioModificacion, e.fechaModificacion = current_timestamp " +//:fechaModificacion  " +
            "WHERE e.sucursalId IN :sucursalIdLst")
    Integer updateLstTransaccionSucursal(@Param("sucursalIdLst") List<Long> sucursalIdLst,
                                            @Param("transaccion") String transaccion,
                                            @Param("usuarioModificacion") Long usuarioModificacion);

    @Query(value = "SELECT new bo.com.tesla.administracion.dto.SucursalAdmDto(" +
            "su.sucursalId, su.recaudador.recaudadorId, su.nombre, su.direccion, su.telefono, " +
            "su.departamento.dominioId, su.departamento.descripcion, " +
            "su.localidad.dominioId, su.localidad.descripcion, " +
            "s.login, su.fechaCreacion, su.estado) " +
            "FROM SucursalEntity su " +
            "INNER JOIN SegUsuarioEntity s ON s.usuarioId = su.usuarioCreacion " +
            "WHERE su.estado <> 'ELIMINADO'" )
    List<SucursalAdmDto> findRecaudadorDtoAll();

    @Query(value = "SELECT new bo.com.tesla.administracion.dto.SucursalAdmDto(" +
            "su.sucursalId, su.recaudador.recaudadorId, su.nombre, su.direccion, su.telefono, " +
            "su.departamento.dominioId, su.departamento.descripcion, " +
            "su.localidad.dominioId, su.localidad.descripcion, " +
            "s.login, su.fechaCreacion, su.estado) " +
            "FROM SucursalEntity su " +
            "INNER JOIN SegUsuarioEntity s ON s.usuarioId = su.usuarioCreacion " +
            "WHERE su.sucursalId = :sucursalId")
    Optional<SucursalAdmDto> findSucursalDtoById(@Param("sucursalId") Long sucursalId);

    @Query(value = "SELECT new bo.com.tesla.administracion.dto.SucursalAdmDto(" +
            "su.sucursalId, su.recaudador.recaudadorId, su.nombre, su.direccion, su.telefono, " +
            "su.departamento.dominioId, su.departamento.descripcion, " +
            "su.localidad.dominioId, su.localidad.descripcion, " +
            "s.login, su.fechaCreacion, su.estado) " +
            "FROM SucursalEntity su " +
            "INNER JOIN SegUsuarioEntity s ON s.usuarioId = su.usuarioCreacion " +
            "WHERE su.estado <> 'ELIMINADO' " +
            "AND su.recaudador.recaudadorId = :recaudadorId ")
    List<SucursalAdmDto> findLstSucursalesDtoByRecaudadorId(@Param("recaudadorId") Long recaudadorId);

    @Query(value = "SELECT new bo.com.tesla.administracion.dto.SucursalAdmDto(" +
            "su.sucursalId, su.recaudador.recaudadorId, su.nombre, su.direccion, su.telefono, " +
            "su.departamento.dominioId, su.departamento.descripcion, " +
            "su.localidad.dominioId, su.localidad.descripcion, " +
            "s.login, su.fechaCreacion, su.estado) " +
            "FROM SucursalEntity su " +
            "INNER JOIN SegUsuarioEntity s ON s.usuarioId = su.usuarioCreacion " +
            "WHERE su.estado <> 'ELIMINADO' " +
            "AND (su.recaudador.recaudadorId = :recaudadorId OR su.sucursalId=34 )")
    List<SucursalAdmDto> findLstSucursalesDtoForAddUserByRecaudadorId(@Param("recaudadorId") Long recaudadorId);
    /************************COBROS****************************/

    @Query(value = "select e.sucursalId "
            + " from SegUsuarioEntity u "
            + " inner join PersonaEntity  p on p.personaId = u.personaId.personaId "
            + " inner join EmpleadoEntity e on e.personaId.personaId = p.personaId "
            + " where u.estado = 'ACTIVO' "
            + " and e.sucursalId.estado = 'ACTIVO' "
            + " and e.sucursalId.recaudador.estado = 'ACTIVO'"
            + " and u.usuarioId=:usuarioId")
    Optional<SucursalEntity> findSucursalByUserId(@Param("usuarioId") Long usuarioId);
    
    
    @Query(" Select s "
    		+ " from SucursalEntity s "
    		+ " where s.sucursalId= :sucursalId ")
    public Optional<SucursalEntity> findById(@Param("sucursalId") Long sucursalId);
    
    
    @Query(" Select s "
    		+ " from SucursalEntity s "
    		+ " where s.recaudador.recaudadorId= :recaudadorId "
    		+ " and s.estado='ACTIVO' ")
    public List<SucursalEntity> findByRecaudadoraId(@Param("recaudadorId") Long recaudadorId);

}
