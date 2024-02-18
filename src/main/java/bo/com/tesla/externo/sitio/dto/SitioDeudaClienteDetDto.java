package bo.com.tesla.externo.sitio.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SitioDeudaClienteDetDto {

    private Long deudaClienteId;
    private String tipoServicio;
    private String servicio;
    private String periodo;
    private BigDecimal cantidad;
    private String concepto;
    private BigDecimal montoUnitario;
    private BigDecimal subTotal;

    public SitioDeudaClienteDetDto(Long deudaClienteId, String tipoServicio, String servicio, String periodo, BigDecimal cantidad, String concepto, BigDecimal montoUnitario, BigDecimal subTotal) {
        this.deudaClienteId = deudaClienteId;
        this.tipoServicio = tipoServicio;
        this.servicio = servicio;
        this.periodo = periodo;
        this.cantidad = cantidad;
        this.concepto = concepto;
        this.montoUnitario = montoUnitario;
        this.subTotal = subTotal;
    }

    public SitioDeudaClienteDetDto() {
    }
}
