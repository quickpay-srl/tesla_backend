package bo.com.tesla.pagosv2.service;

import bo.com.tesla.administracion.entity.*;
import bo.com.tesla.externo.sitio.dao.ISitioDeudaClienteDao;
import bo.com.tesla.externo.sitio.dto.SitioDatosCobroDto;
import bo.com.tesla.facturacion.electronica.services.ConexionFacElectronicaService;
import bo.com.tesla.pagosv2.dao.ISitioDatosConfirmadoQrDao;
import bo.com.tesla.pagosv2.dao.ISitioQrGeneradoDao;
import bo.com.tesla.recaudaciones.dao.*;
import bo.com.tesla.recaudaciones.dto.requestGenerarFactura.*;
import bo.com.tesla.recaudaciones.services.ITransaccionCobroService;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.constant.PlantillaEmail;
import bo.com.tesla.useful.cross.SendEmail;
import bo.com.tesla.useful.dto.ResponseDto;
import javassist.expr.Cast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Convert;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.*;

@Service
public class ConfirmarPagoQRService {

    @Autowired
    private ISitioDatosConfirmadoQrDao iSitioDatosConfirmadoQrDao;

    @Autowired
    private ISitioDeudaClienteDao iSitioDeudaClienteDao;

    @Autowired
    private IRecaudadorDao iRecaudadorDao;
    @Autowired
    private IDominioDao iDominioDao;

    @Autowired
    private IEntidadRDao iEntidadRDao;

    @Autowired
    private ITransaccionCobroService iTransaccionCobroService;

    @Autowired
    private IHistoricoDeudaDao iHistoricoDeudaDao;

    @Autowired
    private ICobroClienteDao iCobroClienteDao;
    @Autowired
    private ConexionFacElectronicaService conexionFacElectronicaService;
    @Autowired
    IFacturaDao iFacturaDao;
    @Autowired
    private ITransaccionCobroDao iTransaccionCobroDao;

    @Autowired
    private ISitioQrGeneradoDao iSitioQrGeneradoDao;

    @Autowired
    private ConexionConfirmaPagoQrService conexionConfirmaPagoQrService;

    @Value("${tesla.mail.correoEnvio}")
    private String correoEnvio;

    @Autowired
    private SendEmail sendEmail;

