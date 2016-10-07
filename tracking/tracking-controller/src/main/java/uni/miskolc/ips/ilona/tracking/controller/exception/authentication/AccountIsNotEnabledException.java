package uni.miskolc.ips.ilona.tracking.controller.exception.authentication;

import org.springframework.security.core.AuthenticationException;

public class AccountIsNotEnabledException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountIsNotEnabledException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	public AccountIsNotEnabledException(String msg, Throwable error) {
		super(msg, error);
	}

}
