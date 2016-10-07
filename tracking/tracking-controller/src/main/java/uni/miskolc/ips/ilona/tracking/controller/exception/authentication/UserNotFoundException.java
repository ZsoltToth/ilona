package uni.miskolc.ips.ilona.tracking.controller.exception.authentication;

import org.springframework.security.core.AuthenticationException;

public class UserNotFoundException extends AuthenticationException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userid;

	public UserNotFoundException(String msg, String userid) {
		super(msg);
		this.userid = userid;
		// TODO Auto-generated constructor stub
	}

	public UserNotFoundException(String msg, String userid, Throwable error) {
		super(msg, error);
		this.userid = userid;
	}
}
