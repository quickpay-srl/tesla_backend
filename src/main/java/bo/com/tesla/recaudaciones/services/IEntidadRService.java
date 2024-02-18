package bo.com.tesla.recaudaciones.services;

import bo.com.tesla.administracion.dto.EntidadAdmDto;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.administracion.entity.SegUsuarioEntity;
import bo.com.tesla.recaudaciones.dto.DominioDto;
import bo.com.tesla.recaudaciones.dto.EntidadDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IEntidadRService {

    /*********************ABM**************************/
    public EntidadAdmDto addUpdateEntidad(EntidadAdmDto entidadAdmDto, Long usuarioId);
    public void setTransaccion(Long entidadId, String transaccion, Long usuarioId);
    public void setLstTransaccion(List<Long> entidadIdLst, String transaccion, Long usuarioId);
    //public void setLstEstado(List<Long> entidadIdLst, String estado, Long usuarioId);

    public EntidadAdmDto getEntidadById(Long entidadId);
    public List<EntidadAdmDto> getAllEntidades();
    //public void updatePathLogo(Long entidadId, String pathLogo, Long usuarioId);
    public void uploadLogo(MultipartFile file, Long entidadId, Long usuarioId) throws Exception;
    /*********************COBROS**************************/
    public List<EntidadDto> getEntidadesByTipoEntidad(Long tipoEntidadId, SegUsuarioEntity usuario);
    public List<EntidadDto> getByRecaudadoraId(SegUsuarioEntity usuario);
    public List<DominioDto> getTipoEntidadByRecaudador(SegUsuarioEntity usuario);
    public List<DominioDto> findTipoEntidadPagadoras(SegUsuarioEntity usuario);
    
}