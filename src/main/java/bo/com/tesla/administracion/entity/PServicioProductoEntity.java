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
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bo.com.tesla.useful.cross.Util;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "p_servicios_productos", catalog = "exactabo_tesla", schema = "tesla")

public class PServicioProductoEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "servicio_producto_id", nullable = false)
    private Long servicioProductoId;
    @Basic(optional = false)
    @Column(name = "codigo_producto", nullable = false, length = 10)
    private String codigoProducto;
    @Basic(optional = false)
    @Column(nullable = false, length = 150)
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "usuario_creacion", nullable = false)
    private long usuarioCreacion;
    @Basic(optional = false)
    @Column(name = "fecha_creacion", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private String estado;
    @Column(length = 15)
    private String transaccion;
    @Column(name = "imagen",length = 20)    
    private String imagen;
    @JsonIgnore
    @JoinColumn(name = "entidad_id", referencedColumnName = "entidad_id", nullable = false)
    @ManyToOne(optional = false)
    private EntidadEntity entidadId;  
   
    @OneToMany(mappedBy = "productoServicioPadreId")
    private List<PServicioProductoEntity> servicioProductoEntityList;
    @JsonIgnore
    @JoinColumn(name = "producto_servicio_padre_id", referencedColumnName = "servicio_producto_id")
    @ManyToOne
    private PServicioProductoEntity productoServicioPadreId;
    @JsonIgnore
    @OneToMany(mappedBy = "servicioProductoId")
    private List<ArchivoEntity> archivoEntityList;
    @JsonIgnore
    @OneToMany(mappedBy = "servicioProductoId")
    private List<PTransaccionPagoEntity> pTransaccionPagoEntityList;
    
    @Transient
    private String imagenBase64;
    

    public PServicioProductoEntity() {
    }

    public PServicioProductoEntity(Long servicioProductoId) {
        this.servicioProductoId = servicioProductoId;
    }

    public PServicioProductoEntity(Long servicioProductoId, String codigoProducto, String descripcion, long usuarioCreacion, Date fechaCreacion, String estado) {
        this.servicioProductoId = servicioProductoId;
        this.codigoProducto = codigoProducto;
        this.descripcion = descripcion;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
    }

    public Long getServicioProductoId() {
        return servicioProductoId;
    }

    public void setServicioProductoId(Long servicioProductoId) {
        this.servicioProductoId = servicioProductoId;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getUsuarioCreacion() {
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

   

  
  

    public EntidadEntity getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(EntidadEntity entidadId) {
        this.entidadId = entidadId;
    }

    public List<PServicioProductoEntity> getServicioProductoEntityList() {
        return servicioProductoEntityList;
    }

    public void setServicioProductoEntityList(List<PServicioProductoEntity> servicioProductoEntityList) {
        this.servicioProductoEntityList = servicioProductoEntityList;
    }

    public PServicioProductoEntity getProductoServicioPadreId() {
        return productoServicioPadreId;
    }

    public void setProductoServicioPadreId(PServicioProductoEntity productoServicioPadreId) {
        this.productoServicioPadreId = productoServicioPadreId;
    }
    
    
    public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	

	public List<ArchivoEntity> getArchivoEntityList() {
		return archivoEntityList;
	}

	public void setArchivoEntityList(List<ArchivoEntity> archivoEntityList) {
		this.archivoEntityList = archivoEntityList;
	}
	
	
	
	public String getImagenBase64() {
		return imagenBase64;
	}

	public void setImagenBase64(String imagenBase64) {
		this.imagenBase64 = imagenBase64;
	}
	
	public List<PTransaccionPagoEntity> getpTransaccionPagoEntityList() {
		return pTransaccionPagoEntityList;
	}

	public void setpTransaccionPagoEntityList(List<PTransaccionPagoEntity> pTransaccionPagoEntityList) {
		this.pTransaccionPagoEntityList = pTransaccionPagoEntityList;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (servicioProductoId != null ? servicioProductoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PServicioProductoEntity)) {
            return false;
        }
        PServicioProductoEntity other = (PServicioProductoEntity) object;
        if ((this.servicioProductoId == null && other.servicioProductoId != null) || (this.servicioProductoId != null && !this.servicioProductoId.equals(other.servicioProductoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.ServicioProductoEntity[ servicioProductoId=" + servicioProductoId + " ]";
    }
    
}
