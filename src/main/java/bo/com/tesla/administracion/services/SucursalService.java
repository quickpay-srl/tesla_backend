package bo.com.tesla.administracion.services;

import bo.com.tesla.administracion.dto.RecaudadorAdmDto;
import bo.com.tesla.administracion.dto.SucursalAdmDto;
import bo.com.tesla.administracion.entity.DominioEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.administracion.entity.SucursalEntity;
import bo.com.tesla.recaudaciones.dao.IDominioDao;
import bo.com.tesla.recaudaciones.dao.IRecaudadorDao;
import bo.com.tesla.recaudaciones.dao.ISucursalDao;
import bo.com.tesla.recaudaciones.services.IRecaudadoraService;
import bo.com.tesla.useful.config.Technicalexception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class SucursalService implements ISucursalService{

    @Autowired
    private ISucursalDao iSucursalDao;

    @Autowired
    private IDominioDao iDominioDao;

    @Autowired
    private IRecaudadorDao iRecaudadorDao;

    @Autowired
    private IRecaudadoraService recaudadoraService;
    /*********************ABM**************************/

    @Override
    public SucursalAdmDto addUpdateSucursal(SucursalAdmDto sucursalAdmDto, Long usuarioId) throws Technicalexception {
        try {
            //Al ser invocado desde REcaudadores
            if(sucursalAdmDto.recaudadorId == null) {
                RecaudadorEntity recaudadorEntity = recaudadoraService.findRecaudadorByUserId(usuarioId);
                sucursalAdmDto.recaudadorId = recaudadorEntity.getRecaudadorId();
            }


            if (sucursalAdmDto.sucursalId != null) {
                /***Modificación***/
                Optional<SucursalEntity> sucursalEntityOptional = iSucursalDao.findById(sucursalAdmDto.sucursalId);
                if(!sucursalEntityOptional.isPresent()) {
                    throw new Technicalexception("No existe sucursalId=" + sucursalAdmDto.sucursalId);
                }
                SucursalEntity sucursalEntityOriginal = sucursalEntityOptional.get();
                sucursalEntityOriginal.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                sucursalEntityOriginal.setUsuarioModificacion(usuarioId);
                sucursalEntityOriginal.setTransaccion("MODIFICAR");
                return saveSucursal(sucursalAdmDto, sucursalEntityOriginal);
            } else {
                /***Creación***/
                SucursalEntity sucursalEntity = new SucursalEntity();
                sucursalEntity.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                sucursalEntity.setUsuarioCreacion(usuarioId);
                //sucursalEntity.setEstado("INICIAL"); // NO ESTA ESCUCHANDO EL TRIGGER, esto no deberia estar en codigo pero funciona
                sucursalEntity.setTransaccion("CREAR");
                return saveSucursal(sucursalAdmDto, sucursalEntity);
            }
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }

    private SucursalAdmDto saveSucursal(SucursalAdmDto sucursalAdmDto, SucursalEntity sucursalEntity) {

        Optional<DominioEntity> departamentoOptional = iDominioDao.getDominioEntityByDominioIdAndDominioAndEstado(sucursalAdmDto.departamentoId, "departamento_id","ACTIVO");
        if (!departamentoOptional.isPresent()) {
            throw new Technicalexception("No existe Dominio departamento_id=" + sucursalAdmDto.departamentoId);
        }

        Optional<DominioEntity> localidadOptional = iDominioDao.getDominioEntityByDominioIdAndDominioAndEstado(sucursalAdmDto.localidadId, "municipio_id","ACTIVO");
        if (!localidadOptional.isPresent()) {
            throw new Technicalexception("No existe Dominio municipio_id=" + sucursalAdmDto.departamentoId);
        }

        Optional<RecaudadorEntity> recaudadorEntityOptional = iRecaudadorDao.findById(sucursalAdmDto.recaudadorId);
        if (!recaudadorEntityOptional.isPresent()) {
            throw new Technicalexception("No existe recaudadoraId =" + sucursalAdmDto.recaudadorId);
        }

        sucursalEntity.setRecaudador(recaudadorEntityOptional.get());
        sucursalEntity.setDepartamento(departamentoOptional.get());
        sucursalEntity.setLocalidad(localidadOptional.get());

        sucursalEntity.setNombre(sucursalAdmDto.nombre.toUpperCase().trim());
        sucursalEntity.setDireccion(sucursalAdmDto.direccion.toUpperCase().trim());
        sucursalEntity.setTelefono(sucursalAdmDto.telefono);

        sucursalEntity = iSucursalDao.save(sucursalEntity);
        sucursalAdmDto.sucursalId = sucursalEntity.getSucursalId();

        return sucursalAdmDto;
    }

    @Override
    public void setTransaccion(Long sucursalId, String transaccion, Long usuarioId) throws Technicalexception{
        try {
            if(!iSucursalDao.existsById(sucursalId)) {
                throw new Technicalexception("No existe registro sucursalId=" + sucursalId);
            }
            Integer countUpdate = iSucursalDao.updateTransaccionSucursal(sucursalId, transaccion, usuarioId);
            if(countUpdate != 1) {
                throw new Technicalexception("No se actualizó el estado de sucursalId=" + sucursalId);
            }
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }

    @Transactional
    @Override
    public void setLstTransaccion(List<Long> sucursalIdLst, String transaccion, Long usuarioId) throws Technicalexception{
        try {

            Integer countUpdate = iSucursalDao.updateLstTransaccionSucursal(sucursalIdLst, transaccion, usuarioId);
            if(countUpdate != sucursalIdLst.size()) {
                throw new Technicalexception("No se actualizaron todos los registros o no se encuentran algunos registros.");
            }
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }

    @Override
    public SucursalAdmDto getSucursalById(Long sucursalId) throws Technicalexception{
        Optional<SucursalAdmDto> sucursalAdmDtoOptional = iSucursalDao.findSucursalDtoById(sucursalId);
        if(!sucursalAdmDtoOptional.isPresent()) {
            return null;//Se controlará el status 204 en controllee
        }
        return sucursalAdmDtoOptional.get();
    }

    @Override
    public List<SucursalAdmDto> getAllSucursales() throws Technicalexception{
        return iSucursalDao.findRecaudadorDtoAll();
    }

    @Override
    public List<SucursalAdmDto> getListSucursalesByRecaudadora(Long recaudadorId) throws Technicalexception{
        return iSucursalDao.findLstSucursalesDtoByRecaudadorId(recaudadorId);
    }
    @Override
    public List<SucursalAdmDto> getListSucursalesForAddUserByRecaudadora(Long recaudadorId) throws Technicalexception{
        return iSucursalDao.findLstSucursalesDtoForAddUserByRecaudadorId(recaudadorId);
    }

	@Override
	public Optional<SucursalEntity> findById(Long sucursalId) {
	
		return this.iSucursalDao.findById(sucursalId);
	}

	@Override
	public List<SucursalEntity> findByRecaudadoraId(Long recaudadorId) {
		
		return this.iSucursalDao.findByRecaudadoraId(recaudadorId);
	}


    

}
