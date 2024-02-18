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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Carmin
 */
@Entity
@Table(name = "recaudadores_comisiones", catalog = "exacta_tesla", schema = "tesla")
public class RecaudadorComisionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "recaudador_comision_id", nullable = false)
    private Long recaudadorComisionId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(nullable = false, precision = 17, scale = 2)
    private BigDecimal comision;
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
    @Column(nullable = false, length = 255)
    private String estado;
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private String transaccion;
    @JoinColumn(name = "tipo_comision_id", referencedColumnName = "dominio_id", nullable = false)
    @ManyToOne(optional = false)
    private DominioEntity tipoComision;
    @JsonIgnore
    @JoinColumn(name = "recaudador_id", referencedColumnName = "recaudador_id", nullable = false)
    @ManyToOne(optional = false)
    private RecaudadorEntity recaudador;

    public RecaudadorComisionEntity() {
    }

    public RecaudadorComisionEntity(Long recaudadorComisionId) {
        this.recaudadorComisionId = recaudadorComisionId;
    }

    public RecaudadorComisionEntity(Long recaudadorComisionId, BigDecimal comision, long usuarioCreacion, Date fechaCreacion, String estado, String transaccion) {
        this.recaudadorComisionId = recaudadorComisionId;
        this.comision = comision;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.transaccion = transaccion;
    }

    public Long getRecaudadorComisionId() {
        return recaudadorComisionId;
    }

    public void setRecaudadorComisionId(Long recaudadorComisionId) {
        this.recaudadorComisionId = recaudadorComisionId;
    }

    public BigDecimal getComision() {
        return comision;
    }

    public void setComision(BigDecimal comision) {
        this.comision = comision;
    }

    public Long getUsuarioCreacion() {
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

    public RecaudadorEntity getRecaudador() {
        return recaudador;
    }

    public void setRecaudador(RecaudadorEntity recaudadorId) {
        this.recaudador = recaudadorId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recaudadorComisionId != null ? recaudadorComisionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecaudadorComisionEntity)) {
            return false;
        }
        RecaudadorComisionEntity other = (RecaudadorComisionEntity) object;
        if ((this.recaudadorComisionId == null && other.recaudadorComisionId != null) || (this.recaudadorComisionId != null && !this.recaudadorComisionId.equals(other.recaudadorComisionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.RecaudadorComisionEntity[ recaudadorComisionId=" + recaudadorComisionId + " ]";
    }
    
}
