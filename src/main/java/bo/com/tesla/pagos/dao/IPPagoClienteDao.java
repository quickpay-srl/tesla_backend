package bo.com.tesla.pagos.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bo.com.tesla.administracion.entity.PPagoClienteEntity;

@Repository
public interface IPPagoClienteDao extends JpaRepository<PPagoClienteEntity, Long> {

	
	@Query("Select p "
			+ " from PPagoClienteEntity p "
			+ " where p.transaccionPagoId.transaccionPagoId= :transaccionPagoId "
			+ " Order by p.nroRegistro, p.periodo ")
	public List<PPagoClienteEntity>  findByTransaccionPagoId(
			@Param("transaccionPagoId") Long transaccionPagoId);
	
}
