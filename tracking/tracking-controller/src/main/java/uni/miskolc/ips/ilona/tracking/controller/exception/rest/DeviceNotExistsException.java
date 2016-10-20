package uni.miskolc.ips.ilona.tracking.controller.exception.rest;

public class DeviceNotExistsException extends RestException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeviceNotExistsException() {
		super();
	}

	public DeviceNotExistsException(String message) {
		super(message);
	}

	public DeviceNotExistsException(Throwable cause) {
		super(cause);
	}

	public DeviceNotExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}
