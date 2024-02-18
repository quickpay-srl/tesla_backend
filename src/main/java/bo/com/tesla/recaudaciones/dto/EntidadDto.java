package bo.com.tesla.recaudaciones.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EntidadDto {

    public Long entidadId;
    public String nombre;
    public String nombreComercial;
    @JsonIgnore
    public String pathLogo;
    public String imagen64;

    public EntidadDto() {
    }

    public EntidadDto(Long entidadId, String nombre, String nombreComercial, String pathLogo) {
        this.entidadId = entidadId;
        this.nombre = nombre;
        this.nombreComercial = nombreComercial;
        this.pathLogo = pathLogo;
    }

}


