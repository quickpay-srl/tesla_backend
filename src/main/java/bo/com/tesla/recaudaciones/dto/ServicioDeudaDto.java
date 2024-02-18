package bo.com.tesla.recaudaciones.dto;

import bo.com.tesla.administracion.entity.DeudaClienteEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public class ServicioDeudaDto {

    private Long key;
    @NotBlank(message = "El 'Tipo Servicio' es obligatorio.")
    private String tipoServicio;
    @NotBlank(message = "El 'Servicio' es obligatorio.")
    private String servicio;
    @NotBlank(message = "El 'Periodo' es obligatorio.")
    private String periodo;
    @DecimalMin(value="0.01", message = "El subTotal de la agrupación debe ser mayor a 0.01.")
    @NotNull(message = "El 'Subtotal' de la agrupación es obligatoria.")
    private BigDecimal subTotal;
    @JsonIgnore
    private String plantilla;
    private Boolean editable;
    private Boolean editando;
    @Valid
    @NotNull (message = "Debe enviar al menos una deuda.")
    @Size(min = 1, message = "Debe enviar al menos una deuda.")
    private List<DeudaClienteDto> deudaClienteDtos;

    public ServicioDeudaDto(String tipoServicio, String servicio, String periodo, BigDecimal subTotal) {
        this.tipoServicio = tipoServicio;
        this.servicio = servicio;
        this.periodo = periodo;
        this.subTotal = subTotal;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
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

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public String getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(String plantilla) {
        this.plantilla = plantilla;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getEditando() {
        return editando;
    }

    public void setEditando(Boolean editando) {
        this.editando = editando;
    }

    public List<DeudaClienteDto> getDeudaClienteDtos() {
        return deudaClienteDtos;
    }

    public void setDeudaClienteDtos(List<DeudaClienteDto> deudaClienteDtos) {
        this.deudaClienteDtos = deudaClienteDtos;
    }
}
