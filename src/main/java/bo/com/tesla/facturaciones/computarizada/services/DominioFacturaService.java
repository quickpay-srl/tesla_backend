package bo.com.tesla.facturaciones.computarizada.services;

import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DominioFacturaService implements IDominioFacturaService{

    @Value("${host.facturacion.electronica}")
    private String hostComputarizada;

    @Autowired
    private ConexionService conexionService;

    @Override
    public ResponseDto getDominiosLst(Long entidadId, String dominio) {
        try {
            String url = this.hostComputarizada + "/api/dominios/" + dominio;

            return conexionService.getResponseMethodGet(entidadId, url);
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(),e.getCause());
        }
    }

    @Override
    public ResponseDto getDimensionesFacturas(Long entidadId) {
        try {
            String url = this.hostComputarizada + "/api/dominios/facturas/dimensiones";

            return conexionService.getResponseMethodGet(entidadId, url);
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(),e.getCause());
        }
    }

}
