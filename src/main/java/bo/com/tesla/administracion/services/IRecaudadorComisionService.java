package bo.com.tesla.administracion.services;

import bo.com.tesla.administracion.dto.RecaudadorComisionAdmDto;
import bo.com.tesla.administracion.entity.RecaudadorComisionEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.useful.config.Technicalexception;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface IRecaudadorComisionService {

    public RecaudadorComisionAdmDto addUpdateRecaudadorComision(RecaudadorComisionAdmDto recaudadorComisionAdmDto, Long usuarioId);
    public void setTransaccion(Long recaudadorComisionId, String transaccion, Long usuarioId);
    public RecaudadorComisionAdmDto getRecaudadorComisionById(Long recaudadorComisionId);
    public List<RecaudadorComisionAdmDto> getAllRecaudadoresComisionesByRecaudadorId(Long recaudadorId);

    public RecaudadorComisionAdmDto getRecaudadoresComisionesActivosByRecaudadorId(Long recaudadorId);
    public RecaudadorComisionEntity getRecaudadorComisionActual(RecaudadorEntity recaudadorEntity);
    public BigDecimal calcularComision(RecaudadorComisionEntity recaudadorComisionEntity, BigDecimal monto);

}
