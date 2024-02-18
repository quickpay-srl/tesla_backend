package bo.com.tesla.recaudaciones.services;

import bo.com.tesla.administracion.entity.DeudaClienteEntity;
import bo.com.tesla.recaudaciones.dao.IDeudaClienteRDao;
import bo.com.tesla.recaudaciones.dto.CampoBusquedaClienteEnum;
import bo.com.tesla.recaudaciones.dto.ClienteDto;
import bo.com.tesla.recaudaciones.dto.DeudaClienteDto;
import bo.com.tesla.recaudaciones.dto.ServicioDeudaDto;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeudaClienteRService implements IDeudaClienteRService {

    @Autowired
    private IDeudaClienteRDao iDeudaClienteRDao;

    @Transactional(readOnly = true)
    @Override
    public List<ClienteDto> getByEntidadAndClienteLike(Long entidadId, String datoCliente) {
        return iDeudaClienteRDao.findByEntidadAndClienteLike(entidadId, datoCliente);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ServicioDeudaDto> getDeudasByCliente(Long entidadId, String codigoCliente) throws BusinesException {
        //Controlar que el cargado de deudas no tenga 2 codigos de cliente con diferente dato
        List<ClienteDto> clienteDtoList = iDeudaClienteRDao.findCodigoClienteByEntidad(entidadId, codigoCliente);
        if(clienteDtoList.size() > 1) {
            throw new BusinesException("El código de cliente '" + codigoCliente + "' se encuentra repetido con datos diferentes del cliente, resultante del cargado de deudas, comuníquese con su Administrador.");
        }
        try {
            List<ServicioDeudaDto> servicioDeudaDtos = iDeudaClienteRDao.groupByDeudasClientes(entidadId, codigoCliente);
            if (servicioDeudaDtos.isEmpty()) {
                return new ArrayList<ServicioDeudaDto>();//el controller procedera con la captura del mensaje
            }
            Long key = 0L;
            for (ServicioDeudaDto servicioDeuda : servicioDeudaDtos) {
                servicioDeuda.setKey(key);
                List<DeudaClienteDto> deudaClienteDtosDeudas = iDeudaClienteRDao.findByEntidadByServiciosDeudas(entidadId,
                        servicioDeuda.getTipoServicio(),
                        servicioDeuda.getServicio(),
                        servicioDeuda.getPeriodo(),
                        codigoCliente);

                if (deudaClienteDtosDeudas.isEmpty()) {
                    throw new Technicalexception("No existen DEUDAS para EntidadId=" + entidadId + " y CodigoCliente=" + codigoCliente);
                }
                servicioDeuda.setDeudaClienteDtos(deudaClienteDtosDeudas);
                //Para la edición verificar
                Boolean esEditable = deudaClienteDtosDeudas.stream().anyMatch(d -> !d.getEsPostpago() && d.getSubTotal().compareTo(BigDecimal.ZERO) == 0);
                servicioDeuda.setEditable(esEditable);
                servicioDeuda.setEditando(false);
                servicioDeuda.setPlantilla(deudaClienteDtosDeudas.get(0).getTipoComprobante() ? "FACTURA" : "RECIBO");
                key++;
            }
            return servicioDeudaDtos;
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<DeudaClienteEntity> getAllDeudasByCliente(Long entidadId,
                                                          String tipoServicio,
                                                          String servicio,
                                                          String periodo,
                                                          String codigoCliente) {
        return iDeudaClienteRDao.findAllGroupByServicio(entidadId,
                                                        tipoServicio,
                                                        servicio,
                                                        periodo,
                                                        codigoCliente);
    }



    @Override
    public Long deleteDeudasClientes(List<DeudaClienteEntity> deudaClienteEntities) {
        List<Long> deudaClienteIdLst = deudaClienteEntities.stream()
                                            .mapToLong(d -> d.getDeudaClienteId()).boxed()
                                            .collect(Collectors.toList());

        return iDeudaClienteRDao.deleteByDeudaClienteIdIn(deudaClienteIdLst);
    }

    @Override
    public Integer recoverDeudasByFacturas(List<Long> facturaIdLst) {
        return iDeudaClienteRDao.recoverDeudasByFacturas(facturaIdLst);
    }

    @Override
    public Integer recoverDeudasByFactura(Long facturaIdLst) {
        return iDeudaClienteRDao.recoverDeudasByFactura(facturaIdLst);
    }

    @Override
    public List<String> getCodigosActividadUnicos(List<DeudaClienteEntity> deudaClienteEntityList) {
        List<String> codActEcoList = deudaClienteEntityList.stream().map(d -> d.getCodigoActividadEconomica())
                .collect(Collectors.toList());
        return codActEcoList.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<Boolean> getTipoComprobanteUnicos(List<DeudaClienteEntity> deudaClienteEntityList) {
        List<Boolean> tipoComprobantes = deudaClienteEntityList.stream().map(d -> d.getTipoComprobante())
                .collect(Collectors.toList());
        return tipoComprobantes.stream().distinct().collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    @Override
    public List<ClienteDto> getByEntidadAndDatoCliente(Long entidadId, String campoBusqueda, String datoCliente) {
        List<ClienteDto> clienteDtoList = new ArrayList<>();
        CampoBusquedaClienteEnum codCliente = CampoBusquedaClienteEnum.CODIGO_CLIENTE;
        CampoBusquedaClienteEnum nomCliente = CampoBusquedaClienteEnum.NOMBRE_CLIENTE;
        CampoBusquedaClienteEnum nroDocumento = CampoBusquedaClienteEnum.CARNET_IDENTIDAD;
        CampoBusquedaClienteEnum nit = CampoBusquedaClienteEnum.NIT;
        CampoBusquedaClienteEnum telefono = CampoBusquedaClienteEnum.TELEFONO;

        if(codCliente.getAlias().equals(campoBusqueda)) {
            clienteDtoList = iDeudaClienteRDao.findByEntidadAndCodigoCliente(entidadId, datoCliente);
        } else if(nomCliente.getAlias().equals(campoBusqueda)) {
            clienteDtoList = iDeudaClienteRDao.findByEntidadAndNombreCliente(entidadId, datoCliente);
        } else if(nroDocumento.getAlias().equals(campoBusqueda)) {
            clienteDtoList = iDeudaClienteRDao.findByEntidadAndNroDocumento(entidadId, datoCliente);
        } else if(nit.getAlias().equals(campoBusqueda)) {
            clienteDtoList = iDeudaClienteRDao.findByEntidadAndNit(entidadId, datoCliente);
        } else if(telefono.getAlias().equals(campoBusqueda)) {
            clienteDtoList = iDeudaClienteRDao.findByEntidadAndTelefono(entidadId, datoCliente);
        }

        return clienteDtoList;
    }

    @Override
    public List<String> findCamposBusquedasClientes() {
        List<String> lista = new ArrayList<>();
        CampoBusquedaClienteEnum[] campos = CampoBusquedaClienteEnum.values();
        for(CampoBusquedaClienteEnum c : campos) {
            lista.add(c.getAlias());
        }
        return lista;
    }

    @Override
    public void verifiedCampoInEnum(String campo) throws BusinesException {
        CampoBusquedaClienteEnum[] campos = CampoBusquedaClienteEnum.values();
        boolean respuesta= Arrays.stream(campos).anyMatch((t) -> t.getAlias().equals(campo));
        if(!respuesta) {
            throw new BusinesException("Campo búsqueda erróneo");
        }
    }

}
