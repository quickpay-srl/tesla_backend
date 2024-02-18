package bo.com.tesla.security.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bo.com.tesla.administracion.entity.SegUsuarioEntity;

@Repository
public interface ISegUsuarioDao extends JpaRepository<SegUsuarioEntity, Long> {
	
	
	@Query("Select u  "
			+ " from SegUsuarioEntity u "
			+ " inner join PersonaEntity p on p.personaId=u.personaId.personaId "			
			+ " where u.estado ='ACTIVO' "
			+ " and p.estado='ACTIVO' "
			+ " and u.login= :login ")
	public SegUsuarioEntity findByLogin(@Param("login") String login);
	
	@Query("Select u  "
			+ " from SegUsuarioEntity u "
			+ " inner join PersonaEntity p on p.personaId=u.personaId.personaId "			
			+ " where u.estado ='ACTIVO' "
			+ " and p.estado='ACTIVO' "
			+ " and u.login= :login "
			+ " and u.bloqueado = false")
	public SegUsuarioEntity findByLoginAuthentication(@Param("login") String login);
	
	@Query(" Select u  "
			+ " from SegUsuarioEntity u "
			+ " where "
			+ " u.personaId.personaId = :personaId"
			+ " and u.estado ='ACTIVO' ")
	public Optional<SegUsuarioEntity> findByPersonaIdAndEstado(@Param("personaId") Long personaId);
	
	
}
