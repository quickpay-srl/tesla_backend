package bo.com.tesla.entidades.batch;

import org.springframework.batch.core.ItemReadListener;
import org.springframework.beans.factory.annotation.Autowired;

import bo.com.tesla.administracion.entity.DeudaClienteEntity;
import bo.com.tesla.entidades.services.IDeudaClienteService;
import bo.com.tesla.useful.config.BusinesException;


public class StepItemReadListener implements  ItemReadListener<DeudaClienteEntity>{
	
	private Long  archivoId;	
	
	@Autowired
	private IDeudaClienteService deudaClienteService;

	public StepItemReadListener(Long archivoId) {	
		this.archivoId = archivoId;
	}

	@Override
	public void beforeRead() {				
	}

	@Override
	public void afterRead(DeudaClienteEntity item) {		
		
	}

	@Override
	public void onReadError(Exception ex)  {
		
		
		
		
	}

	

}
