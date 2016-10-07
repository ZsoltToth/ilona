package uni.miskolc.ips.ilona.tracking.controller.exception.authentication;

import org.springframework.security.core.AuthenticationException;

public class AccountLockedException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long lockedUntil;

	public AccountLockedException(String msg, long lockedUntil) {
		super(msg);
		this.lockedUntil = lockedUntil;
		// TODO Auto-generated constructor stub
	}

	public AccountLockedException(String msg, long lockedUntil, Throwable error) {
		super(msg, error);
		this.lockedUntil = lockedUntil;
	}

	public long getLockedUntil() {
		return lockedUntil;
	}

}
