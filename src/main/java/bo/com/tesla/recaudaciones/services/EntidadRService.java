package bo.com.tesla.recaudaciones.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import bo.com.tesla.administracion.dao.IEntidadRecaudadorDao;
import bo.com.tesla.administracion.dto.EntidadAdmDto;
import bo.com.tesla.administracion.entity.*;
import bo.com.tesla.useful.converter.UtilBase64Image;
import bo.com.tesla.recaudaciones.dao.IDominioDao;
import bo.com.tesla.useful.config.Technicalexception;
import bo.com.tesla.useful.cross.HandlingFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bo.com.tesla.recaudaciones.dao.IEntidadRDao;
import bo.com.tesla.recaudaciones.dao.IRecaudadorDao;
import bo.com.tesla.recaudaciones.dto.DominioDto;
import bo.com.tesla.recaudaciones.dto.EntidadDto;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EntidadRService implements IEntidadRService {

    @Autowired
    private IEntidadRDao iEntidadRDao;

    @Autowired
    private IRecaudadorDao iRecaudadorDao;

    @Autowired
    private IDominioDao iDominioDao;

    @Autowired
    private IEntidadRecaudadorDao iEntidadRecaudadorDao;

    @Value("${tesla.path.logos}")
    private String pathLogos;

    @Value("${tesla.path.server-files}")
    private String serverFile;

    /*********************ABM**************************/

    @Override
    public EntidadAdmDto addUpdateEntidad(EntidadAdmDto entidadAdmDto, Long usuarioId) throws Technicalexception {
        try {
            if (entidadAdmDto.entidadId != null) {
                /***Modificación***/
                Optional<EntidadEntity> entidadEntityOriginalOptional = iEntidadRDao.findByEntidadId(entidadAdmDto.entidadId);
                if(!entidadEntityOriginalOptional.isPresent()) {
                    throw new Technicalexception("No existe EntidadId=" + entidadAdmDto.entidadId);
                }
                EntidadEntity entidadEntityOriginal = entidadEntityOriginalOptional.get();
                entidadEntityOriginal.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                entidadEntityOriginal.setUsuarioModificacion(usuarioId);
                entidadEntityOriginal.setTransaccion("MODIFICAR");

                return saveEntidad(entidadAdmDto, entidadEntityOriginal);
            } else {
                /***Creación***/
                EntidadEntity entidadEntity = new EntidadEntity();
                entidadEntity.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                entidadEntity.setUsuarioCreacion(usuarioId);
                entidadEntity.setTransaccion("CREAR");

                loadEntidadRecaudadorEntityList(entidadAdmDto, entidadEntity, usuarioId);
                return saveEntidad(entidadAdmDto, entidadEntity);
            }
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }

    private void loadEntidadRecaudadorEntityList(EntidadAdmDto entidadAdmDto, EntidadEntity entidadEntity, Long usuarioId) {
        if(entidadAdmDto.recaudadorIdLst != null) {
            if (entidadAdmDto.recaudadorIdLst.size() > 0) {
                List<EntidadRecaudadorEntity> entidadRecaudadorEntityList = new ArrayList<>();
                for (Long recaudadorId : entidadAdmDto.recaudadorIdLst) {
                    Optional<RecaudadorEntity> recaudadorEntityOptional = iRecaudadorDao.findById(recaudadorId);
                    if (!recaudadorEntityOptional.isPresent()) {
                        throw new Technicalexception("No existe la recaudadoraId=" + recaudadorId);
                    }

                    EntidadRecaudadorEntity entidadRecaudadorEntity = new EntidadRecaudadorEntity();
                    entidadRecaudadorEntity.setEntidad(entidadEntity);
                    entidadRecaudadorEntity.setRecaudador(recaudadorEntityOptional.get());
                    entidadRecaudadorEntity.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                    entidadRecaudadorEntity.setUsuarioCreacion(usuarioId);
                    entidadRecaudadorEntity.setTransaccion("ACTIVAR");

                    entidadRecaudadorEntityList.add(entidadRecaudadorEntity);
                }
                entidadEntity.setEntidadRecaudadorEntityList(entidadRecaudadorEntityList);
            }
        }
    }

    private EntidadAdmDto saveEntidad(EntidadAdmDto entidadAdmDto, EntidadEntity entidadEntity) {

        Optional<DominioEntity> actividadEconomicaOptional = iDominioDao.getDominioEntityByDominioIdAndDominioAndEstado(entidadAdmDto.actividadEconomicaId, "actividad_economica_id","ACTIVO");
        if (!actividadEconomicaOptional.isPresent()) {
            throw new Technicalexception("No existe Dominio actividad_economica_id=" + entidadAdmDto.actividadEconomicaId);
        }

        Optional<DominioEntity> tipoEntidadOptional = iDominioDao.getDominioEntityByDominioIdAndDominioAndEstado(entidadAdmDto.tipoEntidadId, "tipo_entidad_id", "ACTIVO");
        if (!tipoEntidadOptional.isPresent()) {
            throw new Technicalexception("No existe Dominio tipo_entidad_id=" + entidadAdmDto.tipoEntidadId);
        }

        Optional<DominioEntity> modalidadFacturacionOptional = iDominioDao.getDominioEntityByDominioIdAndDominioAndEstado(entidadAdmDto.modalidadFacturacionId, "modalidad_facturacion_id", "ACTIVO");
        if (!modalidadFacturacionOptional.isPresent()) {
            throw new Technicalexception("No existe Dominio modalidad_facturacion_id=" + entidadAdmDto.modalidadFacturacionId);
        }


        entidadEntity.setActividadEconomica(actividadEconomicaOptional.get());
        entidadEntity.setTipoEntidad(tipoEntidadOptional.get());
        entidadEntity.setNombre(entidadAdmDto.nombre.toUpperCase().trim());
        entidadEntity.setNombreComercial(entidadAdmDto.nombreComercial != null ? entidadAdmDto.nombreComercial.toUpperCase().trim() : null);
        entidadEntity.setDireccion(entidadAdmDto.direccion.toUpperCase().trim());
        entidadEntity.setTelefono(entidadAdmDto.telefono.toUpperCase().trim());
        entidadEntity.setNit(entidadAdmDto.nit);
        entidadEntity.setPathLogo(entidadAdmDto.pathLogo);
        entidadEntity.setComprobanteEnUno(entidadAdmDto.comprobanteEnUno != null ? entidadAdmDto.comprobanteEnUno : false);
        entidadEntity.setEsCobradora(entidadAdmDto.esCobradora != null ? entidadAdmDto.esCobradora : false);
        entidadEntity.setEsPagadora(entidadAdmDto.esPagadora != null ? entidadAdmDto.esPagadora : false);
        entidadEntity.setModalidadFacturacion(modalidadFacturacionOptional.get());

        entidadEntity = iEntidadRDao.save(entidadEntity);
        entidadAdmDto.entidadId = entidadEntity.getEntidadId();

        return entidadAdmDto;
    }

    @Override
    public void setTransaccion(Long entidadId, String transaccion, Long usuarioId) throws Technicalexception {
        try {
            if(!iEntidadRDao.existsById(entidadId)) {
                throw new Technicalexception("No existe registro EntidadId=" + entidadId);
            }
            Integer countUpdate = iEntidadRDao.updateTransaccionEntidad(entidadId, transaccion, usuarioId);
            if(countUpdate < 1) {
                throw new Technicalexception("No se actualizó el estado de entidadId=" + entidadId);
            }
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }
    /*@Transactional
    @Override
    public void setLstEstado(List<Long> entidadIdLst, String estado, Long usuarioId) throws Technicalexception{
        try {
            Integer countUpdate = iEntidadRDao.updateLstEstadoEntidad(entidadIdLst, estado, usuarioId);
            if(countUpdate != entidadIdLst.size()) {
                throw new Technicalexception("No se actualizaron todos los registros o no se encuentran algunos registros.");
            }
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }*/

    @Transactional
    @Override
    public void setLstTransaccion(List<Long> entidadIdLst, String transaccion, Long usuarioId) throws Technicalexception{
        try {
            Integer countUpdate = iEntidadRDao.updateLstTransaccionEntidad(entidadIdLst, transaccion, usuarioId);
            if(countUpdate != entidadIdLst.size()) {
                throw new Technicalexception("No se actualizaron todos los registros o no se encuentran algunos registros.");
            }
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }

    @Override
    public EntidadAdmDto getEntidadById(Long entidadId) throws Technicalexception {
        Optional<EntidadAdmDto> entidadAdmDtoOptional = iEntidadRDao.findEntidadDtoById(entidadId);
        if(!entidadAdmDtoOptional.isPresent()) {
            return null;//Se controlará el status 204 en controller
        }
        EntidadAdmDto entidadAdmDto = entidadAdmDtoOptional.get();

        //Obtener Listado de recuadadoras asociadas
        List<Long> recaudadorIdLst = new ArrayList<>();
        recaudadorIdLst = iEntidadRecaudadorDao.getLstByEntidadIdActivo(entidadId);
        entidadAdmDto.recaudadorIdLst = recaudadorIdLst;
        return entidadAdmDto;
    }

    @Override
    public List<EntidadAdmDto> getAllEntidades() {
        //return iEntidadRDao.findEntidadesDtoAll();
        List<EntidadAdmDto> entidadAdmDtoList = iEntidadRDao.findEntidadesDtoAll();
        entidadAdmDtoList.forEach(e -> {
            if(e.pathLogo != null) {
                String base64 = UtilBase64Image.encoder(pathLogos + e.pathLogo);
                e.imagen64 = base64 != null ? "data:image/png;base64," + base64 : null;
                e.pathLogo = serverFile + e.pathLogo;
            }
        });
        return  entidadAdmDtoList;
    }

    @Transactional
    @Override
    public void uploadLogo(MultipartFile file, Long entidadId, Long usuarioId) throws Exception {

        String pathLogo = HandlingFiles.saveLogoToDisc(file, entidadId, pathLogos);

        Integer countUpdate = iEntidadRDao.updatePathLogo(entidadId, pathLogo, "MODIFICAR", usuarioId);
        if(countUpdate != 1) {
            throw new Technicalexception("No se ha actualizado el pathLogo de entidadId=" +entidadId);
        }
    }



    /*********************COBROS**************************/

    //@Transactional(readOnly = true)
    @Override
    public List<EntidadDto> getEntidadesByTipoEntidad(Long tipoEntidadId, SegUsuarioEntity usuario) throws Technicalexception {
        Optional<RecaudadorEntity> recaudadorEntityOptional = iRecaudadorDao.findRecaudadorByUserId(usuario.getUsuarioId());
        if (!recaudadorEntityOptional.isPresent()) {
            throw new Technicalexception("El usuario " + usuario.getLogin() + " no cuenta con la configuración correcta.");
        }
        List<EntidadDto> entidadDtoList = iEntidadRDao.findByRecaudadoraIdAndTipoEntidadId(recaudadorEntityOptional.get().getRecaudadorId(), tipoEntidadId);
        entidadDtoList.forEach(e -> {
            if(e.pathLogo != null) {
                String base64 = UtilBase64Image.encoder(pathLogos + e.pathLogo);
                e.imagen64 = base64 != null ? "data:image/png;base64," + base64 : null;
                e.pathLogo = serverFile + e.pathLogo;
            }
        });
        return  entidadDtoList;
    }

    //@Transactional(readOnly = true)
    @Override
    public List<EntidadDto> getByRecaudadoraId(SegUsuarioEntity usuario) throws Technicalexception{
        try {
            Optional<RecaudadorEntity> recaudadorEntityOptional = iRecaudadorDao.findRecaudadorByUserId(usuario.getUsuarioId());
            if (!recaudadorEntityOptional.isPresent()) {
                throw new Technicalexception("El usuario " + usuario.getLogin() + " no cuenta con la configuración correcta.");
            }
            List<EntidadDto> entidadAdmDtoList = iEntidadRDao.findByRecaudadoraId(recaudadorEntityOptional.get().getRecaudadorId());
            entidadAdmDtoList.forEach(e -> {
                if(e.pathLogo != null) {
                    String base64 = UtilBase64Image.encoder(pathLogos + e.pathLogo);
                    e.imagen64 = base64 != null ? "data:image/png;base64," + base64 : null;
                    e.pathLogo = serverFile + e.pathLogo;
                }
            });

            return  entidadAdmDtoList;
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<DominioDto> getTipoEntidadByRecaudador(SegUsuarioEntity usuario) throws Technicalexception{
        try {
        
            Optional<RecaudadorEntity> recaudadorEntityOptional = iRecaudadorDao.findRecaudadorByUserId(usuario.getUsuarioId());
            if(!recaudadorEntityOptional.isPresent()) {
                throw new Technicalexception("El usuario " + usuario.getLogin() + " no esta registrado en ninguna sucursal de recaudadción");
            }
            List<DominioDto> dominioDtos = iDominioDao.findTipoEntidadByRecaudadorId(recaudadorEntityOptional.get().getRecaudadorId());
            dominioDtos.forEach(e -> {
                if(e.abreviatura != null) {
                    String base64 = UtilBase64Image.encoder(pathLogos + "/tipos/" + e.abreviatura);
                    e.imagen64 = base64 != null ? "data:image/png;base64," + base64 : null;
                    e.abreviatura = serverFile + "/tipos/" + e.abreviatura;
                }
            });
            return dominioDtos;
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<DominioDto> findTipoEntidadPagadoras(SegUsuarioEntity usuario) throws Technicalexception{
        try {
        
            Optional<RecaudadorEntity> recaudadorEntityOptional = iRecaudadorDao.findRecaudadorByUserId(usuario.getUsuarioId());
            if(!recaudadorEntityOptional.isPresent()) {
                throw new Technicalexception("El usuario " + usuario.getLogin() + " no esta registrado en ninguna sucursal de recaudadción");
            }
            List<DominioDto> dominioDtos = this.iDominioDao.findTipoEntidadPagadoras(recaudadorEntityOptional.get().getRecaudadorId());
            dominioDtos.forEach(e -> {
                if(e.abreviatura != null) {
                    String base64 = UtilBase64Image.encoder(pathLogos + "/tipos/" + e.abreviatura);
                    e.imagen64 = base64 != null ? "data:image/png;base64," + base64 : null;
                    e.abreviatura = serverFile + "/tipos/" + e.abreviatura;
                }
            });
            return dominioDtos;
        } catch (Exception e) {
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }

}