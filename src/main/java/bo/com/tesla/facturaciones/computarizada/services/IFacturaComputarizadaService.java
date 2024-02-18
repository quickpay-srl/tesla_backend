package bo.com.tesla.facturaciones.computarizada.services;

import bo.com.tesla.administracion.entity.SucursalEntidadEntity;
import bo.com.tesla.administracion.entity.TransaccionCobroEntity;
import bo.com.tesla.facturaciones.computarizada.dto.CodigoControlDto;
import bo.com.tesla.facturaciones.computarizada.dto.FacturaFilterDto;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.dto.ResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface IFacturaComputarizadaService {
    ResponseDto postCodigoControl(CodigoControlDto codigoControlDto, Long entidadId);
    ResponseDto postFacturas(SucursalEntidadEntity sucursalEntidadEntity, List<TransaccionCobroEntity> transaccionCobroEntityList, Boolean comprobanteEnUno, BigDecimal montoTotal, Long dimensionId);
    ResponseDto postFacturaLstFilter(Long entidadId, int page, FacturaFilterDto facturaFilterDto, Long recaudadoraId);
    ResponseDto getFacturaReport(Long entidadId,Long recaudadorId, Long facturaId) throws BusinesException;
    ResponseDto getLibroVentasReport(Long entidadId, FacturaFilterDto facturaFilterDto);
    ResponseDto getFacturaDto(Long entidadId, Long facturaId);
    List<Long> findFacturasByEntidadAndRecaudador(Long entidadId, Long recaudadorId);

}
