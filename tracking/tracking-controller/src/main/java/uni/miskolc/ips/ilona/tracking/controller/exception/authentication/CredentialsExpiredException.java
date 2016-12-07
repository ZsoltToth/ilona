package uni.miskolc.ips.ilona.tracking.controller.exception.authentication;

import org.springframework.security.core.AuthenticationException;

public class CredentialsExpiredException extends AuthenticationException {

	private String userid;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CredentialsExpiredException(String msg) {
		super(msg);
	}

	public CredentialsExpiredException(String msg, String userid) {
		super(msg);
		this.userid = userid;
	}

	public CredentialsExpiredException(String msg, Throwable error) {
		super(msg, error);
	}

	public CredentialsExpiredException(String msg, String userid, Throwable error) {
		super(msg, error);
		this.userid = userid;
	}

	public String getUserid() {
		return userid;
	}

}
