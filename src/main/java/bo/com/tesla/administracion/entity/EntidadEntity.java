package bo.com.tesla.administracion.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "entidades", catalog = "exacta_tesla", schema = "tesla")

public class EntidadEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "entidad_id", nullable = false)
    private Long entidadId;
    @Basic(optional = false)
    @Column(nullable = false, length = 200)
    private String nombre;
    @Basic(optional = false)
    @Column(name = "nombre_comercial", nullable = false, length = 200)
    private String nombreComercial;
    @Basic(optional = false)
    @Column(nullable = false, length = 200)
    private String direccion;
    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String telefono;
    @Column(length = 13)
    private String nit;
    @Column(name = "llave_dosificacion", length = 255)
    private String llaveDosificacion;
    @Column(name = "path_logo", length = 255)
    private String pathLogo;
    @Column(name = "comprobante_en_uno", nullable = false)
    private Boolean comprobanteEnUno;
    @Basic(optional = false)
    @Column(name = "fecha_creacion", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @Column(name = "usuario_creacion", nullable = false)
    private Long usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "usuario_modificacion")
    private Long usuarioModificacion;
    @Basic(optional = false)
    @Column(nullable = false, length = 10)
    private String estado;
    @JsonIgnore
    @OneToMany(mappedBy = "entidadId")
    private List<TipoTransaccionEntity> tipoTransaccionEntityList;
    @JsonIgnore
    @OneToMany(mappedBy = "entidadId")    
    private List<ArchivoEntity> archivoEntityList;
    @JsonIgnore
    @OneToMany(mappedBy = "entidadId")    
    private List<TransaccionCobroEntity> transaccionCobroEntityList;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entidadId")    
    private List<ComprobanteCobroEntity> comprobanteCobroEntityList;  
    @JsonIgnore
    @OneToMany(mappedBy = "entidad", fetch= FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<EntidadRecaudadorEntity> entidadRecaudadorEntityList;
    @JsonIgnore
    @OneToMany(mappedBy = "entidadId")    
    private List<EmpleadoEntity> empleadoEntityList;    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entidadId")    
    private List<DosificacionEntity> dosificacionEntityList;    
    @JsonIgnore
    @JoinColumn(name = "tipo_entidad_id", referencedColumnName = "dominio_id", nullable = false)
    @ManyToOne(optional = false)    
    private DominioEntity tipoEntidad;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entidadId")    
    private List<PlantillaEntity> plantillaEntityList;
    @Column(name = "login_sin", length = 30)
    private String loginSin;
    @Column(name = "password_sin", length = 50)
    private String passwordSin;
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private String transaccion;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entidad")
    private List<SucursalEntidadEntity> sucursalEntidadEntityList;
    @JsonIgnore
    @JoinColumn(name = "actividad_economica_id", referencedColumnName = "dominio_id")
    @ManyToOne
    private DominioEntity actividadEconomica;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entidad")
    private List<EntidadComisionEntity> entidadComisionEntityList;
    @Column(name = "es_cobradora")
    private Boolean esCobradora;
    @Column(name = "es_pagadora")
    private Boolean esPagadora;
    @JsonIgnore
    @JoinColumn(name = "modalidad_facturacion_id", referencedColumnName = "dominio_id", nullable = false)
    @ManyToOne(optional = false)
    private DominioEntity modalidadFacturacion;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entidadId")
    private List<PServicioProductoEntity> servicioProductoEntityList;
   
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entidadId")
    private List<PTransaccionPagoEntity> pTransaccionPagoEntityList;
    @JsonIgnore
    @OneToMany(mappedBy = "entidadId")
    private List<ServicioWebEntidadEntity> servicioWebEntidadEntityList;
    
    @JsonIgnore
    @OneToMany(mappedBy = "entidadId")
    private List<EndPointEntidadEntity> endPointEntidadEntityList;
    
    public EntidadEntity() {
    }

    public EntidadEntity(Long entidadId) {
        this.entidadId = entidadId;
    }

    public EntidadEntity(Long entidadId, String nombre, String nombreComercial, String direccion, String telefono, String nit, Date fechaCreacion, Long usuarioCreacion, String estado) {
        this.entidadId = entidadId;
        this.nombre = nombre;
        this.nombreComercial = nombreComercial;
        this.direccion = direccion;
        this.telefono = telefono;
        this.nit = nit;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.estado = estado;
    }

    public Long getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(Long entidadId) {
        this.entidadId = entidadId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getLlaveDosificacion() {
        return llaveDosificacion;
    }

    public void setLlaveDosificacion(String llaveDosificacion) {
        this.llaveDosificacion = llaveDosificacion;
    }

    public String getPathLogo() {
        return pathLogo;
    }

    public void setPathLogo(String pathLogo) {
        this.pathLogo = pathLogo;
    }

    public Boolean getComprobanteEnUno() {
        return comprobanteEnUno;
    }

    public void setComprobanteEnUno(Boolean comprobanteEnUno) {
        this.comprobanteEnUno = comprobanteEnUno;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Long getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(Long usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Long getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(Long usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<TipoTransaccionEntity> getTipoTransaccionEntityList() {
        return tipoTransaccionEntityList;
    }

    public void setTipoTransaccionEntityList(List<TipoTransaccionEntity> tipoTransaccionEntityList) {
        this.tipoTransaccionEntityList = tipoTransaccionEntityList;
    }

    public List<ArchivoEntity> getArchivoEntityList() {
        return archivoEntityList;
    }

    public void setArchivoEntityList(List<ArchivoEntity> archivoEntityList) {
        this.archivoEntityList = archivoEntityList;
    }

    public List<TransaccionCobroEntity> getTransaccionCobroEntityList() {
        return transaccionCobroEntityList;
    }

    public void setTransaccionCobroEntityList(List<TransaccionCobroEntity> transaccionCobroEntityList) {
        this.transaccionCobroEntityList = transaccionCobroEntityList;
    }

    public List<ComprobanteCobroEntity> getComprobanteCobroEntityList() {
        return comprobanteCobroEntityList;
    }

    public void setComprobanteCobroEntityList(List<ComprobanteCobroEntity> comprobanteCobroEntityList) {
        this.comprobanteCobroEntityList = comprobanteCobroEntityList;
    }

    public List<EntidadRecaudadorEntity> getEntidadRecaudadorEntityList() {
        return entidadRecaudadorEntityList;
    }

    public void setEntidadRecaudadorEntityList(List<EntidadRecaudadorEntity> entidadRecaudadorEntityList) {
        this.entidadRecaudadorEntityList = entidadRecaudadorEntityList;
    }

    public List<EmpleadoEntity> getEmpleadoEntityList() {
        return empleadoEntityList;
    }

    public void setEmpleadoEntityList(List<EmpleadoEntity> empleadoEntityList) {
        this.empleadoEntityList = empleadoEntityList;
    }

    public List<DosificacionEntity> getDosificacionEntityList() {
        return dosificacionEntityList;
    }

    public void setDosificacionEntityList(List<DosificacionEntity> dosificacionEntityList) {
        this.dosificacionEntityList = dosificacionEntityList;
    }

    public DominioEntity getTipoEntidad() {
        return tipoEntidad;
    }

    public void setTipoEntidad(DominioEntity tipoEntidadId) {
        this.tipoEntidad = tipoEntidadId;
    }

    public List<PlantillaEntity> getPlantillaEntityList() {
        return plantillaEntityList;
    }

    public void setPlantillaEntityList(List<PlantillaEntity> plantillaEntityList) {
        this.plantillaEntityList = plantillaEntityList;
    }

    public String getLoginSin() {
        return loginSin;
    }

    public void setLoginSin(String loginSin) {
        this.loginSin = loginSin;
    }

    public String getPasswordSin() {
        return passwordSin;
    }

    public void setPasswordSin(String passwordSin) {
        this.passwordSin = passwordSin;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public List<SucursalEntidadEntity> getSucursalEntidadEntityList() {
        return sucursalEntidadEntityList;
    }

    public void setSucursalEntidadEntityList(List<SucursalEntidadEntity> sucursalEntidadEntityList) {
        this.sucursalEntidadEntityList = sucursalEntidadEntityList;
    }

    public DominioEntity getActividadEconomica() {
        return actividadEconomica;
    }

    public void setActividadEconomica(DominioEntity actividadEconomicaId) {
        this.actividadEconomica = actividadEconomicaId;
    }

    public List<EntidadComisionEntity> getEntidadComisionEntityList() {
        return entidadComisionEntityList;
    }

    public void setEntidadComisionEntityList(List<EntidadComisionEntity> entidadComisionEntityList) {
        this.entidadComisionEntityList = entidadComisionEntityList;
    }

    public Boolean getEsCobradora() {
        return esCobradora;
    }

    public void setEsCobradora(Boolean esCobradora) {
        this.esCobradora = esCobradora;
    }

    public Boolean getEsPagadora() {
        return esPagadora;
    }

    public void setEsPagadora(Boolean esPagadora) {
        this.esPagadora = esPagadora;
    }

    public DominioEntity getModalidadFacturacion() {
        return modalidadFacturacion;
    }

    public void setModalidadFacturacion(DominioEntity modalidadFacturacion) {
        this.modalidadFacturacion = modalidadFacturacion;
    }
    
    
    public List<PServicioProductoEntity> getServicioProductoEntityList() {
		return servicioProductoEntityList;
	}

	public void setServicioProductoEntityList(List<PServicioProductoEntity> servicioProductoEntityList) {
		this.servicioProductoEntityList = servicioProductoEntityList;
	}

	

	public List<PTransaccionPagoEntity> getpTransaccionPagoEntityList() {
		return pTransaccionPagoEntityList;
	}

	public void setpTransaccionPagoEntityList(List<PTransaccionPagoEntity> pTransaccionPagoEntityList) {
		this.pTransaccionPagoEntityList = pTransaccionPagoEntityList;
	}
	
	

	public List<ServicioWebEntidadEntity> getServicioWebEntidadEntityList() {
		return servicioWebEntidadEntityList;
	}

	public void setServicioWebEntidadEntityList(List<ServicioWebEntidadEntity> servicioWebEntidadEntityList) {
		this.servicioWebEntidadEntityList = servicioWebEntidadEntityList;
	}
	
	

	public List<EndPointEntidadEntity> getEndPointEntidadEntityList() {
		return endPointEntidadEntityList;
	}

	public void setEndPointEntidadEntityList(List<EndPointEntidadEntity> endPointEntidadEntityList) {
		this.endPointEntidadEntityList = endPointEntidadEntityList;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (entidadId != null ? entidadId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EntidadEntity)) {
            return false;
        }
        EntidadEntity other = (EntidadEntity) object;
        if ((this.entidadId == null && other.entidadId != null) || (this.entidadId != null && !this.entidadId.equals(other.entidadId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.EntidadEntity[ entidadId=" + entidadId + " ]";
    }


}
