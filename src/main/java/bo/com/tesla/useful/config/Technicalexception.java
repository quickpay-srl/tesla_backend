package bo.com.tesla.useful.config;

public class Technicalexception extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public Technicalexception(String message) {
		super(message);
	}
	
	public Technicalexception(String message, Throwable cause) {
		super(message,cause);		
	}

}
