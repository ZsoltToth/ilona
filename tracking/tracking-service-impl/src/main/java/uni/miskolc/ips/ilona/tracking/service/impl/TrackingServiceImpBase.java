package uni.miskolc.ips.ilona.tracking.service.impl;

import uni.miskolc.ips.ilona.tracking.model.UserData;
import uni.miskolc.ips.ilona.tracking.persist.TrackingLoginDAO;
import uni.miskolc.ips.ilona.tracking.service.TrackingLoginService;
import uni.miskolc.ips.ilona.tracking.service.exceptions.DatabaseUnavailableException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.DuplicatedUserException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.InvalidDataException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.NoSuchUserException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.UnenabledUserException;

public class TrackingServiceImpBase implements TrackingLoginService {

	private TrackingLoginDAO trackingLoginDAO;

	public TrackingServiceImpBase(TrackingLoginDAO trackingLoginDAO) {
		super();
		this.trackingLoginDAO = trackingLoginDAO;
	}

	@Override
	public void createUser(UserData user)
			throws DatabaseUnavailableException, InvalidDataException, DuplicatedUserException {
		this.trackingLoginDAO.createUser(user);

	}

	@Override
	public UserData getUser(String userID)
			throws DatabaseUnavailableException, NoSuchUserException, UnenabledUserException {
		return this.trackingLoginDAO.getUser(userID);
	}

	@Override
	public void updateUser(UserData user)
			throws DatabaseUnavailableException, NoSuchUserException, InvalidDataException {
		this.trackingLoginDAO.updateUser(user);
	}

	@Override
	public void deleteUser(String userID) throws DatabaseUnavailableException, NoSuchUserException {
		this.trackingLoginDAO.deleteUser(userID);
	}

}
