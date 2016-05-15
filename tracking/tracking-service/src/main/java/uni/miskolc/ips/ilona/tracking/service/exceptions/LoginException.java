package uni.miskolc.ips.ilona.tracking.service.exceptions;

/**
 * The base class of the login exceptions.
 * 
 * @author Patrik
 *
 */
public class LoginException extends TrackingException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4612278136084229824L;

	public LoginException() {
		super();
	}
	
	public LoginException(String message) {
		super(message);
	}
	
	public LoginException(String message, Exception ex) {
		super(message, ex);
	}
}
