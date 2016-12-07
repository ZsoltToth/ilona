package uni.miskolc.ips.ilona.tracking.util;

/**
 * This is responsible for managing the program flow in the tracking
 * system.<br/>
 * <br/>
 * 
 * This holds the necessary properties to manage the behavior of the tracking
 * system. <br/>
 * <br/>
 * 
 * With this class you can manage the security checks in the system.<br/>
 * user account expiration check, bad login check etc.<br/>
 * <br/>
 * 
 * <b>This class is syncronized for every field individually! </b>
 * 
 * @author Patrik / A5USl0
 *
 */
public class TrackingModuleCentralManager {

	/*
	 * TIME CONSTANTS
	 */
	// =======================================================================================

	/*
	 * Time intervals in milliseconds:
	 */
	// 1 hour: 3600000L
	// 1 day: 86400000L
	// 1 week: 604800000L
	// 1 month: 2678400000L
	// 1 year: 31536000000L

	/**
	 * Tracking module utility property.<br />
	 * One hour in milliseconds with type long.
	 */
	public static final long oneHourInMilliseconds = 3_600_000L;

	/**
	 * Tracking module utility property.<br />
	 * Five seconds in milliseconds.
	 */
	public static final long fiveMinutesInMilliseconds = 300000L;

	/**
	 * Tracking module utility property.<br />
	 * One day in milliseconds with type long.
	 */
	public static final long oneDayInMiliseconds = 86_400_000L;

	/**
	 * Tracking module utility property.<br />
	 * One week in milliseconds with type long.
	 */
	public static final long oneWeekInMilliseconds = 604_800_000L;

	/**
	 * Tracking module utility property.<br />
	 * One month in milliseconds with type long.
	 */
	public static final long oneMonthInMilliseconds = 2_678_400_000L;

	/**
	 * Tracking module utility property.<br />
	 * One year in milliseconds with type long.
	 */
	public static final long oneYearInMilliseconds = 31_536_000_000L;

	/*
	 * LOCKS
	 */
	// ===================================================================================
	private Object enabledCheckEnabledLock = new Object();

	private Object accountExpirationCheckEnabledLock = new Object();

	private Object accountExpirationTimeLock = new Object();

	private Object credentialsValidityPeriodLock = new Object();

	private Object lockedCheckEnabledLock = new Object();

	private Object badLoginsUpperBoundLock = new Object();

	private Object lockedTimeAfterBadLoginsLock = new Object();

	private Object passwordRecoveryTokenValidityTimeLock = new Object();

	private Object lockedCheckIntervalLock = new Object();

	/*
	 * ACCOUNT CHECK RESTRICTIONS
	 */
	// =====================================================================================

	/**
	 * If this field is true, then the system will execute the enabled checking.
	 * 
	 */
	private boolean enabledCheckEnabled = true;

	/**
	 * If this field is true, then the system will check the account expiration.
	 */
	private boolean accountExpirationCheckEnabled = true;

	/**
	 * Account expiration: (last login date in milliseconds) +
	 * accountExpirationTime <br/>
	 * The account validation time after last login if this value is less than
	 * the current system timestamp, the account is expired!
	 * 
	 */
	private long accountExpirationTime = 31_536_000_000L;

	/**
	 * Credentials validity time in milliseconds. <br />
	 * Password expiration: (password creation time) + credentialsValidityPeriod
	 * <br/>
	 * If this value is less than the current system time, then the password is
	 * invalid.
	 * 
	 */
	private long credentialsValidityPeriod = 31536000000L;

	/**
	 * The account will be locked if the number of the bad login exceed this
	 * value.
	 */
	private long badLoginsUpperBound = 10;

	/**
	 * If this field is true, then the system will check the account lock
	 * status. The account is locked if the bad login number exceed a given
	 * value.
	 */
	private boolean lockedCheckEnabled = true;

	private long lockedCheckInterval = fiveMinutesInMilliseconds;

	/**
	 * The lock period after to many bad logins.
	 */
	private long lockedTimeAfterBadLogins = 3600000L;

	/**
	 * Formula: Token creation time + passwordRecoveryTokenValidityTime <br />
	 * If the token time less than the current time, the token is invalid.
	 */
	private long passwordRecoveryTokenValidityTime = 86_400_000L;

	/*
	 * Setters / getters with lock/field
	 */
	// =======================================================================================

	/**
	 * Credentials validity time in milliseconds. <br />
	 * Password expiration: (password creation time) + credentialsValidityPeriod
	 * <br/>
	 * If this value is less than the current system time, then the password is
	 * invalid.<br/>
	 * <br/>
	 * 
	 * <b>The syncronizations(setter / getter) of this field is independent from
	 * the syncronization of the other properties!</b>
	 * 
	 * @return the current value of the period.
	 */
	public long getCredentialsValidityPeriod() {
		synchronized (credentialsValidityPeriodLock) {
			return this.credentialsValidityPeriod;
		}

	}

