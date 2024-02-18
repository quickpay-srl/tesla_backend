package bo.com.tesla.facturaciones.computarizada.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class AnulacionFacturaLstDto {

    @NotBlank(message = "Motivo obligatorio.")
    public String motivo;
    @NotNull(message = "Tipo acnulacion obligatorio.")
    public boolean cargadoErroneo;
    @Valid
    @NotNull (message = "Debe enviar al menos una factura par anulación.")
    @Size(min = 1, message = "Debe enviar al menos una factura par anulación.")
    public List<Long> facturaIdLst;
}
