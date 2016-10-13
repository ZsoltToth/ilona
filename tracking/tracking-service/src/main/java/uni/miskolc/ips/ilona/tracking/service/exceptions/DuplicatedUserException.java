package uni.miskolc.ips.ilona.tracking.service.exceptions;

/**
 * The current user is already exists.
 * @author Patrik
 *
 */
public class DuplicatedUserException extends UserServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicatedUserException() {
		super();
	}

	public DuplicatedUserException(String message) {
		super(message);
	}

	public DuplicatedUserException(Throwable cause) {
		super(cause);
	}

	public DuplicatedUserException(String message, Throwable cause) {
		super(message, cause);
	}

}
