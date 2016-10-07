package uni.miskolc.ips.ilona.tracking.controller.exception.authentication;

import org.springframework.security.core.AuthenticationException;

public class InvalidUsernamePasswordException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidUsernamePasswordException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	public InvalidUsernamePasswordException(String msg, Throwable error) {
		super(msg, error);
	}

}
