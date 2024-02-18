package bo.com.tesla.externos.recaudaciones.larazon.dto;

import java.util.Date;

public class CobroEntidadDto {

	public String idAviso;
	public Date fecha;

	public CobroEntidadDto(String idAviso, Date fecha) {
		this.idAviso = idAviso;
		this.fecha = fecha;
	}
}
