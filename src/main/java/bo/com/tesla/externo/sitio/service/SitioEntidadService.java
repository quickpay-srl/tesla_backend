package bo.com.tesla.externo.sitio.service;

import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.externo.sitio.dao.ISitioEntidadDao;
import bo.com.tesla.externo.sitio.dto.SitioEntidadDto;
import bo.com.tesla.useful.converter.UtilBase64Image;
import bo.com.tesla.useful.dto.ResponseDto;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SitioEntidadService {

    @Autowired
    private ISitioEntidadDao iEntidadDao;

    @Value("${tesla.path.logos}")
    private String pathLogos;
    @Value("${tesla.path.server-files}")
    private String serverFile;

    public ResponseDto entidadByTipoEntidadId(Long tipoEntidadId){
        ResponseDto res = new ResponseDto();
        try{
            List<EntidadEntity> lstEntidad =  iEntidadDao.findEntidadByTipoEntidadId(tipoEntidadId);

            List<SitioEntidadDto> lstEntidadDto = lstEntidad.stream().map(obj -> {
                        return new SitioEntidadDto(
                                obj.getEntidadId(),
                                obj.getNombre(),
                                obj.getNombreComercial(),
                                obj.getSubdominioEmpresa(),
                                obj.getDireccion(),
                                obj.getTelefono(),
                                obj.getNit(),
                                obj.getLlaveDosificacion(),
                                obj.getPathLogo(),
                                obj.getComprobanteEnUno());
                    }).collect(Collectors.toList());
            res.status = true;
            res.result = lstEntidadDto;
            return res;
        }catch (Exception ex){
            res.status = false;
            res.message = ex.toString();
            return res;
        }
    }

    public ResponseDto getEntidadForTipo(Long tipoEntidadId) {

        ResponseDto res = new ResponseDto();
        try{
            List<SitioEntidadDto> lstEntidad =  iEntidadDao.findByTipoEntidadId(tipoEntidadId);
            if(!lstEntidad.isEmpty()){
                lstEntidad.forEach(e -> {
                    if(e.getPathLogo() != null) {



                        String base64 = UtilBase64Image.encoder(pathLogos + e.getPathLogo());
                       // String base64 = UtilBase64Image.encoderFromFile(new File(pathLogos + e.getPathLogo()));

                        e.setImagen64(base64 != null ? "data:image/png;base64," + base64 : null) ;
                        //e.setPathLogo(serverFile + e.getPathLogo());
                    }
                });
            }
            res.status = true;
            res.result = lstEntidad;
            return res;
        }catch (Exception ex){
            res.status = false;
            res.message = ex.toString();
            return res;
        }

        /*
         List<EntidadAdmDto> entidadAdmDtoList = iEntidadRDao.findEntidadesDtoAll();
        entidadAdmDtoList.forEach(e -> {
            if(e.pathLogo != null) {
                String base64 = UtilBase64Image.encoder(pathLogos + e.pathLogo);
                e.imagen64 = base64 != null ? "data:image/png;base64," + base64 : null;
                e.pathLogo = serverFile + e.pathLogo;
            }
        });
        return  entidadAdmDtoList;
         */
    }
    public ResponseDto getEntidadForEntidadId(Long entidadId) {

        ResponseDto res = new ResponseDto();
        try{
            Optional<SitioEntidadDto> objEntidad =  iEntidadDao.findByEntidadId(entidadId);
            if(objEntidad.isEmpty()){
                res.status = false;
                res.message="no hay entidad";
            }else{
                SitioEntidadDto obj = objEntidad.get();
                String base64= UtilBase64Image.encoder(pathLogos + obj.getPathLogo());
                obj.setImagen64(base64 != null ? "data:image/png;base64," + base64 : null) ;


                res.status = true;
                res.result = obj;
            }

            return res;
        }catch (Exception ex){
            res.status = false;
            res.message = ex.toString();
            return res;
        }
    }    public ResponseDto getEntidadForSubDominioEmpresa(String pSubdominioEmpresa) {

        ResponseDto res = new ResponseDto();
        try{
            Optional<SitioEntidadDto> objEntidad =  iEntidadDao.findBySubdominioEmpresa(pSubdominioEmpresa);
            if(objEntidad.isEmpty()){
                res.status = false;
                res.message="no hay entidad";
            }else{
                res.status = true;
                res.result = objEntidad.get();
            }

            return res;
        }catch (Exception ex){
            res.status = false;
            res.message = ex.toString();
            return res;
        }
    }
}
