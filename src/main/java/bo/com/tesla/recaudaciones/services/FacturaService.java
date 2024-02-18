package bo.com.tesla.recaudaciones.services;

import bo.com.tesla.recaudaciones.dao.IFacturaDao;
import bo.com.tesla.recaudaciones.dto.FacturaDto;
import bo.com.tesla.useful.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacturaService {

    @Autowired
    IFacturaDao iFacturaDao;

    public ResponseDto findFacturaByEntidadId(Long entidadId){
        ResponseDto res = new ResponseDto();
        try{
            List<FacturaDto> lstFacturaDto =  iFacturaDao.findAllByEntidadId(entidadId);
            res.status = true;
            res.message = "factura encontrado";
            res.result = lstFacturaDto;
            return res;
        }catch (Exception  ex){
            res.status = false;
            res.message = "error";
            return res;
        }
    }
}
