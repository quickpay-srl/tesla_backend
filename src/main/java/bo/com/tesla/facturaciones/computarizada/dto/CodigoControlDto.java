package bo.com.tesla.facturaciones.computarizada.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

public class CodigoControlDto {
    public String numeroAutorizacion;
    public Long numeroFactura;
    public String numeroDocumento;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "EST")
    public Date fechaFactura;
    public BigDecimal montoTotal;
    public String llaveDosificacion;
}
