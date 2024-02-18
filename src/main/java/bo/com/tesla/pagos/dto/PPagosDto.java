package bo.com.tesla.pagos.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import bo.com.tesla.useful.config.BusinesException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PPagosDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public int key;

	public Integer nroRegistro;

	public String codigoCliente;

	public String nombreCliente;

	public Date fechaNacimientoCliente;

	public String genero;

	public String nroDocumentoCliente;

	public String extencionDocumento;

	public String tipoDocumentoId;

	public Integer cantidad;

	public BigDecimal montoUnitario;

	public String periodo;

	public Long archivoId;

	public String concepto;

	public BigDecimal subTotal;

	public BigDecimal totalPagar;

	public Long servicioProductoId;

	public BigDecimal comisionRecaudacion;

	public BigDecimal comisionExacta;

	public Long entidadId;

	public Long recaudadorId;

	public String nombreTitular;

	public String documentoTitular;

	public String codigoTransaccion;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/La_Paz")
	public Date fechaModificacion;

	public String estado;	
	public Date fechaInicio;
	public Date fechaFin;	
	public Long usuarioId;
	public String parameter;
	public String nombreServicioProducto;
	public int paginacion;
	

	public List<PPagosDto> abonosClientesList;

	/*
	 * Contructor para pagos IPAbonoClienteDao.groupByAbonosClientes
	 */

	public PPagosDto(String codigoCliente, String periodo, BigDecimal totalPagar) {
		this.codigoCliente = codigoCliente;
		this.totalPagar = totalPagar;
		this.periodo = periodo;
	}

	/*
	 * Contructor para pagos IPAbonoClienteDao.findByCodigoAndArchivoId
	 */

	public PPagosDto(Long archivoId, int nroRegistro, String codigoCliente, String nombreCliente,
			Date fechaNacimientoCliente, String genero, String nroDocumentoCliente, String extencionDocumento,
			String tipoDocumentoId, int cantidad, BigDecimal montoUnitario, String periodo, String concepto) {

		this.archivoId = archivoId;
		this.nroRegistro = nroRegistro;
		this.codigoCliente = codigoCliente;
		this.nombreCliente = nombreCliente;
		this.fechaNacimientoCliente = fechaNacimientoCliente;
		this.genero = genero;
		this.nroDocumentoCliente = nroDocumentoCliente;
		this.extencionDocumento = extencionDocumento;
		this.tipoDocumentoId = tipoDocumentoId;
		this.cantidad = cantidad;
		this.montoUnitario = montoUnitario;
		this.periodo = periodo;
		this.concepto = concepto;
		this.subTotal = montoUnitario.multiply(new BigDecimal(cantidad));

	}

	/*
	 * Contructor para pagos IPAbonoClienteDao.getAbonosParaPagar
	 */

	public PPagosDto(String codigoCliente, String nroDocumentoCliente, String nombreCliente) {
		this.key = new Integer(codigoCliente);
		this.codigoCliente = codigoCliente;
		this.nroDocumentoCliente = nroDocumentoCliente;
		this.nombreCliente = nombreCliente;
	}

	/*
	 * Contructor para pagos IPAbonoClienteDao.getAbonosParaPagar
	 */

	public PPagosDto(Long archivoId, String codigoCliente, String nroDocumentoCliente, String nombreCliente) {
		this.key = new Integer(codigoCliente);
		this.archivoId = archivoId;
		this.codigoCliente = codigoCliente;
		this.nroDocumentoCliente = nroDocumentoCliente;
		this.nombreCliente = nombreCliente;
	}

	/*
	 * Contructor para pagos IPAbonoClienteDao.getBeneficiario
	 */

	public PPagosDto(String codigoCliente, String nroDocumentoCliente, String nombreCliente, String periodo,
			BigDecimal totalPagar) {
		this.key = new Integer(codigoCliente);
		this.codigoCliente = codigoCliente;
		this.nroDocumentoCliente = nroDocumentoCliente;
		this.nombreCliente = nombreCliente;
		this.periodo = periodo;
		this.totalPagar = totalPagar;
	}

	/*
	 * Contructor para pagos IPAbonoClienteDao.getBeneficiarioAndMontoToal
	 */

	public PPagosDto(String codigoCliente, String nroDocumentoCliente, String nombreCliente,
			BigDecimal totalPagar) {
		this.key = new Integer(codigoCliente);
		this.codigoCliente = codigoCliente;
		this.nroDocumentoCliente = nroDocumentoCliente;
		this.nombreCliente = nombreCliente;

		this.totalPagar = totalPagar;
	}

	/*
	 * findTransaccionsByUsuario
	 * */
	public PPagosDto(Long key,String codigoTransaccion, Date fechaModificacion, String estado, String codigoCliente,
			String nombreCliente, String nroDocumentoCliente, String extencionDocumentoId,String nombreServicioProducto, BigDecimal totalPagar) {
		
		this.key= key.intValue();
		
		this.codigoTransaccion = codigoTransaccion;
		this.fechaModificacion = fechaModificacion;
		this.estado = estado;
		this.codigoCliente = codigoCliente;
		this.nombreCliente = nombreCliente;
		this.nroDocumentoCliente = nroDocumentoCliente;
		this.extencionDocumento = extencionDocumentoId;
		this.totalPagar = totalPagar;
		this.nombreServicioProducto=nombreServicioProducto;
		
		}

	public PPagosDto() {

	}

	public Integer getNroRegistro() {
		return nroRegistro;
	}

	public void setNroRegistro(Integer nroRegistro) throws BusinesException {

		if (nroRegistro == null) {
			this.nroRegistro = null;
			throw new BusinesException("El campo Nro Registro no puede ser nulo");

		} else {
			this.nroRegistro = nroRegistro;
		}

	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(String codigoCliente) throws BusinesException {
		if (codigoCliente == null || codigoCliente.isEmpty()) {
			this.codigoCliente = null;
			throw new BusinesException("El campo 'Código de Beneficiario' no puede ser nulo");

		} else {
			this.codigoCliente = codigoCliente;
		}

	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) throws BusinesException {
		if (nombreCliente == null || nombreCliente.isEmpty()) {
			this.nombreCliente = null;
			throw new BusinesException("El campo 'Nombre Beneficiario' no puede ser nulo");

		} else {
			this.nombreCliente = nombreCliente;
		}

	}

	public Date getFechaNacimientoCliente() {
		return fechaNacimientoCliente;
	}

	public void setFechaNacimientoCliente(Date fechaNacimientoCliente) throws BusinesException {
		if (fechaNacimientoCliente == null) {
			this.fechaNacimientoCliente = null;
			throw new BusinesException("El campo 'Fecha Nacimiento' no puede ser nulo");
		} else {
			this.fechaNacimientoCliente = fechaNacimientoCliente;
		}
	}

	public String getNroDocumentoCliente() {
		return nroDocumentoCliente;
	}

	public void setNroDocumentoCliente(String nroDocumentoCliente) throws BusinesException {

		if (nroDocumentoCliente == null || nroDocumentoCliente.isEmpty()) {
			this.nroDocumentoCliente = null;
			throw new BusinesException("El campo 'Nro. Documento Beneficiario' no puede ser nulo");
		} else {
			this.nroDocumentoCliente = nroDocumentoCliente;
		}

	}

	public String getExtencionDocumento() {
		return extencionDocumento;
	}

	public void setExtencionDocumento(String extencionDocumento) throws BusinesException {
		if (extencionDocumento == null || extencionDocumento.isEmpty()) {
			this.extencionDocumento = null;
			throw new BusinesException("El campo 'Extencion de Documento' no puede ser nulo");
		} else {
			this.extencionDocumento = extencionDocumento;
		}

	}

	public String getTipoDocumentoId() {
		return tipoDocumentoId;
	}

	public void setTipoDocumentoId(String tipoDocumentoId) throws BusinesException {
		if (tipoDocumentoId == null || tipoDocumentoId.isEmpty()) {
			this.tipoDocumentoId = null;
			throw new BusinesException("El campo 'Tipo Documento' no puede ser nulo");
		} else {
			this.tipoDocumentoId = tipoDocumentoId;
		}

	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) throws BusinesException {
		if (cantidad == null) {
			this.cantidad = null;
			throw new BusinesException("El campo 'Cantidad' no puede ser nulo");
		} else {
			this.cantidad = cantidad;
		}
	}

	public BigDecimal getMontoUnitario() {
		return montoUnitario;
	}

	public void setMontoUnitario(BigDecimal montoUnitario) throws BusinesException {

		if (montoUnitario == null) {
			this.montoUnitario = null;
			throw new BusinesException("El campo 'Monto Unitario' no puede ser nulo");
		} else {
			this.montoUnitario = montoUnitario;
		}

	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) throws BusinesException {

		if (periodo == null || periodo.isEmpty()) {
			this.periodo = null;
			throw new BusinesException("El campo 'Periodo' no puede ser nulo");
		} else {
			this.periodo = periodo;
		}

	}

	public Long getArchivoId() {
		return archivoId;
	}

	public void setArchivoId(Long archivoId) {
		this.archivoId = archivoId;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) throws BusinesException {
		if (concepto == null || concepto.isEmpty()) {
			this.concepto = null;
			throw new BusinesException("El campo 'Concepto' no puede ser nulo");

		} else {
			this.concepto = concepto;
		}

	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) throws BusinesException {

		if (genero == null || genero.isEmpty()) {
			this.genero = null;
			throw new BusinesException("El campo 'Fecha Nacimiento' no puede ser nulo");

		} else {
			this.genero = genero;
		}

	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public List<PPagosDto> getAbonosClientesList() {
		return abonosClientesList;
	}

	public void setAbonosClientesList(List<PPagosDto> abonosClientesList) {
		this.abonosClientesList = abonosClientesList;
	}

	public BigDecimal getTotalPagar() {
		return totalPagar;
	}

	public void setTotalPagar(BigDecimal totalPagar) {
		this.totalPagar = totalPagar;
	}

	public Long getServicioProductoId() {
		return servicioProductoId;
	}

	public void setServicioProductoId(Long servicioProductoId) {
		this.servicioProductoId = servicioProductoId;
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

	public Long getEntidadId() {
		return entidadId;
	}

	public void setEntidadId(Long entidadId) {
		this.entidadId = entidadId;
	}

	public Long getRecaudadorId() {
		return recaudadorId;
	}

	public void setRecaudadorId(Long recaudadorId) {
		this.recaudadorId = recaudadorId;
	}

	public String getNombreTitular() {
		return nombreTitular;
	}

	public void setNombreTitular(String nombreTitular) {
		this.nombreTitular = nombreTitular;
	}

	public String getDocumentoTitular() {
		return documentoTitular;
	}

	public void setDocumentoTitular(String documentoTitular) {
		this.documentoTitular = documentoTitular;
	}

	public String getCodigoTransaccion() {
		return codigoTransaccion;
	}

	public void setCodigoTransaccion(String codigoTransaccion) {
		this.codigoTransaccion = codigoTransaccion;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
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

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getNombreServicioProducto() {
		return nombreServicioProducto;
	}

	public void setNombreServicioProducto(String nombreServicioProducto) {
		this.nombreServicioProducto = nombreServicioProducto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getPaginacion() {
		return paginacion;
	}

	public void setPaginacion(int paginacion) {
		this.paginacion = paginacion;
	}
	
	
	
	
	

}
