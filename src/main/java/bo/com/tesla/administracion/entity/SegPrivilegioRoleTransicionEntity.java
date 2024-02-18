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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "seg_privilegios_roles_transiciones", catalog = "exacta_tesla", schema = "tesla")

public class SegPrivilegioRoleTransicionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "priv_rol_transcicion_id", nullable = false)
    private Long privRolTranscicionId;
    @JsonIgnore
    @JoinColumn(name = "privilegio_rol_id", referencedColumnName = "privilegio_rol_id")
    @ManyToOne
    private SegPrivilegioRolEntity privilegioRolId;
    @JsonIgnore
    @JoinColumns({
        @JoinColumn(name = "tabla_id", referencedColumnName = "tabla_id")
        , @JoinColumn(name = "estado_inicial_id", referencedColumnName = "estado_inicial")
        , @JoinColumn(name = "transaccion_id", referencedColumnName = "transaccion_id")})
    @ManyToOne
    private SegTransicionEntity segTransicionEntity;

    public SegPrivilegioRoleTransicionEntity() {
    }

    public SegPrivilegioRoleTransicionEntity(Long privRolTranscicionId) {
        this.privRolTranscicionId = privRolTranscicionId;
    }

    public Long getPrivRolTranscicionId() {
        return privRolTranscicionId;
    }

    public void setPrivRolTranscicionId(Long privRolTranscicionId) {
        this.privRolTranscicionId = privRolTranscicionId;
    }

    public SegPrivilegioRolEntity getPrivilegioRolId() {
        return privilegioRolId;
    }

    public void setPrivilegioRolId(SegPrivilegioRolEntity privilegioRolId) {
        this.privilegioRolId = privilegioRolId;
    }

    public SegTransicionEntity getSegTransicionEntity() {
        return segTransicionEntity;
    }

    public void setSegTransicionEntity(SegTransicionEntity segTransicionEntity) {
        this.segTransicionEntity = segTransicionEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (privRolTranscicionId != null ? privRolTranscicionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SegPrivilegioRoleTransicionEntity)) {
            return false;
        }
        SegPrivilegioRoleTransicionEntity other = (SegPrivilegioRoleTransicionEntity) object;
        if ((this.privRolTranscicionId == null && other.privRolTranscicionId != null) || (this.privRolTranscicionId != null && !this.privRolTranscicionId.equals(other.privRolTranscicionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.SegPrivilegioRoleTransicionEntity[ privRolTranscicionId=" + privRolTranscicionId + " ]";
    }
    
}
