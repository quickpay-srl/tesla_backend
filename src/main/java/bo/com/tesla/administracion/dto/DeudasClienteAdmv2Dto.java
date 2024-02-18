package bo.com.tesla.administracion.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.Date;


public interface DeudasClienteAdmv2Dto {

	Long getArchivoId();
	String getCodigoCliente();
	String getServicio();
	String getTipoServicio();
	String getPeriodo();
	String getEstado();

	String getNombreCliente();
	@JsonFormat( pattern = "dd/MM/yyyy HH:mm:ss",timezone="America/La_Paz")
	Date getFechaCreacion();
	BigDecimal getTotal();
	String getNombreRecaudadora();
	BigDecimal getComision();
	String getCajero();
	String getNombre();

	/*private Long archivoId;
	private String codigoCliente;
	private String servicio;
	private String tipoServicio;
	private String periodo;
	private String estado;
	private String nombreCliente;
	@JsonFormat( pattern = "dd/MM/yyyy HH:mm:ss",timezone="America/La_Paz")
	private Date fechaCreacion;
	private BigDecimal total;
	private String nombreRecaudadora;
	private BigDecimal comision;
	private String cajero;
	private String nombre;

	public DeudasClienteAdmv2Dto(Long archivoId, String codigoCliente,String servicio,
							   String tipoServicio,String periodo, String estado,
							   String nombreCliente,Date fechaCreacion,BigDecimal total,String nombreRecaudadora,
								 BigDecimal comision,String cajero,String nombre) {


		this.archivoId=archivoId;
		this.codigoCliente=codigoCliente;
		this.servicio=servicio;
		this.tipoServicio=tipoServicio;
		this.periodo=periodo;
		if(estado.equals("ACTIVO")) {
			this.estado="POR PAGAR";
		}else if(estado.equals("COBRADO")) {

			this.estado="COBRADAS";
			this.nombreCliente=nombreCliente;
			this.fechaCreacion=fechaCreacion;
			this.total=total;
			this.nombreRecaudadora=nombreRecaudadora;
			this.comision=comision;
			this.cajero=cajero;
			this.nombre = nombre;

		}else if(estado.equals("ANULADO")) {
			this.estado="ANULADO";
			this.nombreCliente=nombreCliente;
			this.fechaCreacion=fechaCreacion;
			this.total=total;
			this.nombreRecaudadora=nombreRecaudadora;
			this.comision=comision;
			this.cajero=cajero;
			this.nombre = nombre;
		} else if(estado.equals("ERRONEO")) {
			this.estado="ERRONEO";
			this.nombreCliente=nombreCliente;
			this.fechaCreacion=fechaCreacion;
			this.total=total;
			this.nombreRecaudadora=nombreRecaudadora;
			this.comision=comision;
			this.cajero=cajero;
			this.nombre = nombre;
		}
	}

	public Long getArchivoId() {
		return archivoId;
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public String getServicio() {
		return servicio;
	}

	public String getTipoServicio() {
		return tipoServicio;
	}

	public String getPeriodo() {
		return periodo;
	}

	public String getEstado() {
		return estado;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public String getNombreRecaudadora() {
		return nombreRecaudadora;
	}

	public BigDecimal getComision() {
		return comision;
	}

	public String getCajero() {
		return cajero;
	}

	public String getNombre() {
		return nombre;
	}*/
}
