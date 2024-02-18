package bo.com.tesla.administracion.dto;

import bo.com.tesla.administracion.entity.DominioEntity;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

public class SucursalEntidadAdmDto {

    public Long sucursalEntidadId;
    public Long entidadId;
    public String nombreSucursal;
    public String direccion;
    public String telefono;
    public String usuarioCreacionLogin;
    public Date fechaCreacion;
    public String estado;
    public String email;
    public Short numeroSucursalSin;
    public Boolean emiteFacturaTesla;
    public Long departamentoId;
    public String departamentoDescripcion;
    public Long municipioId;
    public String municipioDescripcion;

    public SucursalEntidadAdmDto(Long sucursalEntidadId, Long entidadId, String nombreSucursal, String direccion, String telefono, String usuarioCreacionLogin, Date fechaCreacion, String estado, String email, Short numeroSucursalSin, Boolean emiteFacturaTesla, Long departamentoId, String departamentoDescripcion, Long municipioId, String municipioDescripcion) {
        this.sucursalEntidadId = sucursalEntidadId;
        this.entidadId = entidadId;
        this.nombreSucursal = nombreSucursal;
        this.direccion = direccion;
        this.telefono = telefono;
        this.usuarioCreacionLogin = usuarioCreacionLogin;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.email = email;
        this.numeroSucursalSin = numeroSucursalSin;
        this.emiteFacturaTesla = emiteFacturaTesla;
        this.departamentoId = departamentoId;
        this.departamentoDescripcion = departamentoDescripcion;
        this.municipioId = municipioId;
        this.municipioDescripcion = municipioDescripcion;
    }
}
