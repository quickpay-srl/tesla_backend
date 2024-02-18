package bo.com.tesla.administracion.services;

import bo.com.tesla.administracion.dao.ISucursalEntidadDao;
import bo.com.tesla.administracion.dto.CredencialFacturacionDto;
import bo.com.tesla.administracion.dto.SucursalEntidadAdmDto;
import bo.com.tesla.administracion.entity.DominioEntity;
import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.administracion.entity.SucursalEntidadEntity;
import bo.com.tesla.entidades.services.IEntidadService;
import bo.com.tesla.recaudaciones.dao.IDominioDao;
import bo.com.tesla.recaudaciones.dao.IEntidadRDao;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class SucursalEntidadService implements ISucursalEntidadService {

    @Autowired
    private ISucursalEntidadDao iSucursalEntidadDao;

    @Autowired
    private IEntidadRDao iEntidadRDao;

    @Autowired
    private IDominioDao iDominioDao;

    @Autowired
    private IEntidadService entidadService;

    /*********************ABM**************************/

    @Override
    public SucursalEntidadAdmDto addUpdateSucursalEntidad(SucursalEntidadAdmDto sucursalEntidadAdmDto, Long usuarioId) throws BusinesException {

        //Caso en el que se ingresa desde Entidades
        if(sucursalEntidadAdmDto.entidadId == null) {
            EntidadEntity entidad = this.entidadService.findEntidadByUserId(usuarioId);
            if(entidad == null) {
                throw new BusinesException("El usuario debe pertenecer a una Entidad.");
            }
            sucursalEntidadAdmDto.entidadId = entidad.getEntidadId();
        }

        sucursalEntidadAdmDto.emiteFacturaTesla = sucursalEntidadAdmDto.emiteFacturaTesla != null ? sucursalEntidadAdmDto.emiteFacturaTesla : false;
        if(sucursalEntidadAdmDto.emiteFacturaTesla) {
            Long countEmiteFacturaTesla = iSucursalEntidadDao.countEmiteFacturaTesla(sucursalEntidadAdmDto.entidadId, sucursalEntidadAdmDto.sucursalEntidadId);
            if (countEmiteFacturaTesla > 0) {
                throw new BusinesException("Ya existe una sucursal que emite Facturas.");
            }
        }

        try {
            if (sucursalEntidadAdmDto.sucursalEntidadId != null) {
                /***Modificacion***/

                Optional<SucursalEntidadEntity> sucursalEntidadEntityOptional = iSucursalEntidadDao.findById(sucursalEntidadAdmDto.sucursalEntidadId);
                if(!sucursalEntidadEntityOptional.isPresent()) {
                    throw new Technicalexception("No existe sucursalEntidadId=" + sucursalEntidadAdmDto.sucursalEntidadId);
                }
                SucursalEntidadEntity sucursalEntidadEntityOriginal = sucursalEntidadEntityOptional.get();
                sucursalEntidadEntityOriginal.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                sucursalEntidadEntityOriginal.setUsuarioModificacion(usuarioId);
                sucursalEntidadEntityOriginal.setTransaccion("MODIFICAR");

                return saveSucursalEntidad(sucursalEntidadAdmDto, sucursalEntidadEntityOriginal);
            } else {
                /***Creación***/
                SucursalEntidadEntity sucursalEntidadEntity = new SucursalEntidadEntity();
                sucursalEntidadEntity.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                sucursalEntidadEntity.setUsuarioCreacion(usuarioId);
                sucursalEntidadEntity.setTransaccion("CREAR");

                return saveSucursalEntidad(sucursalEntidadAdmDto, sucursalEntidadEntity);
            }
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }

    private SucursalEntidadAdmDto saveSucursalEntidad(SucursalEntidadAdmDto sucursalEntidadAdmDto, SucursalEntidadEntity sucursalEntidadEntity) throws BusinesException {


        Optional<EntidadEntity> entidadEntityOptional = iEntidadRDao.findById(sucursalEntidadAdmDto.entidadId);
        if(!entidadEntityOptional.isPresent()) {
            throw new Technicalexception("No existe ENTIDAD EntidadId=" + sucursalEntidadAdmDto.entidadId);
        }

        Optional<DominioEntity> departamentoOptional = iDominioDao.getDominioEntityByDominioIdAndDominioAndEstado(sucursalEntidadAdmDto.departamentoId, "departamento_id","ACTIVO");
        if (!departamentoOptional.isPresent()) {
            throw new Technicalexception("No existe Dominio departamento_id=" + sucursalEntidadAdmDto.departamentoId);
        }

        Optional<DominioEntity> municipioOptional = iDominioDao.getDominioEntityByDominioIdAndDominioAndEstado(sucursalEntidadAdmDto.municipioId, "municipio_id","ACTIVO");
        if (!municipioOptional.isPresent()) {
            throw new Technicalexception("No existe Dominio municipio_id=" + sucursalEntidadAdmDto.departamentoId);
        }

        sucursalEntidadEntity.setEntidad(entidadEntityOptional.get());
        sucursalEntidadEntity.setNombreSucursal(sucursalEntidadAdmDto.nombreSucursal.toUpperCase().trim());
        sucursalEntidadEntity.setDireccion(sucursalEntidadAdmDto.direccion.toUpperCase().trim());
        sucursalEntidadEntity.setTelefono(sucursalEntidadAdmDto.telefono);
        sucursalEntidadEntity.setEmail(sucursalEntidadAdmDto.email);
        sucursalEntidadEntity.setNumeroSucursalSin(sucursalEntidadAdmDto.numeroSucursalSin);
        sucursalEntidadEntity.setEmiteFacturaTesla(sucursalEntidadAdmDto.emiteFacturaTesla);
        sucursalEntidadEntity.setDepartamentoId(departamentoOptional.get());
        sucursalEntidadEntity.setMunicipioId(municipioOptional.get());

        sucursalEntidadEntity = iSucursalEntidadDao.save(sucursalEntidadEntity);
        sucursalEntidadAdmDto.sucursalEntidadId = sucursalEntidadEntity.getSucursalEntidadId();

        return sucursalEntidadAdmDto;
    }

    @Override
    public void setTransaccionSucursalEntidad(Long sucursalEntidadId, String transaccion, Long usuarioId) {
        try {
            if(!iSucursalEntidadDao.existsById(sucursalEntidadId)) {
                throw new Technicalexception("No existe registro SucursalEntidadId=" + sucursalEntidadId);
            }
            Integer countUpdate = iSucursalEntidadDao.updateTransaccionSucursalEntidad(sucursalEntidadId, transaccion, usuarioId);
            if(countUpdate != 1) {
                throw new Technicalexception("No se actualizó el estado de sucursalEntidadId=" + sucursalEntidadId);
            }
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }

    @Transactional
    @Override
    public void setLstTransaccion(List<Long> sucursalEntidadIdLst, String transaccion, Long usuarioId) throws Technicalexception{
        try {
            Integer countUpdate = iSucursalEntidadDao.updateLstTransaccionSucursalEntidad(sucursalEntidadIdLst, transaccion, usuarioId);
            if(countUpdate != sucursalEntidadIdLst.size()) {
                throw new Technicalexception("No se actualizaron todos los registros o no se encuentran algunos registros.");
            }
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }

    @Override
    public SucursalEntidadAdmDto getSucursalEntidadById(Long sucursalEntidadId) {
        Optional<SucursalEntidadAdmDto> sucursalEntidadAdmDtoOptional = iSucursalEntidadDao.findSucursalEntidadDtoById(sucursalEntidadId);
        if(!sucursalEntidadAdmDtoOptional.isPresent()){
            return null;//Se controlará el status 204 en controllee
        }
        return sucursalEntidadAdmDtoOptional.get();
    }

    @Override
    public List<SucursalEntidadAdmDto> getAllSucursalEntidades() {
        return iSucursalEntidadDao.findSucursalesEntidadesDtoAll();
    }

    @Override
    public List<SucursalEntidadAdmDto> getLisSucursalEntidadesByEntidadId(Long entidadId) {
        return iSucursalEntidadDao.findSucursalesEntidadesDtoByEntidadId(entidadId);
    }

    @Override
    public List<SucursalEntidadAdmDto> getLisSucursalEntidadesForAddUserByEntidadId(Long entidadId) {
        List<SucursalEntidadAdmDto> lst = iSucursalEntidadDao.findSucursalesEntidadesDtoForAddUserByEntidadId(entidadId);
        return lst;
    }

    @Override
    public List<SucursalEntidadAdmDto> getLisSucursalEntidadesByEntidadIdActivos(Long entidadId) {
        return iSucursalEntidadDao.findSucursalesEntidadesDtoByEntidadIdActivos(entidadId);
    }

    @Override
    public Optional<SucursalEntidadAdmDto> findsucursalEmtidadEmiteFacturaTesla(Long entidadId) {
        return iSucursalEntidadDao.findDtoByEmiteFacturaTesla(entidadId);
    }

    @Transactional
    @Override
    public void updateCredencialesFacturacion(CredencialFacturacionDto credencialFacturacionDto, SegUsuarioEntity usuarioEntity) {
        try {
            iSucursalEntidadDao.updateCredencialesFacturacion(credencialFacturacionDto.sucursalEntidadId, credencialFacturacionDto.login.trim(), credencialFacturacionDto.password.trim(), usuarioEntity.getUsuarioId());
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Optional<CredencialFacturacionDto> findCredencialFacturacion(Long sucursalEntidadId) {
        return iSucursalEntidadDao.findCredencialFacturacion(sucursalEntidadId);
    }

}