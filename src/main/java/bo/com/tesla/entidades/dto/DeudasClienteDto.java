package bo.com.tesla.entidades.dto;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class DeudasClienteDto {

	public Long archivoId;
	public String servicio;
	public String tipoServicio;
	public String periodo;
	public String codigoCliente;
	public String nroFactura;
	public String nit;
	public String direccion;
	public String nroDocumento;
	public String telefono;
	public String nombreCliente;	
	public String key;
	//@Digits(integer=17, fraction=2)
	public BigDecimal total;
	public Boolean esPostpago;
	public String codigoActividadEconomica;
	public String cajero;
	@JsonFormat( pattern = "dd/MM/yyyy",timezone="America/La_Paz")
	public Date fechaCreacion;
	public String estado;
	public String nombreRecaudadora;
	public String metodoCobro;
	public BigDecimal comision;
	public BigDecimal totalSinComision;
	public BigDecimal cantidad;
	public BigDecimal montoUnitario;
	public Integer nroRegistro;
	public BigDecimal subTotal;
	public Character tipo;
	public Boolean tipoComprobante;
	public String correoCliente;
	

	public List<ConceptoDto> conceptoLisit;

	public DeudasClienteDto() {

	}

	/*
	 * Constructor utilizado para la consulta gropByDeudasClientes de la clase
	 * IDeudaCliente
	 * 
	 * @author aCallejas
	 */
	public DeudasClienteDto(Long archivoId, String servicio, String tipoServicio, String periodo, String codigoCliente,
			 BigDecimal total,String metodoCobro) {

		this.archivoId = archivoId;
		this.servicio = servicio;
		this.tipoServicio = tipoServicio;
		this.periodo = periodo;
		this.codigoCliente = codigoCliente;

		this.total =  total;
		this.metodoCobro = metodoCobro;
		//this.totalSinComision = this.total.subtract(comision);

	}

	/*
	 * Constructor utilizado para la consulta de reporte
	 * 
	 * @author aCallejas
	 */

	public DeudasClienteDto( 
			String servicio, String tipoServicio, String periodo,Date fechaCreacion, String cajero, String nombreCliente,
			BigDecimal total, String metodoCobro) {

		this.servicio = servicio;
		this.tipoServicio = tipoServicio;
		this.periodo = periodo;
		this.nombreCliente = nombreCliente;
		this.total = total;
		this.cajero = cajero;
		this.fechaCreacion=fechaCreacion;
		this.metodoCobro = metodoCobro;
		this.totalSinComision = total.subtract(comision);

	}
	
	public DeudasClienteDto(Long archivoId, 
			String codigoCliente,String servicio,
			String tipoServicio,String periodo, 
			String estado,String cajero,String nombreClientePago,Date fechaCreacion,BigDecimal total,String nombreCliente,
							String nombreRecaudadora,String metodoCobro) {
		
		this.archivoId=archivoId;
		this.codigoCliente=codigoCliente;
		this.servicio=servicio;
		this.tipoServicio=tipoServicio;
		this.periodo=periodo;
		this.estado=estado;
		if(nombreClientePago==null||nombreClientePago=="") {
			this.nombreCliente=nombreCliente;	
		}else {
			this.nombreCliente=nombreClientePago;
		}
		
		
		this.fechaCreacion=fechaCreacion;
		this.total=total;
		this.cajero=cajero;
		this.nombreRecaudadora=nombreRecaudadora;
		this.metodoCobro = metodoCobro;
		this.totalSinComision = total.subtract(comision);
		
	}



	public DeudasClienteDto(
			Long archivoId,
			String codigoCliente,
			String nroFactura,
			String servicio,
			String tipoServicio,
			String periodo,
			String estado,
			String nombreClientePago,
			Date fechaCreacion,
			BigDecimal total,
			String nombreRecaudadora,
			BigDecimal comision,
			String metodoCobro
	) {
		
	
		this.archivoId=archivoId;
		this.codigoCliente=codigoCliente;
		this.servicio=servicio;
		this.tipoServicio=tipoServicio;
		this.periodo=periodo;
		this.nombreCliente=nombreClientePago;
		this.fechaCreacion=fechaCreacion;
		this.total=total;	
		this.nombreRecaudadora=nombreRecaudadora;
		this.comision=comision;
		this.nroFactura = nroFactura;
		
		if(estado.equals("ACTIVO")) {
			this.estado="POR COBRAR";
			this.fechaCreacion=null;
			this.total=null;	
			this.nombreRecaudadora=null;
			this.comision=null;
			
		}else if(estado.equals("COBRADO")) {
			this.estado="COBRADOS";
		}else if(estado.equals("ANULADO")) {
			this.estado="ANULADO";
			
		}else if(estado.equals("ERRONEO")) {
			this.estado = "ERRONEO";
		}
		this.metodoCobro = metodoCobro;
		this.totalSinComision = total.subtract(comision);
	}
	
	
	/*se utiliza en le metodo  findDeudasByArchivoIdAndEstadoForEntidad IHistoricoDeudaDao
	 * */
		
	public DeudasClienteDto(
			Long archivoId, 
			String codigoCliente,
			String servicio,
			String tipoServicio,
			String periodo,
			String estado,
			Date fechaCreacion,
			BigDecimal total,
			String nombreCliente,
			String nombreRecaudadora,
			BigDecimal comision,
			String metodoCobro
			) {
		
		
		this.archivoId=archivoId;
		this.codigoCliente=codigoCliente;
		this.servicio=servicio;
		this.tipoServicio=tipoServicio;
		this.periodo=periodo;
		this.nombreCliente=nombreCliente;
		this.fechaCreacion=fechaCreacion;
		this.total=total;	
		this.nombreRecaudadora=nombreRecaudadora;
		this.comision=comision;
		
		if(estado.equals("ACTIVO")) {
			this.estado="POR COBRAR";
			this.fechaCreacion=null;
			this.total=null;	
			this.nombreRecaudadora=null;
			this.comision=null;
			
		}else if(estado.equals("COBRADO")) {
			this.estado="COBRADOS";
		}else if(estado.equals("ANULADO")) {
			this.estado="ANULADO";
			
		}else if(estado.equals("ERRONEO")) {
			this.estado = "ERRONEO";
		}
		else if(estado.equals("HISTORICO")) {
			this.estado = "HISTORICO";
		}
		this.metodoCobro = metodoCobro;
		this.totalSinComision = total.subtract(comision);

	}

	public String getNroFactura() {
		return nroFactura;
	}

	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
	}

	public String getMetodoCobro() {
		return metodoCobro;
	}

	public void setMetodoCobro(String metodoCobro) {
		this.metodoCobro = metodoCobro;
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

	public List<ConceptoDto> getConceptoLisit() {
		return conceptoLisit;
	}

	public void setConceptoLisit(List<ConceptoDto> conceptoLisit) {
		this.conceptoLisit = conceptoLisit;
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
	
	
	
	
	
	
	

}
