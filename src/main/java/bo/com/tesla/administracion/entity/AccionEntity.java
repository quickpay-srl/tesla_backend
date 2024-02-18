/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.tesla.administracion.entity;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "acciones", catalog = "exacta_tesla", schema = "tesla")
@NamedQueries({
    @NamedQuery(name = "AccionEntity.findAll", query = "SELECT a FROM AccionEntity a")})
public class AccionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "acciones_id", nullable = false)
    private Long accionesId;
    @Basic(optional = false)
    @Column(nullable = false, length = 255)
    private String estado;
    @Basic(optional = false)
    @Column(name = "fecha_estado", nullable = false, length = 255)
    private Date fechaEstado;
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private Long usuario;
    @JsonIgnore
    @JoinColumn(name = "historico_deuda_id", referencedColumnName = "historico_deuda_id", nullable = false)
    @ManyToOne(optional = false)
    private HistoricoDeudaEntity historicoDeudaId;

    public AccionEntity() {
    }

    public AccionEntity(Long accionesId) {
        this.accionesId = accionesId;
    }

    public AccionEntity(Long accionesId, String estado, Date fechaEstado, Long usuario) {
        this.accionesId = accionesId;
        this.estado = estado;
        this.fechaEstado = fechaEstado;
        this.usuario = usuario;
    }

    public Long getAccionesId() {
        return accionesId;
    }

    public void setAccionesId(Long accionesId) {
        this.accionesId = accionesId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaEstado() {
        return fechaEstado;
    }

    public void setFechaEstado(Date fechaEstado) {
        this.fechaEstado = fechaEstado;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    public HistoricoDeudaEntity getHistoricoDeudaId() {
        return historicoDeudaId;
    }

    public void setHistoricoDeudaId(HistoricoDeudaEntity historicoDeudaId) {
        this.historicoDeudaId = historicoDeudaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accionesId != null ? accionesId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccionEntity)) {
            return false;
        }
        AccionEntity other = (AccionEntity) object;
        if ((this.accionesId == null && other.accionesId != null) || (this.accionesId != null && !this.accionesId.equals(other.accionesId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.AccionEntity[ accionesId=" + accionesId + " ]";
    }
    
}
