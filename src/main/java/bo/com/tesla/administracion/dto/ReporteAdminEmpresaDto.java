package bo.com.tesla.administracion.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;


public interface ReporteAdminEmpresaDto {
	String getNombreEmpresa();
	String getNombreSocio();
	String getTipoServicio();
	String getServicio();
	String getCodEstudiante();
	String getNroFactura();
	@JsonFormat( pattern = "dd/MM/yyyy HH:mm:ss",timezone="America/La_Paz")
	Date getFechaCobro();
	String getDeptoBanco();
	String getSucursalEmpresa();
	BigDecimal getMontoCobro();
	BigDecimal getComisionEmpresa();
	BigDecimal getMontoTransferir();
	String getMetodoCobro();

	String getEstado();


}
