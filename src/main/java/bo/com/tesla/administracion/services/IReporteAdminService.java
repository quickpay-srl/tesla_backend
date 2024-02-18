package bo.com.tesla.administracion.services;

import java.util.Date;
import java.util.List;

import bo.com.tesla.administracion.dto.DeudasClienteAdmv2Dto;
import bo.com.tesla.administracion.dto.ReporteAdminEmpresaDto;
import bo.com.tesla.administracion.dto.ReporteAdminSocioDto;
import bo.com.tesla.recaudaciones.dto.ReporteCobrosSocioDto;
import org.springframework.data.domain.Page;

import bo.com.tesla.administracion.dto.DeudasClienteAdmDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface IReporteAdminService {



	public Page<DeudasClienteAdmDto> findDeudasByParameter(
			Date fechaInicio, 
			Date fechaFin, 
			String entidadId,
			String recaudadorId, 
			List<String> estado,
			int page,
			int size
			);

	public Page<DeudasClienteAdmv2Dto> findDeudasByParameterv2(
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

			Boolean vFiltroRangoFechas,
			Integer mes,
			Integer dia
	);


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
	);

	// reporytes admin cobro empresa grilla y socio
	public Page<ReporteAdminEmpresaDto> findReporteAdminCobroEmpresa(
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
	);
	public List<ReporteAdminEmpresaDto> findReporteAdminCobroEmpresaJasper(
			Date fechaInicio,
			Date fechaFin,
			List<Long> entidadId,
			List<Long> recaudadorId,
			List<String> estado,
			boolean tieneEntidad,
			boolean tieneRecaudador,
			boolean tieneEstado,
			Boolean filtroRangoFechas,
			Integer anio,
			Integer mes,
			Integer dia
	);
	// =====================

	// reporytes admin cobro socio grilla y socio
	public Page<ReporteAdminSocioDto> findReporteAdminCobroSocio(
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
	);
	public List<ReporteAdminSocioDto> findReporteAdminCobroSocioJasper(
			Date fechaInicio,
			Date fechaFin,
			List<Long> entidadId,
			List<Long> recaudadorId,
			List<String> estado,
			boolean tieneEntidad,
			boolean tieneRecaudador,
			boolean tieneEstado,
			Boolean filtroRangoFechas,
			Integer anio,
			Integer mes,
			Integer dia
	);
	// =====================

}
