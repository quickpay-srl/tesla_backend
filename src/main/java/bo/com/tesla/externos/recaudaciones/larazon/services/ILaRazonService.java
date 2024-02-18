package bo.com.tesla.externos.recaudaciones.larazon.services;

import bo.com.tesla.administracion.entity.EndPointEntidadEntity;
import bo.com.tesla.administracion.entity.TransaccionCobroEntity;

import java.util.List;

public interface ILaRazonService {
    void updateCobroLaRazon(EndPointEntidadEntity endPoint,
                            List<TransaccionCobroEntity> transaccionCobroEntityList);
}
