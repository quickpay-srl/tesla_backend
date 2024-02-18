package bo.com.tesla.pagos.services;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import bo.com.tesla.administracion.entity.PTransaccionPagoEntity;
import bo.com.tesla.pagos.dto.PPagosDto;

public interface IPTransaccionPagoService {
	
	public PTransaccionPagoEntity save(PTransaccionPagoEntity entity);
	
	public PTransaccionPagoEntity saveForPagoAbonado(PPagosDto beneficiario, Long usuarioId, Long secuencialTransaccion,Date fechaTransaccion);
	
	public Long getSecuencialTransaccion();
	
	public Page<PPagosDto>  findTransaccionsByUsuario(
			 Date fechaIni, 
			 Date fechaFin,
			 Long usuarioId,
			 String param,
			 Long servicioProductoId,
			 int page, int size
			);
	
	public List<PTransaccionPagoEntity>  transaccionessByCodigoTransacciones(String codigoTransaccion);

}
