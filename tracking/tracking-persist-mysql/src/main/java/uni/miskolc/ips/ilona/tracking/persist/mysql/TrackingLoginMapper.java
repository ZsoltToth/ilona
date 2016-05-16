package uni.miskolc.ips.ilona.tracking.persist.mysql;

import uni.miskolc.ips.ilona.tracking.model.UserData;

public interface TrackingLoginMapper {

	public void createUser(UserData user);

	public UserData getUser(String userID);

	public void updateUser(UserData user);

	public void deleteUser(String userID);
}
