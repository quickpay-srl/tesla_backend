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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "seg_privilegios_roles", catalog = "exacta_tesla", schema = "tesla")
public class SegPrivilegioRolEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "privilegio_rol_id", nullable = false)
    private Long privilegioRolId;
    
    @JoinColumn(name = "privilegio_id", referencedColumnName = "privilegios_id")
    @ManyToOne
    private SegPrivilegioEntity privilegioId;
    @JsonIgnore
    @JoinColumn(name = "rol_id", referencedColumnName = "rol_id")
    @ManyToOne
    private SegRolEntity rolId;
    @JsonIgnore
    @OneToMany(mappedBy = "privilegioRolId")
    private List<SegPrivilegioRoleTransicionEntity> segPrivilegioRoleTransicionEntityList;
    @Column(length = 15)
    private String estado;
    public SegPrivilegioRolEntity() {
    }

    public SegPrivilegioRolEntity(Long privilegioRolId) {
        this.privilegioRolId = privilegioRolId;
    }

    public Long getPrivilegioRolId() {
        return privilegioRolId;
    }

    public void setPrivilegioRolId(Long privilegioRolId) {
        this.privilegioRolId = privilegioRolId;
    }

    public SegPrivilegioEntity getPrivilegioId() {
        return privilegioId;
    }

    public void setPrivilegioId(SegPrivilegioEntity privilegioId) {
        this.privilegioId = privilegioId;
    }

    public SegRolEntity getRolId() {
        return rolId;
    }

    public void setRolId(SegRolEntity rolId) {
        this.rolId = rolId;
    }

    public List<SegPrivilegioRoleTransicionEntity> getSegPrivilegioRoleTransicionEntityList() {
        return segPrivilegioRoleTransicionEntityList;
    }

    public void setSegPrivilegioRoleTransicionEntityList(List<SegPrivilegioRoleTransicionEntity> segPrivilegioRoleTransicionEntityList) {
        this.segPrivilegioRoleTransicionEntityList = segPrivilegioRoleTransicionEntityList;
    }

    
    
    
    
    public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (privilegioRolId != null ? privilegioRolId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SegPrivilegioRolEntity)) {
            return false;
        }
        SegPrivilegioRolEntity other = (SegPrivilegioRolEntity) object;
        if ((this.privilegioRolId == null && other.privilegioRolId != null) || (this.privilegioRolId != null && !this.privilegioRolId.equals(other.privilegioRolId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.SegPrivilegioRolEntity[ privilegioRolId=" + privilegioRolId + " ]";
    }
    
}
