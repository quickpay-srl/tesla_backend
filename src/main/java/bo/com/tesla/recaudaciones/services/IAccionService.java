package bo.com.tesla.recaudaciones.services;

import bo.com.tesla.administracion.entity.AccionEntity;
import bo.com.tesla.administracion.entity.DeudaClienteEntity;

import java.util.List;

public interface IAccionService {

    public List<AccionEntity> saveAllAcciones(List<AccionEntity> accionEntities);
    public AccionEntity loadAccion(DeudaClienteEntity deudaClienteEntity, String estado, Long usuarioId);
}
