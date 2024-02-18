package bo.com.tesla.recaudaciones.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public interface ReporteCierreCajaDiarioDto {
    String getNombreEntidad();
    String getTipoServicio();
    String getServicio();
    String getPeriodo();
    String getCodigoCliente();
    @JsonFormat( pattern = "dd/MM/yyyy HH:mm:ss",timezone="America/La_Paz")
    Date getFechaCreacion();
    BigDecimal getTotalDeuda();
    String getNombreClienteArchivo();
    String getNroDocumentoClienteArchivo();
}
