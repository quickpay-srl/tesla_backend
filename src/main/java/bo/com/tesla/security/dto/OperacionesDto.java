package bo.com.tesla.security.dto;

public class OperacionesDto {
	public String transaccion;
	public String etiqueta;
	public String imagen;
	public String orden;
	public OperacionesDto() {	
	}
	public OperacionesDto(String transaccion, String etiqueta, String imagen, String orden) {	
		this.transaccion = transaccion;
		this.etiqueta = etiqueta;
		this.imagen = imagen;
		this.orden = orden;
	}
	
	
	

}
