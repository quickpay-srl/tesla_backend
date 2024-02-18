package bo.com.tesla.administracion.dto;

import java.math.BigDecimal;

public interface DashboardDto {
    BigDecimal getMontoRecaudadoDia();
    Long getCantidadTransaccionDia();
    BigDecimal getMontoRecaudadoMes();
    Long getCantidadTransaccionMes();
    BigDecimal getMontoRecaudadoPlataforma();
    Long getCantidadTransaccionPlataforma();
}
