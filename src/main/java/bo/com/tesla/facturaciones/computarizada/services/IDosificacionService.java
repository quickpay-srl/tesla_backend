package bo.com.tesla.facturaciones.computarizada.services;

import bo.com.tesla.facturaciones.computarizada.dto.DosificacionDto;
import bo.com.tesla.useful.dto.ResponseDto;

public interface IDosificacionService {
    ResponseDto saveDosificacion(Long entidadId, DosificacionDto dosificacionDto);
    ResponseDto getDosificacionesLst(Long entidadId);
    ResponseDto getDosificacionById(Long entidadId, Long dosificacionId);
    ResponseDto putTransaccion(Long entidadId, Long dosificacionId, String transaccion);
    ResponseDto getDosificacionesLstAlerta(Long entidadId);
}
