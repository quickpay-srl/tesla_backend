package bo.com.tesla.recaudaciones.services;

import bo.com.tesla.administracion.entity.CobroClienteEntity;
import bo.com.tesla.administracion.entity.ComprobanteCobroEntity;
import bo.com.tesla.administracion.entity.DetalleComprobanteCobroEntity;
import bo.com.tesla.recaudaciones.dto.ServicioDeudaDto;

import java.math.BigDecimal;
import java.util.List;

public interface IComprobanteCobroService {

    public ComprobanteCobroEntity saveComprobanteCobro(ComprobanteCobroEntity comprobanteCobroEntity);
    public List<ComprobanteCobroEntity> saveAllComprobanteCobro(List<ComprobanteCobroEntity> comprobanteCobroEntities);
    /*public ComprobanteCobroEntity loadComprobanteCobro(ServicioDeudaDto servicioDeudaDto,
                                                       Long usuarioId,
                                                       BigDecimal montoTotal,
                                                       String nombreCliente,
                                                       String nroDocumento);*/

}
