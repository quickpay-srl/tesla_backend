package bo.com.tesla.prueba.service;

import bo.com.tesla.prueba.dao.IClientePruebaDao;
import bo.com.tesla.prueba.dao.IGuitarraPruebaDao;
import bo.com.tesla.prueba.dto.ResponsePruebaDto;
import bo.com.tesla.prueba.entity.ClientePruebaEntity;
import bo.com.tesla.prueba.entity.GuitarraPruebaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ClientePruebaService {
    @Autowired
    private IClientePruebaDao iClientePruebaDao;

    public ResponsePruebaDto registrar (ClientePruebaEntity clientePruebaEntity){
        clientePruebaEntity.setEstadoId(1000);
        clientePruebaEntity.setFechaRegistro(new Date());
        ResponsePruebaDto res = new ResponsePruebaDto();
        iClientePruebaDao.save(clientePruebaEntity);
        res.message = "cliente guardado";
        res.status = true;
        return res;
    }
    public ResponsePruebaDto modificar (ClientePruebaEntity clientePruebaEntity){
        ClientePruebaEntity update =  iClientePruebaDao.findById(clientePruebaEntity.getClienteId()).get();
        update.setNombreCompleto(clientePruebaEntity.getNombreCompleto());
        update.setCorreo(clientePruebaEntity.getCorreo());
        update.setTelefono(clientePruebaEntity.getTelefono());
        iClientePruebaDao.save(update);
        ResponsePruebaDto res = new ResponsePruebaDto();
        res.message = "Modificacion Exitosa";
        res.status = true;
        return res;
    }
    public ResponsePruebaDto eliminar (Long id){
        ClientePruebaEntity update =  iClientePruebaDao.findById(id).get();
        update.setEstadoId(1001);
        iClientePruebaDao.save(update);
        ResponsePruebaDto res = new ResponsePruebaDto();
        res.message = "Eliminacion  Exitosa";
        res.status = true;
        return res;
    }
    public ResponsePruebaDto obtenerTodos (){
        List<ClientePruebaEntity> lstFinal = new ArrayList<>();
        List<ClientePruebaEntity> lst = iClientePruebaDao.findAll();
        for (ClientePruebaEntity guitarra:lst) {
            if(guitarra.getEstadoId()==1000)
                lstFinal.add(guitarra);
        }
        ResponsePruebaDto res = new ResponsePruebaDto();
        res.message = "Datos  Encontrados";
        res.status = true;
        res.result = lstFinal;
        return res;
    }

}
