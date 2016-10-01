package uni.miskolc.ips.ilona.tracking.service.exceptions;

public class InvalidMeasurementException extends TrackingServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidMeasurementException() {
		super();
	}

	public InvalidMeasurementException(String message) {
		super(message);
	}

	public InvalidMeasurementException(Throwable cause) {
		super(cause);
	}

	public InvalidMeasurementException(String message, Throwable cause) {
		super(message, cause);
	}
}
