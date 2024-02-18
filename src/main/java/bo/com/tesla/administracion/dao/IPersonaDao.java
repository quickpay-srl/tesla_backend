package bo.com.tesla.administracion.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bo.com.tesla.administracion.dto.PersonaDto;
import bo.com.tesla.administracion.entity.PersonaEntity;

@Repository
public interface IPersonaDao extends JpaRepository<PersonaEntity, Long> {
	
	@Query("Select new bo.com.tesla.administracion.dto.PersonaDto("
			+ "				p.personaId, "
			+ "				p.nombres,p.paterno,p.materno , "
			+ "				p.direccion, "
			+ "				p.correoElectronico, "
			+ "				p.telefono, "
			+ "				p.nroDocumento, "
			+ "				p.estado, "
			+ "				c.dominioId, "
			+ "				c.descripcion, "
			+ "				e.dominioId,"
			+ "				e.descripcion,"
			+ "				p.fechaModificacion,"
			+ "				u.login,"
			+ "				u.estado,"
			+ "				u.usuarioId,"
			+ "				u.bloqueado,"
			+ "				s.sucursalId, "
			+ "				s.nombre ) "
			+ " from PersonaEntity p "
			+ " 	left join DominioEntity c on c.dominioId=p.ciudadId.dominioId "
			+ " 	left join DominioEntity e on e.dominioId=p.extensionDocumentoId.dominioId "
			+ "		left join EmpleadoEntity ep on ep.personaId.personaId=p.personaId "
			+ "		left join SegUsuarioEntity u on u.personaId.personaId=p.personaId "
			+ " 	left join SucursalEntity s on s.sucursalId=ep.sucursalId.sucursalId "			
			+ " Where "
			+ "  	p.estado !='ELIMINADO'  "
			+ "		and ep.sucursalId.recaudador.recaudadorId= :recaudadorId "
			+ "		and p.admin=false "
			+ "		and( p.nroDocumento like concat('%', :parametro,'%')  "
			+ "			or upper(p.nombres) like upper(concat('%', :parametro,'%'))	"
			+ "			or upper(p.paterno) like upper(concat('%', :parametro,'%')) "
			+ "			or upper(p.materno) like upper(concat('%', :parametro,'%')) "
			+ " 		) "
			
			+ "     and COALESCE(CAST(ep.sucursalId.sucursalId as string ),'') like  :sucursalId "
			+ " Order by p.fechaModificacion desc ")
	public Page<PersonaDto>  findPersonasByRecaudadorGrid(
			@Param("parametro") String parametro,
			@Param("sucursalId") String sucursalId,
			@Param("recaudadorId") Long recaudadorId,
			Pageable pageable);
	
	@Query("Select new bo.com.tesla.administracion.dto.PersonaDto("
			+ "				p.personaId, "
			+ "				p.nombres,p.paterno,p.materno , "
			+ "				p.direccion, "
			+ "				p.correoElectronico, "
			+ "				p.telefono, "
			+ "				p.nroDocumento, "
			+ "				p.estado, "
			+ "				c.dominioId, "
			+ "				c.descripcion, "
			+ "				e.dominioId,"
			+ "				e.descripcion,"
			+ "				p.fechaModificacion,"
			+ "				u.login,"
			+ "				u.estado,"
			+ "				u.bloqueado,"
			+ "				u.usuarioId) "
			+ " from PersonaEntity p "
			+ " 	left join DominioEntity c on c.dominioId=p.ciudadId.dominioId "
			+ " 	left join DominioEntity e on e.dominioId=p.extensionDocumentoId.dominioId "
			+ "		left join EmpleadoEntity ep on ep.personaId.personaId=p.personaId "
			+ "		left join SegUsuarioEntity u on u.personaId.personaId=p.personaId "
			+ " Where "
			+ "  	p.estado !='ELIMINADO'  "
			+ "		and ep.entidadId.entidadId= :entidadId	 "
			+ "		and p.admin=false "
			+ "		and( p.nroDocumento like concat('%', :parametro,'%')  "
			+ "			or upper(p.nombres) like upper(concat('%', :parametro,'%'))	"
			+ "			or upper(p.paterno) like upper(concat('%', :parametro,'%')) "
			+ "			or upper(p.materno) like upper(concat('%', :parametro,'%')) "
			+ " 		) "
			+ " Order by p.fechaModificacion desc ")
	public Page<PersonaDto>  findPersonasByEntidadesGrid(
			@Param("parametro") String parametro,
			@Param("entidadId") Long entidadId,	
			Pageable pageable);
	
	
	
	
	
