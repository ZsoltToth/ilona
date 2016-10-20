package uni.miskolc.ips.ilona.tracking.persist.exception;

/**
 * The device is not found in the system.
 * 
 * @author Patrik
 *
 */
public class DeviceNotFoundException extends DeviceDAOException {

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
