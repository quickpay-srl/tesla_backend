package bo.com.tesla.pagos.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PBeneficiarioReporteDto implements Serializable {

    private static final long serialVersionUID = 1L;
    public Date fechaIni;
    public Date fechaFin;
    
    public List<String> estadoList=new ArrayList<>();
   
    public List<Long> recaudadorIdList=new ArrayList<>();
    public Long entidadId;
    public String codigoCliente;
    public String nombreCliente;
    @JsonFormat( pattern = "dd/MM/yyyy",timezone="America/La_Paz")
    public Date fechaNacimientoCliente;
    public String nroDocumentoCliente;
    public String extencionDocumentoId;
    public String tipoDocumentoId;    
    public BigDecimal total;
    public String periodo;   
    public String genero;
    public BigDecimal comisionRecaudacion;
    public BigDecimal comisionExacta;
    @JsonFormat( pattern = "dd/MM/yyyy HH:mm:ss",timezone="America/La_Paz")
    public Date fechaEstado;
    public String nombreTitular;
    public String nroDocumentoTitular;
    public String recaudadorNombre;
    public String entidadNombre;
    public String estado;
    public Long servicioProductoId;
    public int paginacion;
    public String export;
    
    
    public PBeneficiarioReporteDto(String codigoCliente, String nombreCliente, Date fechaNacimientoCliente,
			String nroDocumentoCliente, String extencionDocumentoId,  BigDecimal total,
			String periodo, String genero, BigDecimal comisionRecaudacion, BigDecimal comisionExacta, Date fechaEstado,
			String nombreTitular, String nroDocumentoTitular, String recaudadorNombre,String estado) {
	
		this.codigoCliente = codigoCliente;
		this.nombreCliente = nombreCliente;
		this.fechaNacimientoCliente = fechaNacimientoCliente;
		this.nroDocumentoCliente = nroDocumentoCliente;
		this.extencionDocumentoId = extencionDocumentoId;	
		this.total = total;
		this.periodo = periodo;
		this.genero = genero;
		this.comisionRecaudacion = comisionRecaudacion;
		this.comisionExacta = comisionExacta;
		this.fechaEstado = fechaEstado;
		this.nombreTitular = nombreTitular;
		this.nroDocumentoTitular = nroDocumentoTitular;
		this.recaudadorNombre = recaudadorNombre;	
		this.estado=estado;
	}
	public String getCodigoCliente() {
		return codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public Date getFechaNacimientoCliente() {
		return fechaNacimientoCliente;
	}
	public void setFechaNacimientoCliente(Date fechaNacimientoCliente) {
		this.fechaNacimientoCliente = fechaNacimientoCliente;
	}
	public String getNroDocumentoCliente() {
		return nroDocumentoCliente;
	}
	public void setNroDocumentoCliente(String nroDocumentoCliente) {
		this.nroDocumentoCliente = nroDocumentoCliente;
	}
	public String getExtencionDocumentoId() {
		return extencionDocumentoId;
	}
	public void setExtencionDocumentoId(String extencionDocumentoId) {
		this.extencionDocumentoId = extencionDocumentoId;
	}
	public String getTipoDocumentoId() {
		return tipoDocumentoId;
	}
	public void setTipoDocumentoId(String tipoDocumentoId) {
		this.tipoDocumentoId = tipoDocumentoId;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public BigDecimal getComisionRecaudacion() {
		return comisionRecaudacion;
	}
	public void setComisionRecaudacion(BigDecimal comisionRecaudacion) {
		this.comisionRecaudacion = comisionRecaudacion;
	}
	public BigDecimal getComisionExacta() {
		return comisionExacta;
	}
	public void setComisionExacta(BigDecimal comisionExacta) {
		this.comisionExacta = comisionExacta;
	}
	public Date getFechaEstado() {
		return fechaEstado;
	}
	public void setFechaEstado(Date fechaEstado) {
		this.fechaEstado = fechaEstado;
	}
	public String getNombreTitular() {
		return nombreTitular;
	}
	public void setNombreTitular(String nombreTitular) {
		this.nombreTitular = nombreTitular;
	}
	public String getNroDocumentoTitular() {
		return nroDocumentoTitular;
	}
	public void setNroDocumentoTitular(String nroDocumentoTitular) {
		this.nroDocumentoTitular = nroDocumentoTitular;
	}
	public String getRecaudadorNombre() {
		return recaudadorNombre;
	}
	public void setRecaudadorNombre(String recaudadorNombre) {
		this.recaudadorNombre = recaudadorNombre;
	}
	public String getEntidadNombre() {
		return entidadNombre;
	}
	public void setEntidadNombre(String entidadNombre) {
		this.entidadNombre = entidadNombre;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Date getFechaIni() {
		return fechaIni;
	}
	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public List<String> getEstadoList() {
		return estadoList;
	}
	public void setEstadoList(List<String> estadoList) {
		this.estadoList = estadoList;
	}
	public List<Long> getRecaudadorIdList() {
		return recaudadorIdList;
	}
	public void setRecaudadorIdList(List<Long> recaudadorIdList) {
		this.recaudadorIdList = recaudadorIdList;
	}
	public Long getEntidadId() {
		return entidadId;
	}
	public void setEntidadId(Long entidadId) {
		this.entidadId = entidadId;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Long getServicioProductoId() {
		return servicioProductoId;
	}
	public void setServicioProductoId(Long servicioProductoId) {
		this.servicioProductoId = servicioProductoId;
	}
	public int getPaginacion() {
		return paginacion;
	}
	public void setPaginacion(int paginacion) {
		this.paginacion = paginacion;
	}
	public String getExport() {
		return export;
	}
	public void setExport(String export) {
		this.export = export;
	}
    
    
    
    

}
