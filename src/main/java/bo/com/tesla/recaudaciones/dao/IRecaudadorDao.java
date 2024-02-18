package bo.com.tesla.recaudaciones.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bo.com.tesla.administracion.dto.RecaudadorAdmDto;
import bo.com.tesla.administracion.entity.RecaudadorEntity;

@Repository
public interface IRecaudadorDao extends JpaRepository <RecaudadorEntity, Long> {

    /************************ABM****************************/

    @Modifying
    @Query(value = "UPDATE RecaudadorEntity e " +
            "SET e.transaccion = :transaccion, e.usuarioModificacion = :usuarioModificacion, e.fechaModificacion = current_timestamp " +
            "WHERE e.recaudadorId = :recaudadoraId")
    Integer updateTransaccionRecaudadora(@Param("recaudadoraId") Long entidadId,
                                      @Param("transaccion") String transaccion,
                                      @Param("usuarioModificacion") Long usuarioModificacion);

    @Modifying
    @Query(value = "UPDATE RecaudadorEntity e " +
            "SET e.transaccion = :transaccion, e.usuarioModificacion = :usuarioModificacion, e.fechaModificacion = current_timestamp " +//:fechaModificacion  " +
            "WHERE e.recaudadorId IN :recaudadoraIdLst")
    Integer updateLstTransaccionRecaudadora(@Param("recaudadoraIdLst") List<Long> recaudadoraIdLst,
                                         @Param("transaccion") String transaccion,
                                         @Param("usuarioModificacion") Long usuarioModificacion);

    @Query(value = "SELECT new bo.com.tesla.administracion.dto.RecaudadorAdmDto(" +
            "r.recaudadorId, r.tipoRecaudador.dominioId, r.tipoRecaudador.descripcion, " +
            "r.nombre, r.direccion, r.telefono, s.login, r.fechaCreacion, r.estado) " +
            "FROM RecaudadorEntity r " +
            "INNER JOIN SegUsuarioEntity s ON s.usuarioId = r.usuarioCreacion " +
            "WHERE r.estado <> 'ELIMINADO' and r.nombre!='-'")
    List<RecaudadorAdmDto> findRecaudadorDtoAll();

    @Query(value = "SELECT new bo.com.tesla.administracion.dto.RecaudadorAdmDto( " +
            "r.recaudadorId, r.tipoRecaudador.dominioId, r.tipoRecaudador.descripcion, " +
            "r.nombre, r.direccion, r.telefono, s.login, r.fechaCreacion, r.estado ) " +
            "FROM RecaudadorEntity r " +
            "INNER JOIN SegUsuarioEntity s ON s.usuarioId = r.usuarioCreacion " +
            "WHERE r.recaudadorId = :recaudadorId")
    Optional<RecaudadorAdmDto> findRecaudadorDtoById(@Param("recaudadorId") Long recaudadorId);


    @Query(value = "SELECT new bo.com.tesla.administracion.dto.RecaudadorAdmDto( " +
            "er.recaudador.recaudadorId, er.recaudador.tipoRecaudador.dominioId, er.recaudador.tipoRecaudador.descripcion, " +
            "er.recaudador.nombre, er.recaudador.direccion, er.recaudador.telefono, s.login, er.recaudador.fechaCreacion, er.recaudador.estado ) " +
            "FROM EntidadRecaudadorEntity er " +
            "INNER JOIN SegUsuarioEntity s ON s.usuarioId = er.recaudador.usuarioCreacion " +
            "WHERE er.entidad.entidadId = :entidadId " +
            "AND er.recaudador.estado <> 'ELIMINADO'")
    List<RecaudadorAdmDto> findRecaudadorDtoByEntidadId(@Param("entidadId") Long entidadId);


    /************************COBROS****************************/

    @Query(value = "select e.sucursalId.recaudador "
            + " from SegUsuarioEntity u "
            + " inner join PersonaEntity  p on p.personaId = u.personaId.personaId "
            + " inner join EmpleadoEntity e on e.personaId.personaId = p.personaId "
            + " where u.estado = 'ACTIVO' "
            + " and e.sucursalId.estado = 'ACTIVO' "
            + " and e.sucursalId.recaudador.estado = 'ACTIVO'"
            + " and p.estado = 'ACTIVO' "
            + " and u.usuarioId=:usuarioId")
    Optional<RecaudadorEntity> findRecaudadorByUserId(@Param("usuarioId") Long usuarioId);
    
    @Query("Select r from RecaudadorEntity r where r.recaudadorId= :recaudadorId and r.estado='ACTIVO'")
	public RecaudadorEntity findByRecaudadorId(@Param("recaudadorId") Long recaudadorId);

	@Query(" Select r " 
			+ " from RecaudadorEntity r "
			+ " left join EntidadRecaudadorEntity er on er.recaudador.recaudadorId=r.recaudadorId "
			+ " left join EntidadEntity e on e.entidadId=er.entidad.entidadId " 
			+ " Where " 
			+ " r.estado='ACTIVO' "
			+ " and er.estado='ACTIVO' "
			+ " and e.entidadId= :entidadId")
	public List<RecaudadorEntity> findRecaudadoresByEntidadId(@Param("entidadId") Long entidadId);

	@Query(" Select r " 
			+ " from RecaudadorEntity r "
			+ " Where r.estado='ACTIVO' and r.nombre!='-' ")
	public List<RecaudadorEntity> findAllRecaudadora();

	@Query(" Select r "
	 		+ " from RecaudadorEntity r "
	 		+ " where r.estado='ACTIVO' ")
	public  List<RecaudadorEntity> findAll();

}