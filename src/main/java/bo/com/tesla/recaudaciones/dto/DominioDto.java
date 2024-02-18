package bo.com.tesla.recaudaciones.dto;

public class DominioDto {

    public Long dominioId;
    public String dominio;
    public String descripcion;
    public String abreviatura;
    public String imagen64;

    public DominioDto(Long dominioId, String dominio, String descripcion, String abreviatura, String img) {
        this.dominioId = dominioId;
        this.dominio = dominio;
        this.descripcion = descripcion;
        this.abreviatura = abreviatura;
        this.imagen64 = img;
    }
}

