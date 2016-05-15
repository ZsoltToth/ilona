package uni.miskolc.ips.ilona.tracking.service.exceptions;

public class UnenabledUserException extends LoginException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7989892660587085024L;

	public UnenabledUserException() {
		super();
	}
	
	public UnenabledUserException(String message) {
		super(message);
	}
	
	public UnenabledUserException(String message, Exception ex) {
		super(message, ex);
	}
}
