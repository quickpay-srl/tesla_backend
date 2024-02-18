package bo.com.tesla.administracion.dao;

import bo.com.tesla.administracion.dto.DashboardDto;
import bo.com.tesla.administracion.entity.TransaccionCobroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IDashboardDao extends JpaRepository<TransaccionCobroEntity, Long>  {
    @Query(value = "select * from tesla.fn_dashboard(:usuarioId)", nativeQuery = true)
    DashboardDto findDashboard(@Param("usuarioId") Long usuarioId);
}
