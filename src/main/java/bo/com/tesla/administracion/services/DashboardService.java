package bo.com.tesla.administracion.services;

import bo.com.tesla.administracion.dao.IDashboardDao;
import bo.com.tesla.administracion.dto.DashboardDto;
import bo.com.tesla.useful.config.Technicalexception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService implements  IDashboardService {

    @Autowired
    IDashboardDao iDashboardDao;

    @Override
    public DashboardDto getDashboard(Long usuarioId) {
        try{
            return iDashboardDao.findDashboard(usuarioId);
        }catch (Exception e){
            throw new Technicalexception(e.getMessage(), e.getCause());
        }
    }
}
