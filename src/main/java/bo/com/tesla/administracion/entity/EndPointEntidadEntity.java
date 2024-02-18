/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.tesla.administracion.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "end_point_entidades", catalog = "exacta_tesla", schema = "tesla")
public class EndPointEntidadEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "end_point_entidad_id", nullable = false)
    private Long endPointEntidadId;
    @Column(length = 255)
    private String ruta;
    @JoinColumn(name = "entidad_id", referencedColumnName = "entidad_id")
    @ManyToOne
    private EntidadEntity entidadId;
    @Column(length = 15)
    private String estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "endPointEntidadId")
    private List<EndPointTransaccionCobroEntity> endPointTransaccionCobroEntityList;


    public EndPointEntidadEntity() {
    }

    public EndPointEntidadEntity(Long endPointEntidadId) {
        this.endPointEntidadId = endPointEntidadId;
    }

    public Long getEndPointEntidadId() {
        return endPointEntidadId;
    }

    public void setEndPointEntidadId(Long endPointEntidadId) {
        this.endPointEntidadId = endPointEntidadId;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public EntidadEntity getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(EntidadEntity entidadId) {
        this.entidadId = entidadId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<EndPointTransaccionCobroEntity> getEndPointTransaccionCobroEntityList() {
        return endPointTransaccionCobroEntityList;
    }

    public void setEndPointTransaccionCobroEntityList(List<EndPointTransaccionCobroEntity> endPointTransaccionCobroEntityList) {
        this.endPointTransaccionCobroEntityList = endPointTransaccionCobroEntityList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (endPointEntidadId != null ? endPointEntidadId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EndPointEntidadEntity)) {
            return false;
        }
        EndPointEntidadEntity other = (EndPointEntidadEntity) object;
        if ((this.endPointEntidadId == null && other.endPointEntidadId != null) || (this.endPointEntidadId != null && !this.endPointEntidadId.equals(other.endPointEntidadId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.EndPointEntidadEntity[ endPointEntidadId=" + endPointEntidadId + " ]";
    }
    
}
