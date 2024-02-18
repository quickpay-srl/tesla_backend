package bo.com.tesla.externos.recaudaciones.larazon.dao;

import bo.com.tesla.administracion.entity.EndPointTransaccionCobroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEndPointTransaccionCobroDao extends JpaRepository<EndPointTransaccionCobroEntity, Long> {

}
