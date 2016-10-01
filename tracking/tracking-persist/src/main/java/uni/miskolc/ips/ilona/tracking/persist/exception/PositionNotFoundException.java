package uni.miskolc.ips.ilona.tracking.persist.exception;

public class PositionNotFoundException extends TrackingException {

	/**
	 * A generate value for serializaton.
	 */
	private static final long serialVersionUID = -2109907583812109430L;

	public PositionNotFoundException() {

	}

	public PositionNotFoundException(String message) {
		super(message);
	}

	public PositionNotFoundException(Throwable cause) {
		super(cause);
	}

	public PositionNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
