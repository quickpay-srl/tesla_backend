package bo.com.tesla.administracion.dto;

public class RecaudadorMetodoCobroDto {
    public Long metodoCobroId;
    public String metodoCobro;

    public RecaudadorMetodoCobroDto(Long metodoCobroId, String metodoCobro) {
        this.metodoCobroId = metodoCobroId;
        this.metodoCobro = metodoCobro;
    }
}
