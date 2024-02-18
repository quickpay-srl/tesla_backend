package bo.com.tesla.administracion.dto;

import bo.com.tesla.administracion.entity.DominioEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

public class SucursalAdmDto {

    public Long sucursalId;
    public Long recaudadorId;
    public String nombre;
    public String direccion;
    public String telefono;
    public Long departamentoId;
    public String departamentoDescripcion;
    public Long localidadId;
    public String localidadDescripcion;
    public String usuarioCreacionLogin;
    public Date fechaCreacion;
    public String estado;

    public SucursalAdmDto(Long sucursalId, Long recaudadorId, String nombre, String direccion, String telefono, Long departamentoId, String departamentoDescripcion, Long localidadId, String localidadDescripcion, String usuarioCreacionLogin, Date fechaCreacion, String estado) {
        this.sucursalId = sucursalId;
        this.recaudadorId = recaudadorId;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.departamentoId = departamentoId;
        this.departamentoDescripcion = departamentoDescripcion;
        this.localidadId = localidadId;
        this.localidadDescripcion = localidadDescripcion;
        this.usuarioCreacionLogin = usuarioCreacionLogin;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
    }
}
