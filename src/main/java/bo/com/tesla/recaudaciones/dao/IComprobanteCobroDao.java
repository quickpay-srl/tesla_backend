package bo.com.tesla.recaudaciones.dao;

import bo.com.tesla.administracion.entity.ComprobanteCobroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IComprobanteCobroDao extends JpaRepository<ComprobanteCobroEntity, Long> {

}
