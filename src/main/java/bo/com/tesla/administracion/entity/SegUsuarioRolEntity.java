/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.tesla.administracion.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "seg_usuarios_roles", catalog = "exacta_tesla", schema = "tesla")
public class SegUsuarioRolEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "usuario_roles_id", nullable = false)
    private Long usuarioRolesId;
    @JsonIgnore
    @JoinColumn(name = "rol_id", referencedColumnName = "rol_id")
    @ManyToOne
    private SegRolEntity rolId;
    @JsonIgnore
    @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id")
    @ManyToOne
    private SegUsuarioEntity usuarioId;
    @Column(name = "estado")    
    private String estado;

    public SegUsuarioRolEntity() {
    }

    public SegUsuarioRolEntity(Long usuarioRolesId) {
        this.usuarioRolesId = usuarioRolesId;
    }

    public Long getUsuarioRolesId() {
        return usuarioRolesId;
    }

    public void setUsuarioRolesId(Long usuarioRolesId) {
        this.usuarioRolesId = usuarioRolesId;
    }

    public SegRolEntity getRolId() {
        return rolId;
    }

    public void setRolId(SegRolEntity rolId) {
        this.rolId = rolId;
    }

    public SegUsuarioEntity getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(SegUsuarioEntity usuarioId) {
        this.usuarioId = usuarioId;
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
        hash += (usuarioRolesId != null ? usuarioRolesId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SegUsuarioRolEntity)) {
            return false;
        }
        SegUsuarioRolEntity other = (SegUsuarioRolEntity) object;
        if ((this.usuarioRolesId == null && other.usuarioRolesId != null) || (this.usuarioRolesId != null && !this.usuarioRolesId.equals(other.usuarioRolesId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.SegUsuarioRolEntity[ usuarioRolesId=" + usuarioRolesId + " ]";
    }
    
}
