package uni.miskolc.ips.ilona.tracking.service.exceptions;

public class InvalidDataException extends LoginException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2093119738392608401L;

	public InvalidDataException() {
		super();
	}
	
	public InvalidDataException(String message) {
		super(message);
	}
	
	public InvalidDataException(String message, Exception ex) {
		super(message, ex);
	}
}
