package bo.com.tesla.recaudaciones.dao;

import bo.com.tesla.administracion.entity.DosificacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDosificacionDao extends JpaRepository<DosificacionEntity, Long> {

    DosificacionEntity findByDosificacionId(Long dosificacionId);
}
