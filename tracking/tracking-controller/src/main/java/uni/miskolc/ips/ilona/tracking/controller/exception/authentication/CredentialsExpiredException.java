package uni.miskolc.ips.ilona.tracking.controller.exception.authentication;

import org.springframework.security.core.AuthenticationException;

public class CredentialsExpiredException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CredentialsExpiredException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	public CredentialsExpiredException(String msg, Throwable error) {
		super(msg, error);
	}

}
