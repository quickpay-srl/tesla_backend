package bo.com.tesla.recaudaciones.services;

import bo.com.tesla.administracion.entity.DeudaClienteEntity;
import bo.com.tesla.recaudaciones.dto.CampoBusquedaClienteEnum;
import bo.com.tesla.recaudaciones.dto.ClienteDto;
import bo.com.tesla.recaudaciones.dto.DeudaClienteDto;
import bo.com.tesla.recaudaciones.dto.ServicioDeudaDto;
import bo.com.tesla.useful.config.BusinesException;

import java.util.List;
import java.util.Optional;

public interface IDeudaClienteRService {

    public List<ClienteDto> getByEntidadAndClienteLike(Long entidadId, String datoCliente);
    public List<ServicioDeudaDto> getDeudasByCliente(Long entidadId, String codigoCliente) throws BusinesException;
    public List<DeudaClienteEntity> getAllDeudasByCliente(Long entidadId,
                                                          String tipoServicio,
                                                          String servicio,
                                                          String periodo,
                                                          String codigoCliente);
    public Long deleteDeudasClientes(List<DeudaClienteEntity> deudaClienteEntities);
    Integer recoverDeudasByFacturas(List<Long> facturaIdLst);
    Integer recoverDeudasByFactura(Long facturaIdLst);
    List<String> getCodigosActividadUnicos(List<DeudaClienteEntity> deudaClienteEntityList);
    List<Boolean> getTipoComprobanteUnicos(List<DeudaClienteEntity> deudaClienteEntityList);
    List<ClienteDto> getByEntidadAndDatoCliente(Long entidadId, String campoBusqueda, String datoCliente);
    List<String> findCamposBusquedasClientes();
    void verifiedCampoInEnum(String campo) throws BusinesException;
}
