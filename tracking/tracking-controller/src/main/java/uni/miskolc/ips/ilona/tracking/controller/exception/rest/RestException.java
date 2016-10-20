package uni.miskolc.ips.ilona.tracking.controller.exception.rest;

/**
 * Rest / mobile exceptions base class.
 * 
 * @author Patrik
 *
 */
public class RestException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RestException() {
		super();
	}

	public RestException(String message) {
		super(message);
	}

	public RestException(Throwable cause) {
		super(cause);
	}

	public RestException(String message, Throwable cause) {
		super(message, cause);
	}
}
