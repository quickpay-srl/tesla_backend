package bo.com.tesla.entidades.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bo.com.tesla.administracion.entity.EntidadEntity;

@Repository
public interface IEntidadDao extends JpaRepository<EntidadEntity, Long>  {

	
	@Query("select e.entidadId "
			+ " from SegUsuarioEntity u "
			+ " left join PersonaEntity  p on p.personaId=u.personaId.personaId "
			+ " left join EmpleadoEntity e on e.personaId.personaId=p.personaId  "
			+ " Where u.estado='ACTIVO' AND e.entidadId.estado='ACTIVO' AND u.usuarioId=:usuarioId")
	public EntidadEntity findEntidadByUserId(@Param("usuarioId") Long usuarioId);
	
	@Query("select e "
			+ " from RecaudadorEntity r "
			+ " left join EntidadRecaudadorEntity er on er.recaudador.recaudadorId=r.recaudadorId "
			+ " left join EntidadEntity e on e.entidadId=er.entidad.entidadId "
			+ " where "
			+ " er.estado='ACTIVO' "
			+ " and r.recaudadorId= :recaudadorId ")
	public List<EntidadEntity> findEntidadByRecaudacionId(@Param("recaudadorId") Long recaudadorId);
	
	@Query("select e "
			+ " from EntidadEntity e "
			+ " Where e.estado='ACTIVO' and e.nombre!='-'")
	public List<EntidadEntity> findAllEntidades();
	
	@Query("select e "
			+ " from EntidadEntity e "
			+ " Where e.estado='ACTIVO' "
			+ " and e.entidadId= :entidadId")
	public Optional<EntidadEntity> findEntidadById(@Param("entidadId") Long entidadId);
	
	
	
	
	
}
