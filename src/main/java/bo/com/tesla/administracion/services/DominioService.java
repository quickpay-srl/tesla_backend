package bo.com.tesla.administracion.services;

import bo.com.tesla.administracion.entity.DominioEntity;
import bo.com.tesla.externos.recaudaciones.larazon.services.EndPointEntidadService;
import bo.com.tesla.recaudaciones.dao.IDominioDao;
import bo.com.tesla.recaudaciones.dto.DominioDto;
import bo.com.tesla.useful.converter.UtilBase64Image;
import bo.com.tesla.useful.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DominioService implements IDominioService {
    private Logger logger = LoggerFactory.getLogger(DominioService.class);

    @Autowired
    private IDominioDao iDominioDao;

    @Value("${tesla.path.tipo_servicios}")
    private String tipoServicios;

    @Override
    public List<DominioDto> getListDominios(String dominio) {
        return iDominioDao.findDominioDtoLstByDominio(dominio);
    }
    @Override
    public ResponseDto getDominios(String dominio) {
        ResponseDto res = new ResponseDto();
        try{
            List<DominioDto> lstDominio2 = new ArrayList<>();
            List<DominioDto> lstDominio = iDominioDao.findDominioDtoLstByDominio(dominio);
            if (!lstDominio.isEmpty()) {
                for ( DominioDto objDto : lstDominio) {
                    if(objDto.imagen64 !=null)
                        objDto.imagen64  = "data:image/png;base64,"+UtilBase64Image.encoder(tipoServicios +"/"+ objDto.imagen64);
                    lstDominio2.add(objDto);
                }
                res.status=true;
                res.message="Dominios encontrados";
                res.result = lstDominio2;
            }else{
                res.status=false;
                res.message="Dominios no encontrados";
            }
        }catch (Exception ex ){
            res.status=false;
            res.message="Error, comuniquese con administrador";
        }
        return res;
    }

    @Override
	public List<DominioEntity> findByDominio(String dominio) {	
		return iDominioDao.findByDominio(dominio);
	}

    @Override
    public List<DominioDto> getLstDominiosByAgrupador(Long agrupadorId) {
        return iDominioDao.findLstByAgrupador(agrupadorId);
    }

	@Override
	public DominioEntity findById(Long dominioId) {	
		return this.iDominioDao.findById(dominioId).get();
	}

    @Override
    public Long getEntidadIdByDominio(String dominio) {
        Optional<String> entidadIdOptional = iDominioDao.findEntidadIdByDominio(dominio);
        if(!entidadIdOptional.isPresent()) {
            this.logger.warn("------------------------------------");
            this.logger.warn("dominio =  " + dominio + ", No existe reg activo en Dominios");
            this.logger.warn("------------------------------------");
            return 0L; //Método es utilizado en cobro en Entidades, no debe generar exception
        }
        return Long.valueOf(entidadIdOptional.get());
    }

}
