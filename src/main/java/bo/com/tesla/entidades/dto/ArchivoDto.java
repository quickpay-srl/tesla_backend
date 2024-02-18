package bo.com.tesla.entidades.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ArchivoDto {
	   
	    public Long archivoId;
		public String nombre;	  
	    public String path;
	    public Date inicioCargado;	 
	    public Date finCargado;
	    public String usuarioCreacion;
	    @JsonFormat( pattern = "dd/MM/yyyy HH:mm:ss",timezone="America/La_Paz")
	    public Date fechaCreacion;
	    public Long nroRegistros;
	    public String estado;
	    public String key;
	    
		public ArchivoDto(Long archivoId, String nombre, String usuarioCreacion, Date fechaCreacion,
				Long nroRegistros,String estado) {
			
			this.archivoId = archivoId;
			this.nombre = nombre;
			this.usuarioCreacion = usuarioCreacion;
			this.fechaCreacion = fechaCreacion;
			this.nroRegistros = nroRegistros;
			this.estado=estado;
			this.key=archivoId+"";
		}
	    
	    
	   
}
