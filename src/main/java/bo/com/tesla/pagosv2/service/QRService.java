package bo.com.tesla.pagosv2.service;

import bo.com.tesla.administracion.entity.DatosConfirmadoQrEntity;
import bo.com.tesla.administracion.entity.QRGeneradoEntity;
import bo.com.tesla.administracion.entity.TransaccionCobroEntity;
import bo.com.tesla.pagosv2.dao.ISitioDatosConfirmadoQrDao;
import bo.com.tesla.pagosv2.dao.ISitioQrGeneradoDao;
import bo.com.tesla.pagosv2.dto.RequestGeneraQrDto;
import bo.com.tesla.recaudaciones.dao.ITransaccionCobroDao;
import bo.com.tesla.useful.dto.ResponseDto;
import bo.com.tesla.useful.utils.FuncionesFechas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QRService {

    @Autowired
    private ConexionGeneraQRService conexionGeneraQRService;

    @Autowired
    private ISitioQrGeneradoDao iSitioQrGeneradoDao;

    @Autowired
    private ISitioDatosConfirmadoQrDao iSitioDatosConfirmadoQrDao;
    @Autowired
    private ITransaccionCobroDao iTransaccionCobroDao;
    public ResponseDto generarQr(RequestGeneraQrDto request) {
        ResponseDto res = new ResponseDto();
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("alias", UUID.randomUUID());
            body.put("callback", "https://quickpay.com.bo:9080/sip/endpoint/confirmaPago");
            body.put("detalleGlosa", request.getDetalleGlosa());
            body.put("monto", request.getMonto());
            body.put("moneda", request.getMoneda());
            body.put("fechaVencimiento", FuncionesFechas.fechaVecimientoQr());
            body.put("tipoSolicitud", "API");
            body.put("unicoUso", "true");

            // verificar que los items no este dentro de los ya pagados
            for (Long deudaClienteId : request.getLstDeudaCliente()) {
                Optional<TransaccionCobroEntity> obj =  iTransaccionCobroDao.findByDeudaClienteId(deudaClienteId);
                if(obj.isPresent()){
                    res.status = false;
                    res.message = "Algunos Items ya fueron Pagados";
                    return res;
                }
            }


            res = conexionGeneraQRService.generaQr(body);
            if (res.status) {
                for (Long deudaClienteId : request.getLstDeudaCliente()) {
                    QRGeneradoEntity insertGeneraQr = new QRGeneradoEntity();
                    insertGeneraQr.setDeudaClienteId(deudaClienteId);
                    insertGeneraQr.setAlias(body.get("alias") + "");
                    insertGeneraQr.setCallback(body.get("callback") + "");
                    insertGeneraQr.setDetalleGlosa(body.get("detalleGlosa") + "");
                    insertGeneraQr.setMonto(body.get("monto") + "");
                    insertGeneraQr.setMoneda(body.get("moneda") + "");
                    insertGeneraQr.setFechaVencimiento(body.get("fechaVencimiento") + "");
                    insertGeneraQr.setTipoSolicitud(body.get("tipoSolicitud") + "");
                    insertGeneraQr.setUnicoUso(body.get("unicoUso") + "");
                    insertGeneraQr.setFechaRegistro(new Date());
                    insertGeneraQr.setEstado("ACTIVO");

                    iSitioQrGeneradoDao.save(insertGeneraQr);
                }
            }
            return res;
        } catch (Exception ex) {
            res.status = false;
            res.message = "Erro al generar QR";
            return res;
        }
    }

    public ResponseDto estadoTransaccionQr(String alias) {
        ResponseDto res = new ResponseDto();
        try {
            if (alias == null) {
                res.status = false;
                res.message = "Debe enviar Alias de QR para verificar Estado";
                return res;
            }
            Optional<DatosConfirmadoQrEntity> datosConfirmadoQrEntity = iSitioDatosConfirmadoQrDao.findByAlias(alias + "");
            if (datosConfirmadoQrEntity.isEmpty()) {
                res.status = false;
                res.message = "No se ha confirmado el Pago";
                return res;
            }
            res.status = true;
            res.message = "El pago se ha confirmado";
            return res;
        } catch (Exception ex) {
            res.status = false;
            res.message = "Credenciales Incorrectas";
            return res;
        }
    }
}
