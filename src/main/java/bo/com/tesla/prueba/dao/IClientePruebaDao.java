package bo.com.tesla.prueba.dao;

import bo.com.tesla.prueba.entity.ClientePruebaEntity;
import bo.com.tesla.prueba.entity.GuitarraPruebaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClientePruebaDao extends JpaRepository<ClientePruebaEntity, Long> {

}