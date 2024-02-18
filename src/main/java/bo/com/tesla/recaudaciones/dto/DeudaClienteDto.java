package bo.com.tesla.recaudaciones.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class DeudaClienteDto {

    private Long deudaClienteId;
    @JsonIgnore
    private Long archivoId;
    private BigDecimal cantidad;
    private String concepto;
    private BigDecimal montoUnitario;
    private BigDecimal subTotal;
    @JsonIgnore
    private Boolean tipoComprobante;
    private String periodoCabecera;
    @JsonIgnore
    private String codigoCliente;
    @JsonIgnore
    private String nombreCliente;
    @JsonIgnore
    private String nroDocumento;
    private Boolean esPostpago;
    private Boolean editable;

    public DeudaClienteDto(Long deudaClienteId, Long archivoId, BigDecimal cantidad, String concepto, BigDecimal montoUnitario, BigDecimal subTotal, Boolean tipoComprobante, String periodoCabecera, String codigoCliente, String nombreCliente, String nroDocumento, Boolean esPostpago, Boolean editable) {
        this.deudaClienteId = deudaClienteId;
        this.archivoId = archivoId;
        this.cantidad = cantidad;
        this.concepto = concepto;
        this.montoUnitario = montoUnitario;
        this.subTotal = subTotal;
        this.tipoComprobante = tipoComprobante;
        this.periodoCabecera = periodoCabecera;
        this.codigoCliente = codigoCliente;
        this.nombreCliente = nombreCliente;
        this.nroDocumento = nroDocumento;
        this.esPostpago = esPostpago;
        this.editable = editable;
    }

    public Long getDeudaClienteId() {
        return deudaClienteId;
    }

    public void setDeudaClienteId(Long deudaClienteId) {
        this.deudaClienteId = deudaClienteId;
    }

    public Long getArchivoId() {
        return archivoId;
    }

    public void setArchivoId(Long archivoId) {
        this.archivoId = archivoId;
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

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public Boolean getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(Boolean tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public String getPeriodoCabecera() {
        return periodoCabecera;
    }

    public void setPeriodoCabecera(String periodoCabecera) {
        this.periodoCabecera = periodoCabecera;
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

    public Boolean getEsPostpago() {
        return esPostpago;
    }

    public void setEsPostpago(Boolean esPostpago) {
        this.esPostpago = esPostpago;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }
}
