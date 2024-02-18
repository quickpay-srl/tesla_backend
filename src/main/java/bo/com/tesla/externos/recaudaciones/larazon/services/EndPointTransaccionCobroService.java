package bo.com.tesla.externos.recaudaciones.larazon.services;

import bo.com.tesla.administracion.entity.EndPointEntidadEntity;
import bo.com.tesla.administracion.entity.EndPointTransaccionCobroEntity;
import bo.com.tesla.administracion.entity.TransaccionCobroEntity;
import bo.com.tesla.externos.recaudaciones.larazon.dao.IEndPointTransaccionCobroDao;
import bo.com.tesla.externos.recaudaciones.larazon.dto.ExceptionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class EndPointTransaccionCobroService implements IEndPointTransaccionCobroService {

    @Autowired
    private IEndPointTransaccionCobroDao endPointTransaccionCobroDao;

    @Override
    public void addEnPointTransaccionCobro(EndPointEntidadEntity endPointEntidadEntity,
                                              TransaccionCobroEntity transaccionCobroEntity,
                                              ExceptionDto exceptionDto) {
        EndPointTransaccionCobroEntity endPointTransac = new EndPointTransaccionCobroEntity();
        endPointTransac.setEndPointEntidadId(endPointEntidadEntity);
        endPointTransac.setTransaccionCobroId(transaccionCobroEntity);
        endPointTransac.setEstadoCodigo(exceptionDto.estadoCodigo);
        endPointTransac.setDetalle(exceptionDto.detalle);
        endPointTransac.setFechaCreacion(new Timestamp(System.currentTimeMillis()));

        endPointTransaccionCobroDao.save(endPointTransac);
    }

}
