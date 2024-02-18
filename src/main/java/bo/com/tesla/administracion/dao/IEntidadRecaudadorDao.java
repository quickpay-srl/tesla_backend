package bo.com.tesla.administracion.dao;

import bo.com.tesla.administracion.dto.RecaudadorAdmDto;
import bo.com.tesla.administracion.entity.EntidadRecaudadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface IEntidadRecaudadorDao extends JpaRepository<EntidadRecaudadorEntity, Long> {

    @Query(value = "SELECT er " +
            "FROM EntidadRecaudadorEntity er " +
            "WHERE er.recaudador.recaudadorId = :recaudadorId " +
            "AND er.entidad.entidadId = :entidadId ")
    Optional<EntidadRecaudadorEntity> getByEntidadIdAndRecaudadorId(@Param("entidadId") Long entidadId,
                                                                    @Param("recaudadorId") Long recaudadorId);


    /*********************ABM por Recaudador**********************/

    @Query(value = "SELECT er.entidad.entidadId " +
            "FROM EntidadRecaudadorEntity er " +
            "WHERE er.recaudador.recaudadorId = :recaudadorId " +
            "AND er.estado = 'ACTIVO' " +
            "AND er.recaudador.estado in ('ACTIVO', 'CREADO') " +
            "AND er.entidad.estado in ('ACTIVO', 'CREADO')")
    List<Long> getLstByRecaudadorIdActivo(@Param("recaudadorId") Long recaudadorId);

    @Modifying
    @Query(value = "UPDATE EntidadRecaudadorEntity er " +
            "SET er.transaccion = :transaccion, er.usuarioModificacion = :usuarioModificacion, er.fechaModificacion = current_timestamp " +
            "WHERE er.recaudador.recaudadorId = :recaudadorId " +
            "AND er.entidad.entidadId IN :entidadIdLst " +
            "AND er.estado = 'ACTIVO'")
    Integer updateLstTransaccionByEntidadrIdActivo(@Param("recaudadorId") Long recaudadorId,
                                                     @Param("entidadIdLst") List<Long> entidadIdLst,
                                                     @Param("transaccion") String transaccion,
                                                     @Param("usuarioModificacion") Long usuarioModificacion);

    @Query("SELECT er " +
            "FROM EntidadRecaudadorEntity er " +
            "WHERE er.recaudador.recaudadorId = :recaudadorId " +
            "AND er.entidad.entidadId = :entidadId " +
            "AND er.estado = 'ACTIVO' " +
            "AND er.recaudador.estado = 'ACTIVO' " +
            "AND er.entidad.estado = 'ACTIVO'")
    List<EntidadRecaudadorEntity> getLstByRecaudadorAndEntidad(@Param("recaudadorId") Long recaudadorId,
                                           @Param("entidadId") Long entidadId);

    /*********************ABM por Entidad**********************/


    @Query(value = "SELECT er.recaudador.recaudadorId " +
            "FROM EntidadRecaudadorEntity er " +
            "WHERE er.entidad.entidadId = :entidadId " +
            "AND er.estado = 'ACTIVO' " +
            "AND er.recaudador.estado in ('ACTIVO', 'CREADO') " +
            "AND er.entidad.estado in ('ACTIVO', 'CREADO')")
    List<Long> getLstByEntidadIdActivo(@Param("entidadId") Long entidadId);


    @Modifying
    @Query(value = "UPDATE EntidadRecaudadorEntity er " +
            "SET er.transaccion = :transaccion, er.usuarioModificacion = :usuarioModificacion, er.fechaModificacion = current_timestamp " +
            "WHERE er.recaudador.recaudadorId IN :recaudadorIdLst " +
            "AND er.entidad.entidadId = :entidadId " +
            "AND er.estado = 'ACTIVO'")
    Integer updateLstTransaccionByRecaudadorIdActivo(@Param("entidadId") Long entidadId,
                              @Param("recaudadorIdLst") List<Long> recaudadorIdLst,
                              @Param("transaccion") String transaccion,
                              @Param("usuarioModificacion") Long usuarioModificacion);


}


