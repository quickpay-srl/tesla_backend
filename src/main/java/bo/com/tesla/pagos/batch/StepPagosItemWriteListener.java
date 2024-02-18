package bo.com.tesla.pagos.batch;

import java.util.List;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Autowired;

import bo.com.tesla.administracion.entity.DeudaClienteEntity;
import bo.com.tesla.entidades.services.IDeudaClienteService;
import bo.com.tesla.pagos.dto.PPagosDto;

public class StepPagosItemWriteListener implements ItemWriteListener<PPagosDto>{
	
	
	public StepPagosItemWriteListener() {
		
	}

	@Override
	public void beforeWrite(List<? extends PPagosDto> items) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterWrite(List<? extends PPagosDto> items) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWriteError(Exception exception, List<? extends PPagosDto> items) {
		// TODO Auto-generated method stub
		
	}
	
	

}
