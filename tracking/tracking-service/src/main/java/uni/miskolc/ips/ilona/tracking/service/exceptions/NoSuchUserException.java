package uni.miskolc.ips.ilona.tracking.service.exceptions;

public class NoSuchUserException extends LoginException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6780706532174469411L;

	public NoSuchUserException() {
		super();
	}
	
	public NoSuchUserException(String message) {
		super(message);
	}
	
	public NoSuchUserException(String message, Exception ex) {
		super(message, ex);
	}
}
