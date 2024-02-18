/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.tesla.administracion.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "detalles_comprobantes_cobros", catalog = "exacta_tesla", schema = "tesla")

public class DetalleComprobanteCobroEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "detalle_comprobante_cobro_id", nullable = false)
    private Long detalleComprobanteCobroId;
    @Basic(optional = false)
    @Column(name = "usuario_creacion", nullable = false)
    private Long usuarioCreacion;
    @Basic(optional = false)
    @Column(name = "fecha_creacion", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "usuario_modificacion")
    private BigInteger usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Basic(optional = false)
    @Column(nullable = false, length = 255)
    private String estado;
    @JsonIgnore
    @JoinColumn(name = "cobro_cliente_id", referencedColumnName = "cobro_cliente_id", nullable = false)
    @ManyToOne(optional = false)
    private CobroClienteEntity cobroClienteId;
    @JsonIgnore
    @JoinColumn(name = "comprobante_cobro_id", referencedColumnName = "comprobante_cobro_id", nullable = false)
    @ManyToOne(optional = false)
    private ComprobanteCobroEntity comprobanteCobroId;
    @JsonIgnore
    @JoinColumn(name = "transaccion_cobro_id", referencedColumnName = "transaccion_cobro_id", nullable = false)
    @ManyToOne(optional = false)
    private TransaccionCobroEntity transaccionCobroId;

    @Column(length = 15)
    private String transaccion;

    public DetalleComprobanteCobroEntity() {
    }

    public DetalleComprobanteCobroEntity(Long detalleComprobanteCobroId) {
        this.detalleComprobanteCobroId = detalleComprobanteCobroId;
    }

    public DetalleComprobanteCobroEntity(Long detalleComprobanteCobroId, Long usuarioCreacion, Date fechaCreacion, String estado) {
        this.detalleComprobanteCobroId = detalleComprobanteCobroId;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
    }

    public Long getDetalleComprobanteCobroId() {
        return detalleComprobanteCobroId;
    }

    public void setDetalleComprobanteCobroId(Long detalleComprobanteCobroId) {
        this.detalleComprobanteCobroId = detalleComprobanteCobroId;
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

    public BigInteger getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(BigInteger usuarioModificacion) {
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

    public CobroClienteEntity getCobroClienteId() {
        return cobroClienteId;
    }

    public void setCobroClienteId(CobroClienteEntity cobroClienteId) {
        this.cobroClienteId = cobroClienteId;
    }

    public ComprobanteCobroEntity getComprobanteCobroId() {
        return comprobanteCobroId;
    }

    public void setComprobanteCobroId(ComprobanteCobroEntity comprobanteCobroId) {
        this.comprobanteCobroId = comprobanteCobroId;
    }

    public TransaccionCobroEntity getTransaccionCobroId() {
        return transaccionCobroId;
    }

    public void setTransaccionCobroId(TransaccionCobroEntity transaccionCobroId) {
        this.transaccionCobroId = transaccionCobroId;
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
        hash += (detalleComprobanteCobroId != null ? detalleComprobanteCobroId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleComprobanteCobroEntity)) {
            return false;
        }
        DetalleComprobanteCobroEntity other = (DetalleComprobanteCobroEntity) object;
        if ((this.detalleComprobanteCobroId == null && other.detalleComprobanteCobroId != null) || (this.detalleComprobanteCobroId != null && !this.detalleComprobanteCobroId.equals(other.detalleComprobanteCobroId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.DetalleComprobanteCobroEntity[ detalleComprobanteCobroId=" + detalleComprobanteCobroId + " ]";
    }
    
}
