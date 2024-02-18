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
public class SegTransicionEntityPK implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
    @Column(name = "tabla_id", nullable = false, length = 100)
    private String tablaId;
    @Basic(optional = false)
    @Column(name = "estado_inicial", nullable = false, length = 15)
    private String estadoInicial;
    @Basic(optional = false)
    @Column(name = "transaccion_id", nullable = false, length = 15)
    private String transaccionId;

    public SegTransicionEntityPK() {
    }

    public SegTransicionEntityPK(String tablaId, String estadoInicial, String transaccionId) {
        this.tablaId = tablaId;
        this.estadoInicial = estadoInicial;
        this.transaccionId = transaccionId;
    }

    public String getTablaId() {
        return tablaId;
    }

    public void setTablaId(String tablaId) {
        this.tablaId = tablaId;
    }

    public String getEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(String estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public String getTransaccionId() {
        return transaccionId;
    }

    public void setTransaccionId(String transaccionId) {
        this.transaccionId = transaccionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tablaId != null ? tablaId.hashCode() : 0);
        hash += (estadoInicial != null ? estadoInicial.hashCode() : 0);
        hash += (transaccionId != null ? transaccionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SegTransicionEntityPK)) {
            return false;
        }
        SegTransicionEntityPK other = (SegTransicionEntityPK) object;
        if ((this.tablaId == null && other.tablaId != null) || (this.tablaId != null && !this.tablaId.equals(other.tablaId))) {
            return false;
        }
        if ((this.estadoInicial == null && other.estadoInicial != null) || (this.estadoInicial != null && !this.estadoInicial.equals(other.estadoInicial))) {
            return false;
        }
        if ((this.transaccionId == null && other.transaccionId != null) || (this.transaccionId != null && !this.transaccionId.equals(other.transaccionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.SegTransicionEntityPK[ tablaId=" + tablaId + ", estadoInicial=" + estadoInicial + ", transaccionId=" + transaccionId + " ]";
    }
    
}
