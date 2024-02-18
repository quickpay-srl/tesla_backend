package bo.com.tesla.recaudaciones.dto;

import lombok.*;

import javax.persistence.Column;
import java.util.Date;


@NoArgsConstructor
@Getter
@Setter
public class FacturaDto {
    private Long facturaId;
    private String responseCuf;
    private String responseState;
    private String responseNroFactura;
    private String responseCodigoCliente;
    private String responseNumeroDocumentoCliente;
    private String responseRazonSocialCliente;
    private String responseComplementoCliente;
    private String responseEmailCliente;
    private String responsePdfRepgrafica;
    private String responseXmlRepgrafica;
    private String responseSinRepgrafica;
    private String responseRolloRepgrafica;
    private Date fechaRegistro;

    public FacturaDto(Long facturaId, String responseCuf
            , String responseState, String responseNroFactura,
                      String responseCodigoCliente, String responseNumeroDocumentoCliente, String responseRazonSocialCliente,
                      String responseComplementoCliente, String responseEmailCliente, String responsePdfRepgrafica,
                      String responseXmlRepgrafica, String responseSinRepgrafica, String responseRolloRepgrafica, Date fechaRegistro) {
        this.facturaId = facturaId;
        this.responseCuf = responseCuf;
        this.responseState = responseState;
        this.responseNroFactura = responseNroFactura;
        this.responseCodigoCliente = responseCodigoCliente;
        this.responseNumeroDocumentoCliente = responseNumeroDocumentoCliente;
        this.responseRazonSocialCliente = responseRazonSocialCliente;
        this.responseComplementoCliente = responseComplementoCliente;
        this.responseEmailCliente = responseEmailCliente;
        this.responsePdfRepgrafica = responsePdfRepgrafica;
        this.responseXmlRepgrafica = responseXmlRepgrafica;
        this.responseSinRepgrafica = responseSinRepgrafica;
        this.responseRolloRepgrafica = responseRolloRepgrafica;
        this.fechaRegistro = fechaRegistro;
    }
}
