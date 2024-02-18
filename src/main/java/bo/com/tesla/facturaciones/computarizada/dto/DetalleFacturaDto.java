package bo.com.tesla.facturaciones.computarizada.dto;

import java.math.BigDecimal;
import java.util.Date;

public class DetalleFacturaDto {
    public Long detalleFacturaId;
    public String tipoServicio;
    public String servicio;
    public String periodo;
    public String codigo;
    public BigDecimal cantidad;
    public String unidad;
    public String concepto;
    public BigDecimal montoUnitario;
    public BigDecimal montoSubtotal;
    public Date fechaCreacion;
    public String estado;

    public DetalleFacturaDto() {
    }
}

