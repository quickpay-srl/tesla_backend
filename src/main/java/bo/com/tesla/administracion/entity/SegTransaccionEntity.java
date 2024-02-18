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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "seg_transacciones", catalog = "exacta_tesla", schema = "tesla")

public class SegTransaccionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SegTransaccionEntityPK segTransaccionEntityPK;
    @Column(length = 250)
    private String descripcion;
    @Column(length = 15)
    private String estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "segTransaccionEntity")
    private List<SegTransicionEntity> segTransicionEntityList;
    @JoinColumn(name = "tabla_id", referencedColumnName = "tabla_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private SegTablaEntity segTablaEntity;

    public SegTransaccionEntity() {
    }

    public SegTransaccionEntity(SegTransaccionEntityPK segTransaccionEntityPK) {
        this.segTransaccionEntityPK = segTransaccionEntityPK;
    }

    public SegTransaccionEntity(String transaccionId, String tablaId) {
        this.segTransaccionEntityPK = new SegTransaccionEntityPK(transaccionId, tablaId);
    }

    public SegTransaccionEntityPK getSegTransaccionEntityPK() {
        return segTransaccionEntityPK;
    }

    public void setSegTransaccionEntityPK(SegTransaccionEntityPK segTransaccionEntityPK) {
        this.segTransaccionEntityPK = segTransaccionEntityPK;
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

    public List<SegTransicionEntity> getSegTransicionEntityList() {
        return segTransicionEntityList;
    }

    public void setSegTransicionEntityList(List<SegTransicionEntity> segTransicionEntityList) {
        this.segTransicionEntityList = segTransicionEntityList;
    }

    public SegTablaEntity getSegTablaEntity() {
        return segTablaEntity;
    }

    public void setSegTablaEntity(SegTablaEntity segTablaEntity) {
        this.segTablaEntity = segTablaEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (segTransaccionEntityPK != null ? segTransaccionEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SegTransaccionEntity)) {
            return false;
        }
        SegTransaccionEntity other = (SegTransaccionEntity) object;
        if ((this.segTransaccionEntityPK == null && other.segTransaccionEntityPK != null) || (this.segTransaccionEntityPK != null && !this.segTransaccionEntityPK.equals(other.segTransaccionEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.SegTransaccionEntity[ segTransaccionEntityPK=" + segTransaccionEntityPK + " ]";
    }
    
}
