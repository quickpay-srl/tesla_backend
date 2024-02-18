/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.tesla.administracion.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "historicos_deudas", catalog = "exacta_tesla", schema = "tesla")

public class HistoricoDeudaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "historico_deuda_id", nullable = false)
    private Long historicoDeudaId;
    @Basic(optional = false)
    @Column(name = "deuda_cliente_id", nullable = false)
    private long deudaClienteId;
    @Basic(optional = false)
    @Column(name = "nro_registro", nullable = false)
    private int nroRegistro;
    @Basic(optional = false)
    @Column(name = "codigo_cliente", nullable = false, length = 15)
    private String codigoCliente;
    @Column(name = "nombre_cliente", length = 200)
    private String nombreCliente;
    @Column(name = "nro_documento", length = 15)
    private String nroDocumento;
    @Basic(optional = false)
    @Column(name = "tipo_servicio", nullable = false, length = 300)
    private String tipoServicio;
    @Basic(optional = false)
    @Column(name = "servicio", nullable = false, length = 300)
    private String servicio;
    @Basic(optional = false)
    @Column(nullable = false, length = 250)
    private String periodo;
    @Basic(optional = false)
    @Column(nullable = false)
    private Character tipo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;
    @Basic(optional = false)
    @Column(nullable = false, length = 250)
    private String concepto;
    @Column(name = "monto_unitario", precision = 17, scale = 2)
    private BigDecimal montoUnitario;
    @Column(name = "dato_extra", length = 250)
    private String datoExtra;
    @Basic(optional = false)
    @Column(name = "tipo_comprobante", nullable = false)
    private boolean tipoComprobante;
    @Column(name = "periodo_cabecera", length = 250)
    private String periodoCabecera;
    @Basic(optional = false)
    @Column(name = "usuario_creacion", nullable = false)
    private Long usuarioCreacion;
	@Basic(optional = false)
    @Column(name = "fecha_creacion", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
	@JsonIgnore
    @JoinColumn(name = "archivo_id", referencedColumnName = "archivo_id", nullable = false)
    @ManyToOne(optional = false)
    private ArchivoEntity archivoId;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "historicoDeudaId")
    private List<AccionEntity> accionEntityList;
    @Column(name = "es_postpago")
	private Boolean esPostpago;
    
    @Basic(optional = false)
    @Column(length = 150)
    private String direccion;

    @Basic(optional = false)
    @Column(length = 10)
    private String telefono;

    @Basic(optional = false)
    @Column(length = 15)
    private String nit;

    @Basic(optional = false)
    @Column(name = "sub_total", precision = 17, scale = 2)
    private BigDecimal subTotal;
    
    
    
    @Column(nullable = false, length = 15)
    private String estado;
    
    @Basic(optional = false)
	@Column(name = "codigo_actividad_economica", nullable = false, length = 10)	
	private String codigoActividadEconomica;


	@Column(name="correo_cliente",length = 50)
	private String correoCliente;
	
    public HistoricoDeudaEntity() {
    }

    public HistoricoDeudaEntity(Long historicoDeudaId) {
        this.historicoDeudaId = historicoDeudaId;
    }


    public HistoricoDeudaEntity(Long historicoDeudaId, long deudaClienteId, int nroRegistro, String codigoCliente, String tipoServicio, String periodo, Character tipo, BigDecimal cantidad, String concepto, boolean tipoComprobante, Long usuarioCreacion, Date fechaCreacion) {

        this.historicoDeudaId = historicoDeudaId;
        this.deudaClienteId = deudaClienteId;
        this.nroRegistro = nroRegistro;
        this.codigoCliente = codigoCliente;
        this.tipoServicio = tipoServicio;
        this.periodo = periodo;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.concepto = concepto;
        this.tipoComprobante = tipoComprobante;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getHistoricoDeudaId() {
        return historicoDeudaId;
    }

    public void setHistoricoDeudaId(Long historicoDeudaId) {
        this.historicoDeudaId = historicoDeudaId;
    }

    public long getDeudaClienteId() {
        return deudaClienteId;
    }

    public void setDeudaClienteId(long deudaClienteId) {
        this.deudaClienteId = deudaClienteId;
    }

    public int getNroRegistro() {
        return nroRegistro;
    }

    public void setNroRegistro(int nroRegistro) {
        this.nroRegistro = nroRegistro;
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

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
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

    public Character getTipo() {
        return tipo;
    }

    public void setTipo(Character tipo) {
        this.tipo = tipo;
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

    public String getDatoExtra() {
        return datoExtra;
    }

    public void setDatoExtra(String datoExtra) {
        this.datoExtra = datoExtra;
    }


     
  

	public boolean isTipoComprobante() {
		return tipoComprobante;
	}

	public void setTipoComprobante(boolean tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}


	public String getPeriodoCabecera() {
        return periodoCabecera;
    }

    public void setPeriodoCabecera(String periodoCabecera) {
        this.periodoCabecera = periodoCabecera;
    }

    public long getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(Long usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public ArchivoEntity getArchivoId() {
        return archivoId;
    }

    public void setArchivoId(ArchivoEntity archivoId) {
        this.archivoId = archivoId;
    }

    public List<AccionEntity> getAccionEntityList() {
        return accionEntityList;
    }

    public void setAccionEntityList(List<AccionEntity> accionEntityList) {
        this.accionEntityList = accionEntityList;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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
    
    
    public Boolean getEsPostpago() {
		return esPostpago;
	}

	public void setEsPostpago(Boolean esPostpago) {
		this.esPostpago = esPostpago;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (historicoDeudaId != null ? historicoDeudaId.hashCode() : 0);
        return hash;
    }

	
		
    public String getCorreoCliente() {
		return correoCliente;
	}

	public void setCorreoCliente(String correoCliente) {
		this.correoCliente = correoCliente;
	}

	public String getCodigoActividadEconomica() {
		return codigoActividadEconomica;
	}

	public void setCodigoActividadEconomica(String codigoActividadEconomica) {
		this.codigoActividadEconomica = codigoActividadEconomica;
	}

	@Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistoricoDeudaEntity)) {
            return false;
        }
        HistoricoDeudaEntity other = (HistoricoDeudaEntity) object;
        if ((this.historicoDeudaId == null && other.historicoDeudaId != null) || (this.historicoDeudaId != null && !this.historicoDeudaId.equals(other.historicoDeudaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.HistoricoDeudaEntity[ historicoDeudaId=" + historicoDeudaId + " ]";
    }
    
}
