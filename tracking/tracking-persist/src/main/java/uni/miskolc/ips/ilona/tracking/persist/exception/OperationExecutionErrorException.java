package uni.miskolc.ips.ilona.tracking.persist.exception;

/**
 * The general dao error class. <br />
 * This exception is general amongst the other exceptions for general error
 * result.
 * 
 * @author Patrik
 *
 */
public class OperationExecutionErrorException extends TrackingDAOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OperationExecutionErrorException() {

	}

	public OperationExecutionErrorException(String message) {
		super(message);
	}

	public OperationExecutionErrorException(Throwable cause) {
		super(cause);
	}

	public OperationExecutionErrorException(String message, Throwable cause) {
		super(message, cause);
	}
}
