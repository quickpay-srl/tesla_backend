package bo.com.tesla.facturaciones.computarizada.dto;

import java.util.Date;

public class DosificacionOutDto {
    public Long dosificacionId;
    public String codigoActividadEconomica;
    public String actividadEconomica;
    public String codigoActividadEconomicaSecundaria;
    public String actividadEconomicaSecundaria;
    public String numeroAutorizacion;
    public Date fechaVigencia;
    public Date fechaLimiteEmision;
    public Long caracteristicasEspecialesId;
    public String caracteristicasEspecialesDescripcion;
    public Long monedaId;
    public String monedaDescripcion;
    public Long tipoDocumentoFiscalId;
    public String tipoDocumentoFiscalDescripcion;
    public Long sucursalId;
    public String nombreSucursal;
    public String estado;
    public String llaveDosificacion;
    public boolean alerta;
    public String titulo;
    public String subtitulo;
    public String leyenda;
    public String subleyenda;
}
