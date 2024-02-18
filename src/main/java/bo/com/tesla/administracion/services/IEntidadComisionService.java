package bo.com.tesla.administracion.services;

import bo.com.tesla.administracion.dto.EntidadComisionAdmDto;
import bo.com.tesla.administracion.entity.EntidadComisionEntity;
import bo.com.tesla.administracion.entity.EntidadEntity;

import java.math.BigDecimal;
import java.util.List;

public interface IEntidadComisionService {

    public EntidadComisionAdmDto addUpdateEntidadComision(EntidadComisionAdmDto entidadComisionAdmDto, Long usuarioId);
    public void setTransaccion(Long entidadAlicuotaId, String transaccion, Long usuarioId);
    public EntidadComisionAdmDto getEntidadComisionById(Long entidadAlicuotaId);
    public List<EntidadComisionAdmDto> getAllEntidadesComisionesByEntidadId(Long entidadId);
    public EntidadComisionAdmDto getEntidadesComisionesActivaByEntidadId(Long entidadId);
    public EntidadComisionEntity getEntidadComisionActual(EntidadEntity entidadEntity);
    public BigDecimal calcularComision(EntidadComisionEntity entidadComisionEntity, BigDecimal monto);
}
