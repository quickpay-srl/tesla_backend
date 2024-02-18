package bo.com.tesla.recaudaciones.dao;

import bo.com.tesla.administracion.dto.EntidadAdmDto;
import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.recaudaciones.dto.DominioDto;
import bo.com.tesla.recaudaciones.dto.EntidadDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface IEntidadRDao extends JpaRepository<EntidadEntity, Long> {


    /*********************PARA ABM****************************/

    @Modifying
    @Query(value = "UPDATE EntidadEntity e " +
            "SET e.transaccion = :transaccion, e.usuarioModificacion = :usuarioModificacion, e.fechaModificacion = current_timestamp " +//:fechaModificacion  " +
            "WHERE e.entidadId = :entidadId")
    Integer updateTransaccionEntidad(@Param("entidadId") Long entidadId,
                                  @Param("transaccion") String transaccion,
                                  @Param("usuarioModificacion") Long usuarioModificacion);

    @Modifying
    @Query(value = "UPDATE EntidadEntity e " +
            "SET e.transaccion = :transaccion, e.usuarioModificacion = :usuarioModificacion, e.fechaModificacion = current_timestamp " +//:fechaModificacion  " +
            "WHERE e.entidadId IN :entidadIdLst")
    Integer updateLstTransaccionEntidad(@Param("entidadIdLst") List<Long> entidadIdLst,
                                     @Param("transaccion") String transaccion,
                                     @Param("usuarioModificacion") Long usuarioModificacion);
   /* @Modifying
    @Query(value = "UPDATE EntidadEntity e " +
            "SET e.estado = :estado, e.transaccion= 'INACTIVAR', e.usuarioModificacion = :usuarioModificacion, e.fechaModificacion = current_timestamp " +//:fechaModificacion  " +
            "WHERE e.entidadId IN :entidadIdLst")
    Integer updateLstEstadoEntidad(@Param("entidadIdLst") List<Long> entidadIdLst,
                                        @Param("estado") String estado,
                                        @Param("usuarioModificacion") Long usuarioModificacion);*/

    @Query(value = "SELECT new bo.com.tesla.administracion.dto.EntidadAdmDto(" +
            "e.entidadId, e.nombre, e.nombreComercial, e.direccion, e.telefono, e.nit, " +
            "e.pathLogo, e.comprobanteEnUno, e.actividadEconomica.dominioId, e.actividadEconomica.descripcion, " +
            "e.tipoEntidad.dominioId, e.tipoEntidad.descripcion, " +
            "e.modalidadFacturacion.dominioId, e.modalidadFacturacion.descripcion, e.esCobradora, e.esPagadora, " +
            "e.fechaCreacion, s.login, e.estado) " +
            "FROM EntidadEntity e " +
            "INNER JOIN SegUsuarioEntity s ON s.usuarioId = e.usuarioCreacion " +
            "WHERE e.estado <> 'ELIMINADO' and e.nombre != '-'" )
    List<EntidadAdmDto> findEntidadesDtoAll();

    @Query(value = "SELECT new bo.com.tesla.administracion.dto.EntidadAdmDto(" +
            "e.entidadId, e.nombre, e.nombreComercial, e.direccion, e.telefono, e.nit, " +
            "e.pathLogo, e.comprobanteEnUno, e.actividadEconomica.dominioId, e.actividadEconomica.descripcion, " +
            "e.tipoEntidad.dominioId, e.tipoEntidad.descripcion, " +
            "e.modalidadFacturacion.dominioId, e.modalidadFacturacion.descripcion, e.esCobradora, e.esPagadora, " +
            "e.fechaCreacion, s.login, e.estado) " +
            "FROM EntidadEntity e " +
            "INNER JOIN SegUsuarioEntity s ON s.usuarioId = e.usuarioCreacion " +
            "WHERE e.entidadId = :entidadId")
    Optional<EntidadAdmDto> findEntidadDtoById(@Param("entidadId") Long entidadId);


    @Query(value = "SELECT new bo.com.tesla.administracion.dto.EntidadAdmDto(" +
            "er.entidad.entidadId, er.entidad.nombre, er.entidad.nombreComercial, er.entidad.direccion, er.entidad.telefono, er.entidad.nit, " +
            "er.entidad.pathLogo, er.entidad.comprobanteEnUno, er.entidad.actividadEconomica.dominioId, er.entidad.actividadEconomica.descripcion, " +
            "er.entidad.tipoEntidad.dominioId, er.entidad.tipoEntidad.descripcion, " +
            "er.entidad.modalidadFacturacion.dominioId, er.entidad.modalidadFacturacion.descripcion, er.entidad.esCobradora, er.entidad.esPagadora, " +
            "er.entidad.fechaCreacion, s.login, er.entidad.estado) " +
            "FROM EntidadRecaudadorEntity er " +
            "INNER JOIN SegUsuarioEntity s ON s.usuarioId = er.entidad.usuarioCreacion " +
            "WHERE er.recaudador.recaudadorId = :recaudadorId " +
            "AND er.entidad.estado <> 'ELIMINADO' ")
    List<EntidadAdmDto> findEntidadesDtoByRecaudadorId(@Param("recaudadorId") Long recaudadorId);

    @Modifying
    @Query(value = "UPDATE EntidadEntity e " +
            "SET e.pathLogo = :pathLogo, e.transaccion = :transaccion, e.usuarioModificacion = :usuarioModificacion, e.fechaModificacion = current_timestamp " +
            "WHERE e.entidadId = :entidadId " +
            "AND e.estado NOT IN ('ELIMINADO', 'INACTIVO')")
    Integer updatePathLogo(@Param("entidadId") Long entidadId,
                           @Param("pathLogo") String pathLogo,
                           @Param("transaccion") String transaccion,
                           @Param("usuarioModificacion") Long usuarioModificacion);

    /*********************PARA COBROS****************************/

    Optional<EntidadEntity> findByEntidadIdAndEstado(Long entidad_id, String estado);

    @Query(value = "select new bo.com.tesla.recaudaciones.dto.EntidadDto(" +
            " er.entidad.entidadId, er.entidad.nombre, er.entidad.nombreComercial, er.entidad.pathLogo) "+
            " from EntidadRecaudadorEntity er " +
            " where er.recaudador.recaudadorId = :pRecaudadorId " +
            " and er.entidad.tipoEntidad.dominioId = :pTipoEntidadId " +
            " and er.entidad.estado = 'ACTIVO' " +
            " and er.recaudador.estado = 'ACTIVO' " +
            " and er.estado = 'ACTIVO' " +
            " and er.entidad.esCobradora = true")
    List<EntidadDto> findByRecaudadoraIdAndTipoEntidadId(@Param("pRecaudadorId") Long pRecaudadorId, @Param("pTipoEntidadId") Long pTipoEntidadId);

    @Query(value = "select new bo.com.tesla.recaudaciones.dto.EntidadDto(" +
            " er.entidad.entidadId, er.entidad.nombre, er.entidad.nombreComercial, er.entidad.pathLogo) "+
            " from EntidadRecaudadorEntity er " +
            " where er.recaudador.recaudadorId = :pRecaudadorId " +
            " and er.entidad.estado = 'ACTIVO' " +
            " and er.recaudador.estado = 'ACTIVO' " +
            " and er.estado = 'ACTIVO' " +
            " and er.entidad.esCobradora = true")
    List<EntidadDto> findByRecaudadoraId(@Param("pRecaudadorId") Long pRecaudadorId);

    Optional<EntidadEntity> findByEntidadId(Long entidadId);
}

