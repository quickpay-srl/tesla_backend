package bo.com.tesla.pagos.services;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import bo.com.tesla.pagos.dto.PBeneficiarioReporteDto;

public interface IReportesPagoService {
	
	
	public List<PBeneficiarioReporteDto> listForReportEntidad(
			 Date fechaIni, 
			 Date fechaFin,
			 List<String> estadoList,
			 List<Long> recaudadorIdList,
			 Long entidadId	,
			 Long servicioProductoId
			);
	
	public Page<PBeneficiarioReporteDto> listForGridEntidad(
			 Date fechaIni, 
			 Date fechaFin,
			 List<String> estadoList,
			 List<Long> recaudadorIdList,
			 Long entidadId	,
			 Long servicioProductoId,
			 int page, int size
			);
	
	public Page<PBeneficiarioReporteDto> listForGridRecaudacion(
			Date fechaIni, 
			Date fechaFin, 
			List<String> estadoList,
			Long recaudadorId, 
			Long servicioProductoId,
			int page,int size);
	
	public List<PBeneficiarioReporteDto> listForReportRecaudacion(
			 Date fechaIni, 
			 Date fechaFin,
			 List<String> estadoList,
			 Long recaudadorId,		
			 Long servicioProductoId			
			);
	
	public List<PBeneficiarioReporteDto> listForReportAdministracion(
			 Date fechaIni, 
			 Date fechaFin,
			 List<String> estadoList,
			 List<Long> recaudadorIdList,			
			 Long servicioProductoId		
			);
	public Page<PBeneficiarioReporteDto> listForGridAdministracion(
			 Date fechaIni, 
			 Date fechaFin,
			 List<String> estadoList,
			 List<Long> recaudadorIdList,			
			 Long servicioProductoId,
			  int page,int size
			);
	

}
