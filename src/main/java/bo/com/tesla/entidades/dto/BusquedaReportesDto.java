package bo.com.tesla.entidades.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BusquedaReportesDto {
	public Long entidadId;
	public String recaudadorId;
	public String recaudador;
	public String estado;
	public Date fechaInicio;
	public Date fechaFin;
	public String export;
	public Long archivoId;
	public Date fechaCreacion;
	public int paginacion;
	public List<String> recaudadorArray=new ArrayList<>();
	public List<String> estadoArray=new ArrayList<>();
	
	
	
	public BusquedaReportesDto() {
	
	}


	/*public BusquedaReportesDto(Long entidadId, String recaudadorId, String estado, Date fechaInicio, Date fechaFin) {
	
		this.entidadId = entidadId;
		this.recaudadorId = recaudadorId;
		this.estado = estado;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}*/
	
	

}
