/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.tesla.administracion.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "recaudadores", catalog = "exacta_tesla", schema = "tesla")
public class RecaudadorEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "recaudador_id", nullable = false)
	private Long recaudadorId;
	@Basic(optional = false)
	@Column(nullable = false, length = 250)
	private String nombre;
	@Basic(optional = false)
	@Column(nullable = false, length = 250)
	private String direccion;
	@Basic(optional = false)
	@Column(nullable = false, length = 10)
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
	@Column(name = "fecha_modificacion")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaModificacion;
	@Basic(optional = false)
	@Column(nullable = false, length = 15)
	private String estado;
	@Basic(optional = false)
	@Column(nullable = false, length = 15)
	private String transaccion;
	@JsonIgnore
	@JoinColumn(name = "tipo_recaudador_id", referencedColumnName = "dominio_id", nullable = false)
	@ManyToOne(optional = false)
	private DominioEntity tipoRecaudador;
	@JsonIgnore
	@OneToMany(mappedBy = "recaudador", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<EntidadRecaudadorEntity> entidadRecaudadorEntityList;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "recaudador")
	private List<RecaudadorComisionEntity> recaudadorComisionEntityLst;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "recaudador")
	private List<SucursalEntity> sucursalEntityList;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "recaudadorId")
	private List<RecaudadorMetodoCobroEntity> recaudadorMetodoCobroEntityList;

	public RecaudadorEntity() {
	}

	public RecaudadorEntity(Long recaudadorId) {
		this.recaudadorId = recaudadorId;
	}

	public RecaudadorEntity(Long recaudadorId, String nombre, String direccion, String telefono, Long usuarioCreacion,
			Date fechaCreacion, String estado, String transaccion) {
		this.recaudadorId = recaudadorId;
		this.nombre = nombre;
		this.direccion = direccion;
		this.telefono = telefono;
		this.usuarioCreacion = usuarioCreacion;
		this.fechaCreacion = fechaCreacion;
		this.estado = estado;
		this.transaccion = transaccion;
	}

	public Long getRecaudadorId() {
		return recaudadorId;
	}

	public void setRecaudadorId(Long recaudadorId) {
		this.recaudadorId = recaudadorId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public DominioEntity getTipoRecaudador() {
		return tipoRecaudador;
	}

	public void setTipoRecaudador(DominioEntity tipoRecaudadorId) {
		this.tipoRecaudador = tipoRecaudadorId;
	}

	public List<EntidadRecaudadorEntity> getEntidadRecaudadorEntityList() {
		return entidadRecaudadorEntityList;
	}

	public void setEntidadRecaudadorEntityList(List<EntidadRecaudadorEntity> entidadRecaudadorEntityList) {
		this.entidadRecaudadorEntityList = entidadRecaudadorEntityList;
	}

	public List<RecaudadorComisionEntity> getRecaudadorComisionEntityLst() {
		return recaudadorComisionEntityLst;
	}

	public void setRecaudadorComisionEntityLst(List<RecaudadorComisionEntity> recaudadorComisionEntityLst) {
		this.recaudadorComisionEntityLst = recaudadorComisionEntityLst;
	}

	public List<SucursalEntity> getSucursalEntityList() {
		return sucursalEntityList;
	}

	public void setSucursalEntityList(List<SucursalEntity> sucursalEntityList) {
		this.sucursalEntityList = sucursalEntityList;
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
		hash += (recaudadorId != null ? recaudadorId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof RecaudadorEntity)) {
			return false;
		}
		RecaudadorEntity other = (RecaudadorEntity) object;
		if ((this.recaudadorId == null && other.recaudadorId != null)
				|| (this.recaudadorId != null && !this.recaudadorId.equals(other.recaudadorId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "bo.com.tesla.administracion.entity.RecaudadorEntity[ recaudadorId=" + recaudadorId + " ]";
	}

}