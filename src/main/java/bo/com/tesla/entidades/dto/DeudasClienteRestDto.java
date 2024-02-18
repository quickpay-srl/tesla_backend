package bo.com.tesla.entidades.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import bo.com.tesla.recaudaciones.dto.RecaudadoraDto;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class DeudasClienteRestDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Long archivoId;
	public String servicio;
	@Size(min = 1, max = 300, message= "debe contener de 1 a 300 caracteres")
	@NotEmpty(message = "no puede estar vacio")
	public String tipoServicio;
	@Size(min = 10, max = 10, message= " debe esta en el formato dd/mm/yyyy")
	@NotEmpty(message = "no puede estar vacio")
	public String periodo;
	@Size(min = 1, max = 15, message= "debe contener de 1 a 15 caracteres")
	@NotEmpty(message = "no puede estar vacio")
	public String codigoCliente;
	@Size(min = 1, max = 15, message= "debe contener de 1 a 15 caracteres")
	@NotEmpty(message = "no puede estar vacio")
	public String nit;
	public String direccion;	
	public String nroDocumento;
	public String telefono;
	@Size(min = 1, max = 200, message= "debe contener de 1 a 200 caracteres")
	@NotEmpty(message = "no puede estar vacio")
	public String nombreCliente;	
	public String key;
	//@Digits(integer=17, fraction=2)
	public BigDecimal total;
	public Boolean esPostpago;
	@Size(min = 1, max = 10, message= "debe contener de 1 a 10 caracteres")
	@NotEmpty(message = "no puede estar vacio")
	public String codigoActividadEconomica;
	public String cajero;
	@JsonFormat( pattern = "dd/MM/yyyy",timezone="America/La_Paz")
	public Date fechaCreacion;
	public String estado;
	public String nombreRecaudadora;
	public BigDecimal comision;
	public BigDecimal cantidad;
	public BigDecimal montoUnitario;
	public Integer nroRegistro;
	public BigDecimal subTotal;
	public Character tipo;
	public Boolean tipoComprobante;
	@Email(message = "no tiene el formato de correo electronico")
	public String correoCliente;
	public String periodoCabecera;
	
	
	@NotEmpty(message = "no puede estar vacio")
	public List<ConceptoDto> conceptoList;

	public DeudasClienteRestDto() {

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

	

	public List<ConceptoDto> getConceptoList() {
		return conceptoList;
	}



	public void setConceptoList(List<ConceptoDto> conceptoList) {
		this.conceptoList = conceptoList;
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

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public String getCodigoActividadEconomica() {
		return codigoActividadEconomica;
	}

	public void setCodigoActividadEconomica(String codigoActividadEconomica) {
		this.codigoActividadEconomica = codigoActividadEconomica;
	}

	public BigDecimal getMontoUnitario() {
		return montoUnitario;
	}

	public void setMontoUnitario(BigDecimal montoUnitario) {
		this.montoUnitario = montoUnitario;
	}

	public Integer getNroRegistro() {
		return nroRegistro;
	}

	public void setNroRegistro(Integer nroRegistro) {
		this.nroRegistro = nroRegistro;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public Character getTipo() {
		return tipo;
	}

	public void setTipo(Character tipo) {
		this.tipo = tipo;
	}

	public Boolean getTipoComprobante() {
		return tipoComprobante;
	}

	public void setTipoComprobante(Boolean tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}



	public String getCorreoCliente() {
		return correoCliente;
	}



	public void setCorreoCliente(String correoCliente) {
		this.correoCliente = correoCliente;
	}
	
	
	
	
	
	
	

}
