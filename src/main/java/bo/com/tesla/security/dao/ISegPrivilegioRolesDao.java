package bo.com.tesla.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bo.com.tesla.administracion.entity.SegPrivilegioRolEntity;

@Repository
public interface ISegPrivilegioRolesDao extends JpaRepository<SegPrivilegioRolEntity, Long>{

}
