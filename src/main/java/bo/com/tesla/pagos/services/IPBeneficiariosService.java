package bo.com.tesla.pagos.services;

import java.util.List;

import org.springframework.data.domain.Page;

import bo.com.tesla.administracion.entity.PBeneficiariosEntity;
import bo.com.tesla.pagos.dto.PPagosDto;

public interface IPBeneficiariosService {
	public Page<PPagosDto> groupByAbonosClientes(Long archivoId, String paramBusqueda, int page, int size);

	public void deletByArchivoId(Long archivoId);

	public List<PPagosDto> getAbonosParaPagar(Long servicioProductoId, Long recaudadorId, String paramBusqueda);

	public List<PPagosDto> getBeneficiarioPagos(Long archivoId, String codigoCliente,
			String nroDocumentoCliente);

	public List<PBeneficiariosEntity> verificarPrelacion(Long archivoId, String codigoCliente, List<Integer> nroRegistro);
}
