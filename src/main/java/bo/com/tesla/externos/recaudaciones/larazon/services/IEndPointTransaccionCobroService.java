package bo.com.tesla.externos.recaudaciones.larazon.services;

import bo.com.tesla.administracion.entity.EndPointEntidadEntity;
import bo.com.tesla.administracion.entity.TransaccionCobroEntity;
import bo.com.tesla.externos.recaudaciones.larazon.dto.ExceptionDto;

public interface IEndPointTransaccionCobroService {
    void addEnPointTransaccionCobro(EndPointEntidadEntity endPointEntidadEntity,
                                    TransaccionCobroEntity transaccionCobroEntity,
                                    ExceptionDto exceptionDto);
}
