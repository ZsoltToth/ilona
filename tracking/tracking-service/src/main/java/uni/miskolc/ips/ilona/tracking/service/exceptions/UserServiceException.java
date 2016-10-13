package uni.miskolc.ips.ilona.tracking.service.exceptions;

/**
 * This is the base the class of the exceptions related to the user management.
 * 
 * @author Patrik
 *
 */
public class UserServiceException extends TrackingServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserServiceException() {
		super();
	}

	public UserServiceException(String message) {
		super(message);
	}

	public UserServiceException(Throwable cause) {
		super(cause);
	}

	public UserServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
