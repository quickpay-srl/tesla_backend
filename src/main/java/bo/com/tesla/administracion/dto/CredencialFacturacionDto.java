package bo.com.tesla.administracion.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CredencialFacturacionDto {

    @NotNull(message = "La entidad es obligatoria")
    public Long entidadId;
    @NotNull(message = "La sucursal de entidad es obligatoria")
    public Long sucursalEntidadId;
    @NotBlank(message = "El login es obligatorio")
    public String login;
    @NotBlank(message = "El password es obligatorio")
    public String password;

    public CredencialFacturacionDto(Long entidadId, Long sucursalEntidadId, String login, String password) {
        this.entidadId = entidadId;
        this.sucursalEntidadId = sucursalEntidadId;
        this.login = login;
        this.password = password;
    }
}
