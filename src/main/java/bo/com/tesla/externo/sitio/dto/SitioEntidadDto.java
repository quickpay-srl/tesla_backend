package bo.com.tesla.externo.sitio.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SitioEntidadDto {
    private Long entidadId;
    private String nombre;
    private String nombreComercial;
    private String subdominioEmpresa;
    private String direccion;
    private String telefono;
    private String nit;
    private String llaveDosificacion;
    private String pathLogo;
    private String imagen64;
    private Boolean comprobanteEnUno;

    public SitioEntidadDto(Long entidadId, String nombre, String nombreComercial,String subdominioEmpresa, String direccion, String telefono, String nit, String llaveDosificacion, String pathLogo, Boolean comprobanteEnUno) {
        this.entidadId = entidadId;
        this.nombre = nombre;
        this.nombreComercial = nombreComercial;
        this.subdominioEmpresa = subdominioEmpresa;
        this.direccion = direccion;
        this.telefono = telefono;
        this.nit = nit;
        this.llaveDosificacion = llaveDosificacion;
        this.pathLogo = pathLogo;
        this.comprobanteEnUno = comprobanteEnUno;
    }

    public SitioEntidadDto() {
    }
}
