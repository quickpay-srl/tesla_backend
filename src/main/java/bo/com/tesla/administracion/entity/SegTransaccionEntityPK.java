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
public class SegTransaccionEntityPK implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
    @Column(name = "transaccion_id", nullable = false, length = 15)
    private String transaccionId;
    @Basic(optional = false)
    @Column(name = "tabla_id", nullable = false, length = 100)
    private String tablaId;

    public SegTransaccionEntityPK() {
    }

    public SegTransaccionEntityPK(String transaccionId, String tablaId) {
        this.transaccionId = transaccionId;
        this.tablaId = tablaId;
    }

    public String getTransaccionId() {
        return transaccionId;
    }

    public void setTransaccionId(String transaccionId) {
        this.transaccionId = transaccionId;
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
        hash += (transaccionId != null ? transaccionId.hashCode() : 0);
        hash += (tablaId != null ? tablaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SegTransaccionEntityPK)) {
            return false;
        }
        SegTransaccionEntityPK other = (SegTransaccionEntityPK) object;
        if ((this.transaccionId == null && other.transaccionId != null) || (this.transaccionId != null && !this.transaccionId.equals(other.transaccionId))) {
            return false;
        }
        if ((this.tablaId == null && other.tablaId != null) || (this.tablaId != null && !this.tablaId.equals(other.tablaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.SegTransaccionEntityPK[ transaccionId=" + transaccionId + ", tablaId=" + tablaId + " ]";
    }
    
}
