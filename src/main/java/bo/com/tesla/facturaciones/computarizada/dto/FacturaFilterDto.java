package bo.com.tesla.facturaciones.computarizada.dto;

import java.util.Date;
import java.util.List;

public class FacturaFilterDto {

    public Long numeroFactura;
    public String codigoCliente;
    public String nombreRazonSocial;
    public String numeroDocumento;
    public String codigoActividadEconomica;
    public String estado;
    public Date fechaInicioFactura;
    public Date fechaFinFactura;
    public List<Long> facturaIdLst;
    public String formatFile;
}