	@Query("Select new bo.com.tesla.administracion.dto.PersonaDto("
			+ "				p.personaId, "
			+ "				p.nombres,p.paterno,p.materno , "
			+ "				p.direccion, "
			+ "				p.correoElectronico, "
			+ "				p.telefono, "
			+ "				p.nroDocumento, "
			+ "				p.estado, "
			+ "				c.dominioId, "
			+ "				c.descripcion, "
			+ "				e.dominioId,"
			+ "				e.descripcion,"
			+ "				p.fechaModificacion,"
			+ "				u.login,"
			+ "				u.estado,"
			+ "				u.usuarioId,"
			+ "				u.bloqueado,"
			+ "				em.empleadoId,"
			+ "				p.admin ) "
			+ " from PersonaEntity p "
			+ " 	left join DominioEntity c on c.dominioId=p.ciudadId.dominioId "
			+ " 	left join DominioEntity e on e.dominioId=p.extensionDocumentoId.dominioId "		
			+ "		left join SegUsuarioEntity u on u.personaId.personaId=p.personaId "
			+ "		left join EmpleadoEntity em on em.personaId.personaId= p.personaId "
			+ "		"
			+ " Where "
			+ "  	p.estado !='ELIMINADO'  "
			+ " 	and p.admin=true "
			+ "		and( p.nroDocumento like concat('%', :parametro,'%')  "
			+ "			or upper(p.nombres) like upper(concat('%', :parametro,'%'))	"
			+ "			or upper(p.paterno) like upper(concat('%', :parametro,'%')) "
			+ "			or upper(p.materno) like upper(concat('%', :parametro,'%')) "
			+ " 		) "
			+ " Order by p.fechaModificacion desc ")
	public Page<PersonaDto>  findPersonasByAdminGrid(
			@Param("parametro") String parametro,
			Pageable pageable);



	@Query("Select new bo.com.tesla.administracion.dto.PersonaDto("
			+ "				p.personaId, "
			+ "				p.nombres,p.paterno,p.materno , "
			+ "				p.direccion, "
			+ "				p.correoElectronico, "
			+ "				p.telefono, "
			+ "				p.nroDocumento, "
			+ "				p.estado, "
			+ "				c.dominioId, "
			+ "				c.descripcion, "
			+ "				e.dominioId,"
			+ "				e.descripcion,"
			+ "				p.fechaModificacion,"
			+ "				u.login,"
			+ "				u.estado,"
			+ "				u.usuarioId,"
			+ "				u.bloqueado,"
			+ "				em.empleadoId,"
			+ "				p.admin ) "
			+ " from PersonaEntity p "
			+ " 	left join DominioEntity c on c.dominioId=p.ciudadId.dominioId "
			+ " 	left join DominioEntity e on e.dominioId=p.extensionDocumentoId.dominioId "
			+ "		left join SegUsuarioEntity u on u.personaId.personaId=p.personaId "
			+ "		left join EmpleadoEntity em on em.personaId.personaId= p.personaId "
			+ "		"
			+ " Where "
			+ "  	p.estado !='ELIMINADO'  "
			+ " 	and p.admin=true "
			+ " 	and em.tipoUsuarioId.dominioId=:pTipoUsuarioId "
			+ "		and( p.nroDocumento like concat('%', :parametro,'%')  "
			+ "			or upper(p.nombres) like upper(concat('%', :parametro,'%'))	"
			+ "			or upper(p.paterno) like upper(concat('%', :parametro,'%')) "
			+ "			or upper(p.materno) like upper(concat('%', :parametro,'%')) "
			+ " 		) "
			+ " Order by p.fechaModificacion desc ")
	public Page<PersonaDto>  findPersonasSocioEmpresaByTipo(
			@Param("parametro") String parametro,
			@Param("pTipoUsuarioId") Long pTipoUsuarioId,
			Pageable pageable);
	
	
	

}
