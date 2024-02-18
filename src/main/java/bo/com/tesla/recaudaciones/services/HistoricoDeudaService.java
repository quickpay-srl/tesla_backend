package bo.com.tesla.recaudaciones.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.com.tesla.administracion.entity.DeudaClienteEntity;
import bo.com.tesla.administracion.entity.HistoricoDeudaEntity;
import bo.com.tesla.entidades.dto.ConceptoDto;
import bo.com.tesla.entidades.dto.DeudasClienteDto;
import bo.com.tesla.recaudaciones.dao.IHistoricoDeudaDao;
import bo.com.tesla.recaudaciones.dto.DeudasCobradasFacturaDto;
import bo.com.tesla.recaudaciones.dto.EstadoTablasDto;
import bo.com.tesla.recaudaciones.dto.RecaudadoraDto;

@Service
public class HistoricoDeudaService implements IHistoricoDeudaService {

	@Autowired
	private IHistoricoDeudaDao iHistoricoDeudaDao;

	/*
	 * @Override public Integer updateEstado(Long deudaClienteId, String estado) {
	 * return iHistoricoDeudaDao.updateEstado(deudaClienteId, estado); }
	 */

	@Override
	public Integer updateHistoricoDeudaLst(List<DeudaClienteEntity> deudaClienteEntities) {
		List<Long> deudaClienteIdLst = deudaClienteEntities.stream().mapToLong(d -> d.getDeudaClienteId()).boxed()
				.collect(Collectors.toList());

		return iHistoricoDeudaDao.updateLstEstado(deudaClienteIdLst, "COBRADO");
	}

	@Transactional(readOnly = true)
	@Override
	public Page<DeudasClienteDto> groupByDeudasClientes(Long archivoId, String paramBusqueda, int page, int size) {
		Page<DeudasClienteDto> historicoDeudasList;
		Pageable paging = PageRequest.of(page, size);
		historicoDeudasList = this.iHistoricoDeudaDao.groupByDeudasClientes(archivoId, paramBusqueda, paging);
		Integer key = 0;

		for (DeudasClienteDto deudasClienteDto : historicoDeudasList) {
			List<ConceptoDto> conceptosList = this.iHistoricoDeudaDao.findConceptos(deudasClienteDto.archivoId,
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
			}
			deudasClienteDto.conceptoLisit = conceptosList;
		}
		return historicoDeudasList;

	}

	@Override
	public List<EstadoTablasDto> findEstadoHistorico() {

		return this.iHistoricoDeudaDao.findEstadoHistorico();
	}

	@Override
	public BigDecimal getMontoTotalCobrados(Long archivoId, Long recaudadorId) {

		return this.iHistoricoDeudaDao.getMontoTotalCobrados(archivoId, recaudadorId);
	}

	@Override
	public List<RecaudadoraDto> getMontoTotalPorRecaudadora(Long archivoId) {

		List<Object[]> objectList = this.iHistoricoDeudaDao.getMontoTotalPorRecaudadora(archivoId);
		List<RecaudadoraDto> recaudadorList = new ArrayList<>();
		for (Object[] objects : objectList) {
			RecaudadoraDto recaudador = new RecaudadoraDto(objects[0] + "", new BigDecimal(objects[1] + ""));
			recaudadorList.add(recaudador);
		}
		return recaudadorList;
	}

	@Override
	public List<DeudasCobradasFacturaDto> findDeudasCobrasForFactura(List<Long> transaccionCobroIds) {
		return iHistoricoDeudaDao.findDeudasCobrasForFactura(transaccionCobroIds);
	}

	@Override
	public ByteArrayInputStream load(Long archivoId) {
		return writeDataToCsv(archivoId);
	}

	public ByteArrayInputStream writeDataToCsv(Long archivoId) {

		String[] HEADERS =  { "nroRegistro", "codigoCliente", "nombreCliente", "nroDocumento", "direccion",
				"telefono", "nit", "servicio", "tipoServicio", "periodo", "tipo", "concepto", "montoUnitario",
				"cantidad","subTotal", "datoExtras", "tipoComprobante", "periodoCabecera","esPostpago" };

		try (final ByteArrayOutputStream stream = new ByteArrayOutputStream();
				final CSVPrinter printer = new CSVPrinter(new PrintWriter(stream),
						CSVFormat.DEFAULT.withHeader(HEADERS))) {
			List<HistoricoDeudaEntity> historicoList = iHistoricoDeudaDao.findHistoricoDeudasByArchivoId(archivoId);
			for (final HistoricoDeudaEntity historico : historicoList) {

				final List<String> data = Arrays.asList(String.valueOf(historico.getNroRegistro()),
						historico.getCodigoCliente(), historico.getNombreCliente(), historico.getNroDocumento(),
						historico.getDireccion(), historico.getTelefono(), historico.getNit(), historico.getServicio(),
						String.valueOf(historico.getTipo()), historico.getPeriodo(),
						String.valueOf(historico.getTipo()), historico.getConcepto(),
						String.valueOf(historico.getMontoUnitario()), String.valueOf(historico.getCantidad()),
						String.valueOf(historico.getSubTotal()), historico.getDatoExtra(),
						String.valueOf(historico.isTipoComprobante()), historico.getPeriodoCabecera(),
						String.valueOf(historico.getEsPostpago()));

				printer.printRecord(data);
			}

			printer.flush();
			return new ByteArrayInputStream(stream.toByteArray());
		} catch (final IOException e) {
			throw new RuntimeException("Csv writing error: " + e.getMessage());
		}
	}
	
	
	@Override
	public List<EstadoTablasDto> findEstadoHistoricoPagos() {

		return this.iHistoricoDeudaDao.findEstadoHistoricoPagos();
	}

	@Override
	public Integer updateHistoricoDeudaLstByFacturas(List<Long> facturaIdLst, String estado) {
		return iHistoricoDeudaDao.updateLstEstadoByFacturas(facturaIdLst, estado);
	}

}