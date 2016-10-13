package uni.miskolc.ips.ilona.tracking.service.exceptions;

/**
 * The current position is already exists.
 * @author Patrik
 *
 */
public class DuplicatedPositionException extends TrackingServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicatedPositionException() {
		super();
	}

	public DuplicatedPositionException(String message) {
		super(message);
	}

	public DuplicatedPositionException(Throwable cause) {
		super(cause);
	}

	public DuplicatedPositionException(String message, Throwable cause) {
		super(message, cause);
	}
}
