package bo.com.tesla.entidades.batch;

import java.util.List;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Autowired;

import bo.com.tesla.administracion.entity.DeudaClienteEntity;
import bo.com.tesla.entidades.services.IDeudaClienteService;

public class StepItemWriteListener implements ItemWriteListener<DeudaClienteEntity>{
	
	
	

	public StepItemWriteListener(Long archivoId) {	
	
	}

	@Override
	public void beforeWrite(List<? extends DeudaClienteEntity> items) {

		
	}

	@Override
	public void afterWrite(List<? extends DeudaClienteEntity> items) {

		
	}

	@Override
	public void onWriteError(Exception exception, List<? extends DeudaClienteEntity> items) {
		
		
	}

}
