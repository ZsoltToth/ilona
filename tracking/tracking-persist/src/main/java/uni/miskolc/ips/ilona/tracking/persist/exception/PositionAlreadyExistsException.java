package uni.miskolc.ips.ilona.tracking.persist.exception;

/**
 * The position is already exists in the system.
 * 
 * @author Patrik
 *
 */
public class PositionAlreadyExistsException extends TrackingException {

	/**
	 * A generate value for serializaton.
	 */
	private static final long serialVersionUID = -2109907583812109430L;

	public PositionAlreadyExistsException() {

	}

	public PositionAlreadyExistsException(String message) {
		super(message);
	}

	public PositionAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	public PositionAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}
