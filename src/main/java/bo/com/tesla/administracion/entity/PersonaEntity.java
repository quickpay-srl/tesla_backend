/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.tesla.administracion.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "personas", catalog = "exacta_tesla", schema = "tesla")

public class PersonaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "persona_id", nullable = false)
    private Long personaId;
    @Basic(optional = false)
    @Column(nullable = false, length = 150)
    private String nombres;
    @Column(length = 100)
    private String paterno;
    @Column(length = 100)
    private String materno;
    @Basic(optional = false)
    @Column(nullable = false, length = 200)
    private String direccion;
    @Column(name = "correo_electronico", length = 100)
    private String correoElectronico;
    @Column(length = 10)
    private String telefono;
    
    @Column(name = "nro_documento", length = 10)
    private String nroDocumento;
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
    @Column(name = "admin")
    private Boolean admin;
    @Column(length = 15)
    private String estado;
    @Column(length = 15)
    private String transaccion; 
    @JsonIgnore
    @OneToMany(mappedBy = "personaId")
    private List<EmpleadoEntity> empleadoEntityList;
    @JsonIgnore
    @JoinColumn(name = "ciudad_id", referencedColumnName = "dominio_id")
    @ManyToOne(optional = false)
    private DominioEntity ciudadId;
    @JsonIgnore
    @JoinColumn(name = "tipo_documento_id", referencedColumnName = "dominio_id")
    @ManyToOne(optional = false)
    private DominioEntity tipoDocumentoId;
    @JsonIgnore
    @OneToMany(mappedBy = "personaId")
    private List<SegUsuarioEntity> segUsuarioEntityList;
    @JsonIgnore
    @JoinColumn(name = "extension_documento_id", referencedColumnName = "dominio_id")
    @ManyToOne
    private DominioEntity extensionDocumentoId;

    public PersonaEntity() {
    }

    public PersonaEntity(Long personaId) {
        this.personaId = personaId;
    }

    public PersonaEntity(Long personaId, String nombres, String direccion, String nroDocumento) {
        this.personaId = personaId;
        this.nombres = nombres;
        this.direccion = direccion;
        this.nroDocumento = nroDocumento;
    }

    public Long getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Long personaId) {
        this.personaId = personaId;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getPaterno() {
        return paterno;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    public String getMaterno() {
        return materno;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
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

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public List<EmpleadoEntity> getEmpleadoEntityList() {
        return empleadoEntityList;
    }

    public void setEmpleadoEntityList(List<EmpleadoEntity> empleadoEntityList) {
        this.empleadoEntityList = empleadoEntityList;
    }

    public DominioEntity getCiudadId() {
        return ciudadId;
    }

    public void setCiudadId(DominioEntity ciudadId) {
        this.ciudadId = ciudadId;
    }

    public DominioEntity getTipoDocumentoId() {
        return tipoDocumentoId;
    }

    public void setTipoDocumentoId(DominioEntity tipoDocumentoId) {
        this.tipoDocumentoId = tipoDocumentoId;
    }

    public List<SegUsuarioEntity> getSegUsuarioEntityList() {
        return segUsuarioEntityList;
    }

    public void setSegUsuarioEntityList(List<SegUsuarioEntity> segUsuarioEntityList) {
        this.segUsuarioEntityList = segUsuarioEntityList;
    }
    
        
	public DominioEntity getExtensionDocumentoId() {
		return extensionDocumentoId;
	}

	public void setExtensionDocumentoId(DominioEntity extensionDocumentoId) {
		this.extensionDocumentoId = extensionDocumentoId;
	}
	
	

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (personaId != null ? personaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonaEntity)) {
            return false;
        }
        PersonaEntity other = (PersonaEntity) object;
        if ((this.personaId == null && other.personaId != null) || (this.personaId != null && !this.personaId.equals(other.personaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.PersonaEntity[ personaId=" + personaId + " ]";
    }
    
}
