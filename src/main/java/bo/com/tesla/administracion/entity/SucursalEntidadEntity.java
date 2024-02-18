/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.tesla.administracion.entity;

import bo.com.tesla.administracion.entity.EntidadEntity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author eCamargo
 */
@Entity
@Table(name = "sucursales_entidades", catalog = "exacta_tesla", schema = "tesla")

public class SucursalEntidadEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sucursal_entidad_id", nullable = false)
    private Long sucursalEntidadId;
    @Basic(optional = false)
    @Column(name = "nombre_sucursal", nullable = false, length = 255)
    private String nombreSucursal;
    @Basic(optional = false)
    @Column(nullable = false, length = 255)
    private String direccion;
    @Column(length = 10)
    private String telefono;
    @Basic(optional = false)
    @Column(name = "usuario_creacion", nullable = false)
    private Long usuarioCreacion;
    @Basic(optional = false)
    @Column(name = "fecha_creacion", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "usuario_modificacion")
    private Long usuarioModificacion;
    @Column(name = "fecha_modificacion", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private String estado;
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private String transaccion;
    @JsonIgnore
    @JoinColumn(name = "entidad_id", referencedColumnName = "entidad_id", nullable = false)
    @ManyToOne(optional = false)
    private EntidadEntity entidad;
    @Column(length = 50)
    private String email;
    @Column(name = "numero_sucursal_sin")
    private Short numeroSucursalSin;
    @Column(name = "usuario_facturacion", length = 30)
    private String usuarioFacturacion;
    @Column(name = "password_facturacion", length = 30)
    private String passwordFacturacion;
    @Column(name = "emite_factura_tesla")
    private Boolean emiteFacturaTesla;
    @JsonIgnore
    @JoinColumn(name = "departamento_id", referencedColumnName = "dominio_id")
    @ManyToOne
    private DominioEntity departamentoId;
    @JsonIgnore
    @JoinColumn(name = "municipio_id", referencedColumnName = "dominio_id")
    @ManyToOne
    private DominioEntity municipioId;

    public SucursalEntidadEntity() {
    }

    public SucursalEntidadEntity(Long sucursalEntidadId) {
        this.sucursalEntidadId = sucursalEntidadId;
    }

    public SucursalEntidadEntity(Long sucursalEntidadId, String nombreSucursal, String direccion, long usuarioCreacion, Date fechaCreacion, String estado, String transaccion) {
        this.sucursalEntidadId = sucursalEntidadId;
        this.nombreSucursal = nombreSucursal;
        this.direccion = direccion;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.transaccion = transaccion;
    }

    public Long getSucursalEntidadId() {
        return sucursalEntidadId;
    }

    public void setSucursalEntidadId(Long sucursalEntidadId) {
        this.sucursalEntidadId = sucursalEntidadId;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
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

    public Long getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(long usuarioCreacion) {
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

    public EntidadEntity getEntidad() {
        return entidad;
    }

    public void setEntidad(EntidadEntity entidad) {
        this.entidad = entidad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Short getNumeroSucursalSin() {
        return numeroSucursalSin;
    }

    public void setNumeroSucursalSin(Short numeroSucursalSin) {
        this.numeroSucursalSin = numeroSucursalSin;
    }

    public String getUsuarioFacturacion() {
        return usuarioFacturacion;
    }

    public void setUsuarioFacturacion(String usuarioFacturacion) {
        this.usuarioFacturacion = usuarioFacturacion;
    }

    public String getPasswordFacturacion() {
        return passwordFacturacion;
    }

    public void setPasswordFacturacion(String passwordFacturacion) {
        this.passwordFacturacion = passwordFacturacion;
    }

    public Boolean getEmiteFacturaTesla() {
        return emiteFacturaTesla;
    }

    public void setEmiteFacturaTesla(Boolean emiteFacturaTesla) {
        this.emiteFacturaTesla = emiteFacturaTesla;
    }

    public DominioEntity getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(DominioEntity departamentoId) {
        this.departamentoId = departamentoId;
    }

    public DominioEntity getMunicipioId() {
        return municipioId;
    }

    public void setMunicipioId(DominioEntity municipioId) {
        this.municipioId = municipioId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sucursalEntidadId != null ? sucursalEntidadId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SucursalEntidadEntity)) {
            return false;
        }
        SucursalEntidadEntity other = (SucursalEntidadEntity) object;
        if ((this.sucursalEntidadId == null && other.sucursalEntidadId != null) || (this.sucursalEntidadId != null && !this.sucursalEntidadId.equals(other.sucursalEntidadId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administrador.entity.SucursalEntidadEntity[ sucursalEntidadId=" + sucursalEntidadId + " ]";
    }
    
}
