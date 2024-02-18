package bo.com.tesla.recaudaciones.dto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BusquedaReportesRecaudacionDto {
	public String entidadId;
	public Long recaudadorId;
	public String recaudador;
	public String estado;
	public Date fechaInicio;
	public Date fechaFin;
	public String export;
	public Long archivoId;
	public Date fechaCreacion;
	public int paginacion;
	public List<String> entidadArray=new ArrayList<>();
	public List<String> estadoArray=new ArrayList<>();
	
	public Date fechaSeleccionada;
	public Long idEntidad;
	public List<Long> lstEntidadId;
	
	
	public BusquedaReportesRecaudacionDto() {
	
	}


	public BusquedaReportesRecaudacionDto(Long entidadId, Long recaudadorId, String estado, Date fechaInicio, Date fechaFin) {
	
		this.entidadId = entidadId+"";
		this.recaudadorId = recaudadorId;
		this.estado = estado;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}
	
	

}
