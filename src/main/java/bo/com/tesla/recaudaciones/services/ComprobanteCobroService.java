package bo.com.tesla.recaudaciones.services;

import bo.com.tesla.administracion.entity.*;
import bo.com.tesla.entidades.dao.IEntidadDao;
import bo.com.tesla.entidades.dto.DeudasClienteDto;
import bo.com.tesla.recaudaciones.dao.IComprobanteCobroDao;
import bo.com.tesla.recaudaciones.dao.IDosificacionDao;
import bo.com.tesla.recaudaciones.dao.IEntidadRDao;
import bo.com.tesla.recaudaciones.dao.ISucursalDao;
import bo.com.tesla.recaudaciones.dto.DeudaClienteDto;
import bo.com.tesla.recaudaciones.dto.ServicioDeudaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class ComprobanteCobroService implements IComprobanteCobroService {

    @Autowired
    private IComprobanteCobroDao iComprobanteCobroDao;

    @Autowired
    private ISucursalDao iSucursalDao;

    @Autowired
    private IEntidadRDao iEntidadRDao;

    @Autowired
    private IDosificacionDao iDosificacionDao;

    @Override
    public ComprobanteCobroEntity saveComprobanteCobro(ComprobanteCobroEntity comprobanteCobroEntity) {
        return iComprobanteCobroDao.save(comprobanteCobroEntity);
    }

    @Override
    public List<ComprobanteCobroEntity> saveAllComprobanteCobro(List<ComprobanteCobroEntity> comprobanteCobroEntities) {
        return iComprobanteCobroDao.saveAll(comprobanteCobroEntities);
    }

    /*
    @Override
    public ComprobanteCobroEntity loadComprobanteCobro(ServicioDeudaDto servicioDeudaDto,
                                                       Long usuarioId,
                                                       BigDecimal montoTotal,
                                                       String nombreCliente,
                                                       String nroDocumento) {

        Optional<EntidadEntity> optionalEntidadEntity = iEntidadRDao.findByEntidadId(servicioDeudaDto.entidadId);//debe considerar estado??
        if(!optionalEntidadEntity.isPresent())
            return null;

        Optional<SucursalEntity> optionalSucursalEntity = iSucursalDao.findSucursalByUserId(usuarioId);
        if(!optionalSucursalEntity.isPresent())
            return null;

        DosificacionEntity dosificacionEntity = iDosificacionDao.findByDosificacionId(2L); //Se debe modiicar para facturacion

        ComprobanteCobroEntity comprobanteCobroEntity = new ComprobanteCobroEntity();
        comprobanteCobroEntity.setEntidadId(optionalEntidadEntity.get());
        comprobanteCobroEntity.setSucursalId(optionalSucursalEntity.get());
        comprobanteCobroEntity.setDosificacionId(dosificacionEntity);
        comprobanteCobroEntity.setNroFactura(1L);//arrglar con facturacion
        comprobanteCobroEntity.setFechaFactura(new Date());//Facturacion
        comprobanteCobroEntity.setNitCliente(nroDocumento);
        comprobanteCobroEntity.setNombreCliente(nombreCliente);
        comprobanteCobroEntity.setMontoTotal(montoTotal);
        comprobanteCobroEntity.setCodigoControl("AA-AA-AA-AA");//Facturacion
        comprobanteCobroEntity.setUsuarioCreacion(usuarioId);
        comprobanteCobroEntity.setFechaCreacion(new Date());
        //comprobanteCobroEntity.setEstado("COBRADO");
        comprobanteCobroEntity.setTransaccion("COBRAR");

        return comprobanteCobroEntity;
    }
*/

}
