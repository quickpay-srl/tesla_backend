package bo.com.tesla.pagos.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import bo.com.tesla.administracion.entity.ArchivoEntity;
import bo.com.tesla.administracion.entity.PPagoClienteEntity;
import bo.com.tesla.administracion.entity.PTitularPagoEntity;
import bo.com.tesla.administracion.entity.PTransaccionPagoEntity;
import bo.com.tesla.entidades.dao.IArchivoDao;
import bo.com.tesla.pagos.dao.IBeneficiarioDao;
import bo.com.tesla.pagos.dao.IPHistoricoBeneficiariosDao;
import bo.com.tesla.pagos.dao.IPPagoClienteDao;
import bo.com.tesla.pagos.dao.PTitularPagoDao;
import bo.com.tesla.pagos.dto.PPagosDto;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.cross.Util;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
public class PPagoClienteService implements IPPagoClienteService {

	@Autowired
	private IPPagoClienteDao pagoClienteDao;

	@Autowired
	private IArchivoDao archivoDao;

	@Autowired
	private IPHistoricoBeneficiariosDao historicoAbonoClienteDao;

	@Autowired
	private IBeneficiarioDao abonoClienteDao;

	@Autowired
	private IPTransaccionPagoService transaccionPagoService;

	@Value("${tesla.path.files-report}")
	private String filesReport;
	@Autowired
	private DataSource dataSource;

	@Override
	public PPagoClienteEntity save(PPagoClienteEntity entity) {
		return this.pagoClienteDao.save(entity);
	}

	@Transactional
	@Override
	public List<PTransaccionPagoEntity> realizarPago(List<PPagosDto> beneficiarioClienteList, Long usuarioId) {

		PTransaccionPagoEntity transaccionPago = new PTransaccionPagoEntity();
		List<PTransaccionPagoEntity> transaccionPagoList = new ArrayList<>();

		try {
			Long secuencialTransaccion = this.transaccionPagoService.getSecuencialTransaccion();
			Date fechaTransaccion=new Date();

			for (PPagosDto pAbonoClienteDto : beneficiarioClienteList) {
				transaccionPago = this.transaccionPagoService.saveForPagoAbonado(pAbonoClienteDto, usuarioId,
						secuencialTransaccion,fechaTransaccion);
				transaccionPagoList.add(transaccionPago);
				ArchivoEntity archivo = this.archivoDao.findById(pAbonoClienteDto.archivoId).get();
				List<PPagosDto> abonosClientesList = this.abonoClienteDao.getBeneficiarioDetalle(
						pAbonoClienteDto.archivoId, pAbonoClienteDto.codigoCliente,
						pAbonoClienteDto.nroDocumentoCliente, pAbonoClienteDto.periodo);

				for (PPagosDto abonoClienteDetalle : abonosClientesList) {
					PPagoClienteEntity pagosCliente = new PPagoClienteEntity();
					pagosCliente.setArchivoId(archivo);
					pagosCliente.setCantidad(abonoClienteDetalle.cantidad);
					pagosCliente.setCodigoCliente(abonoClienteDetalle.codigoCliente);
					pagosCliente.setConcepto(abonoClienteDetalle.concepto);
					pagosCliente.setExtencionDocumentoId(abonoClienteDetalle.extencionDocumento);
					pagosCliente.setFechaNacimientoCliente(abonoClienteDetalle.fechaNacimientoCliente);
					pagosCliente.setGenero(abonoClienteDetalle.genero);
					pagosCliente.setMontoUnitario(abonoClienteDetalle.montoUnitario);
					pagosCliente.setNombreCliente(abonoClienteDetalle.nombreCliente);
					pagosCliente.setNroDocumentoCliente(abonoClienteDetalle.nroDocumentoCliente);
					pagosCliente.setNroRegistro(abonoClienteDetalle.nroRegistro);
					pagosCliente.setPeriodo(abonoClienteDetalle.periodo);
					pagosCliente.setTipoDocumentoId(abonoClienteDetalle.tipoDocumentoId);
					pagosCliente.setTransaccionPagoId(transaccionPago);
					pagosCliente.setEstado("PAGADO");
					pagosCliente.setTransaccion("PAGAR");
					pagosCliente.setUsuarioCreacion(usuarioId);
					pagosCliente.setFechaCreacion(new Date());

					this.pagoClienteDao.save(pagosCliente);
				}

				this.historicoAbonoClienteDao.updateByArchivoId(pAbonoClienteDto.archivoId,
						pAbonoClienteDto.codigoCliente, pAbonoClienteDto.nroDocumentoCliente, usuarioId, new Date(),
						pAbonoClienteDto.periodo);

				this.abonoClienteDao.deleteAbonados(pAbonoClienteDto.archivoId, pAbonoClienteDto.codigoCliente,
						pAbonoClienteDto.nroDocumentoCliente, pAbonoClienteDto.periodo);
			}

		} catch (DataAccessException e) {
			e.printStackTrace();
			new Technicalexception(e.getMessage(), e.getCause());
		}

		//return this.imprimirReporte(transaccionPagoList);
		return transaccionPagoList;
	}

	@Override
	public byte[] imprimirReporte(List<PTransaccionPagoEntity> transaccionPagoList) {
		Connection conection = null;
		byte[] report = null;
		Map<String, Object> parameters = new HashMap<>();
		BigDecimal totalPagado = new BigDecimal(0);
		String codigoTransaccion = null;
		for (PTransaccionPagoEntity transaccion : transaccionPagoList) {
			totalPagado = totalPagado.add(transaccion.getTotal());
			codigoTransaccion = transaccion.getCodigoTransaccion();
		}
		System.out.println("********************** codigoTransaccion : "+codigoTransaccion);
		parameters.put("montoLiteral", Util.translate(totalPagado + ""));
		parameters.put("codigoTransaccion", codigoTransaccion);
		try {
			File file = ResourceUtils
					.getFile(filesReport + "/report_jrxml/reportes/recaudador/comprobanteMaestro.jrxml");

			JasperReport jasper = JasperCompileManager.compileReport(file.getAbsolutePath());
			conection = dataSource.getConnection();
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, parameters, conection);

			report = Util.jasperExportFormat(jasperPrint, "pdf", filesReport);
			
		} catch (JRException | FileNotFoundException | SQLException e) {
			e.printStackTrace();
			new Technicalexception(e.getMessage(), e.getCause());
		} finally {
			
			try {
				if (conection != null) {
					conection.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				new Technicalexception(ex.getMessage(), ex.getCause());
			}
		}
		return report;

	}

}
