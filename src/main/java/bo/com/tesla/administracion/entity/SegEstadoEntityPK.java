/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.tesla.administracion.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author aCallejas
 */
@Embeddable
public class SegEstadoEntityPK implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
    @Column(name = "estado_id", nullable = false, length = 15)
    private String estadoId;
    @Basic(optional = false)
    @Column(name = "tabla_id", nullable = false, length = 100)
    private String tablaId;

    public SegEstadoEntityPK() {
    }

    public SegEstadoEntityPK(String estadoId, String tablaId) {
        this.estadoId = estadoId;
        this.tablaId = tablaId;
    }

    public String getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(String estadoId) {
        this.estadoId = estadoId;
    }

    public String getTablaId() {
        return tablaId;
    }

    public void setTablaId(String tablaId) {
        this.tablaId = tablaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estadoId != null ? estadoId.hashCode() : 0);
        hash += (tablaId != null ? tablaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SegEstadoEntityPK)) {
            return false;
        }
        SegEstadoEntityPK other = (SegEstadoEntityPK) object;
        if ((this.estadoId == null && other.estadoId != null) || (this.estadoId != null && !this.estadoId.equals(other.estadoId))) {
            return false;
        }
        if ((this.tablaId == null && other.tablaId != null) || (this.tablaId != null && !this.tablaId.equals(other.tablaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.SegEstadoEntityPK[ estadoId=" + estadoId + ", tablaId=" + tablaId + " ]";
    }
    
}
