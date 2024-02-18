package bo.com.tesla.pagos.services;

import java.util.List;

import bo.com.tesla.administracion.entity.PPagoClienteEntity;
import bo.com.tesla.administracion.entity.PTransaccionPagoEntity;
import bo.com.tesla.pagos.dto.PPagosDto;

public interface IPPagoClienteService {
	
	public PPagoClienteEntity save(PPagoClienteEntity entity);
	
	public  List<PTransaccionPagoEntity> realizarPago(List<PPagosDto> abonoCliente,Long usuarioId);
	
	public byte[] imprimirReporte(List<PTransaccionPagoEntity> transaccionPagoList);
	

}
