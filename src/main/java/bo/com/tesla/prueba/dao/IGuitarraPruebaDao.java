package bo.com.tesla.prueba.dao;

import bo.com.tesla.administracion.entity.ArchivoEntity;
import bo.com.tesla.prueba.entity.GuitarraPruebaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IGuitarraPruebaDao  extends JpaRepository<GuitarraPruebaEntity, Long> {

}