	/**
	 * Credentials validity time in milliseconds. <br />
	 * Password expiration: (password creation time) + credentialsValidityPeriod
	 * <br/>
	 * If this value is less than the current system time, then the password is
	 * invalid.<br/>
	 * <br/>
	 * 
	 * <b>The syncronizations(setter / getter) of this field is independent from
	 * the syncronization of the other properties!</b>
	 * 
	 * @param credentialsValidityPeriod
	 *            The new value of the period. If the value is less than 0, then
	 *            the new value will be ignored!
	 */
	public void setCredentialsValidityPeriod(long credentialsValidityPeriod) {
		synchronized (credentialsValidityPeriodLock) {
			if (credentialsValidityPeriod < 0) {
				return;
			}
			this.credentialsValidityPeriod = credentialsValidityPeriod;
		}
	}

	/**
	 * Formula: Token creation time + passwordRecoveryTokenValidityTime <br />
	 * If the token time less than the current time, the token is invalid.
	 * 
	 * <b>The syncronizations(setter / getter) of this field is independent from
	 * the syncronization of the other properties!</b>
	 * 
	 * @return The current value of the validity time.
	 */
	public long getPasswordRecoveryTokenValidityTime() {
		synchronized (passwordRecoveryTokenValidityTimeLock) {
			return this.passwordRecoveryTokenValidityTime;
		}

	}

	/**
	 * Formula: Token creation time + passwordRecoveryTokenValidityTime <br />
	 * If the token time less than the current time, the token is invalid.
	 * 
	 * <b>The syncronizations(setter / getter) of this field is independent from
	 * the syncronization of the other properties!</b>
	 * 
	 * @param passwordRecoveryTokenValidityTime
	 *            The new value of vality time. If the value is less than 0,
	 *            then the new value will be ignored!
	 */
	public void setPasswordRecoveryTokenValidityTime(long passwordRecoveryTokenValidityTime) {
		synchronized (passwordRecoveryTokenValidityTimeLock) {
			if (passwordRecoveryTokenValidityTime < 0) {
				return;
			}
			this.passwordRecoveryTokenValidityTime = passwordRecoveryTokenValidityTime;
		}
	}

	/**
	 * Account expiration: (last login date in milliseconds) +
	 * accountExpirationTime <br/>
	 * The account validation time after last login if this value is less than
	 * the current system timestamp, the account is expired!
	 * 
	 * <b>The syncronizations(setter / getter) of this field is independent from
	 * the syncronization of the other properties!</b>
	 * 
	 * @return the current value of the account expiration time.
	 */
	public long getAccountExpirationTime() {
		synchronized (accountExpirationTimeLock) {
			return this.accountExpirationTime;
		}

	}

	/**
	 * Account expiration: (last login date in milliseconds) +
	 * accountExpirationTime <br/>
	 * The account validation time after last login if this value is less than
	 * the current system timestamp, the account is expired!
	 * 
	 * <b>The syncronizations(setter / getter) of this field is independent from
	 * the syncronization of the other properties!</b>
	 * 
	 * @param accountExpirationTime
	 *            The new expiration time value. If the value is less than 0,
	 *            then the new value will be ignored!
	 */
	public void setAccountExpirationTime(long accountExpirationTime) {
		synchronized (accountExpirationTimeLock) {
			if (accountExpirationTime < 0) {
				return;
			}
			this.accountExpirationTime = accountExpirationTime;
		}
	}

	/**
	 * If this field is true, then the system will execute the enabled checking.
	 * 
	 * <b>The syncronizations(setter / getter) of this field is independent from
	 * the syncronization of the other properties!</b>
	 * 
	 * @return the current state of the enabled checking.
	 */
	public boolean isEnabledCheckEnabled() {
		synchronized (enabledCheckEnabledLock) {
			return enabledCheckEnabled;
		}

	}

	/**
	 * If this field is true, then the system will execute the enabled checking.
	 * 
	 * <b>The syncronizations(setter / getter) of this field is independent from
	 * the syncronization of the other properties!</b>
	 * 
	 * @param enabledCheckEnabled
	 *            The new state of the enabled checking.
	 */
	public void setEnabledCheckEnabled(boolean enabledCheckEnabled) {
		synchronized (enabledCheckEnabledLock) {
			this.enabledCheckEnabled = enabledCheckEnabled;
		}
	}

