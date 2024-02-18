package bo.com.tesla.administracion.services;

import bo.com.tesla.administracion.dao.IEntidadRecaudadorDao;
import bo.com.tesla.administracion.dto.EntidadRecaudadorAdmDto;
import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.EntidadRecaudadorEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.recaudaciones.dao.IEntidadRDao;
import bo.com.tesla.recaudaciones.dao.IRecaudadorDao;
import bo.com.tesla.recaudaciones.services.IEntidadRService;
import bo.com.tesla.recaudaciones.services.IRecaudadoraService;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EntidadRecaudadorService implements IEntidadRecaudadorService {

    @Autowired
    private IEntidadRecaudadorDao iEntidadRecaudadorDao;

    @Autowired
    private IRecaudadorDao iRecaudadorDao;

    @Autowired
    private IEntidadRDao entidadRDao;

    @Autowired
    private IRecaudadoraService recaudadoraService;

    @Override
    @Transactional
    public void postEntidadesRecaudadoesPorEntidad(Long entidadId, List<Long> recaudadorIdLst, Long usuarioId) throws Technicalexception {

        try {
            Optional<EntidadEntity> entidadEntityOptional = entidadRDao.findById(entidadId);
            if (!entidadEntityOptional.isPresent()) {
                throw new Technicalexception("No existe entidadId=" + entidadId);
            }

            //Obtener recaudadoras asociadas inicialmente
            List<Long> recaudadorIdOriginalLst = new ArrayList<>();
            recaudadorIdOriginalLst = iEntidadRecaudadorDao.getLstByEntidadIdActivo(entidadId);

            List<Long> compareOriginalToNew = compareLists(recaudadorIdOriginalLst, recaudadorIdLst); //anular
            List<Long> compareNewToOriginal = compareLists(recaudadorIdLst, recaudadorIdOriginalLst); //crear

            if (compareNewToOriginal.size() != 0) {
                crearRecaudadorList(compareNewToOriginal, entidadEntityOptional.get(), usuarioId);
            }
            if (compareOriginalToNew.size() != 0) {
                anularRecaudadorLst(compareOriginalToNew, entidadEntityOptional.get().getEntidadId(), usuarioId);
            }

        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }

    public void crearRecaudadorList(List<Long> recaudadorIdLst, EntidadEntity entidadEntity, Long usuarioId) throws Technicalexception{
        List<EntidadRecaudadorEntity> entidadRecaudadorEntityList = new ArrayList<>();
        for (Long recaudadorId : recaudadorIdLst){
            Optional<RecaudadorEntity> recaudadorEntityOptional = iRecaudadorDao.findById(recaudadorId);
            if(!recaudadorEntityOptional.isPresent()) {
                throw new Technicalexception("No existe la recaudadoraId=" + recaudadorId);
            }

            EntidadRecaudadorEntity entidadRecaudadorEntity = new EntidadRecaudadorEntity();
            entidadRecaudadorEntity.setEntidad(entidadEntity);
            entidadRecaudadorEntity.setRecaudador(recaudadorEntityOptional.get());
            entidadRecaudadorEntity.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            entidadRecaudadorEntity.setUsuarioCreacion(usuarioId);
            //entidadRecaudadorEntity.setEstado("CREADO");
            entidadRecaudadorEntity.setTransaccion("ACTIVAR");
            entidadRecaudadorEntityList.add(entidadRecaudadorEntity);
        }
        iEntidadRecaudadorDao.saveAll(entidadRecaudadorEntityList);
    }

    public void anularRecaudadorLst(List<Long> recaudadorIdLst, Long entidadId, Long usuarioId) {

        Integer countUpdate = iEntidadRecaudadorDao.updateLstTransaccionByRecaudadorIdActivo(entidadId, recaudadorIdLst, "INACTIVAR", usuarioId);
        if(countUpdate != recaudadorIdLst.size()) {
            throw new Technicalexception("No se ha actualizado");
        }
    }

    @Transactional
    @Override
    public void postEntidadesRecaudadoesPorRecaudador(Long recaudadorId, List<Long> entidadIdLst, Long usuarioId) throws Technicalexception {
        try {
            Optional<RecaudadorEntity> recaudadorEntityOptional = iRecaudadorDao.findById(recaudadorId);
            if (!recaudadorEntityOptional.isPresent()) {
                throw new Technicalexception("No existe recaudadorId=" + recaudadorId);
            }
            //Obtener entidaes asociadas inicialmente
            List<Long> entidadIdOriginalLst = new ArrayList<>();
            entidadIdOriginalLst = iEntidadRecaudadorDao.getLstByRecaudadorIdActivo(recaudadorId);

            List<Long> compareOriginalToNew = compareLists(entidadIdOriginalLst, entidadIdLst); //anular
            List<Long> compareNewToOriginal = compareLists(entidadIdLst, entidadIdOriginalLst); //crear

            if (compareNewToOriginal.size() != 0) {
                crearEntidadList(compareNewToOriginal, recaudadorEntityOptional.get(), usuarioId);
            }
            if (compareOriginalToNew.size() != 0) {
                anularEntidadLst(compareOriginalToNew, recaudadorEntityOptional.get().getRecaudadorId(), usuarioId);
            }
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }

    public void crearEntidadList(List<Long> entidadIdLst, RecaudadorEntity recaudadorEntity, Long usuarioId) throws Technicalexception{

        List<EntidadRecaudadorEntity> entidadRecaudadorEntityList = new ArrayList<>();
        for (Long entidadId : entidadIdLst){
            Optional<EntidadEntity> entidadEntityOptional = entidadRDao.findById(entidadId);
            if(!entidadEntityOptional.isPresent()) {
                throw new Technicalexception("No existe entidadId=" + entidadId);
            }

            EntidadRecaudadorEntity entidadRecaudadorEntity = new EntidadRecaudadorEntity();
            entidadRecaudadorEntity.setEntidad(entidadEntityOptional.get());
            entidadRecaudadorEntity.setRecaudador(recaudadorEntity);
            entidadRecaudadorEntity.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            entidadRecaudadorEntity.setUsuarioCreacion(usuarioId);
            //entidadRecaudadorEntity.setEstado("CREADO");
            entidadRecaudadorEntity.setTransaccion("ACTIVAR");

            entidadRecaudadorEntityList.add(entidadRecaudadorEntity);
        }
        iEntidadRecaudadorDao.saveAll(entidadRecaudadorEntityList);
    }

    public void anularEntidadLst(List<Long> entidadIdLst, Long recaudadorId, Long usuarioId) {

        Integer countUpdate = iEntidadRecaudadorDao.updateLstTransaccionByEntidadrIdActivo(recaudadorId, entidadIdLst, "INACTIVAR", usuarioId);
        if(countUpdate != entidadIdLst.size()) {
            throw new Technicalexception("No se ha actualizado");
        }
    }


    private List<Long> compareLists(List<Long> listOne, List<Long> listTwo) {

        List<Long> differences = listOne.stream()
                .filter(element -> !listTwo.contains(element))
                .collect(Collectors.toList());
        return differences;
    }

    @Override
    public void verificarAccesoUsuarioRecaudador(Long usuarioRecaudadorId, Long entidadId) throws BusinesException {
        RecaudadorEntity recaudadorEntity = recaudadoraService.findRecaudadorByUserId(usuarioRecaudadorId);
        if(recaudadorEntity == null) {
            throw new BusinesException("El usuario debe pertenecer a una Recaudadora.");
        }

        List<EntidadRecaudadorEntity> entidadRecaudadorEntityList = iEntidadRecaudadorDao.getLstByRecaudadorAndEntidad(recaudadorEntity.getRecaudadorId(), entidadId);
        if(entidadRecaudadorEntityList.size() == 0) {
            throw new BusinesException("Acceso denegado para la Entidad.");
        }
    }
}
