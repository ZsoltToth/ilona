package uni.miskolc.ips.ilona.tracking.service.exceptions;

/**
 * Device not found exception in the tracking service module.
 * @author Patrik / A5USL0
 */
public class DeviceNotFoundException extends DeviceServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeviceNotFoundException() {
		super();
	}

	public DeviceNotFoundException(String message) {
		super(message);
	}

	public DeviceNotFoundException(Throwable cause) {
		super(cause);
	}

	public DeviceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
