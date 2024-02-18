package bo.com.tesla.administracion.services;

import bo.com.tesla.administracion.dto.RecaudadorAdmDto;
import bo.com.tesla.administracion.entity.DominioEntity;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.administracion.entity.SucursalEntity;
import bo.com.tesla.useful.config.Technicalexception;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;

public interface IRecaudadorService {

    public RecaudadorAdmDto addUpdateRecaudador(RecaudadorAdmDto recaudadorAdmDto, Long usuarioId);
    public void setTransaccion(Long recaudadorId, String transaccion, Long usuarioId);
    public void setLstTransaccion(List<Long> recaudadorIdLst, String transaccion, Long usuarioId);
    public RecaudadorAdmDto getRecaudadoraById(Long recaudadorId);
    public List<RecaudadorAdmDto> getAllRecaudadoras();
    
    public  List<RecaudadorEntity> findAll();
    
   
}
