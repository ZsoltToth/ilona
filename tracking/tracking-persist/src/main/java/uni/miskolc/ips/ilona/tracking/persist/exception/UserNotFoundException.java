package uni.miskolc.ips.ilona.tracking.persist.exception;

/**
 * The current user is not found.
 * 
 * @author Patrik
 *
 */
public class UserNotFoundException extends UserDAOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -712157348319823447L;

	public UserNotFoundException() {

	}

	public UserNotFoundException(String message) {
		super(message);
	}

	public UserNotFoundException(Throwable cause) {
		super(cause);
	}

	public UserNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
