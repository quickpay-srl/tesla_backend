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
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "dominios", catalog = "exacta_tesla", schema = "tesla")

public class DominioEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dominio_id", nullable = false)
    private Long dominioId;
    @Column(length = 60)
    private String dominio;
    @Column(length = 250)
    private String descripcion;
    @Column(length = 10)
    private String abreviatura;
    @Column(name = "usuario_creacion")
    private BigInteger usuarioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "usuario_modificacion")
    private BigInteger usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(length = 10)
    private String estado;

    @Column(name = "img")
    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "metodoCobro")
    private List<TransaccionCobroEntity> transaccionCobroEntityList;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "motivoCancelacionId")
    private List<CancelacionEntity> cancelacionEntityList;
    @JsonIgnore
    @OneToMany(mappedBy = "tipoCancelacionId")
    private List<CancelacionEntity> cancelacionEntityList1;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoDocumentoCobroId")
    private List<DosificacionEntity> dosificacionEntityList;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciudadId")
    private List<PersonaEntity> personaEntityList;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoDocumentoId")
    private List<PersonaEntity> personaEntityList1;
    @JsonIgnore
    @OneToMany(mappedBy = "departamento")
    private List<SucursalEntity> sucursalEntitDepartamentoList;
    @JsonIgnore
    @OneToMany(mappedBy = "localidad")
    private List<SucursalEntity> sucursalEntityList1;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoEntidad")
    private List<EntidadEntity> entidadEntityList;
    @JsonIgnore
    @OneToMany(mappedBy = "actividadEconomica")
    private List<EntidadEntity> entidadEntityActividadEconomicaList;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoRecaudador")
    private List<RecaudadorEntity> recaudadorEntityList;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoComision")
    private List<EntidadComisionEntity> entidadComisionEntityList;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dominio")
    private List<AgrupadorDominioEntity> agrupadorDominioEntityList;
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "dominioAgrupador")
    private AgrupadorDominioEntity agrupadorDominioEntity;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoComision")    
    private List<RecaudadorComisionEntity> recaudadorComisionEntityLst;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modalidadFacturacion")
    private List<EntidadEntity> entidadEntityModalidaFacturaList;
    @JsonIgnore
    @OneToMany(mappedBy = "extensionDocumentoId")
    private List<PersonaEntity> estensionDocumentosList;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modalidadFacturacion")
    private List<EntidadEntity> entidadEntityModalidadFacturaList;
    @JsonIgnore
    @OneToMany(mappedBy = "departamentoId")
    private List<SucursalEntidadEntity> sucursalEntidadEntityDptoList;
    @JsonIgnore
    @OneToMany(mappedBy = "municipioId")
    private List<SucursalEntidadEntity> sucursalEntidadEntityMunicipioList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "metodoCobroId")
    private List<RecaudadorMetodoCobroEntity> recaudadorMetodoCobroEntityList;


    public DominioEntity() {
    }

    public DominioEntity(Long dominioId) {
        this.dominioId = dominioId;
    }

    public Long getDominioId() {
        return dominioId;
    }
    public void setDominioId(Long dominioId) {
        this.dominioId = dominioId;
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
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

    public List<TransaccionCobroEntity> getTransaccionCobroEntityList() {
        return transaccionCobroEntityList;
    }

    public void setTransaccionCobroEntityList(List<TransaccionCobroEntity> transaccionCobroEntityList) {
        this.transaccionCobroEntityList = transaccionCobroEntityList;
    }

    public List<CancelacionEntity> getCancelacionEntityList() {
        return cancelacionEntityList;
    }

    public void setCancelacionEntityList(List<CancelacionEntity> cancelacionEntityList) {
        this.cancelacionEntityList = cancelacionEntityList;
    }

    public List<CancelacionEntity> getCancelacionEntityList1() {
        return cancelacionEntityList1;
    }

    public void setCancelacionEntityList1(List<CancelacionEntity> cancelacionEntityList1) {
        this.cancelacionEntityList1 = cancelacionEntityList1;
    }

    public List<DosificacionEntity> getDosificacionEntityList() {
        return dosificacionEntityList;
    }

    public void setDosificacionEntityList(List<DosificacionEntity> dosificacionEntityList) {
        this.dosificacionEntityList = dosificacionEntityList;
    }

    public List<PersonaEntity> getPersonaEntityList() {
        return personaEntityList;
    }

    public void setPersonaEntityList(List<PersonaEntity> personaEntityList) {
        this.personaEntityList = personaEntityList;
    }

    public List<PersonaEntity> getPersonaEntityList1() {
        return personaEntityList1;
    }

    public void setPersonaEntityList1(List<PersonaEntity> personaEntityList1) {
        this.personaEntityList1 = personaEntityList1;
    }

    public List<SucursalEntity> getSucursalEntitDepartamentoList() {
        return sucursalEntitDepartamentoList;
    }

    public void setSucursalEntitDepartamentoList(List<SucursalEntity> sucursalEntityList) {
        this.sucursalEntitDepartamentoList = sucursalEntityList;
    }

    public List<SucursalEntity> getSucursalEntityList1() {
        return sucursalEntityList1;
    }

    public void setSucursalEntityList1(List<SucursalEntity> sucursalEntityList1) {
        this.sucursalEntityList1 = sucursalEntityList1;
    }

    public List<EntidadEntity> getEntidadEntityList() {
        return entidadEntityList;
    }

    public void setEntidadEntityList(List<EntidadEntity> entidadEntityList) {
        this.entidadEntityList = entidadEntityList;
    }

    public List<EntidadEntity> getEntidadEntityActividadEconomicaList() {
        return entidadEntityActividadEconomicaList;
    }

    public void setEntidadEntityActividadEconomicaList(List<EntidadEntity> entidadEntityActividadEconomicaList) {
        this.entidadEntityActividadEconomicaList = entidadEntityActividadEconomicaList;
    }

    public List<RecaudadorEntity> getRecaudadorEntityList() {
        return recaudadorEntityList;
    }

    public void setRecaudadorEntityList(List<RecaudadorEntity> recaudadorEntityList) {
        this.recaudadorEntityList = recaudadorEntityList;
    }

    public List<RecaudadorComisionEntity> getRecaudadorComisionEntityLst() {
        return recaudadorComisionEntityLst;
    }

    public void setRecaudadorComisionEntityLst(List<RecaudadorComisionEntity> recaudadorComisionEntityLst) {
        this.recaudadorComisionEntityLst = recaudadorComisionEntityLst;
    }

    public List<EntidadEntity> getEntidadEntityModalidadFacturaList() {
        return entidadEntityModalidadFacturaList;
    }

    public void setEntidadEntityModalidadFacturaList(List<EntidadEntity> entidadEntityModalidadFacturaList) {
        this.entidadEntityModalidadFacturaList = entidadEntityModalidadFacturaList;
    }

    public List<SucursalEntidadEntity> getSucursalEntidadEntityDptoList() {
        return sucursalEntidadEntityDptoList;
    }

    public void setSucursalEntidadEntityDptoList(List<SucursalEntidadEntity> sucursalEntidadEntityDptoList) {
        this.sucursalEntidadEntityDptoList = sucursalEntidadEntityDptoList;
    }

    public List<SucursalEntidadEntity> getSucursalEntidadEntityMunicipioList() {
        return sucursalEntidadEntityMunicipioList;
    }

    public void setSucursalEntidadEntityMunicipioList(List<SucursalEntidadEntity> sucursalEntidadEntityMunicipioList) {
        this.sucursalEntidadEntityMunicipioList = sucursalEntidadEntityMunicipioList;
    }
    
    
    

    public List<EntidadComisionEntity> getEntidadComisionEntityList() {
		return entidadComisionEntityList;
	}

	public void setEntidadComisionEntityList(List<EntidadComisionEntity> entidadComisionEntityList) {
		this.entidadComisionEntityList = entidadComisionEntityList;
	}

	public List<AgrupadorDominioEntity> getAgrupadorDominioEntityList() {
		return agrupadorDominioEntityList;
	}

	public void setAgrupadorDominioEntityList(List<AgrupadorDominioEntity> agrupadorDominioEntityList) {
		this.agrupadorDominioEntityList = agrupadorDominioEntityList;
	}

	public AgrupadorDominioEntity getAgrupadorDominioEntity() {
		return agrupadorDominioEntity;
	}

	public void setAgrupadorDominioEntity(AgrupadorDominioEntity agrupadorDominioEntity) {
		this.agrupadorDominioEntity = agrupadorDominioEntity;
	}

	public List<EntidadEntity> getEntidadEntityModalidaFacturaList() {
		return entidadEntityModalidaFacturaList;
	}

	public void setEntidadEntityModalidaFacturaList(List<EntidadEntity> entidadEntityModalidaFacturaList) {
		this.entidadEntityModalidaFacturaList = entidadEntityModalidaFacturaList;
	}

	public List<PersonaEntity> getEstensionDocumentosList() {
		return estensionDocumentosList;
	}

	public void setEstensionDocumentosList(List<PersonaEntity> estensionDocumentosList) {
		this.estensionDocumentosList = estensionDocumentosList;
	}

    public List<RecaudadorMetodoCobroEntity> getRecaudadorMetodoCobroEntityList() {
        return recaudadorMetodoCobroEntityList;
    }

    public void setRecaudadorMetodoCobroEntityList(List<RecaudadorMetodoCobroEntity> recaudadorMetodoCobroEntityList) {
        this.recaudadorMetodoCobroEntityList = recaudadorMetodoCobroEntityList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dominioId != null ? dominioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DominioEntity)) {
            return false;
        }
        DominioEntity other = (DominioEntity) object;
        if ((this.dominioId == null && other.dominioId != null) || (this.dominioId != null && !this.dominioId.equals(other.dominioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.DominioEntity[ dominioId=" + dominioId + " ]";
    }
    
}