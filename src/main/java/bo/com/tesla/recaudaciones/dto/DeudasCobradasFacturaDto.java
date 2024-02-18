package bo.com.tesla.recaudaciones.dto;

import java.math.BigDecimal;

public class DeudasCobradasFacturaDto {

	public Long archivoId;
	public String codigoCliente;
	public String nombreCliente;	
	public String tipoServicio;
	public String servicio;
	public String periodo;
	public BigDecimal cantidad;
	public String concepto;
	public BigDecimal montoUnitario;	
	public String nit;
	public BigDecimal subTotal;
	public BigDecimal totalDeuda;
	
	public DeudasCobradasFacturaDto(Long archivoId,String codigoCliente, String nombreCliente, String tipoServicio, String servicio,
			String periodo, BigDecimal cantidad, String concepto, BigDecimal montoUnitario, String nit,
			BigDecimal subTotal, BigDecimal totalDeuda) {
		
		this.archivoId=archivoId;
		this.codigoCliente = codigoCliente;
		this.nombreCliente = nombreCliente;
		this.tipoServicio = tipoServicio;
		this.servicio = servicio;
		this.periodo = periodo;
		this.cantidad = cantidad;
		this.concepto = concepto;
		this.montoUnitario = montoUnitario;
		this.nit = nit;
		this.subTotal = subTotal;
		this.totalDeuda = totalDeuda;
	}
	
	
	
	
	
	
	public Long getArchivoId() {
		return archivoId;
	}






	public void setArchivoId(Long archivoId) {
		this.archivoId = archivoId;
	}






	public String getCodigoCliente() {
		return codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public String getTipoServicio() {
		return tipoServicio;
	}
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}
	public String getServicio() {
		return servicio;
	}
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public BigDecimal getCantidad() {
		return cantidad;
	}
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public BigDecimal getMontoUnitario() {
		return montoUnitario;
	}
	public void setMontoUnitario(BigDecimal montoUnitario) {
		this.montoUnitario = montoUnitario;
	}
	public String getNit() {
		return nit;
	}
	public void setNit(String nit) {
		this.nit = nit;
	}
	public BigDecimal getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}
	public BigDecimal getTotalDeuda() {
		return totalDeuda;
	}
	public void setTotalDeuda(BigDecimal totalDeuda) {
		this.totalDeuda = totalDeuda;
	}
	
	
	

}
