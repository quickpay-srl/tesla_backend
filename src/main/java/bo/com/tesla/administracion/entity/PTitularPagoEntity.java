/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.tesla.administracion.entity;

import java.io.Serializable;
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

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "p_titulares_pagos", catalog = "exactabo_tesla", schema = "tesla")

public class PTitularPagoEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "titular_id", nullable = false)
    private Long titularId;
    @Basic(optional = false)
    @Column(name = "nombre_completo", nullable = false, length = 150)
    private String nombreCompleto;
    @Basic(optional = false)
    @Column(name = "nro_documento", nullable = false, length = 15)
    private String nroDocumento;
    @Column(name = "fotocopia_cia")
    private Boolean fotocopiaCia;
    @Column(name = "carta_poder")
    private Boolean cartaPoder;
    @Column(name = "uaurio_creacion")
    private BigInteger uaurioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @JoinColumn(name = "transacciones_pago_id", referencedColumnName = "transaccion_pago_id")
    @ManyToOne
    private PTransaccionPagoEntity transaccionesPagoId;

    public PTitularPagoEntity() {
    }

    public PTitularPagoEntity(Long titularId) {
        this.titularId = titularId;
    }

    public PTitularPagoEntity(Long titularId, String nombreCompleto, String nroDocumento) {
        this.titularId = titularId;
        this.nombreCompleto = nombreCompleto;
        this.nroDocumento = nroDocumento;
    }

    public Long getTitularId() {
        return titularId;
    }

    public void setTitularId(Long titularId) {
        this.titularId = titularId;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public Boolean getFotocopiaCia() {
        return fotocopiaCia;
    }

    public void setFotocopiaCia(Boolean fotocopiaCia) {
        this.fotocopiaCia = fotocopiaCia;
    }

    public Boolean getCartaPoder() {
        return cartaPoder;
    }

    public void setCartaPoder(Boolean cartaPoder) {
        this.cartaPoder = cartaPoder;
    }

    public BigInteger getUaurioCreacion() {
        return uaurioCreacion;
    }

    public void setUaurioCreacion(BigInteger uaurioCreacion) {
        this.uaurioCreacion = uaurioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public PTransaccionPagoEntity getTransaccionesPagoId() {
        return transaccionesPagoId;
    }

    public void setTransaccionesPagoId(PTransaccionPagoEntity transaccionesPagoId) {
        this.transaccionesPagoId = transaccionesPagoId;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (titularId != null ? titularId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PTitularPagoEntity)) {
            return false;
        }
        PTitularPagoEntity other = (PTitularPagoEntity) object;
        if ((this.titularId == null && other.titularId != null) || (this.titularId != null && !this.titularId.equals(other.titularId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.PTitularPagoEntity[ titularId=" + titularId + " ]";
    }
    
}
