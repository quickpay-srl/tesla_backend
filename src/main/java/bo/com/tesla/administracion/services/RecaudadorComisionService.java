package bo.com.tesla.administracion.services;

import bo.com.tesla.administracion.dao.IRecaudadorComisionDao;
import bo.com.tesla.administracion.dto.RecaudadorComisionAdmDto;
import bo.com.tesla.administracion.entity.*;
import bo.com.tesla.recaudaciones.dao.IDominioDao;
import bo.com.tesla.recaudaciones.dao.IRecaudadorDao;
import bo.com.tesla.useful.config.Technicalexception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class RecaudadorComisionService implements IRecaudadorComisionService {

    @Autowired
    private IRecaudadorComisionDao iRecaudadorComisionDao;

    @Autowired
    private IRecaudadorDao iRecaudadorDao;

    @Autowired
    private IDominioDao iDominioDao;

    @Transactional
    @Override
    public RecaudadorComisionAdmDto addUpdateRecaudadorComision(RecaudadorComisionAdmDto recaudadorComisionAdmDto, Long usuarioId) throws Technicalexception {
        try {
            if (recaudadorComisionAdmDto.recaudadorComisionId != null) {
                /***Modificación***/

                RecaudadorComisionEntity recaudadorComisionEntityOriginal = iRecaudadorComisionDao.getOne(recaudadorComisionAdmDto.recaudadorComisionId);
                recaudadorComisionEntityOriginal.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                recaudadorComisionEntityOriginal.setUsuarioModificacion(usuarioId);
                recaudadorComisionEntityOriginal.setTransaccion("MODIFICAR");

                return saveRecaudadorComision(recaudadorComisionAdmDto, recaudadorComisionEntityOriginal, usuarioId);
            } else {
                /***Creación***/
                RecaudadorComisionEntity recaudadorComisionEntity = new RecaudadorComisionEntity();
                recaudadorComisionEntity.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                recaudadorComisionEntity.setUsuarioCreacion(usuarioId);
                recaudadorComisionEntity.setTransaccion("ACTIVAR");

                Integer countUpdate = iRecaudadorComisionDao.updateRecaudadorComisionActivo(recaudadorComisionAdmDto.recaudadorId, usuarioId);

                return saveRecaudadorComision(recaudadorComisionAdmDto, recaudadorComisionEntity, usuarioId);
            }
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }

    public RecaudadorComisionAdmDto saveRecaudadorComision(RecaudadorComisionAdmDto recaudadorComisionAdmDto, RecaudadorComisionEntity recaudadorComisionEntity, Long usuarioId) {

        Optional<RecaudadorEntity> recaudadorEntityOptional = iRecaudadorDao.findById(recaudadorComisionAdmDto.recaudadorId);
        if(!recaudadorEntityOptional.isPresent()) {
            throw new Technicalexception("No existe recaudadorId=" + recaudadorComisionAdmDto.recaudadorId);
        }

        Optional<DominioEntity> tipoComisionOptional = iDominioDao.getDominioEntityByDominioIdAndDominioAndEstado(recaudadorComisionAdmDto.tipoComisionId, "tipo_comision_id","ACTIVO");
        if (!tipoComisionOptional.isPresent()) {
            throw new Technicalexception("No existe Dominio tipo_comision_id=" + recaudadorComisionAdmDto.tipoComisionId);
        }

        recaudadorComisionEntity.setTipoComision(iDominioDao.getOne(recaudadorComisionAdmDto.tipoComisionId));
        recaudadorComisionEntity.setComision(recaudadorComisionAdmDto.comision);
        recaudadorComisionEntity.setRecaudador(iRecaudadorDao.getOne(recaudadorComisionAdmDto.recaudadorId));

        recaudadorComisionEntity = iRecaudadorComisionDao.save(recaudadorComisionEntity);
        recaudadorComisionAdmDto.recaudadorComisionId = recaudadorComisionEntity.getRecaudadorComisionId();

        return recaudadorComisionAdmDto;
    }

    @Override
    public void setTransaccion(Long recaudadorComisionId, String transaccion, Long usuarioId) throws Technicalexception {
        try {
            if(!iRecaudadorComisionDao.existsById(recaudadorComisionId)) {
                throw new Technicalexception("No existe registro recaudadorComisionId=" + recaudadorComisionId);
            }
            Integer countUpdate = iRecaudadorComisionDao.updateTransaccion(recaudadorComisionId, transaccion, usuarioId);
            if(countUpdate < 1) {
                throw new Technicalexception("No se actualizó el estado de recaudadorComisionId=" + recaudadorComisionId);
            }
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }

    @Override
    public RecaudadorComisionAdmDto getRecaudadorComisionById(Long recaudadorComisionId) throws Technicalexception {
        Optional<RecaudadorComisionAdmDto> recaudadorComisionAdmDtoOptional = iRecaudadorComisionDao.findREcaudadorComisionDtoById(recaudadorComisionId);
        if(!recaudadorComisionAdmDtoOptional.isPresent()) {
            return null;//Se controlará el status 204 en controller
        }
        return recaudadorComisionAdmDtoOptional.get();
    }

    @Override
    public List<RecaudadorComisionAdmDto> getAllRecaudadoresComisionesByRecaudadorId(Long recaudadorId) {
        return iRecaudadorComisionDao.findRecaudadorComisionByRecaudadorId(recaudadorId);
    }

    @Override
    public RecaudadorComisionAdmDto getRecaudadoresComisionesActivosByRecaudadorId(Long recaudadorId) {
        List<RecaudadorComisionAdmDto> lst =  iRecaudadorComisionDao.findRecaudadorComisionByRecaudadorActivoId(recaudadorId);
        if(lst.isEmpty()){
            return new RecaudadorComisionAdmDto();
        }else{
            return lst.get(0);
        }
    }

    @Override
    public RecaudadorComisionEntity getRecaudadorComisionActual(RecaudadorEntity recaudadorEntity) throws Technicalexception{
        RecaudadorComisionEntity recaudadorComisionEntity = new RecaudadorComisionEntity();

        Optional<RecaudadorComisionEntity> recaudadorComisionEntityOptional = iRecaudadorComisionDao.findRecaudadorComisionEntityByRecaudadorAndEstado(recaudadorEntity, "ACTIVO");
        if(!recaudadorComisionEntityOptional.isPresent()) {
            throw new Technicalexception("No se encuentra registro activo de comision para recaudadorId=" + recaudadorEntity.getRecaudadorId());
        }

        recaudadorComisionEntity = recaudadorComisionEntityOptional.get();
        return recaudadorComisionEntity;
    }

    @Override
    public BigDecimal calcularComision(RecaudadorComisionEntity recaudadorComisionEntity, BigDecimal monto) {
        /*********INGRESAR CALCULOS*****************/
        BigDecimal comision = new BigDecimal(0);
        BigDecimal cien = BigDecimal.valueOf(100);

        BigDecimal montoCalculado = new BigDecimal(0);

        Optional<Long> porcentajeIdOptional = iDominioDao.getDominioIdByDominioAndAbreviatura("tipo_comision_id", "%");
        if(!porcentajeIdOptional.isPresent()) {
            throw new Technicalexception("No se encuentra dominio='tipo_comision_id', abreviatura='%'");
        }

        Optional<Long> montoBsIdOptional = iDominioDao.getDominioIdByDominioAndAbreviatura("tipo_comision_id", "Bs");
        if(!montoBsIdOptional.isPresent()) {
            throw new Technicalexception("No se encuentra dominio='tipo_comision_id', abreviatura='Bs'");
        }

        //Aumentar otros cálculos
        if (recaudadorComisionEntity.getTipoComision().getDominioId() == porcentajeIdOptional.get()) { //Porcentaje
            comision = recaudadorComisionEntity.getComision().divide(cien);
            montoCalculado = monto.multiply(comision);
        } else if (recaudadorComisionEntity.getTipoComision().getDominioId() == montoBsIdOptional.get()) { //Fijo bolivianos
            montoCalculado = recaudadorComisionEntity.getComision();
        }
        return montoCalculado;
    }




}
