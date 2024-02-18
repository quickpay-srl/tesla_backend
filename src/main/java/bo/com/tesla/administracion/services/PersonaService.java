package bo.com.tesla.administracion.services;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import bo.com.tesla.administracion.dao.ISucursalEntidadDao;
import bo.com.tesla.administracion.dto.SucursalEntidadAdmDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.com.tesla.administracion.dao.IEmpleadoDao;
import bo.com.tesla.administracion.dao.IPersonaDao;
import bo.com.tesla.administracion.dto.PersonaDto;
import bo.com.tesla.administracion.entity.DominioEntity;
import bo.com.tesla.administracion.entity.EmpleadoEntity;
import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.PersonaEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.administracion.entity.SucursalEntity;
import bo.com.tesla.entidades.services.IEntidadService;
import bo.com.tesla.recaudaciones.services.IRecaudadoraService;
import bo.com.tesla.security.services.ISegUsuarioService;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.constant.PlantillaEmail;
import bo.com.tesla.useful.cross.SendEmail;
import bo.com.tesla.useful.cross.Util;

import java.util.Calendar;

@Service
public class PersonaService implements IPersonaService {

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

	@Override
	public Page<PersonaDto> findPersonasByRecaudadorGrid(String parametro,Long sucursalId, Long recaudadorId, int page, int size) {
		Pageable paging = PageRequest.of(page, size);
		String sucursal="%";
		if(sucursalId.equals(0L) || sucursalId==null ) {
			sucursal="%";
		}else {
			sucursal=sucursalId+"";
		}
		return this.personaDao.findPersonasByRecaudadorGrid(parametro,sucursal, recaudadorId, paging);
	}

	@Override
	public Page<PersonaDto> findPersonasByEntidadesGrid(String parametro, Long entidadId, int page, int size) {
		Pageable paging = PageRequest.of(page, size);
		return this.personaDao.findPersonasByEntidadesGrid(parametro, entidadId, paging);
	}



