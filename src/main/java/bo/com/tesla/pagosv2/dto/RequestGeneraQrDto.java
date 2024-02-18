package bo.com.tesla.pagosv2.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestGeneraQrDto {

    private String detalleGlosa;
    private Double monto;
    private String moneda;
    private String fechaVencimiento;
    private List<Long> lstDeudaCliente;

}
