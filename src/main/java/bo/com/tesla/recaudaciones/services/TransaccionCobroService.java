package bo.com.tesla.recaudaciones.services;

import bo.com.tesla.administracion.entity.*;
import bo.com.tesla.administracion.services.IEntidadComisionService;
import bo.com.tesla.administracion.services.IRecaudadorComisionService;
import bo.com.tesla.facturacion.electronica.services.ConexionFacElectronicaService;
import bo.com.tesla.facturaciones.computarizada.dto.AnulacionFacturaLstDto;
import bo.com.tesla.facturaciones.computarizada.services.IAnulacionFacturaService;
import bo.com.tesla.recaudaciones.dao.ICobroClienteDao;
import bo.com.tesla.recaudaciones.dao.IFacturaDao;
import bo.com.tesla.recaudaciones.dao.ITransaccionCobroDao;
import bo.com.tesla.recaudaciones.dto.ReporteCierreCajaDiarioDto;
import bo.com.tesla.recaudaciones.dto.ServicioDeudaDto;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class TransaccionCobroService implements ITransaccionCobroService {

    @Autowired
    private ITransaccionCobroDao iTransaccionCobroDao;

    @Autowired
    private IEntidadComisionService iEntidadComisionService;

    @Autowired
    private IRecaudadorComisionService iRecaudadorComisionService;

    @Autowired
    private ICobroClienteDao iCobroClienteDao;

    @Autowired
    private IAnulacionFacturaService anulacionFacturaService;

    @Autowired
    private IDeudaClienteRService deudaClienteRService;

    @Autowired
    private IHistoricoDeudaService historicoDeudaService;
    @Autowired
    private ConexionFacElectronicaService conexionFacElectronicaService;
    @Autowired
    private IFacturaDao iFacturaDao;
    @Override
    public TransaccionCobroEntity saveTransaccionCobro(TransaccionCobroEntity transaccionCobroEntity) {
        return iTransaccionCobroDao.saveAndFlush(transaccionCobroEntity);
    }

    @Override
    public List<TransaccionCobroEntity> saveAllTransaccionesCobros(List<TransaccionCobroEntity> transaccionCobroEntities) {
        return iTransaccionCobroDao.saveAll(transaccionCobroEntities);
    }

    @Transactional(readOnly = true)
    @Override
    public TransaccionCobroEntity loadTransaccionCobro(ServicioDeudaDto servicioDeudaDto, EntidadEntity entidadEntity, Long usuarioId, String nombreCientePago, String nroDocumentoClientePago,
                                                       EntidadComisionEntity entidadComisionEntity, RecaudadorEntity recaudadorEntity, RecaudadorComisionEntity recaudadorComisionEntity,
                                                       ArchivoEntity archivoEntity, DominioEntity metodoCobro,
                                                       DominioEntity modalidadFacturacion,
                                                       String codigoActividadEconomica) {

        TransaccionCobroEntity transaccionCobroEntity = new TransaccionCobroEntity();
        transaccionCobroEntity.setTipoServicio(servicioDeudaDto.getTipoServicio());
        transaccionCobroEntity.setServicio(servicioDeudaDto.getServicio());
        transaccionCobroEntity.setPeriodo(servicioDeudaDto.getPeriodo());
        transaccionCobroEntity.setUsuarioCreacion(usuarioId);
        transaccionCobroEntity.setFechaCreacion(new Date());
        transaccionCobroEntity.setEntidadId(entidadEntity);
        transaccionCobroEntity.setTransaccion("CREAR");
        transaccionCobroEntity.setArchivoId(archivoEntity);
        transaccionCobroEntity.setNombreClientePago(nombreCientePago);
        transaccionCobroEntity.setTotalDeuda(servicioDeudaDto.getSubTotal());
        transaccionCobroEntity.setNroDocumentoClientePago(nroDocumentoClientePago);
        transaccionCobroEntity.setRecaudador(recaudadorEntity);
        transaccionCobroEntity.setEntidadComision(entidadComisionEntity);
        transaccionCobroEntity.setRecaudadorComision(recaudadorComisionEntity);
        transaccionCobroEntity.setMetodoCobro(metodoCobro);
        transaccionCobroEntity.setModalidadFacturacion(modalidadFacturacion);
        transaccionCobroEntity.setCodigoActividadEconomica(codigoActividadEconomica);
        /*************************************************
        14/05/2021 - Segun reunión.
        Se comentas hasta incluir la comision de acuerdo a requerimiento, se asigna 1
        **************************************************
        transaccionCobroEntity.setComision(iEntidadComisionService.calcularComision(entidadComisionEntity, servicioDeudaDto.subTotal));
        transaccionCobroEntity.setComisionRecaudacion(iRecaudadorComisionService.calcularComision(recaudadorComisionEntity, servicioDeudaDto.subTotal));
        *************************************************/
        transaccionCobroEntity.setComision(new BigDecimal(1));
        transaccionCobroEntity.setComisionRecaudacion(new BigDecimal(1));
        return  transaccionCobroEntity;
    }


	@Override
	public List<TransaccionCobroEntity> findDeudasCobradasByUsuarioCreacionForGrid(Long usuarioCreacion,
			Date fechaSeleccionada,Long entidadId) {
	
		return this.iTransaccionCobroDao.findDeudasCobradasByUsuarioCreacionForGrid(usuarioCreacion, fechaSeleccionada, entidadId);
	}

    @Override
    public Boolean anularTransaccion(Long entidadId,
                                      AnulacionFacturaLstDto anulacionFacturaLstDto,

                                      SegUsuarioEntity usuarioEntity) throws BusinesException {

        //Verificar si en la selección de facturas existen archivos que ya no esten activos
        /*Integer countArchivosNoActivos = iTransaccionCobroDao.countArchivosNoActivosPorListaFacturas(anulacionFacturaLstDto.facturaIdLst, modalidadFacturacionId);
        if(countArchivosNoActivos > 0) {
            throw new BusinesException("El archivo del cargado de la deuda ya no es el actual. Comuníquese con el administrador.");
        }*/

        try {
            String transaccion = anulacionFacturaLstDto.cargadoErroneo ? "ANULAR ERRONEO" : "ANULAR";
            String estado = anulacionFacturaLstDto.cargadoErroneo ? "ERRONEO" : "ANULADO";

            //Anular Transacciones
            //Integer countupdate = iTransaccionCobroDao.updateLstTransaccionByFacturas(anulacionFacturaLstDto.facturaIdLst, modalidadFacturacionId, transaccion, usuarioEntity.getUsuarioId());
            Integer countupdate = iTransaccionCobroDao.updateLstTransaccionByFacturas(anulacionFacturaLstDto.facturaIdLst, transaccion, usuarioEntity.getUsuarioId());
            if (countupdate == 0) {
                throw new Technicalexception("No se ha logrado actualizar a ANULADO las transacciones.");
            }

            //Anular Cobros
            Integer countUpdateCobros = iCobroClienteDao.updateLstTransaccionByFacturas(anulacionFacturaLstDto.facturaIdLst, transaccion, usuarioEntity.getUsuarioId());
            if (countUpdateCobros == 0) {
                throw new Technicalexception("No se ha logrado actualizar a ANULADO los CobrosClientes.");
            }

            //Actualizar estado de históricos
            Integer countHistoricos = historicoDeudaService.updateHistoricoDeudaLstByFacturas(anulacionFacturaLstDto.facturaIdLst, estado);
            if(countHistoricos == 0) {
                throw new Technicalexception("No se ha logrado actualizar el estado de los registros históricos de deudas");
            }

            if(!anulacionFacturaLstDto.cargadoErroneo) {
                //Recuperar las deudas
                Integer countRecovery = deudaClienteRService.recoverDeudasByFacturas(anulacionFacturaLstDto.facturaIdLst);
                if (countRecovery < 1) {
                    throw new Technicalexception("No se ha recuperado ninguna deuda");
                }
            }

            /***********************************************************************/
            for (Long facturaId: anulacionFacturaLstDto.facturaIdLst ) {
                Optional<FacturaEntity> fac =  iFacturaDao.findById(facturaId);
                if(fac.isPresent()){
                    ResponseDto responseDto = anularFacturav2(entidadId,fac.get().getResponseCuf(),1);
                    if(responseDto.status){
                        FacturaEntity f = fac.get();
                        Map  map =  (Map)responseDto.result;
                        f.setResponseState(map.get("state")+"");
                        f.setResponseNroFactura(map.get("numeroFactura")+"");
                        Map ma = (Map) map.get("representacionGrafica");
                        f.setResponsePdfRepgrafica(ma.get("pdf")+"");
                        f.setResponseXmlRepgrafica(ma.get("xml")+"");
                        f.setResponseRolloRepgrafica(ma.get("rollo")+"");
                        f.setResponseSinRepgrafica(ma.get("sin")+"");
                        f.setEstado("ANULADO");
                        iFacturaDao.save(f);
                    }
                }

            }

            //Anulación de Facturas
            /*ResponseDto responseDto = anulacionFacturaService.postAnulacionLst(entidadId, anulacionFacturaLstDto);
            if(!responseDto.status) {
                throw new Technicalexception(responseDto.message);
            }*/


            /***********************************************************************/

            return true;
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void updateFacturasTransaccion(List<Map<Long, List<Long>>> facturaTransacLst/*List<FacturaDto> facturaDtoList*/) {
        facturaTransacLst.stream()
                .flatMap(m->m.entrySet().stream())
                .forEach(e-> {
                        Integer countUpdate = iTransaccionCobroDao.updateFacturaTransaccion(e.getValue(), e.getKey());
                        if(e.getValue().size() != countUpdate) {
                            throw new Technicalexception("No se actualizado correctamente las facturas");
                        }
                    }
                );

    }

    @Override
    public List<String> getCodigosActividadUnicos(List<TransaccionCobroEntity> transaccionCobroEntityList) {
        List<String> codActEcoList = transaccionCobroEntityList.stream().map(t -> t.getCodigoActividadEconomica())
                .collect(Collectors.toList());
        return codActEcoList.stream().distinct().collect(Collectors.toList());
    }

	@Override
	public TransaccionCobroEntity loadTransaccionCobro(ServicioDeudaDto servicioDeudaDto, EntidadEntity entidadEntity,
			Long usuarioId, String nombreCientePago, String nroDocumentoClientePago,
			EntidadComisionEntity entidadComisionEntity, RecaudadorEntity recaudadorEntity,
			RecaudadorComisionEntity recaudadorComisionEntity, ArchivoEntity archivoEntity, DominioEntity metodoCobro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Long> findFacturasByModalidadAndEntidadAndRecaudador(Long modalidadFacturaId,
                                                                     Long entidadId,
                                                                     Long recaudadorId) {
        return iTransaccionCobroDao.findFacturasByModalidadAndEntidadAndRecaudador(modalidadFacturaId,
                entidadId,
                recaudadorId
        );
    }

    @Override
    public List<TransaccionCobroEntity> findByEntAndRecAndFac(Long entidadId, Long recaudadorId, Long modalidadFacturacionId, Long facturaId) {
        return iTransaccionCobroDao.findByEntAndRecAndFac(entidadId, recaudadorId, modalidadFacturacionId, facturaId);
    }

    @Override
    public List<ReporteCierreCajaDiarioDto> findDeudasCobradasByUsuarioCreacionForJasper(Long usuarioCreacion, Date fechaSeleccionada, List<Long> lstEntidadId) {
        return this.iTransaccionCobroDao.findDeudasCobradasByUsuarioCreacionForJasper(usuarioCreacion, fechaSeleccionada, lstEntidadId);
    }

    @Override
    public List<ReporteCierreCajaDiarioDto> findCierreCajaDiarioForJasper(Long usuarioCreacion, List<Long> lstEntidadId) {
        return this.iTransaccionCobroDao.findCierreCajaDiarioForJasper(usuarioCreacion, lstEntidadId);
    }

    public ResponseDto anularFacturav2( Long entidadId, String  cuf, Integer codMotivo) {

        ResponseDto res = new ResponseDto();
        try{
            res = conexionFacElectronicaService.getResponseMethodGet( "/api/facturacion-electronica/v1/anularFactura/"+entidadId+"/"+cuf+"/"+codMotivo);
        }catch (Exception ex){
            res.status = false;
            res.message = "error al generar factura : "+ex.toString();
            return res;
        }
        return res;

    }
}
