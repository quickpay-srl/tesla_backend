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
import javax.persistence.CascadeType;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "seg_usuarios", catalog = "exacta_tesla", schema = "tesla")

public class SegUsuarioEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;
    @Basic(optional = false)
    @Column(nullable = false, length = 255)
    private String login;
    @Basic(optional = false)
    @Column(nullable = false, length = 255)
    private String password;       
    @Column(name = "usuario_creacion")
    private Long usuarioCreacion;    
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "usuario_modificacion")
    private Long usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String estado;
    @Column(name = "intentos")
    private Integer intentos;
    @Column(name = "bloqueado")
    private Boolean bloqueado;
    @JsonIgnore
    @OneToMany(mappedBy = "usuarioId")
    private List<SegUsuarioRolEntity> segUsuarioRolEntityList;
    @JsonIgnore
    @JoinColumn(name = "persona_id", referencedColumnName = "persona_id")
    @ManyToOne
    private PersonaEntity personaId;
   

    public SegUsuarioEntity() {
    }

    public SegUsuarioEntity(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public SegUsuarioEntity(Long usuarioId, String login, String password,  Long usuarioCreacion, Date fechaCreacion, String estado) {
        this.usuarioId = usuarioId;
        this.login = login;
        this.password = password;
       
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

  

    public Long getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(Long usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Long getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(Long usuarioModificacion) {
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

    public List<SegUsuarioRolEntity> getSegUsuarioRolEntityList() {
        return segUsuarioRolEntityList;
    }

    public void setSegUsuarioRolEntityList(List<SegUsuarioRolEntity> segUsuarioRolEntityList) {
        this.segUsuarioRolEntityList = segUsuarioRolEntityList;
    }

    public PersonaEntity getPersonaId() {
        return personaId;
    }

    public void setPersonaId(PersonaEntity personaId) {
        this.personaId = personaId;
    }

    
    
   
	public Integer getIntentos() {
		return intentos;
	}

	public void setIntentos(Integer intentos) {
		this.intentos = intentos;
	}

	public Boolean getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (usuarioId != null ? usuarioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SegUsuarioEntity)) {
            return false;
        }
        SegUsuarioEntity other = (SegUsuarioEntity) object;
        if ((this.usuarioId == null && other.usuarioId != null) || (this.usuarioId != null && !this.usuarioId.equals(other.usuarioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.SegUsuarioEntity[ usuarioId=" + usuarioId + " ]";
    }
    
}
