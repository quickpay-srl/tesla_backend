package bo.com.tesla.administracion.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BusquedaReportesAdmv2Dto {
    private Date fechaInicio;
    private Date fechaFin;
    private List<Long> entidadIdArray =new ArrayList<>();
    private List<Long> recaudadorIdArray =new ArrayList<>();
    private List<String> estadoArray=new ArrayList<>();
    private int paginacion;
    private Integer anio;
    private Integer dia;
    private Integer mes;
    private Boolean filtroRangoFechas;
    private String export;

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public List<Long> getEntidadIdArray() {
        return entidadIdArray;
    }

    public void setEntidadIdArray(List<Long> entidadIdArray) {
        this.entidadIdArray = entidadIdArray;
    }

    public List<Long> getRecaudadorIdArray() {
        return recaudadorIdArray;
    }

    public void setRecaudadorIdArray(List<Long> recaudadorIdArray) {
        this.recaudadorIdArray = recaudadorIdArray;
    }

    public List<String> getEstadoArray() {
        return estadoArray;
    }

    public void setEstadoArray(List<String> estadoArray) {
        this.estadoArray = estadoArray;
    }

    public int getPaginacion() {
        return paginacion;
    }

    public void setPaginacion(int paginacion) {
        this.paginacion = paginacion;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Boolean getFiltroRangoFechas() {
        return filtroRangoFechas;
    }

    public void setFiltroRangoFechas(Boolean filtroRangoFechas) {
        this.filtroRangoFechas = filtroRangoFechas;
    }

    public String getExport() {
        return export;
    }

    public void setExport(String export) {
        this.export = export;
    }
}
