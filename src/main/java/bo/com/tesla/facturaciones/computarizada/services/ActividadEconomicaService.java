package bo.com.tesla.facturaciones.computarizada.services;

import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ActividadEconomicaService implements IActividadEconomicaService {
    @Value("${host.facturacion.electronica}")
    private String hostComputarizada;

    @Autowired
    private ConexionService conexionService;

    @Override
    public ResponseDto getByCodigo(Long entidadId, String codigoActividadEconomica) {
        try {
            String url = this.hostComputarizada + "/api/actividadeseconomicas/" + codigoActividadEconomica;

            return conexionService.getResponseMethodGet(entidadId, url);
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(),e.getCause());
        }
    }

    @Override
    public ResponseDto getActividadesEconomicas(Long entidadId) {
        try {
            String url = this.hostComputarizada + "/api/actividadeseconomicas";

            return conexionService.getResponseMethodGet(entidadId, url);
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(),e.getCause());
        }
    }

}
