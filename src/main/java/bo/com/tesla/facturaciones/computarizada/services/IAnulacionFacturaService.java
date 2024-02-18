package bo.com.tesla.facturaciones.computarizada.services;

import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.facturaciones.computarizada.dto.AnulacionFacturaLstDto;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.dto.ResponseDto;

public interface IAnulacionFacturaService {
    ResponseDto postAnulacionLst(Long entidadId, AnulacionFacturaLstDto anulacionFacturaLstDto);
    Boolean anularTransaccionFactura(Long entidadId,
                                            AnulacionFacturaLstDto anulacionFacturaLstDto,
                                            SegUsuarioEntity usuarioEntity) throws BusinesException;
}
