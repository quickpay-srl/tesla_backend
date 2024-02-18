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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "log_sistemas", catalog = "exacta_tesla", schema = "tesla")

public class LogSistemaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "log_sistema_id", nullable = false)
    private Long logSistemaId;
    @Column(length = 100)
    private String modulo;
    @Column(length = 500)
    private String controller;
    @Column(length = 2147483647)
    private String mensaje;
    @Column(length = 2147483647)
    private String causa;
    @Column(name = "usuario_creacion")
    private Long usuarioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    public LogSistemaEntity() {
    }

    public LogSistemaEntity(Long logSistemaId) {
        this.logSistemaId = logSistemaId;
    }

    public Long getLogSistemaId() {
        return logSistemaId;
    }

    public void setLogSistemaId(Long logSistemaId) {
        this.logSistemaId = logSistemaId;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (logSistemaId != null ? logSistemaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LogSistemaEntity)) {
            return false;
        }
        LogSistemaEntity other = (LogSistemaEntity) object;
        if ((this.logSistemaId == null && other.logSistemaId != null) || (this.logSistemaId != null && !this.logSistemaId.equals(other.logSistemaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.LogSistemaEntity[ logSistemaId=" + logSistemaId + " ]";
    }
    
}
