package bo.com.tesla.facturacion.electronica.dto;


public class ResponseFacElectronicaDto<T> {
    public Boolean status;
    public String message;
    public T result;

}
