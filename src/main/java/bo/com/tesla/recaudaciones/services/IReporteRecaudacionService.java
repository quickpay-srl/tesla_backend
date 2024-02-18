package bo.com.tesla.recaudaciones.services;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import bo.com.tesla.entidades.dto.DeudasClienteDto;
import bo.com.tesla.recaudaciones.dto.DeudasClienteRecaudacionDto;

public interface IReporteRecaudacionService {



	public Page<DeudasClienteRecaudacionDto> findDeudasByParameter(
			Date fechaInicio, 
			Date fechaFin, 
			List<String> entidadId,
			Long recaudadorId, 
			List<String> estado,
			int page,
			int size
			);
	
	public List<DeudasClienteRecaudacionDto>  findDeudasByParameterForReport(
			 Date fechaInicio, 
			 Date fechaFin,
			 List<String> entidadId,
			 Long recaudadorId,
			 List<String> estado		
			);

}
