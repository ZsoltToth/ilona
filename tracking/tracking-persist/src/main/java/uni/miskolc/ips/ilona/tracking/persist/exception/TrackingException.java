package uni.miskolc.ips.ilona.tracking.persist.exception;

public class TrackingException extends TrackingDAOException {

	/**
	 * A generate value for serializaton.
	 */
	private static final long serialVersionUID = -2109907583812109430L;

	public TrackingException() {

	}

	public TrackingException(String message) {
		super(message);
	}

	public TrackingException(Throwable cause) {
		super(cause);
	}

	public TrackingException(String message, Throwable cause) {
		super(message, cause);
	}
}
