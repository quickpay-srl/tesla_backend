package bo.com.tesla.administracion.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class DeudasClienteAdmDto {

	public Long archivoId;
	public String servicio;
	public String tipoServicio;
	public String periodo;
	public String codigoCliente;
	public String nit;
	public String direccion;
	public String nroDocumento;
	public String telefono;
	public String nombreCliente;	
	public String key;
	//@Digits(integer=17, fraction=2)
	public BigDecimal total;
	public Boolean esPostpago;
	public String cajero;
	@JsonFormat( pattern = "dd/MM/yyyy HH:mm:ss",timezone="America/La_Paz")
	public Date fechaCreacion;
	public String estado;
	public String nombreRecaudadora;
	public BigDecimal comision;
	public String nombre;



	public DeudasClienteAdmDto() {

	}

	
	
	
	
	public DeudasClienteAdmDto(Long archivoId, String codigoCliente,String servicio,
			String tipoServicio,String periodo, String estado,
			String nombreClientePago,Date fechaCreacion,BigDecimal total,String nombreRecaudadora,BigDecimal comision,String cajero,String nombre) {
		
	
		this.archivoId=archivoId;
		this.codigoCliente=codigoCliente;
		this.servicio=servicio;
		this.tipoServicio=tipoServicio;
		this.periodo=periodo;
		if(estado.equals("ACTIVO")) {
			this.estado="POR PAGAR";	
		}else if(estado.equals("COBRADO")) {
			
			this.estado="COBRADAS";	
			this.nombreCliente=nombreClientePago;
			this.fechaCreacion=fechaCreacion;
			this.total=total;	
			this.nombreRecaudadora=nombreRecaudadora;
			this.comision=comision;
			this.cajero=cajero;
			this.nombre = nombre;
			
		}else if(estado.equals("ANULADO")) {
			this.estado="ANULADO";
			this.nombreCliente=nombreClientePago;
			this.fechaCreacion=fechaCreacion;
			this.total=total;	
			this.nombreRecaudadora=nombreRecaudadora;
			this.comision=comision;
			this.cajero=cajero;
			this.nombre = nombre;
		} else if(estado.equals("ERRONEO")) {
			this.estado="ERRONEO";
			this.nombreCliente=nombreClientePago;
			this.fechaCreacion=fechaCreacion;
			this.total=total;
			this.nombreRecaudadora=nombreRecaudadora;
			this.comision=comision;
			this.cajero=cajero;
			this.nombre = nombre;
		}

		
		
		
		
	}
	

	public Long getArchivoId() {
		return archivoId;
	}

	public void setArchivoId(Long archivoId) {
		this.archivoId = archivoId;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public String getTipoServicio() {
		return tipoServicio;
	}

	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNroDocumento() {
		return nroDocumento;
	}

	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Boolean getEsPostpago() {
		return esPostpago;
	}

	public void setEsPostpago(Boolean esPostpago) {
		this.esPostpago = esPostpago;
	}

	public String getCajero() {
		return cajero;
	}

	public void setCajero(String cajero) {
		this.cajero = cajero;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNombreRecaudadora() {
		return nombreRecaudadora;
	}

	public void setNombreRecaudadora(String nombreRecaudadora) {
		this.nombreRecaudadora = nombreRecaudadora;
	}

	public BigDecimal getComision() {
		return comision;
	}

	public void setComision(BigDecimal comision) {
		this.comision = comision;
	}


	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	

}
