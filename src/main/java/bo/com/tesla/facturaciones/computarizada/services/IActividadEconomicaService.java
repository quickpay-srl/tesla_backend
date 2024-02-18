package bo.com.tesla.facturaciones.computarizada.services;

import bo.com.tesla.useful.dto.ResponseDto;

public interface IActividadEconomicaService {
    ResponseDto getByCodigo(Long entidadId, String codigoActividadEconomica);
    ResponseDto getActividadesEconomicas(Long entidadId);
}
