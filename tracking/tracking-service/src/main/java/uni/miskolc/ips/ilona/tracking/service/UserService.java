package uni.miskolc.ips.ilona.tracking.service;

import java.util.Collection;

import uni.miskolc.ips.ilona.tracking.model.UserData;
import uni.miskolc.ips.ilona.tracking.service.exceptions.DuplicatedUserException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.ServiceGeneralErrorException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.UserNotFoundException;

/**
 * This interface contains the methods related to the user management.
 * 
 * @author Patrik
 *
 */
public interface UserService {

	/**
	 * Create a user.
	 * 
	 * @param user
	 *            The user will be created with this details. {@link UserData}
	 * @throws DuplicatedUserException
	 *             If the user is already exists.
	 * @throws ServiceGeneralErrorException
	 *             General error in the subsystem.
	 */
	void createUser(UserData user) throws DuplicatedUserException, ServiceGeneralErrorException;

	/**
	 * Get the user with the given id.
	 * 
	 * @param userid
	 *            The id of the needed user.
	 * @return An object with the details of the user. {@link UserData}
	 * @throws UserNotFoundException
	 *             If the user is not exists in the system.
	 * @throws ServiceGeneralErrorException
	 *             General server error.
	 */
	UserData getUser(String userid) throws UserNotFoundException, ServiceGeneralErrorException;

	/**
	 * Get the available users in the system.
	 * 
	 * @return A collection with the details of the users. {@link UserData}
	 * @throws ServiceGeneralErrorException
	 *             General error in the system.
	 */
	Collection<UserData> getAllUsers() throws ServiceGeneralErrorException;

	/**
	 * Update the current user details.
	 * 
	 * @param user
	 *            The userdetails with the new values. {@link UserData}
	 * @throws UserNotFoundException
	 *             If the user is not exists.
	 * @throws ServiceGeneralErrorException
	 *             General error in the system.
	 */
	void updateUser(UserData user) throws UserNotFoundException, ServiceGeneralErrorException;

	/**
	 * Delete the user.
	 * 
	 * @param user
	 * @throws UserNotFoundException
	 *             If the user is not found.
	 * @throws ServiceGeneralErrorException
	 *             General service error.
	 */
	void deleteUser(UserData user) throws UserNotFoundException, ServiceGeneralErrorException;
}