/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.tesla.administracion.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author aCallejas
 */
@Entity
@Table(name = "cobros_clientes", catalog = "exacta_tesla", schema = "tesla")

public class CobroClienteEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cobro_cliente_id", nullable = false)
    private Long cobroClienteId;
    @Basic(optional = false)
    @Column(name = "nro_registro", nullable = false)
    private int nroRegistro;
    @Basic(optional = false)
    @Column(name = "codigo_cliente", nullable = false, length = 15)
    private String codigoCliente;
    @Column(name = "nombre_cliente", length = 200)
    private String nombreCliente;
    @Column(name = "nro_documento", length = 15)
    private String nroDocumento;
    @Basic(optional = false)
    @Column(name = "tipo_servicio", nullable = false, length = 300)
    private String tipoServicio;
    @Basic(optional = false)
    @Column(nullable = false, length = 300)
    private String servicio;
    @Basic(optional = false)
    @Column(nullable = false, length = 250)
    private String periodo;
    @Basic(optional = false)
    @Column(nullable = false)
    private Character tipo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;
    @Basic(optional = false)
    @Column(nullable = false, length = 250)
    private String concepto;
    @Column(name = "monto_unitario", precision = 17, scale = 2)
    private BigDecimal montoUnitario;
    @Column(name = "dato_extra", length = 250)
    private String datoExtra;
    @Basic(optional = false)
    @Column(name = "tipo_comprobante", nullable = false)
    private boolean tipoComprobante;
    @Column(name = "periodo_cabecera", length = 250)
    private String periodoCabecera;
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
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private String transaccion;
    @Column(length = 150)
    private String direccion;
    @Column(length = 15)
    private String nit;
    @Column(length = 10)
    private String telefono;
    @Column(name = "sub_total", precision = 17, scale = 2)
    private BigDecimal subTotal;
    @Basic(optional = false)
    @Column(name = "es_postpago", nullable = false)
    private boolean esPostpago;
    @Column(name = "monto_modificado")
    private Boolean montoModificado;
    @JsonIgnore
    @JoinColumn(name = "archivo_id", referencedColumnName = "archivo_id", nullable = false)
    @ManyToOne(optional = false)
    private ArchivoEntity archivoId;
    
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
   
    @Column(name = "usuario_modificacion")
    private Long usuarioModificacion;
    @JsonIgnore
    @JoinColumn(name = "transaccion_cobro_id", referencedColumnName = "transaccion_cobro_id")
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = TransaccionCobroEntity.class)
    private TransaccionCobroEntity transaccionCobro;
    @JsonIgnore
    @JoinColumn(name = "historico_deuda_id", referencedColumnName = "historico_deuda_id")
    @ManyToOne
    private HistoricoDeudaEntity historicoDeuda;

    public CobroClienteEntity() {
    }

    public CobroClienteEntity(Long cobroClienteId) {
        this.cobroClienteId = cobroClienteId;
    }

    public CobroClienteEntity(Long cobroClienteId, int nroRegistro, String codigoCliente, String tipoServicio, String servicio, String periodo, Character tipo, BigDecimal cantidad, String concepto, boolean tipoComprobante, long usuarioCreacion, Date fechaCreacion, String estado, String transaccion, boolean esPostpago) {
        this.cobroClienteId = cobroClienteId;
        this.nroRegistro = nroRegistro;
        this.codigoCliente = codigoCliente;
        this.tipoServicio = tipoServicio;
        this.servicio = servicio;
        this.periodo = periodo;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.concepto = concepto;
        this.tipoComprobante = tipoComprobante;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.transaccion = transaccion;
        this.esPostpago = esPostpago;
    }

    public Long getCobroClienteId() {
        return cobroClienteId;
    }

    public void setCobroClienteId(Long cobroClienteId) {
        this.cobroClienteId = cobroClienteId;
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

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Character getTipo() {
        return tipo;
    }

    public void setTipo(Character tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public BigDecimal getMontoUnitario() {
        return montoUnitario;
    }

    public void setMontoUnitario(BigDecimal montoUnitario) {
        this.montoUnitario = montoUnitario;
    }

    public String getDatoExtra() {
        return datoExtra;
    }

    public void setDatoExtra(String datoExtra) {
        this.datoExtra = datoExtra;
    }

    public boolean getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(boolean tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public String getPeriodoCabecera() {
        return periodoCabecera;
    }

    public void setPeriodoCabecera(String periodoCabecera) {
        this.periodoCabecera = periodoCabecera;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public boolean getEsPostpago() {
        return esPostpago;
    }

    public void setEsPostpago(boolean esPostpago) {
        this.esPostpago = esPostpago;
    }

    public ArchivoEntity getArchivoId() {
        return archivoId;
    }

    public void setArchivoId(ArchivoEntity archivoId) {
        this.archivoId = archivoId;
    }


    public TransaccionCobroEntity getTransaccionCobro() {
        return transaccionCobro;
    }

    public void setTransaccionCobro(TransaccionCobroEntity transaccionCobroId) {
        this.transaccionCobro = transaccionCobroId;
    }

    public HistoricoDeudaEntity getHistoricoDeuda() {
        return historicoDeuda;
    }

    public void setHistoricoDeuda(HistoricoDeudaEntity historicoDeudaId) {
        this.historicoDeuda = historicoDeudaId;
    }

    public boolean getMontoModificado() {
        return montoModificado;
    }

    public void setMontoModificado(boolean montoModificado) {
        this.montoModificado = montoModificado;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cobroClienteId != null ? cobroClienteId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CobroClienteEntity)) {
            return false;
        }
        CobroClienteEntity other = (CobroClienteEntity) object;
        if ((this.cobroClienteId == null && other.cobroClienteId != null) || (this.cobroClienteId != null && !this.cobroClienteId.equals(other.cobroClienteId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bo.com.tesla.administracion.entity.CobroClienteEntity[ cobroClienteId=" + cobroClienteId + " ]";
    }

}
