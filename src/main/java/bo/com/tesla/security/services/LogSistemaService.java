package bo.com.tesla.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bo.com.tesla.administracion.entity.LogSistemaEntity;
import bo.com.tesla.security.dao.ILogSistemasDao;

@Service
public class LogSistemaService implements ILogSistemaService {

	@Autowired
	private ILogSistemasDao logSistemasDao;
	
	@Override
	public LogSistemaEntity save(LogSistemaEntity entity) {		
		return logSistemasDao.save(entity);
	}

}
