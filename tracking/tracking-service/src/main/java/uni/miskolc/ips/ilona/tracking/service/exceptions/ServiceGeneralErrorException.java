package uni.miskolc.ips.ilona.tracking.service.exceptions;

/**
 * This class represents the general error in the service classes.
 * 
 * @author Patrik
 *
 */
public class ServiceGeneralErrorException extends TrackingServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceGeneralErrorException() {
		super();
	}

	public ServiceGeneralErrorException(String message) {
		super(message);
	}

	public ServiceGeneralErrorException(Throwable cause) {
		super(cause);
	}

	public ServiceGeneralErrorException(String message, Throwable cause) {
		super(message, cause);
	}

}
