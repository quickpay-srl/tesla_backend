/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.tesla.administracion.entity;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "empleados", catalog = "exacta_tesla", schema = "tesla")

public class EmpleadoEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "empleado_id", nullable = false)
    private Long empleadoId;
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
    @Column(name = "estado")
    private String estado;

    @JsonIgnore
    @JoinColumn(name = "tipo_usuario_id", referencedColumnName = "dominio_id")
    @ManyToOne(optional = false)
    private DominioEntity tipoUsuarioId;

    @JsonIgnore
    @JoinColumn(name = "sucursal_entidad_id", referencedColumnName = "sucursal_entidad_id")
    @ManyToOne(optional = false)
    private SucursalEntidadEntity sucursalEntidadId;


    public SucursalEntidadEntity getSucursalEntidadId() {
        return sucursalEntidadId;
    }

    public void setSucursalEntidadId(SucursalEntidadEntity sucursalEntidadId) {
        this.sucursalEntidadId = sucursalEntidadId;
    }

    @JsonIgnore
    @JoinColumn(name = "entidad_id", referencedColumnName = "entidad_id")
    @ManyToOne
    private EntidadEntity entidadId;

    @JsonIgnore
    @JoinColumn(name = "recaudador_id", referencedColumnName = "recaudador_id")
    @ManyToOne
    private RecaudadorEntity recaudadorId;

    @JsonIgnore
    @JoinColumn(name = "persona_id", referencedColumnName = "persona_id")
    @ManyToOne
    private PersonaEntity personaId;
    @JsonIgnore
    @JoinColumn(name = "sucursal_id", referencedColumnName = "sucursal_id")
    @ManyToOne
    private SucursalEntity sucursalId;

    public RecaudadorEntity getRecaudadorId() {
        return recaudadorId;
    }

    public DominioEntity getTipoUsuarioId() {
        return tipoUsuarioId;
    }

    public void setTipoUsuarioId(DominioEntity tipoUsuarioId) {
        this.tipoUsuarioId = tipoUsuarioId;
    }

    public void setRecaudadorId(RecaudadorEntity recaudadorId) {
        this.recaudadorId = recaudadorId;
    }

    public EmpleadoEntity() {
    }

    public EmpleadoEntity(Long empleadoId) {
        this.empleadoId = empleadoId;
    }

    public Long getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Long empleadoId) {
        this.empleadoId = empleadoId;
    }

    public EntidadEntity getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(EntidadEntity entidadId) {
        this.entidadId = entidadId;
    }

    public PersonaEntity getPersonaId() {
        return personaId;
    }

    public void setPersonaId(PersonaEntity personaId) {
        this.personaId = personaId;
    }

    public SucursalEntity getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(SucursalEntity sucursalId) {
        this.sucursalId = sucursalId;
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

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (empleadoId != null ? empleadoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmpleadoEntity)) {
            return false;
        }
        EmpleadoEntity other = (EmpleadoEntity) object;
        if ((this.empleadoId == null && other.empleadoId != null) || (this.empleadoId != null && !this.empleadoId.equals(other.empleadoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.EmpleadoEntity[ empleadoId=" + empleadoId + " ]";
    }
    
}
