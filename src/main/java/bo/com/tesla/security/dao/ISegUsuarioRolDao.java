package bo.com.tesla.security.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bo.com.tesla.administracion.entity.SegUsuarioRolEntity;

@Repository
public interface ISegUsuarioRolDao  extends JpaRepository<SegUsuarioRolEntity, Long>{
	
	
	@Query(" Select ur"
			+ " from SegUsuarioRolEntity ur "
			+ "  Where ur.rolId.rolId = :rolId"
			+ "  and ur.usuarioId.usuarioId =:usuarioId")
	public Optional<SegUsuarioRolEntity> findByRolIdAndUsuarioId(
			@Param("usuarioId") Long usuarioId,
			@Param("rolId") Long rolId
			);
	

}
