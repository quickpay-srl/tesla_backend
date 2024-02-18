package bo.com.tesla.pagosv2.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ResponseSipto {
    private String codigo;
    private String mensaje;
    private Map objeto;
    private String pageInfo;
}
