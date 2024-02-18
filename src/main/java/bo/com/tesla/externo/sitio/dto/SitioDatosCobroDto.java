package bo.com.tesla.externo.sitio.dto;

import java.util.List;

public class SitioDatosCobroDto {
    private Long entidadId;

    private List<Long> deudaClienteId;


    public Long getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(Long entidadId) {
        this.entidadId = entidadId;
    }

    public List<Long> getDeudaClienteId() {
        return deudaClienteId;
    }

    public void setDeudaClienteId(List<Long> deudaClienteId) {
        this.deudaClienteId = deudaClienteId;
    }
}
