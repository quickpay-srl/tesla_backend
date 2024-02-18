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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "p_transacciones_pagos", catalog = "exactabo_tesla", schema = "tesla")

public class PTransaccionPagoEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "transaccion_pago_id", nullable = false)
    private Long transaccionPagoId;
    @Basic(optional = false)
    @Column(name = "codigo_cliente", nullable = false, length = 15)
    private String codigoCliente;
    @Basic(optional = false)
    @Column(name = "sub_total", nullable = false, precision = 7, scale = 2)
    private BigDecimal subTotal;
    @Basic(optional = false)
    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal total;
    @Basic(optional = false)
    @Column(name = "comision_recaudacion", nullable = false, precision = 7, scale = 2)
    private BigDecimal comisionRecaudacion;
    @Basic(optional = false)
    @Column(name = "comision_exacta", nullable = false, precision = 7, scale = 2)
    private BigDecimal comisionExacta;
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private String periodo;
    @Basic(optional = false)
    @Column(name = "codigo_transaccion", nullable = false, length = 30)
    private String codigoTransaccion;
    @Basic(optional = false)
    @Column(name = "usuario_creacion", nullable = false)
    private long usuarioCreacion;
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
    @JoinColumn(name = "archivo_id", referencedColumnName = "archivo_id", nullable = false)
    @ManyToOne(optional = false)
    private ArchivoEntity archivoId;   
    @JsonIgnore
    @JoinColumn(name = "entidad_id", referencedColumnName = "entidad_id", nullable = false)
    @ManyToOne(optional = false)
    private EntidadEntity entidadId;
    @JsonIgnore
    @JoinColumn(name = "recaudador_id", referencedColumnName = "recaudador_id", nullable = false)
    @ManyToOne(optional = false)
    private RecaudadorEntity recaudadorId;    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transaccionPagoId")
    private List<PPagoClienteEntity> pPagoClienteEntityList;
    @JsonIgnore
    @OneToMany(mappedBy = "transaccionesPagoId")
    private List<PTitularPagoEntity> pTitularPagoEntityList;
    @JsonIgnore
    @JoinColumn(name = "servicio_producto_id", referencedColumnName = "servicio_producto_id")
    @ManyToOne
    private PServicioProductoEntity servicioProductoId;

    public PTransaccionPagoEntity() {
    }

    public PTransaccionPagoEntity(Long transaccionPagoId) {
        this.transaccionPagoId = transaccionPagoId;
    }

    public PTransaccionPagoEntity(Long transaccionPagoId, String codigoCliente,  BigDecimal subTotal, BigDecimal total, BigDecimal comisionRecaudacion, BigDecimal comisionExacta, long usuarioCreacion, Date fechaCreacion, String estado, String transaccion) {
        this.transaccionPagoId = transaccionPagoId;
        this.codigoCliente = codigoCliente;  
        this.subTotal = subTotal;
        this.total = total;
        this.comisionRecaudacion = comisionRecaudacion;
        this.comisionExacta = comisionExacta;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.transaccion = transaccion;
    }

    public Long getTransaccionPagoId() {
        return transaccionPagoId;
    }

    public void setTransaccionPagoId(Long transaccionPagoId) {
        this.transaccionPagoId = transaccionPagoId;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

   

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getComisionRecaudacion() {
        return comisionRecaudacion;
    }

    public void setComisionRecaudacion(BigDecimal comisionRecaudacion) {
        this.comisionRecaudacion = comisionRecaudacion;
    }

    public BigDecimal getComisionExacta() {
        return comisionExacta;
    }

    public void setComisionExacta(BigDecimal comisionExacta) {
        this.comisionExacta = comisionExacta;
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

    public ArchivoEntity getArchivoId() {
        return archivoId;
    }

    public void setArchivoId(ArchivoEntity archivoId) {
        this.archivoId = archivoId;
    }

   

    public EntidadEntity getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(EntidadEntity entidadId) {
        this.entidadId = entidadId;
    }

    public RecaudadorEntity getRecaudadorId() {
        return recaudadorId;
    }

    public void setRecaudadorId(RecaudadorEntity recaudadorId) {
        this.recaudadorId = recaudadorId;
    }

    public List<PPagoClienteEntity> getPPagoClienteEntityList() {
        return pPagoClienteEntityList;
    }

    public void setPPagoClienteEntityList(List<PPagoClienteEntity> pPagoClienteEntityList) {
        this.pPagoClienteEntityList = pPagoClienteEntityList;
    }
    
    
    

    public List<PPagoClienteEntity> getpPagoClienteEntityList() {
		return pPagoClienteEntityList;
	}

	public void setpPagoClienteEntityList(List<PPagoClienteEntity> pPagoClienteEntityList) {
		this.pPagoClienteEntityList = pPagoClienteEntityList;
	}

	public List<PTitularPagoEntity> getpTitularPagoEntityList() {
		return pTitularPagoEntityList;
	}

	public void setpTitularPagoEntityList(List<PTitularPagoEntity> pTitularPagoEntityList) {
		this.pTitularPagoEntityList = pTitularPagoEntityList;
	}
	
	public PServicioProductoEntity getServicioProductoId() {
		return servicioProductoId;
	}

	public void setServicioProductoId(PServicioProductoEntity servicioProductoId) {
		this.servicioProductoId = servicioProductoId;
	}	
	

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	
	

	public String getCodigoTransaccion() {
		return codigoTransaccion;
	}

	public void setCodigoTransaccion(String codigoTransaccion) {
		this.codigoTransaccion = codigoTransaccion;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (transaccionPagoId != null ? transaccionPagoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PTransaccionPagoEntity)) {
            return false;
        }
        PTransaccionPagoEntity other = (PTransaccionPagoEntity) object;
        if ((this.transaccionPagoId == null && other.transaccionPagoId != null) || (this.transaccionPagoId != null && !this.transaccionPagoId.equals(other.transaccionPagoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.PTransaccionPagoEntity[ transaccionPagoId=" + transaccionPagoId + " ]";
    }
    
}
