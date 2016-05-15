package uni.miskolc.ips.ilona.tracking.service.exceptions;

public class DuplicatedUserException extends LoginException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 949255637855967995L;

	public DuplicatedUserException() {
		super();
	}
	
	public DuplicatedUserException(String message) {
		super(message);
	}
	
	public DuplicatedUserException(String message, Exception ex) {
		super(message, ex);
	}
}
