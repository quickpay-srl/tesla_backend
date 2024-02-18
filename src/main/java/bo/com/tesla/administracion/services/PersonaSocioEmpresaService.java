package bo.com.tesla.administracion.services;

import bo.com.tesla.administracion.dao.IEmpleadoDao;
import bo.com.tesla.administracion.dao.IPersonaDao;
import bo.com.tesla.administracion.dao.ISucursalEntidadDao;
import bo.com.tesla.administracion.dto.PersonaDto;
import bo.com.tesla.administracion.dto.SucursalAdmDto;
import bo.com.tesla.administracion.dto.SucursalEntidadAdmDto;
import bo.com.tesla.administracion.entity.*;
import bo.com.tesla.entidades.services.IEntidadService;
import bo.com.tesla.recaudaciones.services.IRecaudadoraService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.constant.PlantillaEmail;
import bo.com.tesla.useful.cross.SendEmail;
import bo.com.tesla.useful.cross.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class PersonaSocioEmpresaService implements IPersonaSocioEmpresaService {

	@Autowired
	private IPersonaDao personaDao;

	@Autowired
	private IDominioService dominioService;

	@Autowired
	private IEntidadService entidadService;

	@Autowired
	private ISucursalService sucursalService;

	@Autowired
	private ISucursalEntidadService sucursalEntidadService;

	@Autowired
	private ISucursalEntidadDao iSucursalEntidadDao;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private ISegUsuarioService usuarioService;

	@Autowired
	private IRecaudadoraService recaudadoraService;

	@Autowired
	private IEmpleadoDao empleadoDao;



	@Autowired
	private SendEmail sendEmail;

	@Value("${tesla.mail.correoEnvio}")
	private String correoEnvio;
	
	@Value("${tesla.url.tesla}")
	private String urlTesla;


	@Transactional
	@Override
	public PersonaEntity guardarSocioEmpresa(PersonaDto personaDto,Long tipoUsuarioId, SegUsuarioEntity usuario) throws BusinesException {

		EntidadEntity entidad = new EntidadEntity();
		SucursalEntity sucursal = new SucursalEntity();
		SucursalEntidadEntity sucursalEntidadEntity = new SucursalEntidadEntity();

		PersonaEntity persona = new PersonaEntity();
		RecaudadorEntity recaudadora = new RecaudadorEntity();

		try {

			if (personaDto.extensionDocumentoId != null) {
				DominioEntity extensionDocumento = this.dominioService.findById(personaDto.extensionDocumentoId);
				persona.setExtensionDocumentoId(extensionDocumento);
			}

			DominioEntity ciudad = this.dominioService.findById(2L);
			DominioEntity tipoDocumento = this.dominioService.findById(1L);

			persona.setNombres(personaDto.nombres);
			persona.setPaterno(personaDto.paterno);
			persona.setMaterno(personaDto.materno);
			persona.setCorreoElectronico(personaDto.correoElectronico);
			persona.setDireccion(personaDto.direccion);
			persona.setTelefono(personaDto.telefono);
			persona.setNroDocumento(personaDto.nroDocumento);
			persona.setUsuarioCreacion(usuario.getUsuarioId());
			persona.setFechaCreacion(new Date());
			persona.setUsuarioModificacion(usuario.getUsuarioId());
			persona.setFechaModificacion(new Date());
			persona.setCiudadId(ciudad);
			persona.setTipoDocumentoId(tipoDocumento);
			persona.setTransaccion(personaDto.transaccion);
			persona.setAdmin(true);
			persona = this.personaDao.save(persona);

			EmpleadoEntity empleado = new EmpleadoEntity();
			if (personaDto.entidadId != null) {
				entidad = this.entidadService.findEntidadById(personaDto.entidadId).get();
				empleado.setEntidadId(entidad);
			}
			if (personaDto.recaudadorId != null) {
				recaudadora = this.recaudadoraService.findByRecaudadorId(personaDto.recaudadorId);
				empleado.setRecaudadorId(recaudadora);
			}
			if (personaDto.sucursalEntidadId != null) {
				 SucursalEntidadEntity suc = this.iSucursalEntidadDao.findById(personaDto.sucursalEntidadId).get();
				empleado.setSucursalEntidadId(suc);
			}
			if (personaDto.sucursalId != null) {
				SucursalEntity suc = this.sucursalService.findById(personaDto.sucursalId).get();
				empleado.setSucursalId(suc);
			}
			//empleado.setSucursalId(new SucursalEntity());
			empleado.setFechaCreacion(new Date());
			empleado.setUsuarioCreacion(usuario.getUsuarioId());
			empleado.setFechaCreacion(new Date());
			empleado.setUsuarioModificacion(usuario.getUsuarioId());
			empleado.setEstado("ACTIVO");
			empleado.setPersonaId(persona);
			DominioEntity dom = new DominioEntity();
			dom.setDominioId(tipoUsuarioId);
			empleado.setTipoUsuarioId(dom);
			this.empleadoDao.save(empleado);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Technicalexception(e.getMessage(), e.getCause());
		}

		return persona;
	}


	@Transactional
	@Override
	public PersonaEntity modificarSocioEmpresa(PersonaDto personaDto, Long tipoUsuarioId,SegUsuarioEntity usuario) {

		EntidadEntity entidad = new EntidadEntity();
		SucursalEntity sucursal = new SucursalEntity();
		PersonaEntity persona = new PersonaEntity();
		EmpleadoEntity empleado = new EmpleadoEntity();

		try {

			persona = this.personaDao.findById(personaDto.personaId).get();
			if (personaDto.extensionDocumentoId != null) {
				DominioEntity extensionDocumento = this.dominioService.findById(personaDto.extensionDocumentoId);
				persona.setExtensionDocumentoId(extensionDocumento);
			}

			persona.setNombres(personaDto.nombres);
			persona.setPaterno(personaDto.paterno);
			persona.setMaterno(personaDto.materno);
			persona.setCorreoElectronico(personaDto.correoElectronico);
			persona.setDireccion(personaDto.direccion);
			persona.setTelefono(personaDto.telefono);
			persona.setNroDocumento(personaDto.nroDocumento);
			persona.setUsuarioModificacion(usuario.getUsuarioId());
			persona.setFechaModificacion(new Date());
			persona.setTransaccion(personaDto.transaccion);
			persona = this.personaDao.save(persona);

			empleado = this.empleadoDao.findEmpleadosByPersonaId(persona.getPersonaId()).get();
			if(tipoUsuarioId==83) {// si es empresa?
				empleado.setSucursalId(null);
				SucursalEntidadEntity sucursalEntidadEntity = this.iSucursalEntidadDao.findById(personaDto.sucursalEntidadId).get();
				empleado.setSucursalEntidadId(sucursalEntidadEntity);
				empleado.setRecaudadorId(null);
				entidad = this.entidadService.findEntidadById(personaDto.entidadId).get();
				empleado.setEntidadId(entidad);
			}
			else if(tipoUsuarioId==82) {// si es socio?
				empleado.setSucursalEntidadId(null);
				sucursal = this.sucursalService.findById(personaDto.sucursalId).get();
				empleado.setSucursalId(sucursal);
				empleado.setEntidadId(null);
				RecaudadorEntity recaudador = this.recaudadoraService.findByRecaudadorId(personaDto.recaudadorId);
				empleado.setRecaudadorId(recaudador);
			}

			this.empleadoDao.save(empleado);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Technicalexception(e.getMessage(), e.getCause());
		}

		return persona;
	}
	@Override
	public Page<PersonaDto> findPersonasSocioEntidad(String parametro, Long pTipo, int page, int size) {
		Page<PersonaDto> personaList = null;
		try {
			Pageable paging = PageRequest.of(page, size);

			personaList = this.personaDao.findPersonasSocioEmpresaByTipo(parametro,pTipo, paging);

			for (PersonaDto personaDto : personaList) {
				Optional<EmpleadoEntity> empleado = this.empleadoDao.findEmpleadosById(personaDto.empleadoId);
				if (empleado.isPresent()) {

					if (empleado.get().getEntidadId() != null) {
						EntidadEntity entidad = this.entidadService.findEntidadById(empleado.get().getEntidadId().getEntidadId()).get();
						personaDto.nombreEntidad = entidad.getNombre();
						personaDto.entidadId = entidad.getEntidadId();
					}
					if (empleado.get().getSucursalEntidadId() != null) {
						SucursalEntidadAdmDto sucursal = this.sucursalEntidadService.getSucursalEntidadById(empleado.get().getSucursalEntidadId().getSucursalEntidadId());
						personaDto.nombreSucursalEntidad = sucursal.nombreSucursal;
						personaDto.sucursalEntidadId = sucursal.sucursalEntidadId;
					}
					if (empleado.get().getSucursalId() != null) {
						SucursalAdmDto sucursal = this.sucursalService.getSucursalById (empleado.get().getSucursalId().getSucursalId() );
						personaDto.nombreSucursal = sucursal.nombre;
						personaDto.sucursalId = sucursal.sucursalId;
					}
					if (empleado.get().getRecaudadorId() != null) {
						RecaudadorEntity recaudador = this.recaudadoraService.findByRecaudadorId(empleado.get().getRecaudadorId().getRecaudadorId());
						personaDto.nombreRecaudadora = recaudador.getNombre();
						personaDto.recaudadorId =  recaudador.getRecaudadorId();
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return personaList;
	}

}
