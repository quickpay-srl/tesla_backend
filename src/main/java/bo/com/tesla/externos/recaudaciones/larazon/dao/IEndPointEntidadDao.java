package bo.com.tesla.externos.recaudaciones.larazon.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bo.com.tesla.administracion.entity.EndPointEntidadEntity;

@Repository
public interface IEndPointEntidadDao extends JpaRepository<EndPointEntidadEntity, Long>{
	
	 @Query(value = "Select e "
	 		+ " From EndPointEntidadEntity e "
	 		+ " where e.entidadId.entidadId= :entidadId "
			+  "AND e.estado = 'ACTIVO'")
	 Optional<EndPointEntidadEntity> findByEntidadId(@Param("entidadId") Long entidadId);


}
