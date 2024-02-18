package bo.com.tesla.facturaciones.computarizada.services;

import bo.com.tesla.useful.dto.ResponseDto;

public interface IOperacionService {
    ResponseDto findOperacionesLst(String tabla, String estadoInicial, Long entidadId);
}
