package bo.com.tesla.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bo.com.tesla.administracion.entity.LogSistemaEntity;

@Repository
public interface ILogSistemasDao  extends JpaRepository<LogSistemaEntity, Long>{

}
