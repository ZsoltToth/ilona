package uni.miskolc.ips.ilona.tracking.service.exceptions;

/**
 * This is the parent class of the device exceptions related to the device management.
 * @author Patrik / A5USL0
 *
 */
public class DeviceServiceException extends TrackingServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeviceServiceException() {
		super();
	}

	public DeviceServiceException(String message) {
		super(message);
	}

	public DeviceServiceException(Throwable cause) {
		super(cause);
	}

	public DeviceServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
