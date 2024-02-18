package bo.com.tesla.externo.sitio.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SitioDeudaClienteCabDto {

    private String codigoCliente;
    private String nroDocumento;
    private String nombreCliente;
    private String correoCliente;


    public SitioDeudaClienteCabDto(String codigoCliente, String nroDocumento, String nombreCliente, String correoCliente) {

        this.codigoCliente = codigoCliente;
        this.nroDocumento = nroDocumento;
        this.nombreCliente = nombreCliente;
        this.correoCliente = correoCliente;
    }

    public SitioDeudaClienteCabDto() {
    }
}
