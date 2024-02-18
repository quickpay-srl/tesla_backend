package bo.com.tesla.administracion.dao;

import bo.com.tesla.administracion.dto.EntidadComisionAdmDto;
import bo.com.tesla.administracion.dto.RecaudadorComisionAdmDto;
import bo.com.tesla.administracion.entity.EntidadComisionEntity;
import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.RecaudadorComisionEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRecaudadorComisionDao extends JpaRepository<RecaudadorComisionEntity, Long> {

    /************************ABM****************************/

    @Modifying
    @Query(value = "UPDATE RecaudadorComisionEntity e " +
            "SET e.transaccion = :transaccion, e.usuarioModificacion = :usuarioModificacion, e.fechaModificacion = current_timestamp " +
            "WHERE e.recaudadorComisionId = :recaudadorComisionId")
    Integer updateTransaccion(@Param("recaudadorComisionId") Long recaudadorComisionId,
                              @Param("transaccion") String transaccion,
                              @Param("usuarioModificacion") Long usuarioModificacion);

    @Modifying
    @Query(value = "UPDATE RecaudadorComisionEntity e " +
            "SET e.transaccion = 'INACTIVAR', e.usuarioModificacion = :usuarioModificacion, e.fechaModificacion = current_timestamp " +
            "WHERE e.recaudador.recaudadorId = :recaudadorId " +
            "AND e.estado = 'ACTIVO'")
    Integer updateRecaudadorComisionActivo(@Param("recaudadorId") Long recaudadorId, @Param("usuarioModificacion") long usuarioModificacion);

    @Query(value = "SELECT new bo.com.tesla.administracion.dto.RecaudadorComisionAdmDto(" +
            "e.recaudadorComisionId, e.recaudador.recaudadorId, e.tipoComision.dominioId, e.tipoComision.descripcion, e.comision, " +
            "s.login, e.fechaCreacion, e.fechaModificacion, e.estado, e.recaudador.estado) " +
            "FROM RecaudadorComisionEntity e " +
            "INNER JOIN SegUsuarioEntity s ON s.usuarioId = e.usuarioCreacion " +
            "WHERE e.recaudador.recaudadorId = :recaudadorId " +
            "ORDER BY e.fechaCreacion DESC")
    List<RecaudadorComisionAdmDto> findRecaudadorComisionByRecaudadorId(@Param("recaudadorId") Long entidadId);

    @Query(value = "SELECT new bo.com.tesla.administracion.dto.RecaudadorComisionAdmDto(" +
            "e.recaudadorComisionId, e.recaudador.recaudadorId, e.tipoComision.dominioId, e.tipoComision.descripcion, e.comision, " +
            "s.login, e.fechaCreacion, e.fechaModificacion, e.estado, e.recaudador.estado) " +
            "FROM RecaudadorComisionEntity e " +
            "INNER JOIN SegUsuarioEntity s ON s.usuarioId = e.usuarioCreacion " +
            "WHERE e.recaudador.recaudadorId = :recaudadorId and e.estado='ACTIVO' " +
            "ORDER BY e.fechaCreacion DESC")
    List<RecaudadorComisionAdmDto> findRecaudadorComisionByRecaudadorActivoId(@Param("recaudadorId") Long entidadId);

    @Query(value = "SELECT new bo.com.tesla.administracion.dto.RecaudadorComisionAdmDto( " +
            "e.recaudadorComisionId, e.recaudador.recaudadorId, e.tipoComision.dominioId, e.tipoComision.descripcion, e.comision, " +
            "s.login, e.fechaCreacion, e.fechaModificacion, e.estado, e.recaudador.estado) " +
            "FROM RecaudadorComisionEntity e " +
            "INNER JOIN SegUsuarioEntity s ON s.usuarioId = e.usuarioCreacion " +
            "WHERE e.recaudadorComisionId = :recaudadorComisionId")
    Optional<RecaudadorComisionAdmDto> findREcaudadorComisionDtoById(@Param("recaudadorComisionId") Long recaudadorComisionId);

    Optional<RecaudadorComisionEntity> findRecaudadorComisionEntityByRecaudadorAndEstado(RecaudadorEntity recaudadorEntity, String estado);

}
