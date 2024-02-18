package bo.com.tesla.externo.sitio.service;

import bo.com.tesla.administracion.dao.ISucursalEntidadDao;
import bo.com.tesla.administracion.dao.ITransaccionCobrosDao;
import bo.com.tesla.administracion.entity.*;
import bo.com.tesla.entidades.dao.IArchivoDao;
import bo.com.tesla.pagosv2.dao.ISitioDatosConfirmadoQrDao;
import bo.com.tesla.externo.sitio.dao.ISitioDeudaClienteDao;
import bo.com.tesla.externo.sitio.dto.SitioDatosCobroDto;
import bo.com.tesla.externo.sitio.dto.SitioDeudaClienteCabDto;
import bo.com.tesla.externo.sitio.dto.SitioDeudaClienteDetDto;
import bo.com.tesla.externos.recaudaciones.larazon.services.IEndPointEntidadService;
import bo.com.tesla.facturacion.electronica.services.ConexionFacElectronicaService;
import bo.com.tesla.pagosv2.service.QRService;
import bo.com.tesla.recaudaciones.dao.*;
import bo.com.tesla.recaudaciones.dto.requestGenerarFactura.*;
import bo.com.tesla.recaudaciones.services.IDeudaClienteRService;
import bo.com.tesla.recaudaciones.services.ITransaccionCobroService;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.cross.Util;
import bo.com.tesla.useful.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

@Service
public class SitioDeudaClienteService {

    @Autowired
    private ISitioDeudaClienteDao iSitioDeudaClienteDao;

    @Autowired
    private IEntidadRDao iEntidadRDao;

    @Autowired
    private ISucursalEntidadDao sucursalEntidadDao;

    @Autowired
    private IArchivoDao iArchivoDao;

    @Autowired
    private IDominioDao iDominioDao;

    @Autowired
    private IDeudaClienteRService iDeudaClienteRService;

    @Autowired
    private IHistoricoDeudaDao iHistoricoDeudaDao;

    @Autowired
    private IRecaudadorDao iRecaudadorDao;




    @Autowired
    private ICobroClienteDao iCobroClienteDao;

    @Autowired
    private IEndPointEntidadService endPointEntidadService;

    @Autowired
    private ITransaccionCobroDao iTransaccionCobroDao;

    @Value("${tesla.path.files-report}")
    private String pathReport;

    @Autowired
    private IFacturaDao iFacturaDao;

    @Autowired
    private QRService qrService;

    @Autowired
    private ISitioDatosConfirmadoQrDao iSitioDatosConfirmadoQrDao;

    public ResponseDto clienteByEntidadIdAndCodigoCliente(Long pEntidadId, String pCodCliente) {
        ResponseDto res = new ResponseDto();
        try {
            List<SitioDeudaClienteCabDto> lstSitioClienetDto = iSitioDeudaClienteDao.findByEntidadAndCodigoCliente(pEntidadId, pCodCliente);
            if (lstSitioClienetDto.size() == 1) {
                res.status = true;
                res.result = lstSitioClienetDto.get(0);
            } else {
                res.status = false;
                res.message = "Datos Clientes no encontrado";
            }

            return res;
        } catch (Exception ex) {
            res.status = false;
            res.message = ex.toString();
            return res;
        }
    }

    public ResponseDto deudaClienteByEntidadIdAndCodigoCliente(Long pEntidadId, String pCodCliente) {
        ResponseDto res = new ResponseDto();
        try {
            List<SitioDeudaClienteDetDto> lstSitioClienetDto = iSitioDeudaClienteDao.groupByDeudasDetClientes(pEntidadId, pCodCliente);

            res.status = true;
            res.result = lstSitioClienetDto;


            return res;
        } catch (Exception ex) {
            res.status = false;
            res.message = ex.toString();
            return res;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Technicalexception.class)
    public ResponseDto generaFactura(String alias) {
        ResponseDto responseDto = new ResponseDto();
        try {

            ResponseDto res =  qrService.estadoTransaccionQr(alias);
            if(!res.status)
                return res;
            Optional<DatosConfirmadoQrEntity> datosConfirmadoQrEntity = iSitioDatosConfirmadoQrDao.findByAlias(alias);
            List<TransaccionCobroEntity> objCobro =  iTransaccionCobroDao.findByDatosConfirmadoQrId(datosConfirmadoQrEntity.get().getDatosconfirmadoQrId());
            if(objCobro.isEmpty()){
                responseDto.status=false;
                responseDto.message="No se ha realizado el pago";
                return responseDto;
            }
            FacturaEntity facturaEntity =  iFacturaDao.findById(objCobro.get(0).getFacturaId()).get();
            String file = Util.downloadFile(new URL(facturaEntity.getResponsePdfRepgrafica()), pathReport+"/factura.pdf");
            responseDto.status = true;
            responseDto.result = file;
            return responseDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Technicalexception(e.getMessage(), e.getCause());
        }

    }


}
