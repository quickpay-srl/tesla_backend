package bo.com.tesla.administracion.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import bo.com.tesla.administracion.dto.DeudasClienteAdmv2Dto;
import bo.com.tesla.administracion.dto.ReporteAdminEmpresaDto;
import bo.com.tesla.administracion.dto.ReporteAdminSocioDto;
import bo.com.tesla.recaudaciones.dto.ReporteCobrosSocioDto;
import bo.com.tesla.useful.cross.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bo.com.tesla.recaudaciones.dao.ITransaccionCobroDao;
import bo.com.tesla.administracion.dto.DeudasClienteAdmDto;

@Service
public class  ReporteAdminService implements IReporteAdminService {
	
	@Autowired
	private ITransaccionCobroDao transaccionCobrosDao;

	
	

	@Override
	public Page<DeudasClienteAdmDto> findDeudasByParameter(
			Date fechaInicio, 
			Date fechaFin, 
			String entidadId,
			String recaudadorId, 
			List<String> estado,
			int page,
			int size
			) {
		
		Pageable paging = PageRequest.of(page, size);
		return this.transaccionCobrosDao.findDeudasByParameterForAdmin(fechaInicio,	fechaFin,entidadId,	recaudadorId,estado,paging);

	}
	@Override
	public Page<DeudasClienteAdmv2Dto> findDeudasByParameterv2(
			Date fechaInicio,
			Date fechaFin,
			List<Long> lstEntidad,
			List<Long> lstRecaudador,
			List<String> estado,
			int page,
			int size,
			boolean tieneEntidad,
			boolean tieneRecaudador,
			boolean tieneEstado,
			Boolean filtroRangoFechas,
			Integer mes,
			Integer dia

	) {

			Pageable paging = PageRequest.of(page, size);
		Page<DeudasClienteAdmv2Dto> lst = this.transaccionCobrosDao.findDeudasByParameterForAdminv2(fechaInicio, fechaFin,lstEntidad,	lstRecaudador,estado,tieneEntidad,tieneRecaudador,tieneEstado,filtroRangoFechas,mes,dia,paging);

			return lst;

	}






	@Override
	public Page<ReporteCobrosSocioDto> findReporteCobroSocio(
			Date fechaInicio,
			Date fechaFin,
			List<Long> entidadId,
			List<Long> recaudadorId,
			List<String> estado,
			int page,
			int size,
			boolean tieneEntidad,
			boolean tieneRecaudador,
			boolean tieneEstado,
			Boolean filtroRangoFechas,
			Integer anio,
			Integer mes,
			Integer dia

	) {

		Pageable paging = PageRequest.of(page, size);
		Page<ReporteCobrosSocioDto> lst = this.transaccionCobrosDao.findReporteCobroSocio(
				fechaInicio,
				fechaFin,
				entidadId,
				recaudadorId,
				estado,
				tieneEntidad,
				tieneRecaudador,
				tieneEstado,
				filtroRangoFechas,
				anio,
				mes,
				dia,
				paging);
		return lst;
	}

	@Override
	public Page<ReporteAdminEmpresaDto> findReporteAdminCobroEmpresa(Date fechaInicio, Date fechaFin, List<Long> entidadId, List<Long> recaudadorId, List<String> estado, int page, int size, boolean tieneEntidad, boolean tieneRecaudador, boolean tieneEstado, Boolean filtroRangoFechas, Integer anio, Integer mes, Integer dia) {
		Pageable paging = PageRequest.of(page, size);
		Page<ReporteAdminEmpresaDto> lst = this.transaccionCobrosDao.findReporteAdminCobroEmpresa(
				fechaInicio,
				fechaFin,
				entidadId,
				recaudadorId,
				estado,
				tieneEntidad,
				tieneRecaudador,
				tieneEstado,
				filtroRangoFechas,
				anio,
				mes,
				dia,
				paging);
		return lst;
	}

	@Override
	public List<ReporteAdminEmpresaDto> findReporteAdminCobroEmpresaJasper(Date fechaInicio, Date fechaFin, List<Long> entidadId, List<Long> recaudadorId,
																	  List<String> estado, boolean tieneEntidad, boolean tieneRecaudador,
																	  boolean tieneEstado,
																	  Boolean filtroRangoFechas, Integer anio, Integer mes, Integer dia) {
		List<ReporteAdminEmpresaDto> lst = this.transaccionCobrosDao.findReporteAdminCobroEmpresaJasper(
				fechaInicio,
				fechaFin,
				entidadId,
				recaudadorId,
				estado,
				tieneEntidad,
				tieneRecaudador,
				tieneEstado,
				filtroRangoFechas,
				anio,
				mes,
				dia
				);
		return lst;
	}

	@Override
	public Page<ReporteAdminSocioDto> findReporteAdminCobroSocio(Date fechaInicio, Date fechaFin, List<Long> entidadId, List<Long> recaudadorId, List<String> estado, int page, int size, boolean tieneEntidad, boolean tieneRecaudador, boolean tieneEstado, Boolean filtroRangoFechas, Integer anio, Integer mes, Integer dia) {
		Pageable paging = PageRequest.of(page, size);
		Page<ReporteAdminSocioDto> lst = this.transaccionCobrosDao.findReporteAdminCobroSocio(
				fechaInicio,
				fechaFin,
				entidadId,
				recaudadorId,
				estado,
				tieneEntidad,
				tieneRecaudador,
				tieneEstado,
				filtroRangoFechas,
				anio,
				mes,
				dia,
				paging);
		return lst;
	}

	@Override
	public List<ReporteAdminSocioDto> findReporteAdminCobroSocioJasper(Date fechaInicio, Date fechaFin, List<Long> entidadId, List<Long> recaudadorId, List<String> estado, boolean tieneEntidad, boolean tieneRecaudador, boolean tieneEstado, Boolean filtroRangoFechas, Integer anio, Integer mes, Integer dia) {
		List<ReporteAdminSocioDto> lst = this.transaccionCobrosDao.findReporteAdminCobroSocioJasper(
				fechaInicio,
				fechaFin,
				entidadId,
				recaudadorId,
				estado,
				tieneEntidad,
				tieneRecaudador,
				tieneEstado,
				filtroRangoFechas,
				anio,
				mes,
				dia);
		return lst;
	}

}
