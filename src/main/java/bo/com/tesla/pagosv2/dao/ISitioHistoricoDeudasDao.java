package bo.com.tesla.pagosv2.dao;

import bo.com.tesla.administracion.entity.DatosConfirmadoQrEntity;
import bo.com.tesla.administracion.entity.DeudaClienteEntity;
import bo.com.tesla.administracion.entity.HistoricoDeudaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISitioHistoricoDeudasDao extends JpaRepository<HistoricoDeudaEntity,Long> {
    @Query("select d "
            + " from HistoricoDeudaEntity d "
            + " where d.archivoId.estado = 'ACTIVO' "
            + " and d.deudaClienteId =:deudaClienteId ")
    Optional<HistoricoDeudaEntity> findForDeudaClienteId(@Param("deudaClienteId") Long deudaClienteId);

}
