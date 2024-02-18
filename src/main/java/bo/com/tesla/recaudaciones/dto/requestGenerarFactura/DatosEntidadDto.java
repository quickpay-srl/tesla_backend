package bo.com.tesla.recaudaciones.dto.requestGenerarFactura;


import lombok.Getter;
import lombok.Setter;

public class DatosEntidadDto {
    @Getter @Setter
    private Long entidadId;
    @Getter @Setter
    private Integer codigoSucursal;
    @Getter @Setter
    private Integer codigoPuntoVenta;
}
