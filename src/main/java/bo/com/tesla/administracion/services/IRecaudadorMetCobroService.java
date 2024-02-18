package bo.com.tesla.administracion.services;

import bo.com.tesla.administracion.dto.RecaudadorMetodoCobroDto;
import bo.com.tesla.administracion.entity.RecaudadorEntity;
import bo.com.tesla.useful.config.BusinesException;

import java.util.List;

public interface IRecaudadorMetCobroService {
    void addRecaudadoresMetCobro(RecaudadorEntity recaudadorEntity,
                                 Long usuarioId,
                                 List<Long> metodoCobroIdLst);

   void updateRecaudadoresMetCobro(RecaudadorEntity recaudadorEntity,
                                   Long usuarioId,
                                   List<Long> metodoCobroIdLst);
    List<String> findRecMetCobroLst(Long recaudadorId);
    List<Long> findRecMetCobroIdLst(Long recaudadorId);
    List<RecaudadorMetodoCobroDto> findRecMetCobDtoBRecaudador(Long usuarioId) throws BusinesException;
}
