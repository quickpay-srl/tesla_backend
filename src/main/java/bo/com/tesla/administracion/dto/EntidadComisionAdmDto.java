package bo.com.tesla.administracion.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

public class EntidadComisionAdmDto {

    public Long entidadComisionId;
    public Long entidadId;
    public Long tipoComisionId;
    public String tipoComisionDescripcion;
    public BigDecimal comision;
    public String usuarioCreacionLogin;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/La_Paz")
    @Temporal(TemporalType.TIMESTAMP)
    public Date fechaCreacion;
    public Long usuarioModificacion;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/La_Paz")
    @Temporal(TemporalType.TIMESTAMP)
    public Date fechaModificacion;
    public String estado;

    public String estadoEntidad;

    public EntidadComisionAdmDto(Long entidadComisionId, Long entidadId, Long tipoComisionId, String tipoComisionDescripcion, BigDecimal comision, String usuarioCreacionLogin, Date fechaCreacion, Date fechaModificacion, String estado, String estadoEntidad) {
        this.entidadComisionId = entidadComisionId;
        this.entidadId = entidadId;
        this.tipoComisionId = tipoComisionId;
        this.tipoComisionDescripcion = tipoComisionDescripcion;
        this.comision = comision;
        this.usuarioCreacionLogin = usuarioCreacionLogin;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.estado = estado;
        this.estadoEntidad = estadoEntidad;
    }

    public EntidadComisionAdmDto() {
    }
}
