package bo.com.tesla.administracion.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Carmin
 */
@Entity
@Table(name = "recaudadores_metodos_cobros", catalog = "exacta_tesla", schema = "tesla")
@NamedQueries({
        @NamedQuery(name = "RecaudadorMetodoCobroEntity.findAll", query = "SELECT r FROM RecaudadorMetodoCobroEntity r")})
public class RecaudadorMetodoCobroEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "recaudador_metodo_cobro_id", nullable = false)
    private Long recaudadorMetodoCobroId;
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
    @Column(nullable = false, length = 15)
    private String transaccion;
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private String estado;
    @JoinColumn(name = "metodo_cobro_id", referencedColumnName = "dominio_id", nullable = false)
    @ManyToOne(optional = false)
    private DominioEntity metodoCobroId;
    @JoinColumn(name = "recaudador_id", referencedColumnName = "recaudador_id", nullable = false)
    @ManyToOne(optional = false)
    private RecaudadorEntity recaudadorId;

    public RecaudadorMetodoCobroEntity() {
    }

    public RecaudadorMetodoCobroEntity(Long recaudadorMetodoCobroId) {
        this.recaudadorMetodoCobroId = recaudadorMetodoCobroId;
    }

    public RecaudadorMetodoCobroEntity(Long recaudadorMetodoCobroId, Date fechaCreacion, long usuarioCreacion, String estado) {
        this.recaudadorMetodoCobroId = recaudadorMetodoCobroId;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.estado = estado;
    }

    public Long getRecaudadorMetodoCobroId() {
        return recaudadorMetodoCobroId;
    }

    public void setRecaudadorMetodoCobroId(Long recaudadorMetodoCobroId) {
        this.recaudadorMetodoCobroId = recaudadorMetodoCobroId;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public long getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(long usuarioCreacion) {
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

    public DominioEntity getMetodoCobroId() {
        return metodoCobroId;
    }

    public void setMetodoCobroId(DominioEntity metodoCobroId) {
        this.metodoCobroId = metodoCobroId;
    }

    public RecaudadorEntity getRecaudadorId() {
        return recaudadorId;
    }

    public void setRecaudadorId(RecaudadorEntity recaudadorId) {
        this.recaudadorId = recaudadorId;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recaudadorMetodoCobroId != null ? recaudadorMetodoCobroId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecaudadorMetodoCobroEntity)) {
            return false;
        }
        RecaudadorMetodoCobroEntity other = (RecaudadorMetodoCobroEntity) object;
        if ((this.recaudadorMetodoCobroId == null && other.recaudadorMetodoCobroId != null) || (this.recaudadorMetodoCobroId != null && !this.recaudadorMetodoCobroId.equals(other.recaudadorMetodoCobroId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.RecaudadorMetodoCobroEntity[ recaudadoraMetodoCobroId=" + recaudadorMetodoCobroId + " ]";
    }

}
