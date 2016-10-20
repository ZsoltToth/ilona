package uni.miskolc.ips.ilona.tracking.persist.mysql.model;

import uni.miskolc.ips.ilona.tracking.model.PasswordRecoveryToken;

/**
 * {@link PasswordRecoveryToken} mapper.<br/>
 * Because the milliseconds part of the date cannot be stored directly in the
 * database. A system must load a long value and convert to date after the
 * loading. <br/>
 * The validUntil property holds the timestamp value with the milliseconds part.
 * 
 * @author Patrik / A5USL0
 *
 */
public class PasswordRecoveryTokenMapper {

	private String userid;

	private String token;

	private long validUntil;

	public PasswordRecoveryTokenMapper() {

	}

	public PasswordRecoveryTokenMapper(String userid, String token, long validUntil) {
		super();
		this.userid = userid;
		this.token = token;
		this.validUntil = validUntil;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(long validUntil) {
		this.validUntil = validUntil;
	}

}
