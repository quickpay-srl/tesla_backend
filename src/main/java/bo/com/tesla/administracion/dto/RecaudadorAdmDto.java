package bo.com.tesla.administracion.dto;

import bo.com.tesla.administracion.entity.EntidadRecaudadorEntity;
import bo.com.tesla.administracion.entity.SucursalEntity;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class RecaudadorAdmDto {

    public Long recaudadorId;
    public Long tipoRecaudadorId;
    public String tipoRecaudadorDescripcion;
    public String nombre;
    public String direccion;
    public String telefono;
    public String usuarioCreacionLogin;
    public Date fechaCreacion;
    public String estado;
    public List<SucursalAdmDto> sucursalAdmDtoList;
    public List<EntidadAdmDto> entidadAdmDtoList;
    public List<Long> entidadIdLst;

    //public List<RecaudadorMetodoCobroDto> recaudadorMetodoCobroDtoList;
    public List<Long> metodoCobroIdLst;
    public String metodoCobroLstJoin;


    public RecaudadorAdmDto(Long recaudadorId, Long tipoRecaudadorId, String tipoRecaudadorDescripcion, String nombre, String direccion, String telefono, String usuarioCreacionLogin, Date fechaCreacion, String estado) {
        this.recaudadorId = recaudadorId;
        this.tipoRecaudadorId = tipoRecaudadorId;
        this.tipoRecaudadorDescripcion = tipoRecaudadorDescripcion;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.usuarioCreacionLogin = usuarioCreacionLogin;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
    }
}
