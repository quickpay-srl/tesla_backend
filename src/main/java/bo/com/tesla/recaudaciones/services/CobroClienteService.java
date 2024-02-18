package bo.com.tesla.recaudaciones.services;

import bo.com.tesla.administracion.dao.ISucursalEntidadDao;
import bo.com.tesla.administracion.entity.*;
import bo.com.tesla.administracion.services.IEntidadComisionService;
import bo.com.tesla.administracion.services.IRecaudadorComisionService;
import bo.com.tesla.entidades.dao.IArchivoDao;
import bo.com.tesla.externos.recaudaciones.larazon.services.IEndPointEntidadService;
import bo.com.tesla.facturacion.electronica.services.ConexionFacElectronicaService;
import bo.com.tesla.facturaciones.computarizada.services.IFacturaComputarizadaService;
import bo.com.tesla.recaudaciones.dao.*;
import bo.com.tesla.recaudaciones.dto.*;
import bo.com.tesla.recaudaciones.dto.requestGenerarFactura.*;
import bo.com.tesla.useful.config.Technicalexception;

import bo.com.tesla.useful.cross.Util;
import bo.com.tesla.useful.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;


import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

@Service
public class CobroClienteService implements ICobroClienteService {

    @Autowired
    private ICobroClienteDao iCobroClienteDao;

    @Autowired
    private IDeudaClienteRService iDeudaClienteRService;

    @Autowired
    private ITransaccionCobroService iTransaccionCobroService;

    @Autowired
    private IDominioDao iDominioDao;

    @Autowired
    private IHistoricoDeudaService iHistoricoDeudaService;

    @Autowired
    private IEntidadRDao iEntidadRDao;

    @Autowired
    private IHistoricoDeudaDao iHistoricoDeudaDao;

    @Autowired
    private IRecaudadorDao iRecaudadorDao;

    @Autowired
    private IEntidadComisionService iEntidadComisionService;

    @Autowired
    private IRecaudadorComisionService iRecaudadorComisionService;

    @Autowired
    private IArchivoDao iArchivoDao;

    @Autowired
    private IFacturaComputarizadaService facturacionComputarizadaService;

    @Autowired
    private ITransaccionCobroDao transaccionCobroDao;

    @Autowired
    private ISucursalEntidadDao sucursalEntidadDao;
    
    @Autowired
    private IEndPointEntidadService endPointEntidadService;

    @Autowired
    IFacturaDao iFacturaDao;

    @Autowired
    private ConexionFacElectronicaService conexionFacElectronicaService;

    @Override
    public List<CobroClienteEntity> saveAllCobrosClientes(List<CobroClienteEntity> cobroClienteEntities) {
        return this.iCobroClienteDao.saveAll(cobroClienteEntities);
    }

