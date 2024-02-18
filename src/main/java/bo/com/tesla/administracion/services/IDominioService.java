package bo.com.tesla.administracion.services;

import bo.com.tesla.administracion.entity.DominioEntity;
import bo.com.tesla.recaudaciones.dto.DominioDto;
import bo.com.tesla.useful.dto.ResponseDto;

import java.util.List;

public interface IDominioService {
    public List<DominioDto> getListDominios(String dominio);
    public ResponseDto getDominios(String dominio);

    public List<DominioEntity> findByDominio(String dominio);

    public List<DominioDto> getLstDominiosByAgrupador(Long agrupadorId);
    
    public DominioEntity findById(Long dominioId);

    /*DominioEntity findDominioByDominio(String dominio);
    Long findEntidadIdByDominio(String dominio);*/
    Long getEntidadIdByDominio(String dominio);

}
