package bo.com.tesla.facturaciones.computarizada.services;

import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OperacionService implements IOperacionService{

    @Value("${host.facturacion.electronica}")
    private String hostComputarizada;

    @Autowired
    private ConexionService conexionService;

    @Override
    public ResponseDto findOperacionesLst(String tabla, String estadoInicial, Long entidadId) {
        try {
            String url = this.hostComputarizada + "/api/menus/operaciones/" + tabla + "/estados/" + estadoInicial;

            return conexionService.getResponseMethodGet(entidadId, url);
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(),e.getCause());
        }
    }
}