    @Override
    public CobroClienteEntity loadCobroClienteEntity(DeudaClienteEntity deudaClienteEntity,
                                                     List<DeudaClienteDto> deudaClienteDtos,
                                                     Long usuarioId,
                                                     Long metodoPagoId,
                                                     TransaccionCobroEntity transaccionCobroEntity) {
    	
    	
        Optional<HistoricoDeudaEntity> historicoDeudaEntityOptional = iHistoricoDeudaDao.findByDeudaClienteId(deudaClienteEntity.getDeudaClienteId());
        if(!historicoDeudaEntityOptional.isPresent()) {
            throw new Technicalexception("No existe el registro historico de deudaClienteId=" + deudaClienteEntity.getDeudaClienteId());
        }

        CobroClienteEntity cobroClienteEntity = new CobroClienteEntity();
        cobroClienteEntity.setArchivoId(deudaClienteEntity.getArchivoId());
        cobroClienteEntity.setNroRegistro(deudaClienteEntity.getNroRegistro());
        cobroClienteEntity.setCodigoCliente(deudaClienteEntity.getCodigoCliente());
        cobroClienteEntity.setNombreCliente(deudaClienteEntity.getNombreCliente());
        cobroClienteEntity.setNroDocumento(deudaClienteEntity.getNroDocumento());
        cobroClienteEntity.setTipoServicio(deudaClienteEntity.getTipoServicio());
        cobroClienteEntity.setServicio(deudaClienteEntity.getServicio());
        cobroClienteEntity.setPeriodo(deudaClienteEntity.getPeriodo());
        cobroClienteEntity.setTipo(deudaClienteEntity.getTipo());
        cobroClienteEntity.setCantidad(deudaClienteEntity.getCantidad());
        cobroClienteEntity.setConcepto(deudaClienteEntity.getConcepto());
        cobroClienteEntity.setDatoExtra(deudaClienteEntity.getDatoExtras());
        cobroClienteEntity.setTipoComprobante(deudaClienteEntity.getTipoComprobante());
        cobroClienteEntity.setPeriodoCabecera(deudaClienteEntity.getPeriodoCabecera());
        cobroClienteEntity.setNit(deudaClienteEntity.getNit());
        cobroClienteEntity.setDireccion(deudaClienteEntity.getDireccion());
        cobroClienteEntity.setTelefono(deudaClienteEntity.getTelefono());
        cobroClienteEntity.setEsPostpago(deudaClienteEntity.getEsPostpago());
        cobroClienteEntity.setUsuarioCreacion(usuarioId);
        cobroClienteEntity.setFechaCreacion(new Date());
        cobroClienteEntity.setTransaccion("COBRAR");
        cobroClienteEntity.setTransaccionCobro(transaccionCobroEntity);
        cobroClienteEntity.setHistoricoDeuda(historicoDeudaEntityOptional.get());

        //control de MontoUnitarios y Subtotales por prepago
        if(deudaClienteEntity.getTipo() == 'D' && !deudaClienteEntity.getEsPostpago() && deudaClienteEntity.getSubTotal().compareTo(BigDecimal.ZERO) == 0) {
            DeudaClienteDto deudaClienteDto = deudaClienteDtos.stream().filter(d -> d.getDeudaClienteId().equals(deudaClienteEntity.getDeudaClienteId())).findAny().orElse(null);
            if(deudaClienteDto == null) {
                throw new Technicalexception("No se ha logrado encontrar la deuda enviada, deudaClienteId=" + deudaClienteEntity.getDeudaClienteId());
            }
            cobroClienteEntity.setMontoUnitario(deudaClienteDto.getMontoUnitario());
            cobroClienteEntity.setSubTotal(deudaClienteDto.getSubTotal());
            cobroClienteEntity.setMontoModificado(true);

        } else {
            cobroClienteEntity.setMontoUnitario(deudaClienteEntity.getMontoUnitario());
            cobroClienteEntity.setSubTotal(deudaClienteEntity.getSubTotal());
            cobroClienteEntity.setMontoModificado(false);
        }

        return  cobroClienteEntity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Technicalexception.class)
    public ResponseDto postCobrarDeudas(ClienteDto clienteDto,
                                    Long usuarioId,
                                    Long dimensionId) {
        List<TransaccionCobroEntity> transaccionesCobroList = new ArrayList<>();
        try{
            //Obtencion Datos Entidad
            Optional<EntidadEntity> entidadEntityOptional = iEntidadRDao.findByEntidadIdAndEstado(clienteDto.getEntidadId(), "ACTIVO");
            if(!entidadEntityOptional.isPresent()) {
                throw new Technicalexception("No existe Entidad, por tanto no hay configuracion de comprobanteEnUno");
            }

            //Encontrar Sucursal que emite Factura
            Optional<SucursalEntidadEntity> sucursalEntidadEntityOptional  = sucursalEntidadDao.findByEmiteFacturaTesla(clienteDto.getEntidadId());
            if(!sucursalEntidadEntityOptional.isPresent()) {
                throw new Technicalexception("No se encuentra registrada la sucursal que emitirá la(s) factura(s).");
            }

            //Obtencion de Archivo
            ArchivoEntity archivoEntity = iArchivoDao.findByEstado("ACTIVO", clienteDto.getEntidadId());
            if(archivoEntity == null) {
                throw new Technicalexception("No existe un archivo activo o no se encontra el archivoId" + entidadEntityOptional.get().getEntidadId());
            }

            //Obtencon Datos de Recaudador
            Optional<RecaudadorEntity> recaudadorEntityOptional = iRecaudadorDao.findRecaudadorByUserId(usuarioId);
            if(!recaudadorEntityOptional.isPresent()) {
                throw new Technicalexception("El usuarioId=" + usuarioId + " no esta registrado en ninguna sucursal de recaudadción");
            }

            /*************************************************
            14/05/2021 - Segun reunión.
            Se comentas hasta incluir la comision de acuerdo a requerimiento
            **************************************************
            //Obtencion de comisión entidad
            EntidadComisionEntity entidadComisionEntity = iEntidadComisionService.getEntidadComisionActual(entidadEntityOptional.get());

            //Obtencion de comisión recaudadora
            RecaudadorComisionEntity recaudadorComisionEntity = iRecaudadorComisionService.getRecaudadorComisionActual(recaudadorEntityOptional.get());
            *************************************************/

            //Verificar el dominio metodoCobro
            Optional<DominioEntity> metodoCobroOptional = iDominioDao.getDominioEntityByDominioIdAndDominioAndEstado(clienteDto.getMetodoCobroId(), "metodo_cobro_id", "ACTIVO");
            if(!metodoCobroOptional.isPresent()) {
                throw new Technicalexception("No existe el dominio='metodo_cobro_id' para dominioId=" + clienteDto.getMetodoCobroId() );
            }

            //Para emision de comprobante
            //boolean comprobanteEnUno = entidadEntityOptional.get().getComprobanteEnUno();
            List<DeudaClienteEntity> deudaClienteEntityList =  new ArrayList<>();
            //Recorrer las agrupaciones
            for(ServicioDeudaDto servicioDeudaDto : clienteDto.getServicioDeudaDtoList()){
                //Recuperar Deudas Completas por agrupacion
                deudaClienteEntityList = iDeudaClienteRService.getAllDeudasByCliente(clienteDto.getEntidadId(),
                        servicioDeudaDto.getTipoServicio(),
                        servicioDeudaDto.getServicio(),
                        servicioDeudaDto.getPeriodo(),
                        clienteDto.getCodigoCliente());
                if(deudaClienteEntityList.isEmpty()) {
                    throw new Technicalexception("No se ha encontrado el Listado de Todas las Deudas del cliente: " + clienteDto.getCodigoCliente());

                }


                //Verificar un solo Tipo de Comprobante por Transacción, en este caso solo Facturas.
                List<Boolean> tipoComprobanteLst = iDeudaClienteRService.getTipoComprobanteUnicos(deudaClienteEntityList);
                if(tipoComprobanteLst.size() > 1) {
                    throw new Technicalexception("Se ha identificado en el cargado de deudas, diferentes Tipos de Comprobante para la agrupacipon: " +
                             " ArchivoId=" + archivoEntity.getArchivoId() +
                            ", TipoServicio=" + servicioDeudaDto.getTipoServicio() +
                            ", Servicio= " + servicioDeudaDto.getServicio() +
                            ", Periodo=" + servicioDeudaDto.getPeriodo() +
                            ", Codigo   Cliente=" + clienteDto.getCodigoCliente());
                }
                if(tipoComprobanteLst.contains(false)) {
                    throw new Technicalexception("Se ha detectado un tipo de comprobante Recibo en alguna de las deudas.");
                }

                //Verificar un solo Código Actividad Económica por Transacción,
                //caso nulo sigue adelante(puede ser para recibo)
                List<String> codActEcoList = iDeudaClienteRService.getCodigosActividadUnicos(deudaClienteEntityList);
                if(codActEcoList.size() > 1) {
                    throw new Technicalexception("Se ha identificado en el cargado de deudas, diferentes Códigos de Actividad Económica para la agrupacipon: " +
                            " ArchivoId=" + archivoEntity.getArchivoId() +
                            ", TipoServicio=" + servicioDeudaDto.getTipoServicio() +
                            ", Servicio= " + servicioDeudaDto.getServicio() +
                            ", Periodo=" + servicioDeudaDto.getPeriodo() +
                            ", CodigoCliente=" + clienteDto.getCodigoCliente());
                }

                /*************************************************
                14/05/2021 - Segun reunión.
                Se comentas hasta incluir la comision de acuerdo a requerimiento
                **************************************************
                //Cargar transaccion las agrupaciones
                TransaccionCobroEntity transaccionCobroEntity = iTransaccionCobroService.loadTransaccionCobro(servicioDeudaDto, entidadEntityOptional.get(), usuarioId, clienteDto.nombreCliente, clienteDto.nroDocumento,
                        entidadComisionEntity, recaudadorEntityOptional.get(), recaudadorComisionEntity, archivoEntity, metodoCobroOptional.get(),
                        entidadEntityOptional.get().getModalidadFacturacion(),
                        deudaClienteEntityList.get(0).getCodigoActividadEconomica());//Habiendo validado unico por transaccion
                **************************************************/
                //Cargar transaccion las agrupaciones (prepara transaccion cobro)
                TransaccionCobroEntity transaccionCobroEntity = iTransaccionCobroService.loadTransaccionCobro(servicioDeudaDto, entidadEntityOptional.get(), usuarioId, clienteDto.getNombreCliente(), clienteDto.getNroDocumento(),
                        null, recaudadorEntityOptional.get(), null, archivoEntity, metodoCobroOptional.get(),
                        entidadEntityOptional.get().getModalidadFacturacion(),
                        deudaClienteEntityList.get(0).getCodigoActividadEconomica());//Habiendo validado unico por transaccion


                List<CobroClienteEntity> cobroClienteEntityList = new ArrayList<>();
                //Recorrer cada deuda asociada a la agrupacion (prepara DeudaCliente)
                for(DeudaClienteEntity deudaClienteEntity : deudaClienteEntityList) {
                    //Cargando Cobro
                    CobroClienteEntity cobroClienteEntity = loadCobroClienteEntity(deudaClienteEntity, servicioDeudaDto.getDeudaClienteDtos(), usuarioId, clienteDto.getMetodoCobroId(), transaccionCobroEntity);
                    cobroClienteEntity.setEstado("COBRADO"); // en postgre de nuve obliga
                    cobroClienteEntityList.add(cobroClienteEntity);

                }
                //Registrar transaccion  por agrupacion y complementar
                transaccionCobroEntity.setCodigoCliente(deudaClienteEntityList.get(0).getCodigoCliente());
                transaccionCobroEntity.setNombreClienteArchivo(deudaClienteEntityList.get(0).getNombreCliente());
                transaccionCobroEntity.setNroDocumentoClienteArchivo(deudaClienteEntityList.get(0).getNroDocumento());
                transaccionCobroEntity.setCorreoCliente(clienteDto.getCorreoCliente());
                transaccionCobroEntity.setCobroClienteEntityList(cobroClienteEntityList);
                //transaccionCobroEntity.setEstado("CREADO");
                transaccionCobroEntity.setTransaccion("CREAR");
                transaccionCobroEntity = iTransaccionCobroService.saveTransaccionCobro(transaccionCobroEntity);

                if(transaccionCobroEntity.getTransaccionCobroId() == null) {
                    throw new Technicalexception("No se ha registrado la transacción");
                }
                transaccionesCobroList.add(transaccionCobroEntity);

                //Actualizar Historico por agrupacion
                Integer countUpdate = iHistoricoDeudaService.updateHistoricoDeudaLst(deudaClienteEntityList);
                if(countUpdate != deudaClienteEntityList.size()) {
                    throw new Technicalexception("Se produjo un problema al actualizar Historico de la lista de DeudasClientes");
                }
                //Eliminar Deudas por agrupacion
                Long recordDeletes = iDeudaClienteRService.deleteDeudasClientes(deudaClienteEntityList);
                if (recordDeletes != deudaClienteEntityList.size()) {
                    throw new Technicalexception("Se produjo un problema al borrar la lista de DeudasClientes");
                }
            }

            /******************************************************************************************/
            //Lamar a Facturacion
            /******************************************************************************************/

            DatosEntidadDto datosEntidadDto = new DatosEntidadDto();
            datosEntidadDto.setEntidadId(clienteDto.getEntidadId());
            datosEntidadDto.setCodigoPuntoVenta(0);
            datosEntidadDto.setCodigoSucursal(0);

            DatosClienteDto datosClienteDto = new DatosClienteDto();
            datosClienteDto.setCodigoCliente(clienteDto.getCodigoCliente());
            datosClienteDto.setNumeroDocumento(clienteDto.getNroDocumento());
            datosClienteDto.setRazonSocial(clienteDto.getNombreCliente());
            datosClienteDto.setEmail(clienteDto.getCorreoCliente());
            //datosClienteDto.setEmail("alvaro.quispe@quickpay.com.bo");

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

            ResponseDto responseDto = generarFacturav2(requestGeneraFacturaDto);
            if (!responseDto.status) {
                System.out.println("error: "+responseDto.message);
                throw new Technicalexception("Se produjo un problema al generar factura");
            }
            //*****************************************************************************************

            endPointEntidadService.updateCobroEnEntidad(clienteDto.getEntidadId(), transaccionesCobroList);

            FacturaEntity facturaDto =  this.getFacturaFromMap((Map) responseDto.result);
            facturaDto.setFechaRegistro(new Date());
            facturaDto.setEstado("ACTIVO");
            FacturaEntity facturaEntity =  iFacturaDao.save(facturaDto);
            for ( TransaccionCobroEntity obj  :transaccionesCobroList) {
                obj.setFacturaId(facturaEntity.getFacturaId());
                obj.setTransaccion("COBRAR");
                //obj.setEstado("UPDATE");
                iTransaccionCobroService.saveTransaccionCobro(obj);
            }





            String file = Util.downloadFile(new URL(facturaDto.getResponsePdfRepgrafica()),"I:/opt/aplicaciones/exacta_qa/report/factura.pdf");
            responseDto.result = file;
            return responseDto;
        } catch (Exception e) {
        	e.printStackTrace();
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }
    public ResponseDto generarFactura(EntidadEntity entidadEntity,
                                      SucursalEntidadEntity sucursalEntidadEntity,
                                      List<TransaccionCobroEntity> transaccionCobroEntityList,
                                      boolean comprobanteEnUno,
                                      BigDecimal montoTotalCobrado,
                                      Long dimensionId) {

        //Controlar la parametrización de las Modalidades de Facturacion
        Optional<Long> modFactCompuOptional = iDominioDao.getDominioIdByDominioAndAbreviatura("modalidad_facturacion_id", "FEEL");
        if(!modFactCompuOptional.isPresent()) {
            throw new Technicalexception("No existe el dominio='modalidad_facturacion_id, abreviatura='FEEL' para la facturación computarizada");
        }
        /******************************************************
         Habilitar cuando se incluya demás modalidades
         *******************************************************
         Optional<Long> modFactCompuELOptional = iDominioDao.getDominioIdByDominioAndAbreviatura("modalidad_facturacion_id", "FCEL");
         if(!modFactCompuOptional.isPresent()) {
         throw new Technicalexception("No existe el dominio='modalidad_facturacion_id, abreviatura='FCEL' para la facturación computarizada en línea");
         }

         Optional<Long> modFactElectOptional = iDominioDao.getDominioIdByDominioAndAbreviatura("modalidad_facturacion_id", "FEEL");
         if(!modFactCompuOptional.isPresent()) {
         throw new Technicalexception("No existe el dominio='modalidad_facturacion_id, abreviatura='FEEL' para la facturación electrónica en línea");
         }
         *******************************************************/

        //Facturación computarizada
        if(entidadEntity.getModalidadFacturacion().getDominioId() == modFactCompuOptional.get()) {


            ResponseDto responseDto = facturacionComputarizadaService.postFacturas(sucursalEntidadEntity, transaccionCobroEntityList, comprobanteEnUno, montoTotalCobrado, dimensionId);
            if (!responseDto.status) {
                throw new Technicalexception(responseDto.message);
            }
            iTransaccionCobroService.updateFacturasTransaccion((List<Map<Long, List<Long>>>) responseDto.result);


            return responseDto;
        } else { //Otra Modalidad
            throw new Technicalexception("Solo esta habilitada la Modalidad de Facturación Computarizada, verifique configuración en la tabla Entidad.");
        }
    }
    public ResponseDto generarFacturav2(RequestGeneraFacturaDto requestGeneraFacturaDto) {

        ResponseDto res = new ResponseDto();
            try{
                res = conexionFacElectronicaService.getResponseMethodPost(requestGeneraFacturaDto, "/api/facturacion-electronica/v1/crearFactura");
            }catch (Exception ex){
                res.status = false;
                res.message = "error al generar factura : "+ex.toString();
                return res;
            }
            return res;

    }

    private FacturaEntity getFacturaFromMap (Map map){
        FacturaEntity obj = new FacturaEntity();
        obj.setResponseCuf(map.get("cuf")+"");
        obj.setResponseState(map.get("state")+"");
        obj.setResponseNroFactura(map.get("numeroFactura")+"");
        Map ma= (Map) map.get("cliente");
        obj.setResponseCodigoCliente(ma.get("codigoCliente")+"");
        obj.setResponseNumeroDocumentoCliente(ma.get("numeroDocumento")+"");
        obj.setResponseRazonSocialCliente(ma.get("razonSocial")+"");
        obj.setResponseComplementoCliente(ma.get("complemento")+"");
        obj.setResponseEmailCliente(ma.get("email")+"");
        Map rep= (Map) map.get("representacionGrafica");
        obj.setResponsePdfRepgrafica(rep.get("pdf")+"");
        obj.setResponseXmlRepgrafica(rep.get("xml")+"");
        obj.setResponseSinRepgrafica(rep.get("sin")+"");
        obj.setResponseRolloRepgrafica(rep.get("rollo")+"");
        return obj;
    }

}
