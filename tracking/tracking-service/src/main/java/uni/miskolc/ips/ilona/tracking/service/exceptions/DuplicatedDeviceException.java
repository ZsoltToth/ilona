package uni.miskolc.ips.ilona.tracking.service.exceptions;

/**
 * Duplicated device.
 * @author Patrik / A5USL0
 *
 */
public class DuplicatedDeviceException extends DeviceServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicatedDeviceException() {
		super();
	}

	public DuplicatedDeviceException(String message) {
		super(message);
	}

	public DuplicatedDeviceException(Throwable cause) {
		super(cause);
	}

	public DuplicatedDeviceException(String message, Throwable cause) {
		super(message, cause);
	}
}
