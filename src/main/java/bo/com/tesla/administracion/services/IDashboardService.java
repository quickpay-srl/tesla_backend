package bo.com.tesla.administracion.services;

import bo.com.tesla.administracion.dto.DashboardDto;
import bo.com.tesla.recaudaciones.dto.DominioDto;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IDashboardService {
    public DashboardDto getDashboard( Long usuarioId);


}
