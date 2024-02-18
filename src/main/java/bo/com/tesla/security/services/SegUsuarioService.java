package bo.com.tesla.security.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.com.tesla.administracion.dao.IEmpleadoDao;
import bo.com.tesla.administracion.entity.EmpleadoEntity;
import bo.com.tesla.administracion.entity.PersonaEntity;
//import bo.com.tesla.administracion.dao.ISegRolDao;
//import bo.com.tesla.administracion.dao.ISegUsuarioDao;
import bo.com.tesla.administracion.entity.SegRolEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.administracion.services.IPersonaService;
import bo.com.tesla.security.dao.ISegRolDao;
import bo.com.tesla.security.dao.ISegUsuarioDao;
import bo.com.tesla.security.dto.CambiarPasswordDto;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.constant.PlantillaEmail;
import bo.com.tesla.useful.cross.SendEmail;

@Service
public class SegUsuarioService implements ISegUsuarioService,UserDetailsService {

	private Logger logger = LoggerFactory.getLogger(SegUsuarioService.class);
	@Autowired
	private ISegUsuarioDao segUsuarioDao;
	@Autowired
	private ISegRolDao segRolDao;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private IPersonaService personaService;
	
	@Autowired
	private IEmpleadoDao empleadoDao;
	
	@Autowired
	private SendEmail sendEmail;
	
	@Value("${tesla.url.tesla}")
	private String urlTesla;
	
	@Value("${tesla.mail.correoEnvio}")
	private String correoEnvio;
	
	@Override
	public SegUsuarioEntity save(SegUsuarioEntity entity) {		
		return this.segUsuarioDao.save(entity);
	}
	
	
	@Override
	public UserDetails loadUserByUsername(String login)throws UsernameNotFoundException  {
		
			List<GrantedAuthority> authorities = new ArrayList<>();
			SegUsuarioEntity usuarios = this.segUsuarioDao.findByLoginAuthentication(login);
			if (usuarios == null) {	
				logger.error("Error en el login: no existe el usuario " + login + " en el sistema");
				throw new UsernameNotFoundException("Error en el Login: no existe el usuario");
			}
			
			
			List<SegRolEntity> segRoles=segRolDao.findRolesByUsuarioLogin(login);
			
			if(!segRoles.isEmpty()) {			
				authorities=segRoles.stream().map(role -> new SimpleGrantedAuthority(role.getRol()))
						.peek(authority -> logger.info("Role :" + authority.getAuthority())).collect(Collectors.toList());
			}
			
			return new User(usuarios.getLogin(), usuarios.getPassword(), usuarios.getEstado().equals("ACTIVO") ? true : false,
					true, true, true, authorities);
		
		
		
		
	}

	@Override
	@Transactional(readOnly = true)
	public SegUsuarioEntity findByLogin(String login) {		
		return this.segUsuarioDao.findByLogin(login);
	}
	
	@Override
	@Transactional(readOnly = true)
	public SegUsuarioEntity findById(Long usuarioId) {		
		return this.segUsuarioDao.findById(usuarioId).get();
	}

	@Override
	public Optional<SegUsuarioEntity> findByPersonaIdAndEstado(Long personaId) {
		
		return this.segUsuarioDao.findByPersonaIdAndEstado(personaId);
	}
	

	@Override
	@Transactional
	public void  cambiarPassword(CambiarPasswordDto passwords, SegUsuarioEntity usuario) throws BusinesException {
		
		BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
		
		if(!bCrypt.matches(passwords.passwordActual, usuario.getPassword())) {
			throw new BusinesException(
					"La contraseña actual no coincide, por favor verifique e intente nuevamente. ");		
		}
		
		if(!passwords.passwordNew1.equals(passwords.passwordNew2)) {
			throw new BusinesException(
					"Las nuevas contraseñas nuevas no coinciden, por favor verifique e intente nuevamente.");
		}
		
		try {
			
			usuario.setPassword(bCrypt.encode(passwords.passwordNew1));
			usuario.setFechaModificacion(new Date());
			usuario.setUsuarioModificacion(usuario.getUsuarioId());				
			this.segUsuarioDao.save(usuario);	
			
			PersonaEntity	persona = usuario.getPersonaId();
			EmpleadoEntity empleado = this.empleadoDao.findEmpleadosByPersonaId(usuario.getPersonaId().getPersonaId()).get();
			String mensaje = "";
			String nombreCompleto = "";
			if (persona.getMaterno() != null) {
				nombreCompleto = persona.getPaterno() + " " + persona.getMaterno() + " " + persona.getNombres();
			} else {
				nombreCompleto = persona.getPaterno() + " " + persona.getNombres();
			}
			if (empleado.getEntidadId() != null) {
				/*mensaje = "Sus credenciales para la administración de la Empresa " + empleado.getEntidadId().getNombre()
						+ " son:";*/

				mensaje = "Sus credenciales de acceso a QuickPay son:";
			} else {
				/*mensaje = "Sus credenciales para la administración de la Empresa Recaudadora  "
						+ empleado.getSucursalId().getRecaudador().getNombre() + " son:";*/

				mensaje = "Sus credenciales de acceso a QuickPay son:";
			}
			String plantillaCorreo = PlantillaEmail.plantillaModificacionPassord(nombreCompleto, usuario.getLogin(),
				passwords.passwordNew1, mensaje,urlTesla);
			this.sendEmail.sendHTML(correoEnvio, persona.getCorreoElectronico(), "Cambio de password QUCKPAY.",
					plantillaCorreo);
			
			
		} catch (Exception e) {
			throw new Technicalexception("Ocurrió un problema en el servidor, por favor intente la operación más tarde o consulte con su administrador.", e.getCause());
		}
	}
	
	@Override
	@Transactional
	public void  toUnlock(String login, SegUsuarioEntity usuarioSession) {		
		
		try {
			SegUsuarioEntity usuario = this.segUsuarioDao.findByLogin(login);
			usuario.setBloqueado(false);
			if(usuario.getIntentos()!=null && usuario.getIntentos()>0) {
				usuario.setIntentos(0);
			}
			this.segUsuarioDao.save(usuario);
			this.personaService.generarCredenciales(usuario.getPersonaId().getPersonaId(), usuarioSession);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}

}
