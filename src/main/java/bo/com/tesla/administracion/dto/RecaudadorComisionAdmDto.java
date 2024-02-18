package bo.com.tesla.administracion.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

public class RecaudadorComisionAdmDto {
    public Long recaudadorComisionId;
    public Long recaudadorId;
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

    public String estadoRecaudador;

    public RecaudadorComisionAdmDto(Long recaudadorComisionId, Long recaudadorId, Long tipoComisionId, String tipoComisionDescripcion, BigDecimal comision, String usuarioCreacionLogin, Date fechaCreacion, Date fechaModificacion, String estado, String estadoRecaudador) {
        this.recaudadorComisionId = recaudadorComisionId;
        this.recaudadorId = recaudadorId;
        this.tipoComisionId = tipoComisionId;
        this.tipoComisionDescripcion = tipoComisionDescripcion;
        this.comision = comision;
        this.usuarioCreacionLogin = usuarioCreacionLogin;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.estado = estado;
        this.estadoRecaudador = estadoRecaudador;
    }

    public RecaudadorComisionAdmDto() {
    }
}
