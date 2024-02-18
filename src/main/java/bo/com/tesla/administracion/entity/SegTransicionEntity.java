/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.tesla.administracion.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "seg_transiciones", catalog = "exacta_tesla", schema = "tesla")

public class SegTransicionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SegTransicionEntityPK segTransicionEntityPK;
    @Column(name = "estado_final", length = 15)
    private String estadoFinal;
    @Column(length = 30)
    private String etiqueta;
    @Column(length = 75)
    private String imagen;
    private Short orden;
    @Column(length = 15)
    private String estado;
    @JsonIgnore
    @JoinColumns({
        @JoinColumn(name = "tabla_id", referencedColumnName = "estado_id", nullable = false, insertable = false, updatable = false)
        , @JoinColumn(name = "estado_inicial", referencedColumnName = "tabla_id", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private SegEstadoEntity segEstadoEntity;
    @JsonIgnore
    @JoinColumns({
        @JoinColumn(name = "tabla_id", referencedColumnName = "transaccion_id", nullable = false, insertable = false, updatable = false)
        , @JoinColumn(name = "transaccion_id", referencedColumnName = "tabla_id", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private SegTransaccionEntity segTransaccionEntity;
    @JsonIgnore
    @OneToMany(mappedBy = "segTransicionEntity")
    private List<SegPrivilegioRoleTransicionEntity> segPrivilegioRoleTransicionEntityList;

    public SegTransicionEntity() {
    }

    public SegTransicionEntity(SegTransicionEntityPK segTransicionEntityPK) {
        this.segTransicionEntityPK = segTransicionEntityPK;
    }

    public SegTransicionEntity(String tablaId, String estadoInicial, String transaccionId) {
        this.segTransicionEntityPK = new SegTransicionEntityPK(tablaId, estadoInicial, transaccionId);
    }

    public SegTransicionEntityPK getSegTransicionEntityPK() {
        return segTransicionEntityPK;
    }

    public void setSegTransicionEntityPK(SegTransicionEntityPK segTransicionEntityPK) {
        this.segTransicionEntityPK = segTransicionEntityPK;
    }

    public String getEstadoFinal() {
        return estadoFinal;
    }

    public void setEstadoFinal(String estadoFinal) {
        this.estadoFinal = estadoFinal;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Short getOrden() {
        return orden;
    }

    public void setOrden(Short orden) {
        this.orden = orden;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public SegEstadoEntity getSegEstadoEntity() {
        return segEstadoEntity;
    }

    public void setSegEstadoEntity(SegEstadoEntity segEstadoEntity) {
        this.segEstadoEntity = segEstadoEntity;
    }

    public SegTransaccionEntity getSegTransaccionEntity() {
        return segTransaccionEntity;
    }

    public void setSegTransaccionEntity(SegTransaccionEntity segTransaccionEntity) {
        this.segTransaccionEntity = segTransaccionEntity;
    }

    public List<SegPrivilegioRoleTransicionEntity> getSegPrivilegioRoleTransicionEntityList() {
        return segPrivilegioRoleTransicionEntityList;
    }

    public void setSegPrivilegioRoleTransicionEntityList(List<SegPrivilegioRoleTransicionEntity> segPrivilegioRoleTransicionEntityList) {
        this.segPrivilegioRoleTransicionEntityList = segPrivilegioRoleTransicionEntityList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (segTransicionEntityPK != null ? segTransicionEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SegTransicionEntity)) {
            return false;
        }
        SegTransicionEntity other = (SegTransicionEntity) object;
        if ((this.segTransicionEntityPK == null && other.segTransicionEntityPK != null) || (this.segTransicionEntityPK != null && !this.segTransicionEntityPK.equals(other.segTransicionEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.SegTransicionEntity[ segTransicionEntityPK=" + segTransicionEntityPK + " ]";
    }
    
}
