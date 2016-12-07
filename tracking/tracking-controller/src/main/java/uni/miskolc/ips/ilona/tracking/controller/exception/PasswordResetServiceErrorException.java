package uni.miskolc.ips.ilona.tracking.controller.exception;

public class PasswordResetServiceErrorException extends TrackingControllerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int errorCode;

	public PasswordResetServiceErrorException() {
		super();
	}

	public PasswordResetServiceErrorException(String message) {
		super(message);
	}

	public PasswordResetServiceErrorException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public PasswordResetServiceErrorException(Throwable cause) {
		super(cause);
	}

	public PasswordResetServiceErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public PasswordResetServiceErrorException(String message, int errorCode, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

}
