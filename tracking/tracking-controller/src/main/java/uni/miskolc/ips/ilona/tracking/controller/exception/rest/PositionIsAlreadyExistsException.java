package uni.miskolc.ips.ilona.tracking.controller.exception.rest;

public class PositionIsAlreadyExistsException extends RestException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PositionIsAlreadyExistsException() {
		super();
	}

	public PositionIsAlreadyExistsException(String message) {
		super(message);
	}

	public PositionIsAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	public PositionIsAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

}