	/**
	 * If this field is true, then the system will check the account expiration.
	 * 
	 * <b>The syncronizations(setter / getter) of this field is independent from
	 * the syncronization of the other properties!</b>
	 * 
	 * @return The current state of the account expiration checking.
	 */
	public boolean isAccountExpirationCheckEnabled() {
		synchronized (accountExpirationCheckEnabledLock) {
			return accountExpirationCheckEnabled;
		}
	}

	/**
	 * If this field is true, then the system will check the account expiration.
	 * 
	 * <b>The syncronizations(setter / getter) of this field is independent from
	 * the syncronization of the other properties!</b>
	 * 
	 * @param accountExpirationCheckEnabled
	 *            The new state of the account expiration checking.
	 */
	public void setAccountExpirationCheckEnabled(boolean accountExpirationCheckEnabled) {
		synchronized (accountExpirationCheckEnabledLock) {
			this.accountExpirationCheckEnabled = accountExpirationCheckEnabled;
		}
	}

	/**
	 * The account will be locked if the number of the bad login exceed this
	 * value.
	 * 
	 * <b>The syncronizations(setter / getter) of this field is independent from
	 * the syncronization of the other properties!</b>
	 * 
	 * @return The current value of the upper bound.
	 */
	public long getBadLoginsUpperBound() {
		synchronized (badLoginsUpperBoundLock) {
			return badLoginsUpperBound;
		}
	}

	/**
	 * The account will be locked if the number of the bad login exceed this
	 * value.
	 * 
	 * <b>The syncronizations(setter / getter) of this field is independent from
	 * the syncronization of the other properties!</b>
	 * 
	 * @param badLoginsUpperBound
	 *            The new value of the upper bound. If the value is less than 0,
	 *            then the new value will be ignored!
	 */
	public void setBadLoginsUpperBound(long badLoginsUpperBound) {
		synchronized (badLoginsUpperBoundLock) {
			if (badLoginsUpperBound < 0) {
				return;
			}
			this.badLoginsUpperBound = badLoginsUpperBound;
		}
	}

	/**
	 * If this field is true, then the system will check the account lock
	 * status. The account is locked if the bad login number exceed a given
	 * value.
	 * 
	 * <b>The syncronizations(setter / getter) of this field is independent from
	 * the syncronization of the other properties!</b>
	 * 
	 * @return The current status of the lock checking.
	 */
	public boolean isLockedCheckEnabled() {
		synchronized (lockedCheckEnabledLock) {
			return lockedCheckEnabled;
		}
	}

	/**
	 * If this field is true, then the system will check the account lock
	 * status. The account is locked if the bad login number exceed a given
	 * value.
	 * 
	 * <b>The syncronizations(setter / getter) of this field is independent from
	 * the syncronization of the other properties!</b>
	 * 
	 * @param lockedCheckEnabled
	 *            The new status of the lock checking.
	 */
	public void setLockedCheckEnabled(boolean lockedCheckEnabled) {
		synchronized (lockedCheckEnabledLock) {
			this.lockedCheckEnabled = lockedCheckEnabled;
		}
	}

	/**
	 * The lock period after to many bad logins.
	 * 
	 * <b>The syncronizations(setter / getter) of this field is independent from
	 * the syncronization of the other properties!</b>
	 * 
	 * @return The current value of the lock.
	 */
	public long getLockedTimeAfterBadLogins() {
		synchronized (lockedTimeAfterBadLoginsLock) {
			return lockedTimeAfterBadLogins;
		}
	}

	/**
	 * The lock period after to many bad logins.
	 * 
	 * <b>The syncronizations(setter / getter) of this field is independent from
	 * the syncronization of the other properties!</b>
	 * 
	 * @param lockedTimeAfterBadLogins
	 *            The new lock period. If the value is less than 0, then the new
	 *            value will be ignored!
	 */
	public void setLockedTimeAfterBadLogins(long lockedTimeAfterBadLogins) {
		synchronized (lockedTimeAfterBadLoginsLock) {
			if (lockedTimeAfterBadLogins < 0) {
				return;
			}
			this.lockedTimeAfterBadLogins = lockedTimeAfterBadLogins;
		}
	}

	/**
	 * The system will check the bad logins after this pattern: <br/>
	 * timeNow - this value > current value. If this expression is true, then
	 * the system will count that bad login.
	 * 
	 * @return
	 */
	public long getLockedCheckInterval() {
		synchronized (lockedCheckIntervalLock) {
			return lockedCheckInterval;
		}

	}

	/**
	 * The system will check the bad logins after this pattern: <br/>
	 * timeNow - this value > current value. If this expression is true, then
	 * the system will count that bad login.
	 * 
	 * @param lockedCheckInterval
	 */
	public void setLockedCheckInterval(long lockedCheckInterval) {
		synchronized (lockedCheckIntervalLock) {
			this.lockedCheckInterval = lockedCheckInterval;
		}

	}

}
