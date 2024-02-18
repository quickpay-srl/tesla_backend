package bo.com.tesla.recaudaciones.dto;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.*;

public class ClienteDto {

    private Long entidadId;//Si se enviara una api a entidad, no deberia ser obligatorio
    @NotBlank(message = "El 'Codigo Cliente' es obligatorio.")
    private String codigoCliente;
    @NotBlank(message = "El 'Nombre Cliente' es obligatorio.")
    private String nombreCliente;
    @NotBlank(message = "El 'Nro. Documento Cliente' es obligatorio.")
    private String nroDocumento;
    @DecimalMin(value="0.01", message = "El montoTotalCobrado debe ser mayor a 0.01.")
    @NotNull(message = "El monto total cobrado debe ser enviado")
    private BigDecimal montoTotalCobrado;
    @NotNull(message = "El Método de Cobro es obligatorio.")
    private Long metodoCobroId;
    @NotNull(message = "La Dimensión de reimpresión es obligatoria.")
    private Long dimensionId;
    private String correoCliente;
    @Valid
    @NotNull (message = "Debe enviar al menos un agrupador de deuda.")
    @Size(min = 1, message = "Debe enviar al menos un agrupador de deuda.")
    private List<ServicioDeudaDto> servicioDeudaDtoList;

    public ClienteDto() {
    }

    public ClienteDto(String codigoCliente, String nombreCliente, String nroDocumento, String correoCliente) {
        this.codigoCliente = codigoCliente;
        this.nombreCliente = nombreCliente;
        this.nroDocumento = nroDocumento;
        this.correoCliente = correoCliente;
    }

    public Long getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(Long entidadId) {
        this.entidadId = entidadId;
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

    public BigDecimal getMontoTotalCobrado() {
        return montoTotalCobrado;
    }

    public void setMontoTotalCobrado(BigDecimal montoTotalCobrado) {
        this.montoTotalCobrado = montoTotalCobrado;
    }

    public Long getMetodoCobroId() {
        return metodoCobroId;
    }

    public void setMetodoCobroId(Long metodoCobroId) {
        this.metodoCobroId = metodoCobroId;
    }

    public Long getDimensionId() {
        return dimensionId;
    }

    public void setDimensionId(Long dimensionId) {
        this.dimensionId = dimensionId;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    public List<ServicioDeudaDto> getServicioDeudaDtoList() {
        return servicioDeudaDtoList;
    }

    public void setServicioDeudaDtoList(List<ServicioDeudaDto> servicioDeudaDtoList) {
        this.servicioDeudaDtoList = servicioDeudaDtoList;
    }
}
