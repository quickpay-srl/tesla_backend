package bo.com.tesla.recaudaciones.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.Optional;

public interface ReporteCobrosSocioDto {
    String getNombreSocio();
    String getServicio();
    String getNombreEmpresa();
    String getNombreClienteFinal();
    @JsonFormat( pattern = "dd/MM/yyyy HH:mm:ss",timezone="America/La_Paz")
    Date getFechaCobro();
    String getMontoCobro();

    String getMontoComision();
    String getSaldo();
    String getEstado();

}
