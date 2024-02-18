package bo.com.tesla.recaudaciones.services;

public interface IWebServiceEntidadesService {
	
	public Boolean sendTransaccionForEntidad(Long entidadId, Long archivoId, String codigoCliente, String servicio,String tipoServicio,String periodo);

}
