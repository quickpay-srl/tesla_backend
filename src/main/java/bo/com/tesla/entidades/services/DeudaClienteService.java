package bo.com.tesla.entidades.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.com.tesla.administracion.entity.ArchivoEntity;
import bo.com.tesla.administracion.entity.DeudaClienteEntity;
import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.entidades.dao.IArchivoDao;
import bo.com.tesla.entidades.dao.IDeudaClienteDao;
import bo.com.tesla.entidades.dao.IEntidadDao;
import bo.com.tesla.entidades.dto.ConceptoDto;
import bo.com.tesla.entidades.dto.DeudasClienteDto;
import bo.com.tesla.entidades.dto.DeudasClienteRestDto;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.cross.HandlingFiles;

@Service

public class DeudaClienteService implements IDeudaClienteService {

	@Autowired
	private IDeudaClienteDao deudaClienteDao;

	@Autowired
	private IArchivoDao archivoDao;

	@Autowired
	private IEntidadDao entidadDao;

	@Value("${tesla.path.files-debts}")
	private String filesBets;

	@Override
	public void deletByArchivoId(Long archivoId) {
		this.deudaClienteDao.deletByArchivoId(archivoId);

	}
	
	@Override
	public void updateHitoricoDeudas(Long archivoId) {
		this.deudaClienteDao.updateHitoricoDeudas(archivoId);

	}
	
	@Override
	public void updateDeudasCargadasEndPoint(Long archivoId,Long archivoPreviusId) {
		this.deudaClienteDao.updateDeudasCargadasEndPoint(archivoId,archivoPreviusId);

	}
	
	

	@Transactional(readOnly = true)
	@Override
	public Page<DeudasClienteDto> findDeudasClientesByArchivoId(Long archivoId, String paramBusqueda, int page,
			int size) {
		Page<DeudasClienteDto> deudaClienteList;
		Pageable paging = PageRequest.of(page, size);
		List<DeudaClienteEntity> ls = this.deudaClienteDao.findByArchivoId(archivoId);
		if(ls.isEmpty()){
			return  new PageImpl<>(new ArrayList<>());

		}
		deudaClienteList = this.deudaClienteDao.groupByDeudasClientes(archivoId,paramBusqueda, paging);
		Integer key = 0;

		for (DeudasClienteDto deudasClienteDto : deudaClienteList) {

			List<ConceptoDto> conceptosList = this.deudaClienteDao.findConceptos(deudasClienteDto.archivoId,
					deudasClienteDto.servicio, deudasClienteDto.tipoServicio, deudasClienteDto.periodo,
					deudasClienteDto.codigoCliente);

			if (!conceptosList.isEmpty()) {
				key++;
				deudasClienteDto.key = key + "";
				deudasClienteDto.nombreCliente = conceptosList.get(0).nombreCliente;
				deudasClienteDto.direccion = conceptosList.get(0).direccion;
				deudasClienteDto.nit = conceptosList.get(0).nit;
				deudasClienteDto.nroDocumento = conceptosList.get(0).nroDocumento;
				deudasClienteDto.telefono = conceptosList.get(0).telefono;
				deudasClienteDto.esPostpago = conceptosList.get(0).esPostpago;
				deudasClienteDto.correoCliente = conceptosList.get(0).correoCliente;

			}

			deudasClienteDto.conceptoLisit = conceptosList;
		}

		return deudaClienteList;
	}

	@Transactional
	@Override
	public String saveCustomerDebt(DeudasClienteRestDto deuda, SegUsuarioEntity usuario) throws BusinesException {

		DeudaClienteEntity deudaCliente = new DeudaClienteEntity();

		EntidadEntity entidad = this.entidadDao.findEntidadByUserId(usuario.getUsuarioId());
		ArchivoEntity archivo = this.archivoDao.findByEstadoAndEntidad("ACTIVO", entidad.getEntidadId());

		if (archivo == null) {
			String path = HandlingFiles.createFile(filesBets, entidad.getNombre());
			archivo = new ArchivoEntity();
			archivo.setTransaccion("CREAR");
			archivo.setEntidadId(entidad);
			archivo.setFechaCreacion(new Date());
			archivo.setUsuarioCreacion(usuario.getUsuarioId());
			archivo.setNombre(entidad.getNombre());
			archivo.setPath(path);
			archivo = this.archivoDao.save(archivo);

			archivo.setFechaModificacion(new Date());
			archivo.setUsuarioModificacion(usuario.getUsuarioId());
			archivo.setTransaccion("PROCESAR");
			archivo = this.archivoDao.save(archivo);
		}

		for (ConceptoDto concepto : deuda.conceptoList) {
			deudaCliente = new DeudaClienteEntity();
			deudaCliente.setEsPorServicioWeb(true);
			deudaCliente.setNroRegistro(0);
			deudaCliente.setArchivoId(archivo);
			deudaCliente.setCodigoCliente(deuda.codigoCliente);
			deudaCliente.setNombreCliente(deuda.nombreCliente);
			deudaCliente.setNroDocumento(deuda.nroDocumento);
			deudaCliente.setDireccion(deuda.direccion);
			deudaCliente.setTelefono(deuda.telefono);
			deudaCliente.setCorreoCliente(deuda.correoCliente);
			deudaCliente.setPeriodoCabecera(deuda.periodoCabecera);
			if(deuda.esPostpago==null) {
				deudaCliente.setEsPostpago(true);	
			}else {
				deudaCliente.setEsPostpago(deuda.esPostpago);
			}
			
			deudaCliente.setNit(deuda.nit);
			deudaCliente.setPeriodo(deuda.periodo);
			deudaCliente.setTipoServicio(deuda.tipoServicio);
			
			if (concepto.tipo == null) {
				deudaCliente.setTipo('D');
			} else {
				deudaCliente.setTipo(deuda.tipo.toString().toUpperCase().charAt(0));
			}
			if (concepto.tipoComprobante == null) {
				deudaCliente.setTipoComprobante(true);
			} else {
				deudaCliente.setTipoComprobante(concepto.tipoComprobante);
			}
			deudaCliente.setCodigoActividadEconomica(deuda.codigoActividadEconomica);

			if (deuda.servicio == null) {
				deudaCliente.setServicio(deuda.tipoServicio);
			} else {
				deudaCliente.setServicio(deuda.servicio);
			}

			if (concepto.concepto == null) {
				throw new BusinesException("El Campo 'concepto' no puede ser nulo");
			}else {
				deudaCliente.setConcepto(concepto.concepto);
			}
			
			if (concepto.subTotal == null) {
				throw new BusinesException("El Campo 'Sub-Total' no puede ser nulo");
			}else {
				deudaCliente.setSubTotal(concepto.subTotal);
			}

			if (concepto.cantidad == null) {
				throw new BusinesException("El Campo 'Cantidad' no puede ser nulo");
			}else {
				deudaCliente.setCantidad(concepto.cantidad);
			}
			
			if (concepto.montoUnitario == null) {
				throw new BusinesException("El Campo 'Monto Unitario' no puede ser nulo");
			}else {
				deudaCliente.setMontoUnitario(concepto.montoUnitario);
			}
			
			
			this.deudaClienteDao.save(deudaCliente);

		}

		return deudaCliente.getCodigoCliente();

	}

}
