package bo.com.tesla.externo.sitio.dto;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class DatosConfirmadoQrDto {
    private Long datosconfirmadoQrId;
    private String aliasSip;
    private String numeroOrdenOriginanteSip;
    private String montoSip;
    private String idQrSip;
    private String monedaSip;
    private String fechaProcesoSip;
    private String cuentaClienteSip;
    private String nombreClienteSip;
    private String documentoClienteSip;
    private String fechaVencimientoSip;
    private Date fechaRegistro;
    private String  estado;
}
