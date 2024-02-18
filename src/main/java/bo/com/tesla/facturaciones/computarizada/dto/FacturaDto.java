package bo.com.tesla.facturaciones.computarizada.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FacturaDto {

    public BigDecimal montoTotal;
    public BigDecimal montoBaseImporteFiscal;
    public String codigoCliente;
    public String nombreRazonSocial;
    public String numeroDocumento;
    public BigDecimal montoDescuento;
    public String domicilioCliente;
    public String nombreAlumno;
    public String codigoActividadEconomica;
    public String actividadEconomica;
    public List<DetalleFacturaDto> detalleFacturaDtoList;
    public List<Long> transaccionIdLst;
    public String formatFile;
    public List<Long> facturaIdLst=new ArrayList<>();
    public Long dimensionId;
    public String correoCliente;
    public FacturaDto() {
    }
}
