/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.tesla.administracion.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "seg_modulos", catalog = "exacta_tesla", schema = "tesla")

public class SegModuloEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "modulo_id", nullable = false)
    private Long moduloId;
    @Column(length = 150)
    private String descripcion;
    @Column(length = 15)
    private String estado;
    @JsonIgnore
    @OneToMany(mappedBy = "moduloId")
    private List<SegPrivilegioEntity> segPrivilegioEntityList;
    @JsonIgnore
    @OneToMany(mappedBy = "moduloId")
    private List<SegRolEntity> segRolEntityList;
    public SegModuloEntity() {
    }

    public SegModuloEntity(Long moduloId) {
        this.moduloId = moduloId;
    }

    public Long getModuloId() {
        return moduloId;
    }

    public void setModuloId(Long moduloId) {
        this.moduloId = moduloId;
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

    public List<SegPrivilegioEntity> getSegPrivilegioEntityList() {
        return segPrivilegioEntityList;
    }

    public void setSegPrivilegioEntityList(List<SegPrivilegioEntity> segPrivilegioEntityList) {
        this.segPrivilegioEntityList = segPrivilegioEntityList;
    }

    
    
    public List<SegRolEntity> getSegRolEntityList() {
		return segRolEntityList;
	}

	public void setSegRolEntityList(List<SegRolEntity> segRolEntityList) {
		this.segRolEntityList = segRolEntityList;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (moduloId != null ? moduloId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SegModuloEntity)) {
            return false;
        }
        SegModuloEntity other = (SegModuloEntity) object;
        if ((this.moduloId == null && other.moduloId != null) || (this.moduloId != null && !this.moduloId.equals(other.moduloId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.SegModuloEntity[ moduloId=" + moduloId + " ]";
    }
    
}
