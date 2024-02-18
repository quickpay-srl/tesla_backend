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
@Table(name = "cancelaciones", catalog = "exacta_tesla", schema = "tesla")

public class CancelacionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cancelacion_id", nullable = false)
    private Long cancelacionId;
    @Basic(optional = false)
    @Column(nullable = false, length = 255)
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "fecha_cancelacion", nullable = false, length = 255)
    private String fechaCancelacion;
    @Basic(optional = false)
    @Column(name = "usuario_creacion", nullable = false)
    private long usuarioCreacion;
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
    @Column(nullable = false, length = 15)
    private String estado;
    @JsonIgnore
    @JoinColumn(name = "comprobante_cobro_id", referencedColumnName = "comprobante_cobro_id", nullable = false)
    @ManyToOne(optional = false)
    private ComprobanteCobroEntity comprobanteCobroId;
    @JsonIgnore
    @JoinColumn(name = "motivo_cancelacion_id", referencedColumnName = "dominio_id", nullable = false)
    @ManyToOne(optional = false)
    private DominioEntity motivoCancelacionId;
    @JsonIgnore
    @JoinColumn(name = "tipo_cancelacion_id", referencedColumnName = "dominio_id")
    @ManyToOne
    private DominioEntity tipoCancelacionId;

    public CancelacionEntity() {
    }

    public CancelacionEntity(Long cancelacionId) {
        this.cancelacionId = cancelacionId;
    }

    public CancelacionEntity(Long cancelacionId, String descripcion, String fechaCancelacion, long usuarioCreacion, Date fechaCreacion, String estado) {
        this.cancelacionId = cancelacionId;
        this.descripcion = descripcion;
        this.fechaCancelacion = fechaCancelacion;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
    }

    public Long getCancelacionId() {
        return cancelacionId;
    }

    public void setCancelacionId(Long cancelacionId) {
        this.cancelacionId = cancelacionId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(String fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
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

    public ComprobanteCobroEntity getComprobanteCobroId() {
        return comprobanteCobroId;
    }

    public void setComprobanteCobroId(ComprobanteCobroEntity comprobanteCobroId) {
        this.comprobanteCobroId = comprobanteCobroId;
    }

    public DominioEntity getMotivoCancelacionId() {
        return motivoCancelacionId;
    }

    public void setMotivoCancelacionId(DominioEntity motivoCancelacionId) {
        this.motivoCancelacionId = motivoCancelacionId;
    }

    public DominioEntity getTipoCancelacionId() {
        return tipoCancelacionId;
    }

    public void setTipoCancelacionId(DominioEntity tipoCancelacionId) {
        this.tipoCancelacionId = tipoCancelacionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cancelacionId != null ? cancelacionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CancelacionEntity)) {
            return false;
        }
        CancelacionEntity other = (CancelacionEntity) object;
        if ((this.cancelacionId == null && other.cancelacionId != null) || (this.cancelacionId != null && !this.cancelacionId.equals(other.cancelacionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.CancelacionEntity[ cancelacionId=" + cancelacionId + " ]";
    }
    
}
