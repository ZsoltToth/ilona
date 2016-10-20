package uni.miskolc.ips.ilona.tracking.persist.exception;

/**
 * General parent exception related to the device dao operations.
 * 
 * @author Patrik
 *
 */
public class DeviceDAOException extends TrackingDAOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeviceDAOException() {

	}

	public DeviceDAOException(String message) {
		super(message);
	}

	public DeviceDAOException(Throwable cause) {
		super(cause);
	}

	public DeviceDAOException(String message, Throwable cause) {
		super(message, cause);
	}
}
