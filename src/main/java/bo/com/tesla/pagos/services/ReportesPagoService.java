package bo.com.tesla.pagos.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bo.com.tesla.pagos.dao.IPHistoricoBeneficiariosDao;
import bo.com.tesla.pagos.dto.PPagosDto;
import bo.com.tesla.pagos.dto.PBeneficiarioReporteDto;

@Service
public class ReportesPagoService implements IReportesPagoService {
	@Autowired
	private IPHistoricoBeneficiariosDao historicoBeneficiariosDao;

	@Override
	public List<PBeneficiarioReporteDto> listForReportEntidad(Date fechaIni, Date fechaFin,
			List<String> estadoList, List<Long> recaudadorIdList, Long entidadId,Long servicioProductoId) {
		
		return this.historicoBeneficiariosDao.listForReportEntidad(
				fechaIni, 
				fechaFin, 
				estadoList, 
				recaudadorIdList, 
				entidadId,
				servicioProductoId);
	}

	@Override
	public Page<PBeneficiarioReporteDto> listForGridEntidad(Date fechaIni, Date fechaFin, List<String> estadoList,
			List<Long> recaudadorIdList, Long entidadId, Long servicioProductoId, int page, int size) {
		
		Pageable paging = PageRequest.of(page, size);
		
		return this.historicoBeneficiariosDao.listForGridEntidad(
					fechaIni, 
					fechaFin, 
					estadoList, 
					recaudadorIdList, 
					entidadId,
					servicioProductoId,paging);
	
	}

	@Override
	public Page<PBeneficiarioReporteDto> listForGridRecaudacion(Date fechaIni, Date fechaFin, List<String> estadoList,
			Long recaudadorId, Long servicioProductoId, int page,int size) {
		Pageable paging = PageRequest.of(page, size);
		return this.historicoBeneficiariosDao.listForGridRecaudacion(fechaIni, fechaFin, estadoList, recaudadorId, servicioProductoId, paging);
		
	}

	@Override
	public List<PBeneficiarioReporteDto> listForReportRecaudacion(Date fechaIni, Date fechaFin, List<String> estadoList,
			Long recaudadorId, Long servicioProductoId) {	
		return this.historicoBeneficiariosDao.listForReportRecaudacion(fechaIni, fechaFin, estadoList, recaudadorId, servicioProductoId);
	}

	@Override
	public List<PBeneficiarioReporteDto> listForReportAdministracion(Date fechaIni, Date fechaFin,
			List<String> estadoList, List<Long> recaudadorIdList, Long servicioProductoId) {
		
		return this.historicoBeneficiariosDao.listForReportAdministracion(fechaIni, fechaFin, estadoList, recaudadorIdList,  servicioProductoId);
	}

	@Override
	public Page<PBeneficiarioReporteDto> listForGridAdministracion(Date fechaIni, Date fechaFin,
			List<String> estadoList, List<Long> recaudadorIdList, Long servicioProductoId, int page,int size) {
		Pageable paging = PageRequest.of(page, size);
		return this.historicoBeneficiariosDao.listForGridAdministracion(fechaIni, fechaFin, estadoList, recaudadorIdList, servicioProductoId, paging);
	}
	
	
	

}
