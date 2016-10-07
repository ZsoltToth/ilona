package uni.miskolc.ips.ilona.tracking.controller.exception.authentication;

import org.springframework.security.core.AuthenticationException;

public class ServiceErrorException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceErrorException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	public ServiceErrorException(String msg, Throwable error) {
		super(msg, error);
	}

}
