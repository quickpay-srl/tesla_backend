package bo.com.tesla.recaudaciones.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.ServicioWebEntidadEntity;
import bo.com.tesla.entidades.dao.IEntidadDao;
import bo.com.tesla.recaudaciones.dao.ITransaccionCobroDao;
import bo.com.tesla.recaudaciones.dto.TransaccionesCobroApiDto;
import bo.com.tesla.useful.constant.Entidad;

@Service
public class WebServiceEntidadesService implements IWebServiceEntidadesService {
	
	@Autowired
	private IEntidadDao entidadDao;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ITransaccionCobroDao ITransaccionCobroDao;

	@Override
	public Boolean sendTransaccionForEntidad(Long entidadId, Long archivoId, String codigoCliente, String servicio,String tipoServicio,String periodo) {
		
		switch (entidadId+"") {
		case Entidad.LA_RAZON:
			 this.sendTransaccionLaRazon(entidadId,archivoId, codigoCliente, servicio, tipoServicio, periodo);
			break;		
		}
		return null;
	}
	
	
	private Boolean sendTransaccionLaRazon(Long entidadId, Long archivoId, String codigoCliente,String tipoServicio, String servicio, String periodo ) {
		EntidadEntity entidad= this.entidadDao.findEntidadById(entidadId).get();
		List<ServicioWebEntidadEntity> serviciosWebList=entidad.getServicioWebEntidadEntityList();
		TransaccionesCobroApiDto transaccion =	this.ITransaccionCobroDao.getTransaccionCobroForEntidad(archivoId, codigoCliente, servicio, tipoServicio, periodo);
		// TODO CONSUMIR SERVICIO REST
		return true;		
	}

}
