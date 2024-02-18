package bo.com.tesla.administracion.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;

import bo.com.tesla.administracion.dto.SucursalAdmDto;
import bo.com.tesla.administracion.entity.SucursalEntity;

public interface ISucursalService {

	public SucursalAdmDto addUpdateSucursal(SucursalAdmDto sucursalAdmDto, Long usuarioId);

	public void setTransaccion(Long sucursalId, String transaccion, Long usuarioId);

	public void setLstTransaccion(List<Long> sucursalIdLst, String transaccion, Long usuarioId);

	public SucursalAdmDto getSucursalById(Long sucursalId);

	public List<SucursalAdmDto> getAllSucursales();

	public List<SucursalAdmDto> getListSucursalesByRecaudadora(Long recaudadorId);
	public List<SucursalAdmDto> getListSucursalesForAddUserByRecaudadora(Long recaudadorId);

	public Optional<SucursalEntity> findById(@Param("sucursalId") Long sucursalId);

	public List<SucursalEntity> findByRecaudadoraId(Long recaudadorId);

}
