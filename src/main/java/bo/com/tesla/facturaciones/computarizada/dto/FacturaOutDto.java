package bo.com.tesla.facturaciones.computarizada.dto;

import java.math.BigDecimal;
import java.util.Date;

public class FacturaOutDto {

    public Long facturaId;
    public Long numeroFactura;
    public Date fechaFactura;
    public BigDecimal montoTotal;
    public BigDecimal montoBaseImporteFiscal;
    public String codigoControl;
    public String codigoCliente;
    public String nombreRazonSocial;
    public String numeroDocumento;
    public BigDecimal montoDescuento;
    public String domicilioCliente;
    public String nombreAlumno;
    public String numeroAutorizacion;
    public Date fechaLimiteEmision;
    public String estado;
    public String actividadEconomica;
    public String codigoActividadEconomica;
    public String actividadEconomicaSecundaria;
    public String codigoActividadEconomicaSecundaria;
    public String correoCliente;

}
