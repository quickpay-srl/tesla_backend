package bo.com.tesla.administracion.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bo.com.tesla.administracion.entity.EmpleadoEntity;

@Repository
public interface IEmpleadoDao extends JpaRepository<EmpleadoEntity, Long> {

	@Query("Select e " 
			+ " from EmpleadoEntity e " 
			+ " where e.personaId.personaId= :personaId "
			+ " and e.sucursalId.sucursalId= :sucursalId "
			)
	public Optional<EmpleadoEntity> findByPersonaIdAndSucursalIdSucursalId(
			@Param("personaId") Long personaId,
			@Param("sucursalId") Long sucursalId);
	
	
	@Query("Select e "
			+ " from EmpleadoEntity e"
			+ " where e.empleadoId= :empleadoId")
	public Optional<EmpleadoEntity>  findEmpleadosById(@Param("empleadoId") Long empleadoId);
	
	
	@Query("Select e "
			+ " from EmpleadoEntity e"
			+ " where e.personaId.personaId= :personaId "
			)
	public Optional<EmpleadoEntity>  findEmpleadosByPersonaId(@Param("personaId") Long personaId);
	
	
	
	

}
