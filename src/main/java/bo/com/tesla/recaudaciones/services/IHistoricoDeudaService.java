package bo.com.tesla.recaudaciones.services;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.List;

import bo.com.tesla.administracion.entity.CobroClienteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import bo.com.tesla.administracion.entity.DeudaClienteEntity;
import bo.com.tesla.administracion.entity.HistoricoDeudaEntity;
import bo.com.tesla.entidades.dto.DeudasClienteDto;
import bo.com.tesla.recaudaciones.dto.DeudasCobradasFacturaDto;
import bo.com.tesla.recaudaciones.dto.EstadoTablasDto;
import bo.com.tesla.recaudaciones.dto.RecaudadoraDto;

public interface IHistoricoDeudaService {

	public Integer updateHistoricoDeudaLst(List<DeudaClienteEntity> deudaClienteEntities);

	public Page<DeudasClienteDto> groupByDeudasClientes(Long archivoId, String paramBusqueda, int page, int size);

	public List<EstadoTablasDto> findEstadoHistorico();

	public BigDecimal getMontoTotalCobrados(Long archivoId, Long recaudadorId);

	public List<RecaudadoraDto> getMontoTotalPorRecaudadora(Long archivoId);

	public List<DeudasCobradasFacturaDto> findDeudasCobrasForFactura(List<Long> transaccionCobroIds);
	
	public ByteArrayInputStream load(Long archivoId);
	
	public ByteArrayInputStream writeDataToCsv(Long archivoId);
	
	public List<EstadoTablasDto> findEstadoHistoricoPagos();

	public Integer updateHistoricoDeudaLstByFacturas(List<Long> facturaIdLst, String estado);


}