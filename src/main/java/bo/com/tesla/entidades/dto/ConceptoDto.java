package bo.com.tesla.entidades.dto;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import bo.com.tesla.administracion.entity.ArchivoEntity;

public class ConceptoDto {

	
	

	
	public Integer nroRegistro;
	public String nombreCliente;	
	public String nroDocumento;
	public String direccion;
	public String telefono;
	public String nit;
	public Character tipo;
	@NotEmpty(message = "no puede estar vacio")
	public String concepto;
	@NotEmpty(message = "no puede estar vacio")
	public BigDecimal montoUnitario;
	@NotEmpty(message = "no puede estar vacio")
	public BigDecimal cantidad;
	@NotEmpty(message = "no puede estar vacio")
	public BigDecimal subTotal;
	public String datoExtras;
	public Boolean tipoComprobante;
	public String periodoCabecera;
    public Long archivoId;
    public Boolean esPostpago;
    public String correoCliente;
	
	
    
    
	
    public ConceptoDto() {

	}

	public ConceptoDto(Integer nroRegistro, String nombreCliente, String nroDocumento, String direccion,
			String telefono, String nit, Character tipo, String concepto, BigDecimal montoUnitario, BigDecimal cantidad,
			BigDecimal subTotal, String datoExtras, Boolean tipoComprobante, String periodoCabecera,Boolean esPostpago) {
		
		this.nroRegistro = nroRegistro;
		this.nombreCliente = nombreCliente;
		this.nroDocumento = nroDocumento;
		this.direccion = direccion;
		this.telefono = telefono;
		this.nit = nit;
		this.tipo = tipo;
		this.concepto = concepto;
		this.montoUnitario = montoUnitario;
		this.cantidad = cantidad;
		this.subTotal = subTotal;
		this.datoExtras = datoExtras;
		this.tipoComprobante = tipoComprobante;
		this.periodoCabecera = periodoCabecera;
		this.esPostpago=esPostpago;
	
	}
	
	public ConceptoDto(Integer nroRegistro, String nombreCliente, String nroDocumento, String direccion,
			String telefono, String nit, Character tipo, String concepto, BigDecimal montoUnitario, BigDecimal cantidad,
			BigDecimal subTotal, String datoExtras, Boolean tipoComprobante, String periodoCabecera,Boolean esPostpago,String correoCliente) {
		
		this.nroRegistro = nroRegistro;
		this.nombreCliente = nombreCliente;
		this.nroDocumento = nroDocumento;
		this.direccion = direccion;
		this.telefono = telefono;
		this.nit = nit;
		this.tipo = tipo;
		this.concepto = concepto;
		this.montoUnitario = montoUnitario;
		this.cantidad = cantidad;
		this.subTotal = subTotal;
		this.datoExtras = datoExtras;
		this.tipoComprobante = tipoComprobante;
		this.periodoCabecera = periodoCabecera;
		this.esPostpago=esPostpago;
		this.correoCliente=correoCliente;
	}
	
	
	
	
	
	
	

	
}
