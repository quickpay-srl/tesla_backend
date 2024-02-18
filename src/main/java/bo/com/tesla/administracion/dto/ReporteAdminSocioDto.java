package bo.com.tesla.administracion.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;


public interface ReporteAdminSocioDto {
	String getNombreSocio();
	String getNombreEmpresa();
	String getDeptoBanco();
	String getSucursalBanco();
	String getCajero();
	String getTipoServicio();
	String getServicio();
	String getCodEstudiante();
	@JsonFormat( pattern = "dd/MM/yyyy HH:mm:ss",timezone="America/La_Paz")
	Date getFechaCobro();
	BigDecimal getMontoCobro();
	BigDecimal getComisionSocio();
	String getEstado();


}
