package bo.com.tesla.entidades.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.com.tesla.administracion.entity.ArchivoEntity;
import bo.com.tesla.entidades.dao.IArchivoDao;
import bo.com.tesla.entidades.dao.IDeudaClienteDao;
import bo.com.tesla.entidades.dao.IEntidadDao;
import bo.com.tesla.entidades.dto.ArchivoDto;
import bo.com.tesla.entidades.dto.DeudasClienteDto;
import bo.com.tesla.security.dao.ISegUsuarioDao;

@Service
public class ArchivoService implements IArchivoService{
	
	
	@Autowired
	private IArchivoDao archivoDao;
	
	
	@Transactional
	@Override
	public ArchivoEntity save(ArchivoEntity entity) {
		return this.archivoDao.save(entity);		
	}

	@Transactional(readOnly = true)
	@Override
	public ArchivoEntity findById(Long archivoId) {	
		return this.archivoDao.findById(archivoId).get();
	}
	
	@Transactional(readOnly = true)
	@Override
	public ArchivoEntity findByEstado(String estado, Long archivoId) {
		return this.archivoDao.findByEstado(estado, archivoId);
		
	}

	@Override
	public Page<ArchivoDto> findByEntidadIdAndFechaIniAndFechaFin(Long entidadId, Date fechaIni, Date fechaFin,String estado,
			int page,int size) {
		Page<DeudasClienteDto> deudaClienteList ;
		Pageable paging = PageRequest.of(page, size);
		return this.archivoDao.findByEntidadIdAndFechaIniAndFechaFin(entidadId, fechaIni, fechaFin,estado, paging);
	}

	@Override
	public ArchivoEntity findByEstadoAndEntidadAndServicio(String estado, Long entidadId, Long servicioProductoId) {		
		return this.archivoDao.findByEstadoAndEntidadAndServicio(estado, entidadId, servicioProductoId);
	}

	@Override
	public ArchivoEntity findByEstadoAndEntidadId(String estado, Long entidadId) {
	
		return this.archivoDao.findByEstadoAndEntidad(estado, entidadId);
	}

	
	
	

	
	

}
