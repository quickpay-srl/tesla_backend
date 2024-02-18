/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.tesla.administracion.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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

/**
 *
 * @author Carmin
 */
@Entity
@Table(name = "entidades_comisiones", catalog = "exacta_tesla", schema = "tesla")

public class EntidadComisionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "entidad_comision_id", nullable = false)
    private Long entidadComisionId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(nullable = false, precision = 17, scale = 2)
    private BigDecimal comision;
    @Basic(optional = false)
    @Column(name = "usuario_creacion", nullable = false)
    private long usuarioCreacion;
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
    @Column(nullable = false, length = 255)
    private String estado;
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private String transaccion;
    @JoinColumn(name = "tipo_comision_id", referencedColumnName = "dominio_id", nullable = false)
    @ManyToOne(optional = false)
    private DominioEntity tipoComision;
    @JoinColumn(name = "entidad_id", referencedColumnName = "entidad_id", nullable = false)
    @ManyToOne(optional = false)
    private EntidadEntity entidad;

    public EntidadComisionEntity() {
    }

    public EntidadComisionEntity(Long entidadComisionId) {
        this.entidadComisionId = entidadComisionId;
    }

    public EntidadComisionEntity(Long entidadComisionId, BigDecimal comision, long usuarioCreacion, Date fechaCreacion, String estado, String transaccion) {
        this.entidadComisionId = entidadComisionId;
        this.comision = comision;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.transaccion = transaccion;
    }

    public Long getEntidadComisionId() {
        return entidadComisionId;
    }

    public void setEntidadComisionId(Long entidadComisionId) {
        this.entidadComisionId = entidadComisionId;
    }

    public BigDecimal getComision() {
        return comision;
    }

    public void setComision(BigDecimal comision) {
        this.comision = comision;
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

    public DominioEntity getTipoComision() {
        return tipoComision;
    }

    public void setTipoComision(DominioEntity tipoComisionId) {
        this.tipoComision = tipoComisionId;
    }

    public EntidadEntity getEntidad() {
        return entidad;
    }

    public void setEntidad(EntidadEntity entidadId) {
        this.entidad = entidadId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (entidadComisionId != null ? entidadComisionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EntidadComisionEntity)) {
            return false;
        }
        EntidadComisionEntity other = (EntidadComisionEntity) object;
        if ((this.entidadComisionId == null && other.entidadComisionId != null) || (this.entidadComisionId != null && !this.entidadComisionId.equals(other.entidadComisionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.EntidadComisionEntity[ entidadComisionId=" + entidadComisionId + " ]";
    }

}
