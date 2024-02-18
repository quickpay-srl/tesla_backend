package bo.com.tesla.externos.recaudaciones.larazon.dto;

public class RptaCobroEntidadDto {
    public String id;
    public boolean resp;

    public RptaCobroEntidadDto(String id, boolean resp) {
        this.id = id;
        this.resp = resp;
    }

    public RptaCobroEntidadDto(){}
}
