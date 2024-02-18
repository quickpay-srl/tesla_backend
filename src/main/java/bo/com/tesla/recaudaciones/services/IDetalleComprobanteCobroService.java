package bo.com.tesla.recaudaciones.services;

import bo.com.tesla.administracion.entity.CobroClienteEntity;
import bo.com.tesla.administracion.entity.ComprobanteCobroEntity;
import bo.com.tesla.administracion.entity.DetalleComprobanteCobroEntity;
import bo.com.tesla.administracion.entity.TransaccionCobroEntity;
import bo.com.tesla.recaudaciones.dto.DeudaClienteDto;

import java.util.List;

public interface IDetalleComprobanteCobroService {

    public List<DetalleComprobanteCobroEntity> saveAllDetallesComprobantesCobos(List<DetalleComprobanteCobroEntity> detalleComprobanteCobroEntities);
    public DetalleComprobanteCobroEntity loadDetalleComprobanteCobroEntity(TransaccionCobroEntity transaccionCobro,
                                                                           CobroClienteEntity cobroClienteEntity,
                                                                           ComprobanteCobroEntity comprobanteCobroEntity,
                                                                           Long usuarioId) ;
}
