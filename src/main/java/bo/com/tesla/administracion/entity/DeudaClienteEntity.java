
package bo.com.tesla.administracion.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.validation.annotation.Validated;

import bo.com.tesla.useful.config.BusinesException;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "deudas_clientes", catalog = "exacta_tesla", schema = "tesla")
public class DeudaClienteEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "deuda_cliente_id", nullable = false)
	private Long deudaClienteId;
	

	
	@Basic(optional = false)
	@Column(name = "nro_registro", nullable = false)
	private Integer nroRegistro;

	
	
	@Basic(optional = false)
	@Column(name = "codigo_cliente", nullable = false, length = 15)
	private String codigoCliente;

	
	@Column(name = "nombre_cliente", length = 200)
	private String nombreCliente;

	
	@Column(name = "nro_documento", length = 10)
	private String nroDocumento;

	
	@Column(length = 150)
	private String direccion;

	
	@Column(length = 10)
	private String telefono;

	
	@Column(length = 10)
	private String nit;

	
	@Basic(optional = false)
	@Column(length = 300, nullable = false)
	private String servicio;

	
	@Basic(optional = false)
	@Column(name = "tipo_servicio", nullable = false, length = 300)
	private String tipoServicio;

	@Basic(optional = false)
	@Column(nullable = false, length = 250)
	private String periodo;

	@Basic(optional = false)
	@Column(nullable = false, name = "tipo")
	private Character tipo;

	@Basic(optional = false)
	@Column(nullable = false, length = 250)
	private String concepto;

	@Basic(optional = false)
	@Column(name = "monto_unitario", nullable = false, precision = 17, scale = 2)	
	private BigDecimal montoUnitario;

	@Basic(optional = false)
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal cantidad;

	@Basic(optional = false)
	@Column(name = "sub_total", precision = 17, scale = 2)
	private BigDecimal subTotal;

	@Column(name = "dato_extras", length = 250)
	private String datoExtras;

	@Basic(optional = false)
	@Column(name = "tipo_comprobante")
	private Boolean tipoComprobante;

	@Column(name = "periodo_cabecera", length = 250)
	private String periodoCabecera;

	@JoinColumn(name = "archivo_id", referencedColumnName = "archivo_id", nullable = false)
    @ManyToOne( optional = false)
    private ArchivoEntity archivoId;

	@Column(name = "es_postpago")
	private Boolean esPostpago;
	
	
	@Basic(optional = false)
	@Column(name = "codigo_actividad_economica", nullable = false, length = 10)	
	private String codigoActividadEconomica;
	
	

	@Column(name="correo_cliente",length = 50)
	private String correoCliente;


	@Transient
	private Boolean esPorServicioWeb=false;

	public DeudaClienteEntity() {
	}

	public DeudaClienteEntity(Long deudaClienteId) {
		this.deudaClienteId = deudaClienteId;
	}
	
	

	public DeudaClienteEntity(Long deudaClienteId, int nroRegistro, String codigoCliente, String tipoServicio,
			String periodo, Character tipo, String concepto, BigDecimal cantidad) {
		this.deudaClienteId = deudaClienteId;
		this.nroRegistro = nroRegistro;
		this.codigoCliente = codigoCliente;
		this.tipoServicio = tipoServicio;
		this.periodo = periodo;
		this.tipo = tipo;
		this.concepto = concepto;
		this.cantidad = cantidad;
	
	}
	
	
	

	public Boolean getEsPostpago() {
		return esPostpago;
	}

	public void setEsPostpago(Boolean esPostpago)throws BusinesException {
		
		if (/*esPostpago.toString().isBlank() ||*/ esPostpago.toString().isEmpty()) {
			this.esPostpago = null;
			throw new BusinesException(
					"El campo de verificacion de POST/PRE(PAGO) no puede ser nulo");
			

		} else {
			this.esPostpago = esPostpago;
		}
		
	}

	public Long getDeudaClienteId() {
		return deudaClienteId;
	}

	public void setDeudaClienteId(Long deudaClienteId) {
		this.deudaClienteId = deudaClienteId;
	}

	

	public ArchivoEntity getArchivoId() {
		return archivoId;
	}

	public void setArchivoId(ArchivoEntity archivoId) {
		this.archivoId = archivoId;
	}

	public Integer getNroRegistro() {
		return nroRegistro;
	}

	public void setNroRegistro(Integer nroRegistro) throws BusinesException {
		if(this.esPorServicioWeb) {
			this.nroRegistro = 0;
		}else {
			if (nroRegistro==null || nroRegistro<=0) {
				this.nroRegistro = null;
				throw new BusinesException(
						"El campo Nro Registro no puede ser nulo");
				//throw new Exception("El campo Nro Registro no puede ser nulo");
			} else {
				this.nroRegistro = nroRegistro;
			}	
		}
		

	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(String codigoCliente) throws BusinesException {
		if (/*codigoCliente.isBlank() ||*/ codigoCliente.isEmpty()) {
			this.codigoCliente = null;
			throw new BusinesException(
					"El campo Codigo Cliente no puede ser nulo");
			
			//throw new Exception("El campo Nro Registro no puede ser nulo");
		} else {
			this.codigoCliente = codigoCliente;
		}

	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getNroDocumento() {
		return nroDocumento;
	}

	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) throws BusinesException {
		if (/*servicio.isBlank() || */servicio.isEmpty()) {
			this.servicio = null;
			throw new BusinesException(
					"El campo Servicio no puede ser nulo");
			
			//throw new Exception("El campo Servicio no puede ser nulo");
		} else {
			this.servicio = servicio;
		}

	}

	public String getTipoServicio() {
		return tipoServicio;
	}

	public void setTipoServicio(String tipoServicio) throws BusinesException {
		if (/*tipoServicio.isBlank() ||*/ tipoServicio.isEmpty()) {
			this.tipoServicio = null;
			throw new BusinesException(
					"El campo Tipo Servicio no puede ser nulo");
			
			//throw new Exception("El campo Tipo Servicio no puede ser nulo");
		} else {
			this.tipoServicio = tipoServicio;
		}

	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) throws BusinesException {
		if (/*periodo.isBlank() ||*/ periodo.isEmpty()) {
			this.periodo = null;
			throw new BusinesException(
					"El campo Periodo no puede ser nulo");
			
			//throw new Exception("El campo Periodo no puede ser nulo");
		} else {
			this.periodo = periodo;
		}

	}

	public Character getTipo() {
		return tipo;
	}

	public void setTipo(Character tipo) throws BusinesException {
		if (tipo.toString().isEmpty() || tipo==null) {
			this.tipo = null;
			throw new BusinesException(
					"El campo Tipo Secciòn no puede ser nulo");			
			
		} else {
			this.tipo = tipo.toString().toUpperCase().charAt(0);
		}

	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) throws BusinesException {
		
		if ( concepto.isEmpty() || concepto==null) {
			
			this.concepto = null;
			throw new BusinesException(
					"El campo conceto no puede ser nulo");		
		

		} else {		
			this.concepto = concepto;
		}

	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) throws BusinesException {
		if (/*cantidad.toString().isBlank() || */cantidad.toString().isEmpty()) {
			this.cantidad = null;			
			throw new BusinesException("El campo cantidad no puede ser nulo");
		} else {
			this.cantidad = cantidad;
		}

	}

	public BigDecimal getMontoUnitario() {
		return montoUnitario;
	}

	public void setMontoUnitario(BigDecimal montoUnitario) throws BusinesException {
		if ( montoUnitario.toString().isEmpty()) {
			this.montoUnitario = null;
			throw new BusinesException("El campo monto unitario no puede ser nulo");
		} else {
			this.montoUnitario = montoUnitario;
		}

	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) throws BusinesException {
		if (subTotal==null || subTotal.toString().isEmpty()) {
			this.subTotal = null;
			throw new BusinesException("El campo sub-total no puede ser nulo");
		} else {
			this.subTotal = subTotal;
		}

	}

	public String getDatoExtras() {
		return datoExtras;
	}

	public void setDatoExtras(String datoExtras) {
		this.datoExtras = datoExtras;
	}

	public Boolean getTipoComprobante() {
		return tipoComprobante;
	}

	public void setTipoComprobante(Boolean tipoComprobante) throws BusinesException {
		if (tipoComprobante.toString().isEmpty()) {
			this.tipoComprobante = null;
			throw new BusinesException("El campo Tipo Comprobante no puede ser nulo");
		} else {
			this.tipoComprobante = tipoComprobante;
		}

	}

	

	public String getPeriodoCabecera() {
		return periodoCabecera;
	}

	public void setPeriodoCabecera(String periodoCabecera) {
		this.periodoCabecera = periodoCabecera;
	}

	

	@Override
	public String toString() {
		return "bo.com.tesla.administracion.entity.DeudaClienteEntity[ deudaClienteId=" + deudaClienteId + " ]";
	}

	public String getCodigoActividadEconomica() {
		return codigoActividadEconomica;
	}

	public void setCodigoActividadEconomica(String codigoActividadEconomica) throws BusinesException {
		if ( codigoActividadEconomica.isEmpty() || codigoActividadEconomica==null ) {
			this.codigoActividadEconomica = null;
			throw new BusinesException(
					"El campo 'Código Actividad Economica' no puede ser nulo");			
		
		} else {
			this.codigoActividadEconomica = codigoActividadEconomica;
		}
		
	}

	public String getCorreoCliente() {
		return correoCliente;
	}

	public void setCorreoCliente(String correoCliente) {
		this.correoCliente = correoCliente;
	}

	public Boolean getEsPorServicioWeb() {
		return esPorServicioWeb;
	}

	public void setEsPorServicioWeb(Boolean esPorServicioWeb) {
		this.esPorServicioWeb = esPorServicioWeb;
	}
	
	
	
	
	
	
	
	

}
