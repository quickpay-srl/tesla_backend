/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.tesla.administracion.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "seg_estados", catalog = "exacta_tesla", schema = "tesla")

public class SegEstadoEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SegEstadoEntityPK segEstadoEntityPK;
    @Column(length = 200)
    private String descripcion;
    @Column(length = 15)
    private String estado;
    @JsonIgnore
    @JoinColumn(name = "tabla_id", referencedColumnName = "tabla_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private SegTablaEntity segTablaEntity;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "segEstadoEntity")
    private List<SegTransicionEntity> segTransicionEntityList;

    public SegEstadoEntity() {
    }

    public SegEstadoEntity(SegEstadoEntityPK segEstadoEntityPK) {
        this.segEstadoEntityPK = segEstadoEntityPK;
    }

    public SegEstadoEntity(String estadoId, String tablaId) {
        this.segEstadoEntityPK = new SegEstadoEntityPK(estadoId, tablaId);
    }

    public SegEstadoEntityPK getSegEstadoEntityPK() {
        return segEstadoEntityPK;
    }

    public void setSegEstadoEntityPK(SegEstadoEntityPK segEstadoEntityPK) {
        this.segEstadoEntityPK = segEstadoEntityPK;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public SegTablaEntity getSegTablaEntity() {
        return segTablaEntity;
    }

    public void setSegTablaEntity(SegTablaEntity segTablaEntity) {
        this.segTablaEntity = segTablaEntity;
    }

    public List<SegTransicionEntity> getSegTransicionEntityList() {
        return segTransicionEntityList;
    }

    public void setSegTransicionEntityList(List<SegTransicionEntity> segTransicionEntityList) {
        this.segTransicionEntityList = segTransicionEntityList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (segEstadoEntityPK != null ? segEstadoEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SegEstadoEntity)) {
            return false;
        }
        SegEstadoEntity other = (SegEstadoEntity) object;
        if ((this.segEstadoEntityPK == null && other.segEstadoEntityPK != null) || (this.segEstadoEntityPK != null && !this.segEstadoEntityPK.equals(other.segEstadoEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.SegEstadoEntity[ segEstadoEntityPK=" + segEstadoEntityPK + " ]";
    }
    
}
