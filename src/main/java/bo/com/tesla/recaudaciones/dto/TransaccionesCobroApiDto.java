package bo.com.tesla.recaudaciones.dto;

import java.math.BigDecimal;
import java.util.Date;

public class TransaccionesCobroApiDto {
    
    public String tipoServicio;
    public String servicio;
    public String periodo;
    public String codigoCliente;
    public BigDecimal totalDeuda;
    public String nombreClienteArchivo;
    public String nroDocumentoClienteArchivo;
    public String nombreRecaudadora;
    public String nombreSucursalRecaudadora;    
    public Date fechaCobro;
    
	public TransaccionesCobroApiDto(String tipoServicio, String servicio, String periodo, String codigoCliente,
			BigDecimal totalDeuda, String nombreClienteArchivo, String nroDocumentoClienteArchivo,
			String nombreRecaudadora, String nombreSucursalRecaudadora, Date fechaCobro) {
	
		this.tipoServicio = tipoServicio;
		this.servicio = servicio;
		this.periodo = periodo;
		this.codigoCliente = codigoCliente;
		this.totalDeuda = totalDeuda;
		this.nombreClienteArchivo = nombreClienteArchivo;
		this.nroDocumentoClienteArchivo = nroDocumentoClienteArchivo;
		this.nombreRecaudadora = nombreRecaudadora;
		this.nombreSucursalRecaudadora = nombreSucursalRecaudadora;
		this.fechaCobro = fechaCobro;
	}
    
    
    
	
}
