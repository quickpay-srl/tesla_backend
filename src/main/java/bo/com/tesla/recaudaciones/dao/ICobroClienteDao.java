package bo.com.tesla.recaudaciones.dao;

import bo.com.tesla.administracion.entity.CobroClienteEntity;
import bo.com.tesla.administracion.entity.SegEstadoEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICobroClienteDao extends JpaRepository<CobroClienteEntity, Long> {

    @Modifying
    @Query("UPDATE CobroClienteEntity t " +
            "SET t.transaccion = :transaccion, t.usuarioModificacion = :usuarioModificacionId, t.fechaModificacion = CURRENT_TIMESTAMP " +
            "WHERE t.transaccionCobro in (SELECT t " +
                                        "FROM TransaccionCobroEntity t " +
                                        "WHERE t.facturaId in :facturaIdLst )")
    Integer updateLstTransaccionByFacturas(@Param("facturaIdLst") List<Long> facturaIdLst,
                                           @Param("transaccion") String transaccion,
                                           @Param("usuarioModificacionId") Long usuarioModificacionId);

    @Query("SELECT c " +
            "FROM CobroClienteEntity c  " +
            "WHERE c.transaccionCobro.facturaId in :facturaIdLst")
    List<CobroClienteEntity> findByFacturas(@Param("facturaIdLst") List<Long> facturaIdLst);

}
