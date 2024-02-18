package bo.com.tesla.useful.config;

public class BusinesException  extends Exception{
	private static final long serialVersionUID = 1L;
	public BusinesException( String detailMessage) {
		this( detailMessage, null);

	}

	public BusinesException(String detailMessage, Throwable cause) {
		super(generateMessaje(detailMessage), cause);
		
	}

	private static String generateMessaje( String detailMessage) {
		return detailMessage;
	}

}
