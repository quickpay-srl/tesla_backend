package bo.com.tesla.facturaciones.computarizada.services;

import bo.com.tesla.useful.dto.ResponseDto;

public interface IDominioFacturaService {
    ResponseDto getDominiosLst(Long entidadId, String dominio);
    ResponseDto getDimensionesFacturas(Long entidadId);
}
