package bo.com.tesla.administracion.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BusquedaReportesAdmDto {
	public String entidadId;
	public String recaudadorId;
	public String recaudador;
	public String estado;
	public Date fechaInicio;
	public Date fechaFin;
	public String export;
	public Long archivoId;
	public Date fechaCreacion;
	public int paginacion;
	public List<String> estadoArray=new ArrayList<>();
	
	
	public BusquedaReportesAdmDto() {
	
	}


	
	
	

}
