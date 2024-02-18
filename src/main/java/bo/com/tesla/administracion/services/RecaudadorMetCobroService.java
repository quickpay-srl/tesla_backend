package bo.com.tesla.administracion.services;

import bo.com.tesla.administracion.dao.IRecaudadorMetCobroDao;
import bo.com.tesla.administracion.dto.RecaudadorMetodoCobroDto;
import bo.com.tesla.administracion.entity.DominioEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.administracion.entity.RecaudadorMetodoCobroEntity;
import bo.com.tesla.recaudaciones.dao.IDominioDao;
import bo.com.tesla.recaudaciones.services.IRecaudadoraService;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecaudadorMetCobroService implements IRecaudadorMetCobroService{

    @Autowired
    private IRecaudadorMetCobroDao recaudadorMetCobroDao;

    @Autowired
    private IDominioDao dominioDao;

    @Autowired
    private IRecaudadoraService recaudadoraService;


    @Override
    public void addRecaudadoresMetCobro(RecaudadorEntity recaudadorEntity,
                                        Long usuarioId,
                                        List<Long> metodoCobroIdLst) {

        List<RecaudadorMetodoCobroEntity> recaudadorMetodoCobroEntityList = loadRecMetCobroLst(recaudadorEntity,
                usuarioId,
                metodoCobroIdLst);
        recaudadorMetodoCobroEntityList = recaudadorMetCobroDao.saveAll(recaudadorMetodoCobroEntityList);
        if(metodoCobroIdLst.size() != recaudadorMetodoCobroEntityList.size()) {
            throw new Technicalexception("No se registraron todos los registros");
        }
    }

    @Override
    public void updateRecaudadoresMetCobro(RecaudadorEntity recaudadorEntity,
                                           Long usuarioId,
                                           List<Long> metodoCobroIdLst) {
        recaudadorMetCobroDao.updateEstado(recaudadorEntity, "INACTIVAR", usuarioId);
        List<RecaudadorMetodoCobroEntity> recaudadorMetodoCobroEntityList = loadRecMetCobroLst(recaudadorEntity, usuarioId, metodoCobroIdLst);
        //addRecaudadoresMetCobro(recaudadorEntity, usuarioId, recaudadorMetodoCobroDtoList);
        recaudadorMetCobroDao.saveAll(recaudadorMetodoCobroEntityList);
    }

    private List<RecaudadorMetodoCobroEntity> loadRecMetCobroLst(RecaudadorEntity recaudadorEntity,
                                                                Long usuarioId,
                                                                List<Long> metodoCobroIdLst) {
        List<RecaudadorMetodoCobroEntity> recaudadorMetodoCobroEntityList = new ArrayList<>();
        for(Long metodoCobroId : metodoCobroIdLst) {
            Optional<DominioEntity> metodoCobroOptional = dominioDao.getDominioEntityByDominioIdAndDominioAndEstado(metodoCobroId, "metodo_cobro_id", "ACTIVO");
            if(!metodoCobroOptional.isPresent()) {
                throw new Technicalexception("No existe el dominio='metodo_cobro_id' para dominioId=" + metodoCobroId );
            }

            RecaudadorMetodoCobroEntity recMetCobroEntity = new RecaudadorMetodoCobroEntity();
            recMetCobroEntity.setRecaudadorId(recaudadorEntity);
            recMetCobroEntity.setMetodoCobroId(metodoCobroOptional.get());
            recMetCobroEntity.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            recMetCobroEntity.setUsuarioCreacion(usuarioId);
            recMetCobroEntity.setTransaccion("ACTIVAR");
            recaudadorMetodoCobroEntityList.add(recMetCobroEntity);
        }
        return recaudadorMetodoCobroEntityList;
    }

    @Override
    public List<String> findRecMetCobroLst(Long recaudadorId) {
        return recaudadorMetCobroDao.findRecMetCobroLSt(recaudadorId);
    }

    @Override
    public List<Long> findRecMetCobroIdLst(Long recaudadorId) {
        return recaudadorMetCobroDao.findRecMetCobroIdst(recaudadorId);
    }

    @Override
    public List<RecaudadorMetodoCobroDto> findRecMetCobDtoBRecaudador(Long usuarioId) throws BusinesException {
        RecaudadorEntity recaudadorEntity = recaudadoraService.findRecaudadorByUserId(usuarioId);
        if(recaudadorEntity == null) {
            throw new BusinesException("El usuario debe pertenecer a una Recaudadora.");
        }
        return recaudadorMetCobroDao.findRecMetCobroDtoByRecaudador(recaudadorEntity.getRecaudadorId());
    }

}
