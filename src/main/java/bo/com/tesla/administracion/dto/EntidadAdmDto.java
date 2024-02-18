package bo.com.tesla.administracion.dto;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

public class EntidadAdmDto {

    public Long entidadId;
    @NotBlank(message = "El NOMBRE es obligatorio.")
    public String nombre;
    public String nombreComercial;
    @NotBlank(message = "La DIRECCION es obligatoria.")
    public String direccion;
    public String telefono;
    public String nit;
    public String pathLogo;
    public Boolean comprobanteEnUno;
    public Long actividadEconomicaId;
    public String actividadEconomicaDescripcion;
    public Long tipoEntidadId;
    public String tipoEntidadDescripcion;
    public Long modalidadFacturacionId;
    public String modalidadFacturacionDescripcion;
    public Boolean esCobradora;
    public Boolean esPagadora;
    public Date fechaCreacion;
    public String usuarioCreacionLogin;
    public String estado;
    public List<SucursalEntidadAdmDto> sucursalEntidadAdmDtoList;
    public List<Long> recaudadorIdLst;

    public String imagen64;

    public EntidadAdmDto() {}

    public EntidadAdmDto(Long entidadId, @NotBlank(message = "El NOMBRE es obligatorio.") String nombre, String nombreComercial, @NotBlank(message = "La DIRECCION es obligatoria.") String direccion, String telefono, String nit, String pathLogo, Boolean comprobanteEnUno, Long actividadEconomicaId, String actividadEconomicaDescripcion, Long tipoEntidadId, String tipoEntidadDescripcion, Long modalidadFacturacionId, String modalidadFacturacionDescripcion, Boolean esCobradora, Boolean esPagadora, Date fechaCreacion, String usuarioCreacionLogin, String estado) {
        this.entidadId = entidadId;
        this.nombre = nombre;
        this.nombreComercial = nombreComercial;
        this.direccion = direccion;
        this.telefono = telefono;
        this.nit = nit;
        this.pathLogo = pathLogo;
        this.comprobanteEnUno = comprobanteEnUno;
        this.actividadEconomicaId = actividadEconomicaId;
        this.actividadEconomicaDescripcion = actividadEconomicaDescripcion;
        this.tipoEntidadId = tipoEntidadId;
        this.tipoEntidadDescripcion = tipoEntidadDescripcion;
        this.modalidadFacturacionId = modalidadFacturacionId;
        this.modalidadFacturacionDescripcion = modalidadFacturacionDescripcion;
        this.esCobradora = esCobradora;
        this.esPagadora = esPagadora;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacionLogin = usuarioCreacionLogin;
        this.estado = estado;
    }

}
