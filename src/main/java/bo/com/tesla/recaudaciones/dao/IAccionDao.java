package bo.com.tesla.recaudaciones.dao;

import bo.com.tesla.administracion.entity.AccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAccionDao extends JpaRepository<AccionEntity, Long> {
}
