package bo.com.tesla.security.dao;

import bo.com.tesla.administracion.entity.SegTransicionEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

@Repository
public interface ISegTransicionDao extends JpaRepository<SegTransicionEntity, Long> {

    @Query(value = "SELECT count(s) " +
            "FROM SegTransicionEntity s " +
            "WHERE s.segTransaccionEntity.estado = 'ACTIVO' " +
            //"AND s.segTransaccionEntity.segTablaEntity.tablaId = :tabla " +
            //"AND s.segTransaccionEntity.segTablaEntity.estado = 'ACTIVO' " +
            "AND s.segTransicionEntityPK.transaccionId = :transaccion " +
            "AND s.segTransicionEntityPK.tablaId = :tabla " +
            "AND s.segTransicionEntityPK.estadoInicial = :estadoInicial " +
            "AND s.estado = 'ACTIVO'")
    Long countByTablaAndTransaccion(@Param("tabla") String tabla, @Param("transaccion") String transaccion, @Param("estadoInicial") String estadoInicial);

    
    
    @Query(value = "SELECT t.* "
    		+ " FROM tesla.seg_transiciones t "
    		+ " left join tesla.seg_privilegios_roles_transiciones prt on ( prt.tabla_id=t.tabla_id "
    		+ "															 and prt.estado_inicial_id=t.estado_inicial "
    		+ "															 and prt.transaccion_id=t.transaccion_id) "
    		+ " left join tesla.seg_privilegios_roles pr  on pr.privilegio_rol_id=prt.privilegio_rol_id "
    		+ " left join tesla.seg_roles r on r.rol_id=pr.rol_id "
    		+ " WHERE t.tabla_id= :tablaId "
    		+ " and t.estado_inicial= :estadoInicial "
    		+ " and prt.estado='ACTIVO' "
    		+ " and t.estado='ACTIVO' "
    		+ " and r.rol_id= :rolId ",nativeQuery = true)
    public List<Object[]> getOperaciones(
    		@Param("tablaId") String tablaId, 
    		@Param("estadoInicial") String estadoInicial, 
    		@Param("rolId") Long rolId);

}
