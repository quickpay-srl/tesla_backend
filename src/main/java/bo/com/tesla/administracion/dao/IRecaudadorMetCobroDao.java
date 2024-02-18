package bo.com.tesla.administracion.dao;

import bo.com.tesla.administracion.dto.RecaudadorMetodoCobroDto;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.administracion.entity.RecaudadorMetodoCobroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRecaudadorMetCobroDao extends JpaRepository<RecaudadorMetodoCobroEntity, Long> {

    @Query("SELECT new bo.com.tesla.administracion.dto.RecaudadorMetodoCobroDto(" +
            "rm.metodoCobroId.dominioId, rm.metodoCobroId.descripcion) " +
            "FROM RecaudadorMetodoCobroEntity rm " +
            "WHERE rm.estado = 'ACTIVO' " +
            "AND rm.recaudadorId.recaudadorId = :recaudadorId ")
    List<RecaudadorMetodoCobroDto> findRecMetCobroDtoByRecaudador(@Param("recaudadorId") Long recaudadorId);

    @Query("SELECT rm.metodoCobroId.descripcion " +
            "FROM RecaudadorMetodoCobroEntity rm " +
            "WHERE rm.estado = 'ACTIVO' " +
            "AND rm.recaudadorId.recaudadorId = :recaudadorId ")
    List<String> findRecMetCobroLSt(@Param("recaudadorId") Long recaudadorId);

    @Query("SELECT rm.metodoCobroId.dominioId " +
            "FROM RecaudadorMetodoCobroEntity rm " +
            "WHERE rm.estado = 'ACTIVO' " +
            "AND rm.recaudadorId.recaudadorId = :recaudadorId ")
    List<Long> findRecMetCobroIdst(@Param("recaudadorId") Long recaudadorId);

    @Modifying
    @Query("UPDATE RecaudadorMetodoCobroEntity rm " +
            "SET rm.transaccion = :transaccion, rm.usuarioModificacion = :usuarioId, rm.fechaModificacion = CURRENT_TIMESTAMP " +
            "WHERE rm.recaudadorId = :recaudador " +
            "AND rm.estado = 'ACTIVO'")
    void updateEstado(@Param("recaudador") RecaudadorEntity recaudador,
                      @Param("transaccion") String transaccion,
                      @Param("usuarioId") Long usuarioId);
}
