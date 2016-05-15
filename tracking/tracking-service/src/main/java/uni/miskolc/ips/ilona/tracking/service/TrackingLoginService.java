package uni.miskolc.ips.ilona.tracking.service;

import uni.miskolc.ips.ilona.tracking.model.UserData;
import uni.miskolc.ips.ilona.tracking.service.exceptions.*;

/**
 * This interface declares methods for tracking login specific tasks with
 * exception handling.
 * 
 * @author Patrik
 *
 */
public interface TrackingLoginService {

	void createUser(UserData user) throws DatabaseUnavailableException, InvalidDataException, DuplicatedUserException;

	UserData getUser(String userID) throws DatabaseUnavailableException, NoSuchUserException, UnenabledUserException;
	
	void updateUser(UserData user) throws DatabaseUnavailableException, NoSuchUserException, InvalidDataException;
	
	void deleteUser(String userID) throws DatabaseUnavailableException, NoSuchUserException;
	
}
