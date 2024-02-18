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
@Table(name = "mensajes_sistema", catalog = "exacta_tesla", schema = "tesla")

public class MensajeSistemaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mensaje_sistema_id", nullable = false)
    private Long mensajeSistemaId;
    @Column(length = 100)
    private String modulo;
    @Column(length = 255)
    private String mensaje;
    @Column(name = "tipo_mensaje_id")
    private BigInteger tipoMensajeId;
    @Column(name = "usuario_creacion")
    private BigInteger usuarioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    public MensajeSistemaEntity() {
    }

    public MensajeSistemaEntity(Long mensajeSistemaId) {
        this.mensajeSistemaId = mensajeSistemaId;
    }

    public Long getMensajeSistemaId() {
        return mensajeSistemaId;
    }

    public void setMensajeSistemaId(Long mensajeSistemaId) {
        this.mensajeSistemaId = mensajeSistemaId;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public BigInteger getTipoMensajeId() {
        return tipoMensajeId;
    }

    public void setTipoMensajeId(BigInteger tipoMensajeId) {
        this.tipoMensajeId = tipoMensajeId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mensajeSistemaId != null ? mensajeSistemaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MensajeSistemaEntity)) {
            return false;
        }
        MensajeSistemaEntity other = (MensajeSistemaEntity) object;
        if ((this.mensajeSistemaId == null && other.mensajeSistemaId != null) || (this.mensajeSistemaId != null && !this.mensajeSistemaId.equals(other.mensajeSistemaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.MensajeSistemaEntity[ mensajeSistemaId=" + mensajeSistemaId + " ]";
    }
    
}
