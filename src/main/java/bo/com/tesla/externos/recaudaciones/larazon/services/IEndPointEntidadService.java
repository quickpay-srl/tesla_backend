package bo.com.tesla.externos.recaudaciones.larazon.services;

import bo.com.tesla.administracion.entity.TransaccionCobroEntity;

import java.util.List;

public interface IEndPointEntidadService {
    void updateCobroEnEntidad(Long entidadId, List<TransaccionCobroEntity> transaccionCobroEntityList);
}
