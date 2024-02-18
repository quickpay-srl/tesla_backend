package bo.com.tesla.administracion.dao;

import bo.com.tesla.administracion.dto.EntidadComisionAdmDto;
import bo.com.tesla.administracion.entity.EntidadComisionEntity;
import bo.com.tesla.administracion.entity.EntidadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IEntidadComisionesDao extends JpaRepository<EntidadComisionEntity, Long> {



    /************************ABM****************************/

    @Modifying
    @Query(value = "UPDATE EntidadComisionEntity e " +
            "SET e.transaccion = :transaccion, e.usuarioModificacion = :usuarioModificacion, e.fechaModificacion = current_timestamp " +
            "WHERE e.entidadComisionId = :entidadComisionId")
    Integer updateTransaccion(@Param("entidadComisionId") Long entidadComisionId,
                                         @Param("transaccion") String transaccion,
                                         @Param("usuarioModificacion") Long usuarioModificacion);

    @Modifying
    @Query(value = "UPDATE EntidadComisionEntity e " +
            "SET e.transaccion = 'INACTIVAR', e.usuarioModificacion = :usuarioModificacion, e.fechaModificacion = current_timestamp " +
            "WHERE e.entidad.entidadId = :entidadId " +
            "AND e.estado = 'ACTIVO'")
    Integer updateEntidadComisionActiva(@Param("entidadId") Long entidadId, @Param("usuarioModificacion") long usuarioModificacion);

    @Query(value = "SELECT new bo.com.tesla.administracion.dto.EntidadComisionAdmDto(" +
            "e.entidadComisionId, e.entidad.entidadId, e.tipoComision.dominioId, e.tipoComision.descripcion, e.comision, " +
            "s.login, e.fechaCreacion, e.fechaModificacion, e.estado, e.entidad.estado) " +
            "FROM EntidadComisionEntity e " +
            "INNER JOIN SegUsuarioEntity s ON s.usuarioId = e.usuarioCreacion " +
            "WHERE e.entidad.entidadId = :entidadId " +
            "ORDER BY e.fechaCreacion DESC")
    List<EntidadComisionAdmDto> findEntidadComisionByEntidadId(@Param("entidadId") Long entidadId);

    @Query(value = "SELECT new bo.com.tesla.administracion.dto.EntidadComisionAdmDto(" +
            "e.entidadComisionId, e.entidad.entidadId, e.tipoComision.dominioId, e.tipoComision.descripcion, e.comision, " +
            "s.login, e.fechaCreacion, e.fechaModificacion, e.estado, e.entidad.estado) " +
            "FROM EntidadComisionEntity e " +
            "INNER JOIN SegUsuarioEntity s ON s.usuarioId = e.usuarioCreacion " +
            "WHERE e.entidad.entidadId = :entidadId and e.estado = 'ACTIVO'" +
            "ORDER BY e.fechaCreacion DESC")
    List<EntidadComisionAdmDto> findEntidadComisionActivoByEntidadId(@Param("entidadId") Long entidadId);


    @Query(value = "SELECT new bo.com.tesla.administracion.dto.EntidadComisionAdmDto(" +
            "e.entidadComisionId, e.entidad.entidadId, e.tipoComision.dominioId, e.tipoComision.descripcion, e.comision, " +
            "s.login, e.fechaCreacion, e.fechaModificacion, e.estado, e.entidad.estado) " +
            "FROM EntidadComisionEntity e " +
            "INNER JOIN SegUsuarioEntity s ON s.usuarioId = e.usuarioCreacion " +
            "WHERE e.entidadComisionId = :entidadComisionId")
    Optional<EntidadComisionAdmDto> findEntidadComisionDtoById(@Param("entidadComisionId") Long entidadComisionId);

    Optional<EntidadComisionEntity> findEntidadComisionEntityByEntidadAndEstado(EntidadEntity entidadEntity, String estado);

}
