package bo.com.tesla.useful.dto;


public class ResponseDto<T> {
    public Boolean status;
    public String message;
    public T result;

}
