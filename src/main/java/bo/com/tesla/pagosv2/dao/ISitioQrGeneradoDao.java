package bo.com.tesla.pagosv2.dao;

import bo.com.tesla.administracion.entity.DatosConfirmadoQrEntity;
import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.QRGeneradoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISitioQrGeneradoDao extends JpaRepository<QRGeneradoEntity,Long> {
    @Query("select d "
            + " from QRGeneradoEntity d "
            + " where d.estado = 'ACTIVO' "
            + " and d.alias =:alias ")
    List<QRGeneradoEntity> findByAlias(@Param("alias") String alias);



}
