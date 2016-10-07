package uni.miskolc.ips.ilona.tracking.controller.exception.authentication;

import org.springframework.security.core.AuthenticationException;

public class AccountExpiredException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountExpiredException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	public AccountExpiredException(String msg, Throwable error) {
		super(msg, error);
	}

}