	@Override
	public Page<PersonaDto> findPersonasByAdminGrid(String parametro, int page, int size) {
		Page<PersonaDto> personaList = null;
		try {
			Pageable paging = PageRequest.of(page, size);

			personaList = this.personaDao.findPersonasByAdminGrid(parametro, paging);

			for (PersonaDto personaDto : personaList) {
				Optional<EmpleadoEntity> empleado = this.empleadoDao.findEmpleadosById(personaDto.empleadoId);
				if (empleado.isPresent()) {

					if (empleado.get().getEntidadId() != null) {
						EntidadEntity entidad = this.entidadService
								.findEntidadById(empleado.get().getEntidadId().getEntidadId()).get();
						personaDto.nombreEntidad = entidad.getNombre();
						personaDto.entidadId = entidad.getEntidadId();
					} else if (empleado.get().getSucursalId() != null) {
						SucursalEntity sucursal = this.sucursalService.findById(empleado.get().getSucursalId().getSucursalId()).get();
						personaDto.nombreRecaudadora = sucursal.getRecaudador().getNombre();
						personaDto.recaudadorId = sucursal.getRecaudador().getRecaudadorId();
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return personaList;
	}

	@Override
	public Optional<PersonaEntity> findById(Long personaId) {

		return this.personaDao.findById(personaId);
	}

	@Transactional
	@Override
	public PersonaEntity save(PersonaDto personaDto, SegUsuarioEntity usuario) throws BusinesException {

		EntidadEntity entidad = new EntidadEntity();
		SucursalEntity sucursal = new SucursalEntity();
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

			EmpleadoEntity empleado = new EmpleadoEntity();

			switch (personaDto.subModulo) {
			case "ADMIN":
				persona.setAdmin(true);
				if (personaDto.entidadId != null) {
					entidad = this.entidadService.findEntidadById(personaDto.entidadId).get();
					empleado.setEntidadId(entidad);

				} else {
					recaudadora = this.recaudadoraService.findByRecaudadorId(personaDto.recaudadorId);
					List<SucursalEntity> sucursalList = this.sucursalService
							.findByRecaudadoraId(recaudadora.getRecaudadorId());
					if (sucursalList.isEmpty()) {
						throw new BusinesException(
								"No se ha podido encontrar ninguna sucursal registrada para la recaudadora seleccionada.");
					}
					empleado.setSucursalId(sucursalList.get(0));
				}

				break;
			case "ADM_ENTIDADES":
				persona.setAdmin(false);
				entidad = this.entidadService.findEntidadByUserId(usuario.getUsuarioId());
				empleado.setEntidadId(entidad);

				break;
			case "ADM_RECAUDACION":
				persona.setAdmin(false);
				sucursal = this.sucursalService.findById(personaDto.sucursalId).get();
				empleado.setSucursalId(sucursal);

				break;
			}

			persona = this.personaDao.save(persona);
			empleado.setFechaCreacion(new Date());
			empleado.setUsuarioCreacion(usuario.getUsuarioId());
			empleado.setFechaCreacion(new Date());
			empleado.setUsuarioModificacion(usuario.getUsuarioId());
			empleado.setEstado("ACTIVO");
			empleado.setPersonaId(persona);
			this.empleadoDao.save(empleado);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Technicalexception(e.getMessage(), e.getCause());
		}

		return persona;
	}

	@Transactional
	@Override
	public PersonaEntity update(PersonaDto personaDto, SegUsuarioEntity usuario) {

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
			if (personaDto.sucursalId != null) {
				sucursal = this.sucursalService.findById(personaDto.sucursalId).get();
				empleado.setSucursalId(sucursal);
				empleado.setEntidadId(null);
				this.empleadoDao.save(empleado);
			} else if (personaDto.entidadId != null) {
				entidad = this.entidadService.findEntidadById(personaDto.entidadId).get();
				empleado.setEntidadId(entidad);
				empleado.setSucursalId(null);
			} else if (personaDto.recaudadorId != null) {
				RecaudadorEntity recaudador = this.recaudadoraService.findByRecaudadorId(personaDto.recaudadorId);
				empleado.setSucursalId(recaudador.getSucursalEntityList().get(0));
				empleado.setEntidadId(null);
			}

			this.empleadoDao.save(empleado);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Technicalexception(e.getMessage(), e.getCause());
		}

		return persona;
	}

	@Transactional
	@Override
	public PersonaEntity cambiarEstado(PersonaDto personaDto, SegUsuarioEntity usuario) {
		PersonaEntity persona = new PersonaEntity();
		try {
			persona = this.personaDao.findById(personaDto.personaId).get();
			persona.setUsuarioModificacion(usuario.getUsuarioId());
			persona.setFechaModificacion(new Date());
			persona.setTransaccion(personaDto.transaccion);
			persona = this.personaDao.save(persona);

		} catch (Exception e) {
			throw new Technicalexception(e.getMessage(), e.getCause());
		}

		return persona;
	}

	@Transactional
	@Override
	public SegUsuarioEntity generarCredenciales(Long personaId, SegUsuarioEntity usuarioSession) {

		try {
			PersonaEntity persona = new PersonaEntity();
			SegUsuarioEntity usuario = new SegUsuarioEntity();
			

			persona = this.personaDao.findById(personaId).get();
		
			String nombreCompleto = "";
			Random r = new Random();
			int valorDado = r.nextInt(999) + 1;
			
			String nombreUsuario = Util.cleanString(persona.getNombres().substring(0, 1)).toLowerCase()  
					+ Util.cleanString( persona.getPaterno().substring(0,1)).toUpperCase()
					+ Util.cleanString( persona.getPaterno().replace(" ","").substring(1,persona.getPaterno().length()))  
					+ ""+ valorDado;
			
			
			
			if (persona.getMaterno() != null) {
				nombreCompleto = persona.getPaterno() + " " + persona.getMaterno() + " " + persona.getNombres();
			} else {
				nombreCompleto = persona.getPaterno() + " " + persona.getNombres();
			}
			EmpleadoEntity empleado = this.empleadoDao.findEmpleadosByPersonaId(personaId).get();
			String mensaje = "";
			if (this.usuarioService.findByPersonaIdAndEstado(personaId).isPresent()) {
				usuario = this.usuarioService.findByPersonaIdAndEstado(personaId).get();
				usuario.setFechaModificacion(new Date());
				usuario.setUsuarioModificacion(usuarioSession.getUsuarioId());
				usuario.setPassword(this.passwordEncoder.encode(usuario.getLogin()));
				usuario.setEstado("ACTIVO");
				usuario.setBloqueado(false);
				usuario.setIntentos(0);
				if (empleado.getEntidadId() != null) {
					mensaje = "Sus credenciales para la administración de la Empresa " + empleado.getEntidadId().getNombre()
							+ " fueron recuperadas :";
				} else {
					mensaje = "Sus credenciales para la administración de la Empresa Recaudadora  "
							+ empleado.getSucursalId().getRecaudador().getNombre() + " fueron recuperadas :";
				}
				String plantillaCorreo = PlantillaEmail.plantillaCreacionUsuario(nombreCompleto, usuario.getLogin(),
						usuario.getLogin(), mensaje,urlTesla);
				this.sendEmail.sendHTML(correoEnvio, persona.getCorreoElectronico(), "Recuperacion de credenciales QUICKPAY.",
						plantillaCorreo);

			} else {
				usuario.setFechaCreacion(new Date());
				usuario.setUsuarioCreacion(usuarioSession.getUsuarioId());
				usuario.setLogin(nombreUsuario);
				usuario.setPassword(this.passwordEncoder.encode(nombreUsuario));
				usuario.setPersonaId(persona);
				usuario.setEstado("ACTIVO");
				usuario.setBloqueado(false);
				usuario.setIntentos(0);				
				if (empleado.getEntidadId() != null) {
					mensaje = "Sus credenciales para la administración de la Empresa " + empleado.getEntidadId().getNombre()
							+ " son:";
				} else {
					mensaje = "Sus credenciales para la administración de la Empresa Recaudadora  "
							+ empleado.getSucursalId().getRecaudador().getNombre() + " son:";
				}
				String plantillaCorreo = PlantillaEmail.plantillaCreacionUsuario(nombreCompleto, nombreUsuario,
						nombreUsuario, mensaje,urlTesla);
				this.sendEmail.sendHTML(correoEnvio, persona.getCorreoElectronico(), "Generación de credenciales QUICKPAY.",
						plantillaCorreo);
				
			}
			usuario = this.usuarioService.save(usuario);
			return usuario;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Technicalexception(e.getMessage(), e.getCause());
		}

	}

}
