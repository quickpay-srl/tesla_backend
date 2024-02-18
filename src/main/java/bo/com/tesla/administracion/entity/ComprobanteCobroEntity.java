/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.tesla.administracion.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
@Table(name = "comprobantes_cobros", catalog = "exacta_tesla", schema = "tesla")

public class ComprobanteCobroEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "comprobante_cobro_id", nullable = false)
    private Long comprobanteCobroId;
    @Basic(optional = false)
    @Column(name = "nro_factura", nullable = false)
    private Long nroFactura;
    @Basic(optional = false)
    @Column(name = "fecha_factura", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFactura;
    @Column(name = "nit_cliente", length = 255)
    private String nitCliente;
    @Column(name = "nombre_cliente", length = 255)
    private String nombreCliente;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "monto_total", nullable = false, precision = 17, scale = 2)
    private BigDecimal montoTotal;
    @Column(name = "codigo_control", length = 15)
    private String codigoControl;
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
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comprobanteCobroId")
    private List<CancelacionEntity> cancelacionEntityList;
    @JsonIgnore
    @JoinColumn(name = "dosificacion_id", referencedColumnName = "dosificacion_id", nullable = false)
    @ManyToOne(optional = false)
    private DosificacionEntity dosificacionId;
    @JsonIgnore
    @JoinColumn(name = "entidad_id", referencedColumnName = "entidad_id", nullable = false)
    @ManyToOne(optional = false)
    private EntidadEntity entidadId;
    @JsonIgnore
    @JoinColumn(name = "sucursal_id", referencedColumnName = "sucursal_id", nullable = false)
    @ManyToOne(optional = false)
    private SucursalEntity sucursalId;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comprobanteCobroId")
    private List<DetalleComprobanteCobroEntity> detalleComprobanteCobroEntityList;

    @Column(length = 15)
    private String transaccion;

    public ComprobanteCobroEntity() {
    }

    public ComprobanteCobroEntity(Long comprobanteCobroId) {
        this.comprobanteCobroId = comprobanteCobroId;
    }

    public ComprobanteCobroEntity(Long comprobanteCobroId, Long nroFactura, Date fechaFactura, BigDecimal montoTotal, long usuarioCreacion, Date fechaCreacion, String estado) {
        this.comprobanteCobroId = comprobanteCobroId;
        this.nroFactura = nroFactura;
        this.fechaFactura = fechaFactura;
        this.montoTotal = montoTotal;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
    }

    public Long getComprobanteCobroId() {
        return comprobanteCobroId;
    }

    public void setComprobanteCobroId(Long comprobanteCobroId) {
        this.comprobanteCobroId = comprobanteCobroId;
    }

    public Long getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(Long nroFactura) {
        this.nroFactura = nroFactura;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public String getNitCliente() {
        return nitCliente;
    }

    public void setNitCliente(String nitCliente) {
        this.nitCliente = nitCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getCodigoControl() {
        return codigoControl;
    }

    public void setCodigoControl(String codigoControl) {
        this.codigoControl = codigoControl;
    }

    public Long getUsuarioCreacion() {
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

    public List<CancelacionEntity> getCancelacionEntityList() {
        return cancelacionEntityList;
    }

    public void setCancelacionEntityList(List<CancelacionEntity> cancelacionEntityList) {
        this.cancelacionEntityList = cancelacionEntityList;
    }

    public DosificacionEntity getDosificacionId() {
        return dosificacionId;
    }

    public void setDosificacionId(DosificacionEntity dosificacionId) {
        this.dosificacionId = dosificacionId;
    }

    public EntidadEntity getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(EntidadEntity entidadId) {
        this.entidadId = entidadId;
    }

    public SucursalEntity getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(SucursalEntity sucursalId) {
        this.sucursalId = sucursalId;
    }

    public List<DetalleComprobanteCobroEntity> getDetalleComprobanteCobroEntityList() {
        return detalleComprobanteCobroEntityList;
    }

    public void setDetalleComprobanteCobroEntityList(List<DetalleComprobanteCobroEntity> detalleComprobanteCobroEntityList) {
        this.detalleComprobanteCobroEntityList = detalleComprobanteCobroEntityList;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (comprobanteCobroId != null ? comprobanteCobroId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComprobanteCobroEntity)) {
            return false;
        }
        ComprobanteCobroEntity other = (ComprobanteCobroEntity) object;
        if ((this.comprobanteCobroId == null && other.comprobanteCobroId != null) || (this.comprobanteCobroId != null && !this.comprobanteCobroId.equals(other.comprobanteCobroId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.ComprobanteCobroEntity[ comprobanteCobroId=" + comprobanteCobroId + " ]";
    }
    
}
