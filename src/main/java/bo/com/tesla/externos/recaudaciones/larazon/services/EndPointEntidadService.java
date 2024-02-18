package bo.com.tesla.externos.recaudaciones.larazon.services;

import java.util.List;
import java.util.Optional;

import bo.com.tesla.administracion.entity.EndPointEntidadEntity;
import bo.com.tesla.administracion.entity.TransaccionCobroEntity;
import bo.com.tesla.administracion.services.IDominioService;
import bo.com.tesla.externos.recaudaciones.larazon.dao.IEndPointEntidadDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EndPointEntidadService implements IEndPointEntidadService{
	private Logger logger = LoggerFactory.getLogger(EndPointEntidadService.class);

	@Autowired
	private IEndPointEntidadDao endPointEntidadDao;

	@Autowired
	private IDominioService dominioService;

	@Autowired
	private ILaRazonService laRazonService;

	@Override
	public void updateCobroEnEntidad(Long entidadId, List<TransaccionCobroEntity> transaccionCobroEntityList) {

		/*************** LA RAZON *************************************/
		Long laRazonId = dominioService.getEntidadIdByDominio("la_razon_id");
		if(entidadId == laRazonId) {
			//Encontrar configuración de api por entidad solo cuando ingresa
			EndPointEntidadEntity endPoint = getEndPointCobroEntidad(entidadId);
			if(endPoint.getRuta() != null) {
				laRazonService.updateCobroLaRazon(endPoint, transaccionCobroEntityList);
			}
		}
		/**************************************************************/
	}

	private EndPointEntidadEntity getEndPointCobroEntidad(Long entidadId) {
		Optional<EndPointEntidadEntity> endPointEntidadEntityOptional = endPointEntidadDao.findByEntidadId(entidadId);
		if(!endPointEntidadEntityOptional.isPresent()) {
			this.logger.warn("------------------------------------");
			this.logger.warn("entidadId =  " + entidadId + ", No existe reg. activo en EndPointEntidades");
			this.logger.warn("------------------------------------");
			return new EndPointEntidadEntity(); //No generar excepcion por usarse dentro de cobro
		}
		return endPointEntidadEntityOptional.get();
	}


}
