package bo.com.tesla.pagos.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bo.com.tesla.administracion.dao.IEntidadComisionesDao;
import bo.com.tesla.administracion.dao.IRecaudadorComisionDao;
import bo.com.tesla.administracion.entity.ArchivoEntity;
import bo.com.tesla.administracion.entity.EntidadComisionEntity;
import bo.com.tesla.administracion.entity.PTitularPagoEntity;
import bo.com.tesla.administracion.entity.PTransaccionPagoEntity;
import bo.com.tesla.administracion.entity.RecaudadorComisionEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.entidades.dao.IArchivoDao;
import bo.com.tesla.pagos.dao.IBeneficiarioDao;
import bo.com.tesla.pagos.dao.IPHistoricoBeneficiariosDao;
import bo.com.tesla.pagos.dao.IPTransaccionPagoDao;
import bo.com.tesla.pagos.dao.PTitularPagoDao;
import bo.com.tesla.pagos.dto.PPagosDto;
import bo.com.tesla.recaudaciones.dao.IRecaudadorDao;
import bo.com.tesla.useful.constant.TipoComisionConst;
import net.bytebuddy.asm.Advice.OffsetMapping.ForOrigin.Renderer.ForReturnTypeName;

@Service
public class PTransaccionPagoService implements IPTransaccionPagoService {

	@Autowired
	private IPTransaccionPagoDao transaccionPagoDao;

	@Autowired
	private IBeneficiarioDao abonoClienteDao;

	@Autowired
	private IArchivoDao archivoDao;

	@Autowired
	private IEntidadComisionesDao entiadComisionDao;

	@Autowired
	private IRecaudadorComisionDao recaudadorComision;

	@Autowired
	private IRecaudadorDao recaudadorDao;

	@Override
	public PTransaccionPagoEntity save(PTransaccionPagoEntity entity) {

		return this.transaccionPagoDao.save(entity);
	}

	@Autowired
	private PTitularPagoDao titularPagoDao;

	@Override
	public PTransaccionPagoEntity saveForPagoAbonado(PPagosDto beneficiario, Long usuarioId, Long secuencialTransaccion,Date fechaTransaccion) {

		EntidadComisionEntity entidadComision = new EntidadComisionEntity();
		RecaudadorComisionEntity recaudadorComision = new RecaudadorComisionEntity();
		PTransaccionPagoEntity transaccionPago = new PTransaccionPagoEntity();

		List<String> periodosList = new ArrayList<>();

		BigDecimal comisionEntidad;
		BigDecimal comisionRecaudadora;

		periodosList.add(beneficiario.periodo);

		PPagosDto abonosCliente = this.abonoClienteDao.getBeneficiarioAndMontoToal(beneficiario.archivoId,
				beneficiario.codigoCliente, beneficiario.nroDocumentoCliente, periodosList);
		ArchivoEntity archivo = this.archivoDao.findById(beneficiario.archivoId).get();
		RecaudadorEntity recaudadora = this.recaudadorDao.findRecaudadorByUserId(usuarioId).get();
		entidadComision = this.entiadComisionDao
				.findEntidadComisionEntityByEntidadAndEstado(archivo.getEntidadId(), "ACTIVO").get();
		recaudadorComision = this.recaudadorComision
				.findRecaudadorComisionEntityByRecaudadorAndEstado(recaudadora, "ACTIVO").get();

		transaccionPago.setArchivoId(archivo);
		transaccionPago.setCodigoCliente(beneficiario.codigoCliente);

		if (entidadComision.getTipoComision().equals(TipoComisionConst.COMISION_MONTO)) {
			comisionEntidad = entidadComision.getComision();
		} else {
			comisionEntidad = abonosCliente.totalPagar.multiply(entidadComision.getComision())
					.divide(new BigDecimal(100));
		}
		transaccionPago.setComisionExacta(comisionEntidad);

		if (recaudadorComision.getTipoComision().equals(TipoComisionConst.COMISION_MONTO)) {
			comisionRecaudadora = recaudadorComision.getComision();
		} else {
			comisionRecaudadora = abonosCliente.totalPagar.multiply(recaudadorComision.getComision())
					.divide(new BigDecimal(100));
		}
		transaccionPago.setComisionRecaudacion(comisionRecaudadora);
		transaccionPago.setPeriodo(beneficiario.periodo);
		transaccionPago.setEntidadId(archivo.getEntidadId());
		transaccionPago.setRecaudadorId(recaudadora);
		transaccionPago.setTotal(abonosCliente.totalPagar);
		transaccionPago.setUsuarioModificacion(usuarioId);
		transaccionPago.setFechaModificacion(fechaTransaccion);
		transaccionPago.setUsuarioCreacion(usuarioId);
		transaccionPago.setFechaCreacion(fechaTransaccion);
		transaccionPago.setSubTotal(abonosCliente.totalPagar);
		transaccionPago.setServicioProductoId(archivo.getServicioProductoId());
		transaccionPago.setTransaccion("PAGAR");
		transaccionPago.setCodigoTransaccion(""+archivo.getEntidadId().getEntidadId()+"."+recaudadora.getRecaudadorId()+"."+beneficiario.codigoCliente+"."+ secuencialTransaccion);
		
		transaccionPago = this.transaccionPagoDao.save(transaccionPago);

		if (beneficiario.nombreTitular != null && beneficiario.nombreTitular != "") {
			PTitularPagoEntity titular = new PTitularPagoEntity();
			titular.setNombreCompleto(beneficiario.nombreTitular);
			titular.setNroDocumento(beneficiario.documentoTitular);
			titular.setTransaccionesPagoId(transaccionPago);
			titularPagoDao.save(titular);
		}

		return transaccionPago;
	}
	
	@Override
	public Long getSecuencialTransaccion() {
		return this.transaccionPagoDao.getSecuencialTransaccion();
		
	}

	@Override
	public Page<PPagosDto> findTransaccionsByUsuario(Date fechaIni, 
															Date fechaFin, 
															Long usuarioId,
															String param,
															Long servicioProductoId,
															int page, int size) {	
		
		Pageable paging = PageRequest.of(page, size);
		return this.transaccionPagoDao.findTransaccionsByUsuario(fechaIni, fechaFin, usuarioId, param,servicioProductoId,paging);
	}

	@Override
	public List<PTransaccionPagoEntity> transaccionessByCodigoTransacciones(String codigoTransaccion) {
	
		return this.transaccionPagoDao.transaccionessByCodigoTransacciones(codigoTransaccion);
	};

}