    public  void notificaClientePago(String monto,String moneda, String aliasQr){

       try{

           // Verificar si el QR fue generado por nuestro sistema
           List<QRGeneradoEntity>  lstQrGenerado = iSitioQrGeneradoDao.findByAlias(aliasQr);
           if(lstQrGenerado.isEmpty()){
               System.out.println("EL QR no se ha generado desde la Empresa");
               throw new Technicalexception("EL QR no se ha generado desde la Empresa ");
           }

           //obtener coreo y nombre cliente
           Optional<DeudaClienteEntity> objDeudaCliente = iSitioDeudaClienteDao.findForDeudaClienteId(lstQrGenerado.get(0).getDeudaClienteId());


           String plantillaCorreo = PlantillaEmail.plantillaNotfClientePago(objDeudaCliente.get().getNombreCliente(),monto,moneda,aliasQr);
           this.sendEmail.sendHTML(correoEnvio, objDeudaCliente.get().getCorreoCliente(), "Notificación de confirmación de Pago (QUICKPAY)",
                   plantillaCorreo);
       }catch (Exception ex){
           System.out.println("ERROR EN ENVIO SMS");
           System.out.println(ex.toString());
       }


    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Technicalexception.class)
    public Map registrarConfirmacion(Map datosConfirmacionPagoQr) {
        Map<String, Object> response = new HashMap<>();
        List<TransaccionCobroEntity> transaccionesCobroList = new ArrayList<>();
        List<DeudaClienteEntity> deudaClienteEntityList = new ArrayList<>();

        try {
            try {
                conexionConfirmaPagoQrService.getResponseMethodGet("v1/qr/notificar/"+datosConfirmacionPagoQr.get("alias"));
            } catch (Exception ex)
            {
                System.out.println("Error el enviar notificación");
            }


            System.out.println("INGRESANDO A CONFIRMAR");
            // almacenando datos qr
            DatosConfirmadoQrEntity insertdatosQrEntity = new DatosConfirmadoQrEntity();
            insertdatosQrEntity.setAliasSip(datosConfirmacionPagoQr.get("alias") + "");
            insertdatosQrEntity.setNumeroOrdenOriginanteSip(datosConfirmacionPagoQr.get("numeroOrdenOriginante") + "");
            insertdatosQrEntity.setMontoSip(datosConfirmacionPagoQr.get("monto") + "");
            insertdatosQrEntity.setIdQrSip(datosConfirmacionPagoQr.get("idQr") + "");
            insertdatosQrEntity.setMonedaSip(datosConfirmacionPagoQr.get("moneda") + "");
            insertdatosQrEntity.setFechaProcesoSip(datosConfirmacionPagoQr.get("fechaproceso") + "");
            insertdatosQrEntity.setCuentaClienteSip(datosConfirmacionPagoQr.get("cuentaCliente") + "");
            insertdatosQrEntity.setNombreClienteSip(datosConfirmacionPagoQr.get("nombreCliente") + "");
            insertdatosQrEntity.setDocumentoClienteSip(datosConfirmacionPagoQr.get("documentoCliente") + "");
            insertdatosQrEntity.setFechaRegistro(new Date());
            insertdatosQrEntity.setEstado("ACTIVO");
            iSitioDatosConfirmadoQrDao.save(insertdatosQrEntity);
            System.out.println("INGRESANDO DATOS QR");

            // Verificar si el QR fue generado por nuestro sistema
            List<QRGeneradoEntity>  lstQrGenerado = iSitioQrGeneradoDao.findByAlias(datosConfirmacionPagoQr.get("alias") + "");
            if(lstQrGenerado.isEmpty()){
                System.out.println("EL QR no se ha generado desde la Empresa");
                throw new Technicalexception("EL QR no se ha generado desde la Empresa ");
            }

            // verificar monto
            if(Double.parseDouble(lstQrGenerado.get(0).getMonto()) != Double.parseDouble(datosConfirmacionPagoQr.get("monto")+"")){
                System.out.println("El monto generado del QR fue de "+lstQrGenerado.get(0).getMonto());
                throw new Technicalexception("El monto generado del QR fue de "+lstQrGenerado.get(0).getMonto());
            }




            for (QRGeneradoEntity qrGeneradoEntity : lstQrGenerado) {

                Optional<DeudaClienteEntity> objDeudaCliente = iSitioDeudaClienteDao.findForDeudaClienteId(qrGeneradoEntity.getDeudaClienteId());
                if (objDeudaCliente.isEmpty()) {
                    System.out.println("No existe el registro  para las deudas con el alias " + datosConfirmacionPagoQr.get("alias") + "");
                    throw new Technicalexception("No existe el registro  para las deudas con el alias " + datosConfirmacionPagoQr.get("alias") + "");
                }
                deudaClienteEntityList.add(objDeudaCliente.get());
                // Insertar Transacciones Cobro
                TransaccionCobroEntity transaccionCobroEntity = new TransaccionCobroEntity();
                transaccionCobroEntity.setTipoServicio(objDeudaCliente.get().getTipoServicio());
                transaccionCobroEntity.setServicio(objDeudaCliente.get().getServicio());
                transaccionCobroEntity.setPeriodo(objDeudaCliente.get().getPeriodo());
                transaccionCobroEntity.setUsuarioCreacion(130L); // Usuario recaudador
                transaccionCobroEntity.setFechaCreacion(new Date());
                transaccionCobroEntity.setEntidadId(objDeudaCliente.get().getArchivoId().getEntidadId());
                transaccionCobroEntity.setTransaccion("CREAR");
                transaccionCobroEntity.setArchivoId(objDeudaCliente.get().getArchivoId());
                transaccionCobroEntity.setNombreClientePago(objDeudaCliente.get().getNombreCliente());
                transaccionCobroEntity.setTotalDeuda(objDeudaCliente.get().getSubTotal());
                transaccionCobroEntity.setNroDocumentoClientePago(objDeudaCliente.get().getNroDocumento());
                Optional<RecaudadorEntity> recaudadorEntityOptional = iRecaudadorDao.findRecaudadorByUserId(130L);
                transaccionCobroEntity.setRecaudador(recaudadorEntityOptional.get());
                transaccionCobroEntity.setEntidadComision(null);
                transaccionCobroEntity.setRecaudadorComision(null);
                Optional<DominioEntity> metodoCobroOptional = iDominioDao.getDominioEntityByDominioIdAndDominioAndEstado(72L, "metodo_cobro_id", "ACTIVO");
                transaccionCobroEntity.setMetodoCobro(metodoCobroOptional.get());
                Optional<EntidadEntity> entidadEntityOptional = iEntidadRDao.findByEntidadIdAndEstado(objDeudaCliente.get().getArchivoId().getEntidadId().getEntidadId(), "ACTIVO");
                transaccionCobroEntity.setModalidadFacturacion(entidadEntityOptional.get().getModalidadFacturacion());
                transaccionCobroEntity.setCodigoActividadEconomica(objDeudaCliente.get().getCodigoActividadEconomica());
                transaccionCobroEntity.setComision(new BigDecimal(1));
                transaccionCobroEntity.setComisionRecaudacion(new BigDecimal(1));
                transaccionCobroEntity.setCodigoCliente(objDeudaCliente.get().getCodigoCliente());
                transaccionCobroEntity.setNombreClienteArchivo(objDeudaCliente.get().getNombreCliente());
                transaccionCobroEntity.setNroDocumentoClienteArchivo(objDeudaCliente.get().getNroDocumento());
                transaccionCobroEntity.setCorreoCliente(objDeudaCliente.get().getCorreoCliente());
                transaccionCobroEntity.setDatosconfirmadoQrId(insertdatosQrEntity.getDatosconfirmadoQrId());
                //transaccionCobroEntity.setEstado("COBRADO");
                transaccionCobroEntity.setTransaccion("CREAR");
                transaccionCobroEntity.setDeudaClienteId(objDeudaCliente.get().getDeudaClienteId());
                transaccionCobroEntity = iTransaccionCobroService.saveTransaccionCobro(transaccionCobroEntity);

                System.out.println("Transaccion Cobro Insertado");

                transaccionesCobroList.add(transaccionCobroEntity);

                //Verificar deudas en Historico Deudas
                Optional<HistoricoDeudaEntity> historicoDeudaEntityOptional = iHistoricoDeudaDao.findByDeudaClienteId(qrGeneradoEntity.getDeudaClienteId());
                if (!historicoDeudaEntityOptional.isPresent()){
                    System.out.println("No existe el registro historico para las deudas con el alias " + datosConfirmacionPagoQr.get("alias") + "");
                    throw new Technicalexception("No existe el registro historico para las deudas con el alias " + datosConfirmacionPagoQr.get("alias") + "");
                }


                CobroClienteEntity cobroClienteEntity = new CobroClienteEntity();
                cobroClienteEntity.setArchivoId(objDeudaCliente.get().getArchivoId());
                cobroClienteEntity.setNroRegistro(objDeudaCliente.get().getNroRegistro());
                cobroClienteEntity.setCodigoCliente(objDeudaCliente.get().getCodigoCliente());
                cobroClienteEntity.setNombreCliente(objDeudaCliente.get().getNombreCliente());
                cobroClienteEntity.setNroDocumento(objDeudaCliente.get().getNroDocumento());
                cobroClienteEntity.setTipoServicio(objDeudaCliente.get().getTipoServicio());
                cobroClienteEntity.setServicio(objDeudaCliente.get().getServicio());
                cobroClienteEntity.setPeriodo(objDeudaCliente.get().getPeriodo());
                cobroClienteEntity.setTipo(objDeudaCliente.get().getTipo());
                cobroClienteEntity.setCantidad(objDeudaCliente.get().getCantidad());
                cobroClienteEntity.setConcepto(objDeudaCliente.get().getConcepto());
                cobroClienteEntity.setDatoExtra(objDeudaCliente.get().getDatoExtras());
                cobroClienteEntity.setTipoComprobante(objDeudaCliente.get().getTipoComprobante());
                cobroClienteEntity.setPeriodoCabecera(objDeudaCliente.get().getPeriodoCabecera());
                cobroClienteEntity.setNit(objDeudaCliente.get().getNit());
                cobroClienteEntity.setDireccion(objDeudaCliente.get().getDireccion());
                cobroClienteEntity.setTelefono(objDeudaCliente.get().getTelefono());
                cobroClienteEntity.setEsPostpago(objDeudaCliente.get().getEsPostpago());
                cobroClienteEntity.setUsuarioCreacion(126); // usuario de Recaudador
                cobroClienteEntity.setFechaCreacion(new Date());
                cobroClienteEntity.setEstado("COBRADO");
                cobroClienteEntity.setTransaccion("COBRAR"); // SIEMPR SE PONE COBRAR, NOSE POR Q
                cobroClienteEntity.setTransaccionCobro(transaccionCobroEntity);
                cobroClienteEntity.setHistoricoDeuda(historicoDeudaEntityOptional.get());
                cobroClienteEntity.setMontoUnitario(objDeudaCliente.get().getMontoUnitario());
                cobroClienteEntity.setSubTotal(objDeudaCliente.get().getSubTotal());
                cobroClienteEntity.setMontoModificado(false);
                iCobroClienteDao.save(cobroClienteEntity);
                System.out.println("Cobro CLiente Insertado");
                //Actualizar Historico a COBRADO
                HistoricoDeudaEntity objHistorico = iHistoricoDeudaDao.findByDeudaClienteId(qrGeneradoEntity.getDeudaClienteId()).get();
                objHistorico.setEstado("COBRADO");
                System.out.println("Historico modificado a COBRADO");
                iHistoricoDeudaDao.save(objHistorico);

            }

            // Preparar datos para fact electronica
            DatosEntidadDto datosEntidadDto = new DatosEntidadDto();
            datosEntidadDto.setEntidadId(transaccionesCobroList.get(0).getEntidadId().getEntidadId());
            datosEntidadDto.setCodigoPuntoVenta(0);
            datosEntidadDto.setCodigoSucursal(0);
            DatosClienteDto datosClienteDto = new DatosClienteDto();  // a este metod siempre entrada un solo codigo (un solo cliente), por eso se toma el primero de la lista
            datosClienteDto.setCodigoCliente(deudaClienteEntityList.get(0).getCodigoCliente());
            datosClienteDto.setNumeroDocumento(deudaClienteEntityList.get(0).getNroDocumento());
            datosClienteDto.setRazonSocial(deudaClienteEntityList.get(0).getNombreCliente());
            datosClienteDto.setEmail(deudaClienteEntityList.get(0).getCorreoCliente());
            datosClienteDto.setCodigoTipoDocumentoIdentidad(1);
            List<DetalleFacturaDto> lstDetalleFacturaDto = new ArrayList<>();
            for (DeudaClienteEntity objDet : deudaClienteEntityList) {
                DetalleFacturaDto detalleFacturaDto = new DetalleFacturaDto();
                detalleFacturaDto.setCodigoProductoSin("83131");
                detalleFacturaDto.setCodigoProducto(objDet.getDeudaClienteId().toString());
                detalleFacturaDto.setDescripcion(objDet.getConcepto());
                detalleFacturaDto.setCantidad(objDet.getCantidad().intValue());
                detalleFacturaDto.setUnidadMedida(58);
                detalleFacturaDto.setPrecioUnitario(objDet.getMontoUnitario().floatValue());
                detalleFacturaDto.setMontoDescuento(0F);
                lstDetalleFacturaDto.add(detalleFacturaDto);
            }
            InputDto inputDto = new InputDto();
            inputDto.setCliente(datosClienteDto);
            inputDto.setActividadEconomica("620200");// hay q definir con Ricardo , de mommento va quemado
            inputDto.setCodigoMetodoPago(1); // efectivo siempre
            inputDto.setDescuentoAdicional(0F);
            inputDto.setCodigoMoneda(1); // 1 Bs
            inputDto.setTipoCambio(1F); //1  Bs
            inputDto.setDetalle(lstDetalleFacturaDto);
            RequestGeneraFacturaDto requestGeneraFacturaDto = new RequestGeneraFacturaDto();
            requestGeneraFacturaDto.setEntidad(datosEntidadDto);
            requestGeneraFacturaDto.setInput(inputDto);
            System.out.println("entrando a generar factura");
            ResponseDto responseDto = generarFacturav2(requestGeneraFacturaDto);
            if (!responseDto.status) {
                System.out.println("Erro al generar factura "+responseDto.message);
                throw new Technicalexception("Se produjo un problema al generar factura");
            }

            // almacena datos de la factura
            FacturaEntity facturaDto = this.getFacturaFromMap((Map) responseDto.result);
            facturaDto.setFechaRegistro(new Date());
            facturaDto.setEstado("ACTIVO");
            FacturaEntity facturaEntity = iFacturaDao.save(facturaDto);
            System.out.println("Almacena Datos de Factura");
            for (TransaccionCobroEntity obj : transaccionesCobroList) {
                obj.setFacturaId(facturaEntity.getFacturaId());
                //obj.setEstado("UPDATE");
                obj.setTransaccion("COBRAR");
                iTransaccionCobroDao.save(obj);
                System.out.println("Transaccion Cobro Modificado con FactraID");
            }

            //Eliminar Deudas Clientes
            iSitioDeudaClienteDao.deleteAll(deudaClienteEntityList);
            System.out.println("Eliminar Deudas Clientes");

            response.put("codigo", "0000");
            response.put("mensaje", "Registro Exitoso");
            return response;

        } catch (Exception ex) {
            //.. guardar en log
            System.out.println("error: "+ex.toString());
            //throw new Technicalexception("Algo salio mal, comuniquese con administrador");
            throw new Technicalexception(ex.toString());
        }
    }
    public ResponseDto generarFacturav2(RequestGeneraFacturaDto requestGeneraFacturaDto) {

        ResponseDto res = new ResponseDto();
        try {
            res = conexionFacElectronicaService.getResponseMethodPost(requestGeneraFacturaDto, "/api/facturacion-electronica/v1/crearFactura");
        } catch (Exception ex) {
            res.status = false;
            res.message = "error al generar factura : " + ex.toString();
            return res;
        }
        return res;

    }

    private FacturaEntity getFacturaFromMap(Map map) {
        FacturaEntity obj = new FacturaEntity();
        obj.setResponseCuf(map.get("cuf") + "");
        obj.setResponseState(map.get("state") + "");
        obj.setResponseNroFactura(map.get("numeroFactura") + "");
        Map ma = (Map) map.get("cliente");
        obj.setResponseCodigoCliente(ma.get("codigoCliente") + "");
        obj.setResponseNumeroDocumentoCliente(ma.get("numeroDocumento") + "");
        obj.setResponseRazonSocialCliente(ma.get("razonSocial") + "");
        obj.setResponseComplementoCliente(ma.get("complemento") + "");
        obj.setResponseEmailCliente(ma.get("email") + "");
        Map rep = (Map) map.get("representacionGrafica");
        obj.setResponsePdfRepgrafica(rep.get("pdf") + "");
        obj.setResponseXmlRepgrafica(rep.get("xml") + "");
        obj.setResponseSinRepgrafica(rep.get("sin") + "");
        obj.setResponseRolloRepgrafica(rep.get("rollo") + "");
        return obj;
    }
}
