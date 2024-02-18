package bo.com.tesla.administracion.services;

import bo.com.tesla.administracion.entity.EntidadEntity;
import bo.com.tesla.useful.config.BusinesException;
import bo.com.tesla.useful.config.Technicalexception;

import java.util.List;

public interface IEntidadRecaudadorService {

    public void postEntidadesRecaudadoesPorEntidad(Long entidadId, List<Long> recaudadorIdLst, Long usuarioId);
    public void postEntidadesRecaudadoesPorRecaudador(Long recaudadorId, List<Long> entidadIdLst, Long usuarioId);
    void verificarAccesoUsuarioRecaudador(Long usuarioRecaudadorId, Long entidadId) throws BusinesException;
}


