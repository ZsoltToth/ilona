package uni.miskolc.ips.ilona.tracking.controller.exception.rest;

public class InvalidInputDataException extends RestException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidInputDataException() {
		super();
	}

	public InvalidInputDataException(String message) {
		super(message);
	}

	public InvalidInputDataException(Throwable cause) {
		super(cause);
	}

	public InvalidInputDataException(String message, Throwable cause) {
		super(message, cause);
	}
}
