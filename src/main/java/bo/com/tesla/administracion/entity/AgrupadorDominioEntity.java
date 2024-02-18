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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Carmin
 */
@Entity
@Table(name = "agrupadores_dominios", catalog = "exacta_tesla", schema = "tesla")
public class AgrupadorDominioEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "agrupador_dominio_id", nullable = false)
    private Long agrupadorDominioId;
    @Basic(optional = false)
    @Column(name = "agrupador_id", nullable = false)
    private long agrupadorId;
    @Basic(optional = false)
    @Column(name = "fecha_creacion", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private String estado;
    @JsonIgnore
    @JoinColumn(name = "dominio_id", referencedColumnName = "dominio_id", nullable = false)
    @ManyToOne(optional = false)
    private DominioEntity dominio;
    @JsonIgnore
    @JoinColumn(name = "agrupador_dominio_id", referencedColumnName = "dominio_id", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false)
    private DominioEntity dominioAgrupador;

    public AgrupadorDominioEntity() {
    }

    public AgrupadorDominioEntity(Long agrupadorDominioId) {
        this.agrupadorDominioId = agrupadorDominioId;
    }

    public AgrupadorDominioEntity(Long agrupadorDominioId, long agrupadorId, Date fechaCreacion, String estado) {
        this.agrupadorDominioId = agrupadorDominioId;
        this.agrupadorId = agrupadorId;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
    }

    public Long getAgrupadorDominioId() {
        return agrupadorDominioId;
    }

    public void setAgrupadorDominioId(Long agrupadorDominioId) {
        this.agrupadorDominioId = agrupadorDominioId;
    }

    public long getAgrupadorId() {
        return agrupadorId;
    }

    public void setAgrupadorId(long agrupadorId) {
        this.agrupadorId = agrupadorId;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public DominioEntity getDominio() {
        return dominio;
    }

    public void setDominio(DominioEntity dominioId) {
        this.dominio = dominioId;
    }

    public DominioEntity getDominioAgrupador() {
        return dominioAgrupador;
    }

    public void setDominioAgrupador(DominioEntity dominioEntity) {
        this.dominioAgrupador = dominioEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (agrupadorDominioId != null ? agrupadorDominioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AgrupadorDominioEntity)) {
            return false;
        }
        AgrupadorDominioEntity other = (AgrupadorDominioEntity) object;
        if ((this.agrupadorDominioId == null && other.agrupadorDominioId != null) || (this.agrupadorDominioId != null && !this.agrupadorDominioId.equals(other.agrupadorDominioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.AgrupadorDominioEntity[ agrupadorDominioId=" + agrupadorDominioId + " ]";
    }
}
