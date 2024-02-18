package bo.com.tesla.administracion.services;

import java.util.List;
import java.util.Optional;

import bo.com.tesla.administracion.dto.CredencialFacturacionDto;
import bo.com.tesla.administracion.dto.SucursalEntidadAdmDto;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.administracion.entity.SucursalEntidadEntity;
import bo.com.tesla.useful.config.BusinesException;

public interface ISucursalEntidadService {
	public SucursalEntidadAdmDto addUpdateSucursalEntidad(SucursalEntidadAdmDto sucursalEntidadAdmDto, Long usuarioId)
			throws BusinesException;

	public void setTransaccionSucursalEntidad(Long sucursalEntidadId, String transaccion, Long usuarioId);

	public void setLstTransaccion(List<Long> sucursalEntidadIdLst, String transaccion, Long usuarioId);

	public SucursalEntidadAdmDto getSucursalEntidadById(Long sucursalEntidadId);

	public List<SucursalEntidadAdmDto> getAllSucursalEntidades();

	public List<SucursalEntidadAdmDto> getLisSucursalEntidadesByEntidadId(Long entidadId);
	public List<SucursalEntidadAdmDto> getLisSucursalEntidadesForAddUserByEntidadId(Long entidadId);


	List<SucursalEntidadAdmDto> getLisSucursalEntidadesByEntidadIdActivos(Long entidadId);

	Optional<SucursalEntidadAdmDto> findsucursalEmtidadEmiteFacturaTesla(Long entidadId);

	void updateCredencialesFacturacion(CredencialFacturacionDto credencialFacturacionDto,
			SegUsuarioEntity usuarioEntity);

	Optional<CredencialFacturacionDto> findCredencialFacturacion(Long sucursalEntidadId);

}
