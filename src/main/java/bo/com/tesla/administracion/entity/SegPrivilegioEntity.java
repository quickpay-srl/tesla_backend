/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.tesla.administracion.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "seg_privilegios", catalog = "exacta_tesla", schema = "tesla")

public class SegPrivilegioEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "privilegios_id", nullable = false)
    private Long privilegiosId;
    @Column(length = 150)
    private String link;
    @Column(length = 150)
    private String descripcion;
    @Column(length = 50)
    private String icono;
    private Short orden;
    @JsonIgnore
    @Column(name = "usuario_creacion")
    private BigInteger usuarioCreacion;
    @JsonIgnore
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @JsonIgnore
    @Column(name = "usuario_modificacion")
    private BigInteger usuarioModificacion;
    @JsonIgnore
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
  
    @Column(length = 15)
    private String estado;
    @JsonIgnore
    @OneToMany(mappedBy = "privilegioId")
    private List<SegPrivilegioRolEntity> segPrivilegioRolEntityList;
    
    @OneToMany(mappedBy = "privilegioPadreId")
    private List<SegPrivilegioEntity> segPrivilegioEntityList;
    @JsonIgnore
    @JoinColumn(name = "privilegio_padre_id", referencedColumnName = "privilegios_id")
    @ManyToOne
    private SegPrivilegioEntity privilegioPadreId;
    
    @JoinColumn(name = "modulo_id", referencedColumnName = "modulo_id")
    @ManyToOne
    private SegModuloEntity moduloId;

    public SegPrivilegioEntity() {
    }

    public SegPrivilegioEntity(Long privilegiosId) {
        this.privilegiosId = privilegiosId;
    }

    public Long getPrivilegiosId() {
        return privilegiosId;
    }

    public void setPrivilegiosId(Long privilegiosId) {
        this.privilegiosId = privilegiosId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public Short getOrden() {
        return orden;
    }

    public void setOrden(Short orden) {
        this.orden = orden;
    }

    public BigInteger getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(BigInteger usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigInteger getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(BigInteger usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<SegPrivilegioRolEntity> getSegPrivilegioRolEntityList() {
        return segPrivilegioRolEntityList;
    }

    public void setSegPrivilegioRolEntityList(List<SegPrivilegioRolEntity> segPrivilegioRolEntityList) {
        this.segPrivilegioRolEntityList = segPrivilegioRolEntityList;
    }

    public List<SegPrivilegioEntity> getSegPrivilegioEntityList() {
        return segPrivilegioEntityList;
    }

    public void setSegPrivilegioEntityList(List<SegPrivilegioEntity> segPrivilegioEntityList) {
        this.segPrivilegioEntityList = segPrivilegioEntityList;
    }

    public SegPrivilegioEntity getPrivilegioPadreId() {
        return privilegioPadreId;
    }

    public void setPrivilegioPadreId(SegPrivilegioEntity privilegioPadreId) {
        this.privilegioPadreId = privilegioPadreId;
    }
    
    
    

    public SegModuloEntity getModuloId() {
		return moduloId;
	}

	public void setModuloId(SegModuloEntity moduloId) {
		this.moduloId = moduloId;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (privilegiosId != null ? privilegiosId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SegPrivilegioEntity)) {
            return false;
        }
        SegPrivilegioEntity other = (SegPrivilegioEntity) object;
        if ((this.privilegiosId == null && other.privilegiosId != null) || (this.privilegiosId != null && !this.privilegiosId.equals(other.privilegiosId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.SegPrivilegioEntity[ privilegiosId=" + privilegiosId + " ]";
    }
    
}
