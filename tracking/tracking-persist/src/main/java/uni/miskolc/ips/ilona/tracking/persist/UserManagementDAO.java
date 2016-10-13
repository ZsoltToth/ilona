package uni.miskolc.ips.ilona.tracking.persist;

import java.util.Collection;

import uni.miskolc.ips.ilona.tracking.model.UserData;
import uni.miskolc.ips.ilona.tracking.persist.exception.OperationExecutionErrorException;
import uni.miskolc.ips.ilona.tracking.persist.exception.UserAlreadyExistsException;
import uni.miskolc.ips.ilona.tracking.persist.exception.UserNotFoundException;

/**
 * User management DAO interface. <br/>
 * General user management functions.
 * 
 * @author Patrik / A5USL0
 *
 */
public interface UserManagementDAO {

	/*
	 * USER CRUD
	 */
	/**
	 * New user creation.
	 * 
	 * @param user
	 *            User object with details without the devices. {@link UserData}
	 * @throws UserAlreadyExistsException
	 *             If the user is already exists.
	 * @throws OperationExecutionErrorException
	 *             General execution error.
	 */
	void createUser(UserData user) throws UserAlreadyExistsException, OperationExecutionErrorException;

	/**
	 * Get the selected user.
	 * 
	 * @param userid
	 *            The id of the selected user.
	 * @return A populated user object without the devices. {@link UserData}
	 * @throws UserNotFoundException
	 *             If the user is not exists.
	 * @throws OperationExecutionErrorException
	 *             General execution error.
	 */
	UserData getUser(String userid) throws UserNotFoundException, OperationExecutionErrorException;

	/**
	 * Get the available users in the system.
	 * 
	 * @return A collection with user details. {@link UserData}
	 * @throws OperationExecutionErrorException
	 *             General execution error.
	 */
	Collection<UserData> getAllUsers() throws OperationExecutionErrorException;

	/**
	 * Updated the user details without the devices.
	 * 
	 * @param user
	 *            A user object with the updated details. {@link UserData}
	 * @throws UserNotFoundException
	 *             If the user is not exists.
	 * @throws OperationExecutionErrorException
	 *             General execution error.
	 */
	void updateUser(UserData user) throws UserNotFoundException, OperationExecutionErrorException;

	/**
	 * Delete the selected user.
	 * 
	 * @param userid
	 *            The selected user object.
	 * @throws UserNotFoundException
	 *             If the user not exists.
	 * @throws OperationExecutionErrorException
	 *             General execution error.
	 */
	void deleteUser(String userid) throws UserNotFoundException, OperationExecutionErrorException;
}
