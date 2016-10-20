package uni.miskolc.ips.ilona.tracking.persist;

import java.util.Collection;
import java.util.Date;

import uni.miskolc.ips.ilona.tracking.model.PasswordRecoveryToken;
import uni.miskolc.ips.ilona.tracking.persist.exception.OperationExecutionErrorException;
import uni.miskolc.ips.ilona.tracking.persist.exception.PasswordRecoveryTokenNotFoundException;
import uni.miskolc.ips.ilona.tracking.persist.exception.UserNotFoundException;

public interface SecurityFunctionsDAO {

	/**
	 * Password update.
	 * 
	 * @param userid
	 *            The userid of the selected user..
	 * @param hashedPassword
	 *            The new hashed password.
	 * @param validUntil
	 *            The new date until the password is valid.
	 * @throws UserNotFoundException
	 *             If the user is not in the system.
	 * @throws OperationExecutionErrorException
	 *             General error.
	 */
	public void updatePassword(String userid, String hashedPassword, Date validUntil)
			throws UserNotFoundException, OperationExecutionErrorException;

	/**
	 * User enabled property update.
	 * 
	 * @param userid
	 *            The userid of the selected user.
	 * @param enabled
	 *            The new state of the enabled property.
	 * @throws UserNotFoundException
	 *             If the user is not found in the system.
	 * @throws OperationExecutionErrorException
	 *             General system error.
	 */
	public void updateEnabled(String userid, boolean enabled)
			throws UserNotFoundException, OperationExecutionErrorException;

	/**
	 * Account expiration update.
	 * 
	 * @param userid
	 *            The userid of the selected user.
	 * @param expiration
	 *            Last login date.
	 * @throws UserNotFoundException
	 *             If the user is not in the system.
	 * @throws OperationExecutionErrorException
	 *             General system error.
	 */
	public void updateAccountExpiration(String userid, Date expiration)
			throws UserNotFoundException, OperationExecutionErrorException;

	/**
	 * Update user roles.
	 * 
	 * @param userid
	 *            The userid of the selected user.
	 * @param roles
	 *            The new set of roles.<br/>
	 *            Format: ROLE_<role_name>
	 * @throws UserNotFoundException
	 *             If the user is not in the system.
	 * @throws OperationExecutionErrorException
	 *             General system error.
	 */
	public void updateRoles(String userid, Collection<String> roles)
			throws UserNotFoundException, OperationExecutionErrorException;

	/**
	 * Erase bad logins.
	 * 
	 * @param userid
	 *            The userid of the selected user.
	 * @return Affected rows.
	 * @throws UserNotFoundException
	 *             If the user is not in the system.
	 * @throws OperationExecutionErrorException
	 *             General system error.
	 */
	public int eraseBadLogins(String userid) throws UserNotFoundException, OperationExecutionErrorException;

	/**
	 * Loads the bad logins of the selected user.
	 * 
	 * @param userid
	 *            The userid of the selected user.
	 * @return A collection of the bad logins.
	 * @throws OperationExecutionErrorException
	 *             General system error.
	 */
	public Collection<Date> loadBadLogins(String userid) throws OperationExecutionErrorException;

	/**
	 * Update the current bad logins list.<br/>
	 * This procedure will erase the lgoins, then persist the current list.
	 * 
	 * @param userid
	 *            The userid of the selected user.
	 * @param badLogins
	 *            The new bad login values.
	 * @throws UserNotFoundException
	 *             If the user is not in the system.
	 * @throws OperationExecutionErrorException
	 *             General system error.
	 */
	public void updateBadLogins(String userid, Collection<Date> badLogins)
			throws UserNotFoundException, OperationExecutionErrorException;

	/**
	 * Account lock status update.
	 * 
	 * @param userid
	 *            The userid of the selected user.
	 * @param nonLocked
	 *            The new lock state.
	 * @param lockedUntil
	 *            If the nonlocked parameter is false, the account is locked
	 *            until this date. <br />
	 *            If the nonlocked parameter is true, this value is not
	 *            important.<br />
	 *            <b>If this parameter is null, then the paramater will be new
	 *            Date()!</b>
	 * @param deleteBadLogins
	 * @throws UserNotFoundException
	 *             If the user is not in the system.
	 * @throws OperationExecutionErrorException
	 *             General system error.
	 */
	public void updateLockedAndUntilLocked(String userid, boolean nonLocked, Date lockedUntil, boolean deleteBadLogins)
			throws UserNotFoundException, OperationExecutionErrorException;

	/**
	 * Password reset token persist.
	 * 
	 * @param token
	 *            The new token.
	 * @throws OperationExecutionErrorException
	 *             General system error.
	 */
	public void storePasswordResetToken(PasswordRecoveryToken token) throws OperationExecutionErrorException;

	/**
	 * Load a token by userid.
	 * 
	 * @param userid
	 *            The userid of the selected user.
	 * @return The stored token.
	 * @throws PasswordRecoveryTokenNotFoundException
	 *             If the token is not exists.
	 * @throws OperationExecutionErrorException
	 *             General system error.
	 */
	public PasswordRecoveryToken restorePasswordResetToken(String userid)
			throws PasswordRecoveryTokenNotFoundException, OperationExecutionErrorException;

	/**
	 * Delete the token by userid.
	 * 
	 * @param userid
	 *            The userid of the selected user.
	 * @throws OperationExecutionErrorException
	 *             General system error.
	 */
	public void deletePasswordRecoveryToken(String userid) throws OperationExecutionErrorException;
}
