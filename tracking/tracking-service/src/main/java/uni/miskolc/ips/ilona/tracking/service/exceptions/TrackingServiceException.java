package uni.miskolc.ips.ilona.tracking.service.exceptions;

/**
 * This is the ancestor of the exceptions in the tracking service module. This
 * class userful for general error handling while using the service classes.
 * 
 * @author Patrik
 *
 */
public class TrackingServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TrackingServiceException() {
		super();
	}

	public TrackingServiceException(String message) {
		super(message);
	}

	public TrackingServiceException(Throwable cause) {
		super(cause);
	}

	public TrackingServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
