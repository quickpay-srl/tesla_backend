package bo.com.tesla.administracion.entity;

import java.io.Serializable;
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
@Table(name = "end_points_transacciones_cobros", catalog = "exacta_tesla", schema = "tesla")
@NamedQueries({
        @NamedQuery(name = "EndPointTransaccionCobroEntity.findAll", query = "SELECT e FROM EndPointTransaccionCobroEntity e")})
public class EndPointTransaccionCobroEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "end_point_transaccion_cobro_id", nullable = false)
    private Long endPointTransaccionCobroId;
    @Basic(optional = false)
    @Column(name = "estado_codigo", nullable = false)
    private Integer estadoCodigo;
    @Column(length = 2147483647)
    private String detalle;
    @Basic(optional = false)
    @Column(name = "fecha_creacion", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @JoinColumn(name = "end_point_entidad_id", referencedColumnName = "end_point_entidad_id", nullable = false)
    @ManyToOne(optional = false)
    private EndPointEntidadEntity endPointEntidadId;
    @JoinColumn(name = "transaccion_cobro_id", referencedColumnName = "transaccion_cobro_id", nullable = false)
    @ManyToOne(optional = false)
    private TransaccionCobroEntity transaccionCobroId;

    public EndPointTransaccionCobroEntity() {
    }

    public EndPointTransaccionCobroEntity(Long endPointTransaccionCobroId) {
        this.endPointTransaccionCobroId = endPointTransaccionCobroId;
    }

    public EndPointTransaccionCobroEntity(Long endPointTransaccionCobroId, Integer estadoCodigo, String detalle, Date fechaCreacion) {
        this.endPointTransaccionCobroId = endPointTransaccionCobroId;
        this.estadoCodigo = estadoCodigo;
        this.detalle = detalle;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getEndPointTransaccionCobroId() {
        return endPointTransaccionCobroId;
    }

    public void setEndPointTransaccionCobroId(Long endPointTransaccionCobroId) {
        this.endPointTransaccionCobroId = endPointTransaccionCobroId;
    }

    public Integer getEstadoCodigo() {
        return estadoCodigo;
    }

    public void setEstadoCodigo(Integer estadoCodigo) {
        this.estadoCodigo = estadoCodigo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public EndPointEntidadEntity getEndPointEntidadId() {
        return endPointEntidadId;
    }

    public void setEndPointEntidadId(EndPointEntidadEntity endPointEntidadId) {
        this.endPointEntidadId = endPointEntidadId;
    }

    public TransaccionCobroEntity getTransaccionCobroId() {
        return transaccionCobroId;
    }

    public void setTransaccionCobroId(TransaccionCobroEntity transaccionCobroId) {
        this.transaccionCobroId = transaccionCobroId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (endPointTransaccionCobroId != null ? endPointTransaccionCobroId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EndPointTransaccionCobroEntity)) {
            return false;
        }
        EndPointTransaccionCobroEntity other = (EndPointTransaccionCobroEntity) object;
        if ((this.endPointTransaccionCobroId == null && other.endPointTransaccionCobroId != null) || (this.endPointTransaccionCobroId != null && !this.endPointTransaccionCobroId.equals(other.endPointTransaccionCobroId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fbf.bo.com.fbf.compensacion.entity.EndPointTransaccionCobroEntity[ endPointTransaccionCobroId=" + endPointTransaccionCobroId + " ]";
    }

}

