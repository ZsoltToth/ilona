package uni.miskolc.ips.ilona.tracking.service.exceptions;

/**
 * The base exception of the whole exception module. Every other exceptions
 * inherite from this exception.
 * 
 * This class is inherited from Exception class.
 * 
 * @author Patrik
 *
 */
public class TrackingException extends Exception {

	/**
	 * Serialize constant.
	 */
	private static final long serialVersionUID = 4000307461335952383L;

	/**
	 * 
	 */
	public TrackingException() {
		super();
	}

	/**
	 * A constructor with one message parameter.
	 * @param message The string format of the error.
	 */
	public TrackingException(String message) {
		super(message);
	}
	/**
	 * A constructor with 2 parameters.
	 * @param message The string format of the additional information.
	 * @param ex The embedded exception.
	 */
	public TrackingException(String message, Exception ex) {
		super(message, ex);
	}
}
