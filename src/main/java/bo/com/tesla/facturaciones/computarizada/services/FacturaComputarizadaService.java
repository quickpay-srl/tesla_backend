package bo.com.tesla.facturaciones.computarizada.services;

import bo.com.tesla.administracion.entity.CobroClienteEntity;
import bo.com.tesla.administracion.entity.SucursalEntidadEntity;

import bo.com.tesla.administracion.entity.TransaccionCobroEntity;
import bo.com.tesla.facturaciones.computarizada.dto.*;
import bo.com.tesla.recaudaciones.dao.IDominioDao;
import bo.com.tesla.recaudaciones.dao.ITransaccionCobroDao;
import bo.com.tesla.recaudaciones.services.ITransaccionCobroService;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.dto.ResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacturaComputarizadaService implements IFacturaComputarizadaService {

    @Value("${host.facturacion.electronica}")
    private String host;

    @Autowired
    private IConexionService conexionService;

    @Autowired
    private ITransaccionCobroService transaccionCobroService;
    
    @Autowired
    private ITransaccionCobroDao transaccionCobroDao;

    @Autowired
    private IDominioDao dominioDao;

    @Override
    public ResponseDto postCodigoControl(CodigoControlDto codigoControlDto, Long entidadId)  {
        try {
            String url = this.host + "/api/facturas/codigoscontroles";
            return conexionService.getResponseMethodPost(entidadId,codigoControlDto, url);

        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(),e.getCause());
        }

    }

    private List<FacturaDto> loadFacturasPorTransaccion(List<TransaccionCobroEntity> transaccionCobroEntityList, Long dimensionId) {
        //Cargar Facturas
        List<FacturaDto> facturaDtoList = new ArrayList<>();
        for(TransaccionCobroEntity transaccionCobroEntity : transaccionCobroEntityList) {
            FacturaDto facturaDto = new FacturaDto();
            facturaDto.montoTotal = transaccionCobroEntity.getTotalDeuda();
            facturaDto.montoBaseImporteFiscal = transaccionCobroEntity.getTotalDeuda();
            facturaDto.codigoCliente = transaccionCobroEntity.getCodigoCliente();
            facturaDto.nombreRazonSocial = transaccionCobroEntity.getNombreClientePago();
            facturaDto.numeroDocumento = transaccionCobroEntity.getNroDocumentoClientePago();
            facturaDto.montoDescuento = new BigDecimal(0);
            List<Long> transaccionIdLst = new ArrayList<>();
            transaccionIdLst.add(transaccionCobroEntity.getTransaccionCobroId());
            facturaDto.transaccionIdLst = transaccionIdLst;
            facturaDto.codigoActividadEconomica = transaccionCobroEntity.getCodigoActividadEconomica();
            facturaDto.dimensionId = dimensionId;
            facturaDto.correoCliente =  transaccionCobroEntity.getCorreoCliente();
            //Cargar DetalleFacturas
            facturaDto.detalleFacturaDtoList = loadDetallesFacturas(transaccionCobroEntity);

            facturaDtoList.add(facturaDto);
        }

        return facturaDtoList;
    }

    private List<FacturaDto> loadFacturaGlobalXActividad(List<TransaccionCobroEntity> transaccionCobroEntityList, Long dimensionId) {

        List<String> codActEcoListUnique = transaccionCobroService.getCodigosActividadUnicos(transaccionCobroEntityList);

        List<FacturaDto> facturaDtoList = new ArrayList<>();
        for(String codActEconomica : codActEcoListUnique) {
            if(codActEconomica == null) {
                throw new Technicalexception("Uno o varias deudas fueron cargadas sin Código de Actividad Económica, siendo obligatorio para Facturacion.");
            }

            List<TransaccionCobroEntity> transaccionesPorCodActEconomicaLst = transaccionCobroEntityList.stream()
                    .filter(t -> t.getCodigoActividadEconomica().equals(codActEconomica)).collect(Collectors.toList());

            BigDecimal montoTotPorActEco = transaccionesPorCodActEconomicaLst.stream()
                    .map(TransaccionCobroEntity::getTotalDeuda)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            List<FacturaDto> facturaDtoLstPorCodActEco = loadFacturaGlobal(transaccionesPorCodActEconomicaLst, montoTotPorActEco, dimensionId);
            facturaDtoList.addAll(facturaDtoLstPorCodActEco);
        }
        return facturaDtoList;
    }

    private List<FacturaDto> loadFacturaGlobal(List<TransaccionCobroEntity> transaccionCobroEntityList, BigDecimal montoTotalCobrado, Long dimensionId) {

        FacturaDto facturaDto = new FacturaDto();
        facturaDto.montoTotal = montoTotalCobrado;
        facturaDto.montoBaseImporteFiscal = montoTotalCobrado;
        facturaDto.codigoCliente = transaccionCobroEntityList.get(0).getCodigoCliente();
        facturaDto.nombreRazonSocial = transaccionCobroEntityList.get(0).getNombreClientePago();
        facturaDto.numeroDocumento = transaccionCobroEntityList.get(0).getNroDocumentoClientePago();
        facturaDto.montoDescuento = new BigDecimal(0);
        facturaDto.codigoActividadEconomica = transaccionCobroEntityList.get(0).getCodigoActividadEconomica();
        List<Long> transaccionIdList = new ArrayList<>();
        //Cargar Detalle deFactura
        List<DetalleFacturaDto> detalleFacturaDtoList = new ArrayList<>();
        for(TransaccionCobroEntity transaccionCobroEntity : transaccionCobroEntityList) {
            detalleFacturaDtoList.addAll(loadDetallesFacturas(transaccionCobroEntity));
            transaccionIdList.add(transaccionCobroEntity.getTransaccionCobroId());
        }
        facturaDto.detalleFacturaDtoList = detalleFacturaDtoList;
        facturaDto.transaccionIdLst = transaccionIdList;
        facturaDto.dimensionId = dimensionId;
        facturaDto.correoCliente = transaccionCobroEntityList.get(0).getCorreoCliente();
        //Cargar Unica Factura
        List<FacturaDto> facturaDtoList = new ArrayList<>();
        facturaDtoList.add(facturaDto);
        return facturaDtoList;
    }

    private List<DetalleFacturaDto> loadDetallesFacturas(TransaccionCobroEntity transaccionCobroEntity) {
        List<DetalleFacturaDto> detalleFacturaDtoList = new ArrayList<>();

        List<CobroClienteEntity> cobroClienteEntityList = transaccionCobroEntity.getCobroClienteEntityList()
                .stream().filter(c -> c.getTipo().equals('D'))
                .collect(Collectors.toList());

        for(CobroClienteEntity cobroClienteEntity : cobroClienteEntityList) {
            DetalleFacturaDto detalleFacturaDto = new DetalleFacturaDto();
            detalleFacturaDto.tipoServicio = cobroClienteEntity.getTipoServicio();
            detalleFacturaDto.servicio = cobroClienteEntity.getServicio();
            detalleFacturaDto.periodo = cobroClienteEntity.getPeriodo();
            //detalleFacturaDto.codigo = null;
            //detalleFacturaDto.unidad = null;
            detalleFacturaDto.cantidad = cobroClienteEntity.getCantidad();
            detalleFacturaDto.concepto = cobroClienteEntity.getConcepto();
            detalleFacturaDto.montoUnitario = cobroClienteEntity.getMontoUnitario();
            detalleFacturaDto.montoSubtotal = cobroClienteEntity.getSubTotal();

            detalleFacturaDtoList.add(detalleFacturaDto);
        }
        return detalleFacturaDtoList;
    }

    @Override
    public ResponseDto postFacturas(SucursalEntidadEntity sucursalEntidadEntity, List<TransaccionCobroEntity> transaccionCobroEntityList, Boolean comprobanteEnUno, BigDecimal montoTotal, Long dimensionId) {
        try {

            FacturasLstDto facturasLstDto = new FacturasLstDto();

            //Cargar Facturas
            List<FacturaDto> facturaDtoList = comprobanteEnUno ? loadFacturaGlobalXActividad(transaccionCobroEntityList, dimensionId) : loadFacturasPorTransaccion(transaccionCobroEntityList, dimensionId);
            facturasLstDto.facturaDtoList = facturaDtoList;
            facturasLstDto.montoTotalCobrado = montoTotal;

            //api factura
            String url = this.host + "/api/facturas/listas";
            ResponseDto responseDto = conexionService.getResponseMethodPost(transaccionCobroEntityList.get(0).getEntidadId().getEntidadId(), facturasLstDto, url);

            TypeReference<List<Map<Long, List<Long>>>> mapType = new TypeReference<List<Map<Long, List<Long>>>>() {};
            ObjectMapper mapper = new ObjectMapper();
            List<Map<Long, List<Long>>> facturaTransacLst = mapper.convertValue(responseDto.result, mapType);

            responseDto.result = facturaTransacLst;

            return responseDto;
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(),e.getCause());
        }
    }

    @Override
    public ResponseDto postFacturaLstFilter(Long entidadId, int page, FacturaFilterDto facturaFilterDto, Long recaudadoraId) {
        try {
            List<Long> facturaIdLst = findFacturasByEntidadAndRecaudador(entidadId, recaudadoraId);
            facturaFilterDto.facturaIdLst = facturaIdLst;
            String url = this.host + "/api/facturas/filters/pages";

            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url).
                    queryParam("page", page - 1).
                    queryParam("size", 10);

            return conexionService.getResponseMethodPostParameter(entidadId, facturaFilterDto, uriComponentsBuilder);
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }

    @Override
    public ResponseDto getFacturaReport(Long entidadId, Long recaudadorId, Long facturaId) throws BusinesException {
        Optional<Long> modFactCompuOptional = dominioDao.getDominioIdByDominioAndAbreviatura("modalidad_facturacion_id", "FC");
        if(!modFactCompuOptional.isPresent()) {
            throw new Technicalexception("No existe el dominio='modalidad_facturacion_id, abreviatura='FC' para la facturación computarizada");
        }
        List<TransaccionCobroEntity> transaccionCobroEntityList = transaccionCobroService.findByEntAndRecAndFac(entidadId, recaudadorId, modFactCompuOptional.get(), facturaId);
        if(transaccionCobroEntityList.isEmpty()) {
            throw new BusinesException("No cuenta con los privilegios necesarios para ver la factura");
        }
        try {
            String url = this.host + "/api/facturas/reportes/" + facturaId;
            return conexionService.getResponseMethodGet(entidadId, url);
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(),e.getCause());
        }
    }

    @Override
    public ResponseDto getLibroVentasReport(Long entidadId, FacturaFilterDto facturaFilterDto) {
        try {
            List<Long> facturaIdLst = findFacturasByEntidadAndRecaudador(entidadId, null);
            facturaFilterDto.facturaIdLst = facturaIdLst;
            String url = this.host + "/api/facturas/librosventas";
            return conexionService.getResponseMethodPost(entidadId, facturaFilterDto, url);

        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(),e.getCause());
        }
    }

    @Override
    public ResponseDto getFacturaDto(Long entidadId, Long facturaId) {
        try {
            String url = this.host + "/api/facturas/" + facturaId;
            ResponseDto responseDto = conexionService.getResponseMethodGet(entidadId, url);

            TypeReference<FacturaDto> mapType = new TypeReference<FacturaDto>() {};
            ObjectMapper mapper = new ObjectMapper();
            FacturaDto facturaDto = mapper.convertValue(responseDto.result, mapType);
            responseDto.result = facturaDto;

            return responseDto;
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(),e.getCause());
        }
    }

    @Override
    public List<Long> findFacturasByEntidadAndRecaudador(Long entidadId, Long recaudadorId) {
        Optional<Long> modFactCompuOptional = dominioDao.getDominioIdByDominioAndAbreviatura("modalidad_facturacion_id", "FC");
        /*if(!modFactCompuOptional.isPresent()) {
            throw new Technicalexception("No existe el dominio='modalidad_facturacion_id, abreviatura='FC' para la facturación computarizada");
        }*/
        return transaccionCobroService.findFacturasByModalidadAndEntidadAndRecaudador(modFactCompuOptional.get(), entidadId, recaudadorId);
    }

}