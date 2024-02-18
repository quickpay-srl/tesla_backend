package bo.com.tesla.prueba.service;

import bo.com.tesla.prueba.dao.IGuitarraPruebaDao;
import bo.com.tesla.prueba.dto.ResponsePruebaDto;
import bo.com.tesla.prueba.entity.GuitarraPruebaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GuitarraPruebaService {
    @Autowired
    private IGuitarraPruebaDao iGuitarraPruebaDao;

    public ResponsePruebaDto registrar (GuitarraPruebaEntity guitarraPruebaEntity){
        guitarraPruebaEntity.setEstadoId(1000);
        guitarraPruebaEntity.setFechaRegistro(new Date());
        ResponsePruebaDto res = new ResponsePruebaDto();
        iGuitarraPruebaDao.save(guitarraPruebaEntity);
        res.message = "registro guardado";
        res.status = true;
        return res;
    }
    public ResponsePruebaDto modificar (GuitarraPruebaEntity guitarraPruebaEntity){
        GuitarraPruebaEntity update =  iGuitarraPruebaDao.findById(guitarraPruebaEntity.getGuitarraId()).get();
        update.setCosto(guitarraPruebaEntity.getCosto());
        update.setNombre(guitarraPruebaEntity.getNombre());
        update.setDescripcion(guitarraPruebaEntity.getDescripcion());
        iGuitarraPruebaDao.save(update);
        ResponsePruebaDto res = new ResponsePruebaDto();
        res.message = "Modificacion Exitosa";
        res.status = true;
        return res;
    }
    public ResponsePruebaDto eliminar (Long id){
        GuitarraPruebaEntity update =  iGuitarraPruebaDao.findById(id).get();
        update.setEstadoId(1001);
        iGuitarraPruebaDao.save(update);
        ResponsePruebaDto res = new ResponsePruebaDto();
        res.message = "Eliminacion  Exitosa";
        res.status = true;
        return res;
    }
    public ResponsePruebaDto obtenerTodos (){
        List<GuitarraPruebaEntity> lstFinal = new ArrayList<>();
        List<GuitarraPruebaEntity> lst = iGuitarraPruebaDao.findAll();
        for (GuitarraPruebaEntity guitarra:lst) {
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
