package uni.miskolc.ips.ilona.tracking.controller.exception.rest;

/**
 * 
 * @author Patrik
 *
 */
public class InvalidAuthorizationException extends RestException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidAuthorizationException() {
		super();
	}

	public InvalidAuthorizationException(String message) {
		super(message);
	}

	public InvalidAuthorizationException(Throwable cause) {
		super(cause);
	}

	public InvalidAuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}
}
