package uni.miskolc.ips.ilona.tracking.persist;

import uni.miskolc.ips.ilona.tracking.model.UserData;

/**
 * 
 * @author Patrik
 *
 */
public interface TrackingLoginDAO {

	public void createUser(UserData user);
	
	public UserData getUser(String userID);
	
	public void updateUser(UserData user);
	
	public void deleteUser(String userID);
	
}
