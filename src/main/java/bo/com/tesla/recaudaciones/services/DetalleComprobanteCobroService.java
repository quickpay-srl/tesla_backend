package bo.com.tesla.recaudaciones.services;

import bo.com.tesla.administracion.entity.CobroClienteEntity;
import bo.com.tesla.administracion.entity.ComprobanteCobroEntity;
import bo.com.tesla.administracion.entity.DetalleComprobanteCobroEntity;
import bo.com.tesla.administracion.entity.TransaccionCobroEntity;
import bo.com.tesla.recaudaciones.dao.IDetalleComprobanteCobroDao;
import bo.com.tesla.recaudaciones.dto.DeudaClienteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Date;

@Service
public class DetalleComprobanteCobroService implements IDetalleComprobanteCobroService {

    @Autowired
    private IDetalleComprobanteCobroDao iDetalleComprobanteCobroDao;

    @Override
    public List<DetalleComprobanteCobroEntity> saveAllDetallesComprobantesCobos(List<DetalleComprobanteCobroEntity> detalleComprobanteCobroEntities) {
        return iDetalleComprobanteCobroDao.saveAll(detalleComprobanteCobroEntities);
    }

    @Override
    public DetalleComprobanteCobroEntity loadDetalleComprobanteCobroEntity(TransaccionCobroEntity transaccionCobro,
                                                                           CobroClienteEntity cobroClienteEntity,
                                                                           ComprobanteCobroEntity comprobanteCobroEntity,
                                                                           Long usuarioId) {
        DetalleComprobanteCobroEntity detalleComprobanteCobroEntity = new DetalleComprobanteCobroEntity();
        detalleComprobanteCobroEntity.setComprobanteCobroId(comprobanteCobroEntity);
        detalleComprobanteCobroEntity.setTransaccionCobroId(transaccionCobro);
        detalleComprobanteCobroEntity.setCobroClienteId(cobroClienteEntity);
        detalleComprobanteCobroEntity.setUsuarioCreacion(usuarioId);
        detalleComprobanteCobroEntity.setFechaCreacion(new Date());
        //detalleComprobanteCobroEntity.setEstado("COBRADO");//cambiar
        detalleComprobanteCobroEntity.setTransaccion("COBRAR");

        return detalleComprobanteCobroEntity;
    }

}
