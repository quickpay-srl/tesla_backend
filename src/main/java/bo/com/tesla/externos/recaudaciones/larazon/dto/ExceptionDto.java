package bo.com.tesla.externos.recaudaciones.larazon.dto;

public class ExceptionDto {
    public Integer estadoCodigo;
    public String detalle;

       public ExceptionDto(Integer estadoCodigo, String detalle) {
        this.estadoCodigo = estadoCodigo;
        this.detalle = detalle;
    }
}
