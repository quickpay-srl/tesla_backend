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
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "transacciones_cobros", catalog = "exacta_tesla", schema = "tesla")

public class TransaccionCobroEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "transaccion_cobro_id", nullable = false)
    private Long transaccionCobroId;
    @Basic(optional = false)
    @Column(name = "tipo_servicio", nullable = false, length = 300)
    private String tipoServicio;
    @Basic(optional = false)
    @Column(nullable = false, length = 300)
    private String servicio;
    @Basic(optional = false)
    @Column(nullable = false, length = 250)
    private String periodo;
    @Basic(optional = false)
    @Column(name = "codigo_cliente", nullable = false, length = 15)
    private String codigoCliente;
    @Basic(optional = false)
    @Column(name = "usuario_creacion", nullable = false)
    private Long usuarioCreacion;
    @Basic(optional = false)
    @Column(name = "fecha_creacion", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "usuario_modificacion")
    private Long usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private String estado;
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private String transaccion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "total_deuda", nullable = false, precision = 17, scale = 2)
    private BigDecimal totalDeuda;
    @Basic(optional = false)
    @Column(name = "nombre_cliente_pago", nullable = false, length = 200)
    private String nombreClientePago;
    @Basic(optional = false)
    @Column(name = "nro_documento_cliente_pago", nullable = false, length = 15)
    private String nroDocumentoClientePago;
    @Basic(optional = false)
    @Column(nullable = false, precision = 17, scale = 2)
    private BigDecimal comision;
    @Basic(optional = false)
    @Column(name = "comision_recaudacion", nullable = false, precision = 17, scale = 2)
    private BigDecimal comisionRecaudacion;
    @Column(name = "nombre_cliente_archivo", length = 200)
    private String nombreClienteArchivo;
    @Column(name = "nro_documento_cliente_archivo", length = 15)
    private String nroDocumentoClienteArchivo;


    @JsonIgnore
    @OneToMany(mappedBy = "transaccionCobro", fetch= FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<CobroClienteEntity> cobroClienteEntityList;
    @JsonIgnore
    @JoinColumn(name = "archivo_id", referencedColumnName = "archivo_id", nullable = false)
    @ManyToOne(optional = false)
    private ArchivoEntity archivoId;
    @JsonIgnore
    @JoinColumn(name = "entidad_id", referencedColumnName = "entidad_id", nullable = false)
    @ManyToOne(optional = false)
    private EntidadEntity entidadId;
    @JsonIgnore
    @JoinColumn(name = "recaudador_id", referencedColumnName = "recaudador_id", nullable = false)
    @ManyToOne(optional = false)
    private RecaudadorEntity recaudador;
    /****************************************************
    14/05/2021 - Segun reunión.
    Habilitar obligoriedad una vez implementado las comisiones
    *****************************************************
    @JoinColumn(name = "entidad_comision_id", referencedColumnName = "entidad_comision_id", nullable = false)
    @ManyToOne(optional = false)
    private EntidadComisionEntity entidadComision;
    @JoinColumn(name = "recaudador_comision_id", referencedColumnName = "recaudador_comision_id", nullable = false)
    @ManyToOne(optional = false)
    private RecaudadorComisionEntity recaudadorComision;
    *****************************************************/

    @JsonIgnore
    @JoinColumn(name = "entidad_comision_id", referencedColumnName = "entidad_comision_id")
    @ManyToOne
    private EntidadComisionEntity entidadComision;
    @JsonIgnore
    @JoinColumn(name = "recaudador_comision_id", referencedColumnName = "recaudador_comision_id")
    @ManyToOne
    private RecaudadorComisionEntity recaudadorComision;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transaccionCobroId")
    private List<DetalleComprobanteCobroEntity> detalleComprobanteCobroEntityList;
    @JsonIgnore
    @JoinColumn(name = "metodo_cobro_id", referencedColumnName = "dominio_id", nullable = false)
    @ManyToOne(optional = false)
    private DominioEntity metodoCobro;
    @JsonIgnore
    @JoinColumn(name = "modalidad_facturacion_id", referencedColumnName = "dominio_id")
    @ManyToOne
    private DominioEntity modalidadFacturacion;
    @Column(name = "factura_id")
    private Long facturaId;

    @Column(name = "codigo_actividad_economica", length = 10)
    private String codigoActividadEconomica;

    @Basic(optional = false)
    @Column(name = "correo_cliente", nullable = false, length = 50)
    private String correoCliente;

    @Column(name = "datosconfirmado_qr_id")
    private Long datosconfirmadoQrId;

    @Column(name = "deuda_cliente_id")
    private Long deudaClienteId;

    public Long getDeudaClienteId() {
        return deudaClienteId;
    }

    public void setDeudaClienteId(Long deudaClienteId) {
        this.deudaClienteId = deudaClienteId;
    }

    public Long getDatosconfirmadoQrId() {
        return datosconfirmadoQrId;
    }

    public void setDatosconfirmadoQrId(Long datosconfirmadoQrId) {
        this.datosconfirmadoQrId = datosconfirmadoQrId;
    }

    public void setUsuarioCreacion(Long usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }


    public TransaccionCobroEntity() {
    }

    public TransaccionCobroEntity(Long transaccionCobroId) {
        this.transaccionCobroId = transaccionCobroId;
    }

    public TransaccionCobroEntity(Long transaccionCobroId, String tipoServicio, String servicio, String periodo, String codigoCliente, long usuarioCreacion, Date fechaCreacion, String estado, String transaccion, BigDecimal totalDeuda, String nombreClientePago, String nroDocumentoClientePago, BigDecimal comision) {
        this.transaccionCobroId = transaccionCobroId;
        this.tipoServicio = tipoServicio;
        this.servicio = servicio;
        this.periodo = periodo;
        this.codigoCliente = codigoCliente;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.transaccion = transaccion;
        this.totalDeuda = totalDeuda;
        this.nombreClientePago = nombreClientePago;
        this.nroDocumentoClientePago = nroDocumentoClientePago;
        this.comision = comision;
    }

    public Long getTransaccionCobroId() {
        return transaccionCobroId;
    }

    public void setTransaccionCobroId(Long transaccionCobroId) {
        this.transaccionCobroId = transaccionCobroId;
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

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public long getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(long usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Long getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(Long usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public BigDecimal getTotalDeuda() {
        return totalDeuda;
    }

    public void setTotalDeuda(BigDecimal totalDeuda) {
        this.totalDeuda = totalDeuda;
    }

    public String getNombreClientePago() {
        return nombreClientePago;
    }

    public void setNombreClientePago(String nombreClientePago) {
        this.nombreClientePago = nombreClientePago;
    }

    public String getNroDocumentoClientePago() {
        return nroDocumentoClientePago;
    }

    public void setNroDocumentoClientePago(String nroDocumentoClientePago) {
        this.nroDocumentoClientePago = nroDocumentoClientePago;
    }

    public BigDecimal getComision() {
        return comision;
    }

    public void setComision(BigDecimal comision) {
        this.comision = comision;
    }

    public String getNombreClienteArchivo() {
        return nombreClienteArchivo;
    }

    public void setNombreClienteArchivo(String nombreClienteArchivo) {
        this.nombreClienteArchivo = nombreClienteArchivo;
    }

    public String getNroDocumentoClienteArchivo() {
        return nroDocumentoClienteArchivo;
    }

    public void setNroDocumentoClienteArchivo(String nroDocumentoClienteArchivo) {
        this.nroDocumentoClienteArchivo = nroDocumentoClienteArchivo;
    }

    public List<CobroClienteEntity> getCobroClienteEntityList() {
        return cobroClienteEntityList;
    }

    public void setCobroClienteEntityList(List<CobroClienteEntity> cobroClienteEntityList) {
        this.cobroClienteEntityList = cobroClienteEntityList;
    }

    public ArchivoEntity getArchivoId() {
        return archivoId;
    }

    public void setArchivoId(ArchivoEntity archivoId) {
        this.archivoId = archivoId;
    }

    public EntidadEntity getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(EntidadEntity entidadId) {
        this.entidadId = entidadId;
    }

    public RecaudadorEntity getRecaudador() {
        return recaudador;
    }

    public void setRecaudador(RecaudadorEntity recaudadorId) {
        this.recaudador = recaudadorId;
    }

    public List<DetalleComprobanteCobroEntity> getDetalleComprobanteCobroEntityList() {
        return detalleComprobanteCobroEntityList;
    }

    public void setDetalleComprobanteCobroEntityList(List<DetalleComprobanteCobroEntity> detalleComprobanteCobroEntityList) {
        this.detalleComprobanteCobroEntityList = detalleComprobanteCobroEntityList;
    }

    public EntidadComisionEntity getEntidadComision() {
        return entidadComision;
    }

    public void setEntidadComision(EntidadComisionEntity entidadComision) {
        this.entidadComision = entidadComision;
    }
    
    
    
    

    public BigDecimal getComisionRecaudacion() {

		return comisionRecaudacion;
	}

	public void setComisionRecaudacion(BigDecimal comisionRecaudacion) {
		this.comisionRecaudacion = comisionRecaudacion;
	}



    public RecaudadorComisionEntity getRecaudadorComision() {
        return recaudadorComision;
    }

    public void setRecaudadorComision(RecaudadorComisionEntity recaudadorComision) {
        this.recaudadorComision = recaudadorComision;
    }

    public DominioEntity getMetodoCobro() {
        return metodoCobro;
    }

    public void setMetodoCobro(DominioEntity metodoCobroId) {
        this.metodoCobro = metodoCobroId;
    }

    public DominioEntity getModalidadFacturacion() {
        return modalidadFacturacion;
    }

    public void setModalidadFacturacion(DominioEntity modalidadFacturacion) {
        this.modalidadFacturacion = modalidadFacturacion;
    }

    public Long getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(Long facturaId) {
        this.facturaId = facturaId;
    }

    public String getCodigoActividadEconomica() {
        return codigoActividadEconomica;
    }

    public void setCodigoActividadEconomica(String codigoActividadEconomica) {
        this.codigoActividadEconomica = codigoActividadEconomica;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transaccionCobroId != null ? transaccionCobroId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransaccionCobroEntity)) {
            return false;
        }
        TransaccionCobroEntity other = (TransaccionCobroEntity) object;
        if ((this.transaccionCobroId == null && other.transaccionCobroId != null) || (this.transaccionCobroId != null && !this.transaccionCobroId.equals(other.transaccionCobroId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.TransaccionCobroEntity[ transaccionCobroId=" + transaccionCobroId + " ]";
    }
    
}