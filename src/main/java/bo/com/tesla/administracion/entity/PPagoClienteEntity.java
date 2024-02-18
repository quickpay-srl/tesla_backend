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
@Table(name = "p_pagos_clientes", catalog = "exactabo_tesla", schema = "tesla")

public class PPagoClienteEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pago_cliente_id", nullable = false)
    private Long pagoClienteId;
    @Basic(optional = false)
    @Column(name = "nro_registro", nullable = false)
    private int nroRegistro;
    @Basic(optional = false)
    @Column(name = "codigo_cliente", nullable = false, length = 15)
    private String codigoCliente;
    @Basic(optional = false)
    @Column(name = "nombre_cliente", nullable = false, length = 200)
    private String nombreCliente;
    @Column(name = "fecha_nacimiento_cliente")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimientoCliente;
    @Basic(optional = false)
    @Column(name = "nro_documento_cliente", nullable = false, length = 15)
    private String nroDocumentoCliente;
    @Basic(optional = false)
    @Column(name = "extencion_documento_id", nullable = false, length = 5)
    private String extencionDocumentoId;
    @Basic(optional = false)
    @Column(name = "tipo_documento_id", nullable = false)
    private String tipoDocumentoId;
    @Basic(optional = false)
    @Column(nullable = false)
    private int cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "monto_unitario", nullable = false, precision = 17, scale = 2)
    private BigDecimal montoUnitario;
    @Basic(optional = false)
    @Column(nullable = false, length = 250)
    private String periodo;
    @Basic(optional = false)
    @Column(name = "usuario_creacion", nullable = false)
    private Long usuarioCreacion;
    @Basic(optional = false)
    @Column(name = "fecha_creacion", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "usuario_modificacion")
    private BigInteger usuarioModificacion;
    @Basic(optional = false)
    @Column(name = "fecha_modificacion", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Basic(optional = false)
    @Column(nullable = false, length = 150)
    private String concepto;
    @Basic(optional = false)
    @Column(nullable = false, length = 1)
    private String genero;
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private String estado;
    @Column(length = 255)
    private String transaccion;
    @JsonIgnore
    @JoinColumn(name = "archivo_id", referencedColumnName = "archivo_id", nullable = false)
    @ManyToOne(optional = false)
    private ArchivoEntity archivoId;    
    @JsonIgnore
    @JoinColumn(name = "transaccion_pago_id", referencedColumnName = "transaccion_pago_id", nullable = false)
    @ManyToOne(optional = false)
    private PTransaccionPagoEntity transaccionPagoId;
   

    public PPagoClienteEntity() {
    }

    public PPagoClienteEntity(Long pagoClienteId) {
        this.pagoClienteId = pagoClienteId;
    }

    public PPagoClienteEntity(Long pagoClienteId, int nroRegistro, String codigoCliente, String nombreCliente, String nroDocumentoCliente, String extencionDocumentoId, String tipoDocumentoId, int cantidad, BigDecimal montoUnitario, String periodo, Long usuarioCreacion, Date fechaCreacion, Date fechaModificacion, String estado) {
        this.pagoClienteId = pagoClienteId;
        this.nroRegistro = nroRegistro;
        this.codigoCliente = codigoCliente;
        this.nombreCliente = nombreCliente;
        this.nroDocumentoCliente = nroDocumentoCliente;
        this.extencionDocumentoId = extencionDocumentoId;
        this.tipoDocumentoId = tipoDocumentoId;
        this.cantidad = cantidad;
        this.montoUnitario = montoUnitario;
        this.periodo = periodo;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.estado = estado;
    }

    public Long getPagoClienteId() {
        return pagoClienteId;
    }

    public void setPagoClienteId(Long pagoClienteId) {
        this.pagoClienteId = pagoClienteId;
    }

    public int getNroRegistro() {
        return nroRegistro;
    }

    public void setNroRegistro(int nroRegistro) {
        this.nroRegistro = nroRegistro;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Date getFechaNacimientoCliente() {
        return fechaNacimientoCliente;
    }

    public void setFechaNacimientoCliente(Date fechaNacimientoCliente) {
        this.fechaNacimientoCliente = fechaNacimientoCliente;
    }

    public String getNroDocumentoCliente() {
        return nroDocumentoCliente;
    }

    public void setNroDocumentoCliente(String nroDocumentoCliente) {
        this.nroDocumentoCliente = nroDocumentoCliente;
    }

    public String getExtencionDocumentoId() {
        return extencionDocumentoId;
    }

    public void setExtencionDocumentoId(String extencionDocumentoId) {
        this.extencionDocumentoId = extencionDocumentoId;
    }

    public String getTipoDocumentoId() {
        return tipoDocumentoId;
    }

    public void setTipoDocumentoId(String tipoDocumentoId) {
        this.tipoDocumentoId = tipoDocumentoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getMontoUnitario() {
        return montoUnitario;
    }

    public void setMontoUnitario(BigDecimal montoUnitario) {
        this.montoUnitario = montoUnitario;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
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

    public PTransaccionPagoEntity getTransaccionPagoId() {
        return transaccionPagoId;
    }

    public void setTransaccionPagoId(PTransaccionPagoEntity transaccionPagoId) {
        this.transaccionPagoId = transaccionPagoId;
    }

   
    
    
    public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (pagoClienteId != null ? pagoClienteId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PPagoClienteEntity)) {
            return false;
        }
        PPagoClienteEntity other = (PPagoClienteEntity) object;
        if ((this.pagoClienteId == null && other.pagoClienteId != null) || (this.pagoClienteId != null && !this.pagoClienteId.equals(other.pagoClienteId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.PPagoClienteEntity[ pagoClienteId=" + pagoClienteId + " ]";
    }
    
}
