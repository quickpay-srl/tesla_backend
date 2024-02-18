/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.tesla.administracion.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "seg_tablas", catalog = "exacta_tesla", schema = "tesla")

public class SegTablaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "tabla_id", nullable = false, length = 100)
    private String tablaId;
    @Column(name = "descripcion_tabla", length = 250)
    private String descripcionTabla;
    @Column(length = 15)
    private String estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "segTablaEntity")
    private List<SegEstadoEntity> segEstadoEntityList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "segTablaEntity")
    private List<SegTransaccionEntity> segTransaccionEntityList;

    public SegTablaEntity() {
    }

    public SegTablaEntity(String tablaId) {
        this.tablaId = tablaId;
    }

    public String getTablaId() {
        return tablaId;
    }

    public void setTablaId(String tablaId) {
        this.tablaId = tablaId;
    }

    public String getDescripcionTabla() {
        return descripcionTabla;
    }

    public void setDescripcionTabla(String descripcionTabla) {
        this.descripcionTabla = descripcionTabla;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<SegEstadoEntity> getSegEstadoEntityList() {
        return segEstadoEntityList;
    }

    public void setSegEstadoEntityList(List<SegEstadoEntity> segEstadoEntityList) {
        this.segEstadoEntityList = segEstadoEntityList;
    }

    public List<SegTransaccionEntity> getSegTransaccionEntityList() {
        return segTransaccionEntityList;
    }

    public void setSegTransaccionEntityList(List<SegTransaccionEntity> segTransaccionEntityList) {
        this.segTransaccionEntityList = segTransaccionEntityList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tablaId != null ? tablaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SegTablaEntity)) {
            return false;
        }
        SegTablaEntity other = (SegTablaEntity) object;
        if ((this.tablaId == null && other.tablaId != null) || (this.tablaId != null && !this.tablaId.equals(other.tablaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.SegTablaEntity[ tablaId=" + tablaId + " ]";
    }
    
}
