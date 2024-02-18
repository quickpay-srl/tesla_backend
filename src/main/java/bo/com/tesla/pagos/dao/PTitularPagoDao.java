package bo.com.tesla.pagos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bo.com.tesla.administracion.entity.PTitularPagoEntity;

@Repository
public interface PTitularPagoDao extends JpaRepository<PTitularPagoEntity, Long>{

}
