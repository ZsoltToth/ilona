package uni.miskolc.ips.ilona.tracking.persist.exception;

public class TrackingLoginDAOException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1077131508941134323L;

	public TrackingLoginDAOException() {
		super();
	}

	public TrackingLoginDAOException(String message) {
		super(message);
	}

	public TrackingLoginDAOException(String message, Exception ex) {
		super(message, ex);
	}
}
