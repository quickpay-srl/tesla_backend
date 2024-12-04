package bo.com.tesla.pagosv2.dao;

import bo.com.tesla.administracion.entity.DatosConfirmadoQrEntity;
import bo.com.tesla.administracion.entity.LogPagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILogPagoDao extends JpaRepository<LogPagoEntity,Long> {
}
