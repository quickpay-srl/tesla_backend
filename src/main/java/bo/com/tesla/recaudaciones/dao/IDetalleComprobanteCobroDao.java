package bo.com.tesla.recaudaciones.dao;

import bo.com.tesla.administracion.entity.DetalleComprobanteCobroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDetalleComprobanteCobroDao extends JpaRepository<DetalleComprobanteCobroEntity, Long> {

}
