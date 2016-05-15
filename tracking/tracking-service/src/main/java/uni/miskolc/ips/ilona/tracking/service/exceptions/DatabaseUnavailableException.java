package uni.miskolc.ips.ilona.tracking.service.exceptions;

public class DatabaseUnavailableException extends LoginException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6247626517880760889L;

	public DatabaseUnavailableException() {
		super();
	}
	
	public DatabaseUnavailableException(String message) {
		super(message);
	}
	
	public DatabaseUnavailableException(String message, Exception ex) {
		super(message, ex);
	}
}
