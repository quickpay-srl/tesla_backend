package bo.com.tesla.pagosv2.dto;

import lombok.Data;

@Data
public class ResponseEstadoQrDto {
    private String alias;
    private String estadoActual;
    private String fechaProcesamiento;
    private String fechaRegistro;
    private String numeroOrdenOriginante;
    private String monto;
    private String idQr;
    private String moneda;
    private String cuentaCliente;
    private String nombreCliente;
    private String documentoCliente;

}
