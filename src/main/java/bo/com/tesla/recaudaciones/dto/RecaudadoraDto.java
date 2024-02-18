package bo.com.tesla.recaudaciones.dto;

import java.math.BigDecimal;

public class RecaudadoraDto {
	public String nombre;
	public BigDecimal montoTotal;
	
	public RecaudadoraDto() {			
	}
	
	public RecaudadoraDto(String nombre, BigDecimal montoTotal) {	
		this.nombre = nombre;
		this.montoTotal = montoTotal;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public BigDecimal getMontoTotal() {
		return montoTotal;
	}
	public void setMontoTotal(BigDecimal montoTotal) {
		this.montoTotal = montoTotal;
	}
	
	
	

}
