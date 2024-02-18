package bo.com.tesla.pagosv2.dto;

import lombok.Data;

@Data
public class ResponseQrDto {
    private String imagenQr;
    private String idQr;
    private String fechaVencimiento;
    private String bancoDestino;
    private String cuentaDestino;
    private String idTransaccion;

    private String alias; // sip al generar QR no retorna este alias
}
