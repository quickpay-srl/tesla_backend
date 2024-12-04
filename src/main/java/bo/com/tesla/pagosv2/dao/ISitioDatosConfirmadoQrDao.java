package bo.com.tesla.pagosv2.dao;

import bo.com.tesla.administracion.entity.DatosConfirmadoQrEntity;
import bo.com.tesla.administracion.entity.DeudaClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISitioDatosConfirmadoQrDao extends JpaRepository<DatosConfirmadoQrEntity,Long> {

    @Query("select d "
            + " from DatosConfirmadoQrEntity d "
            + " where d.estado = 'ACTIVO' "
            + " and d.aliasSip =:alias ")
    Optional<DatosConfirmadoQrEntity> findByAlias(@Param("alias") String alias);

    @Query("select d "
            + " from DatosConfirmadoQrEntity d "
            + " where d.estado = 'ACTIVO' "
            + " and d.aliasSip =:alias order by d.datosconfirmadoQrId  desc ")
    List<DatosConfirmadoQrEntity> buscarByAlias(@Param("alias") String alias);
}
