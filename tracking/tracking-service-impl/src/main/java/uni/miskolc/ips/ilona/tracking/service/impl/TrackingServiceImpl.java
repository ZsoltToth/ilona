package uni.miskolc.ips.ilona.tracking.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.tracking.model.UserData;
import uni.miskolc.ips.ilona.tracking.service.TrackingService;
import uni.miskolc.ips.ilona.tracking.service.exceptions.DuplicatedPositionException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.InvalidMeasurementException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.ServiceGeneralErrorException;

public class TrackingServiceImpl implements TrackingService {
	
	@Override
	public Position calculatePosition(Measurement measurement)
			throws InvalidMeasurementException, ServiceGeneralErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void storePosition(Position position) throws DuplicatedPositionException, ServiceGeneralErrorException {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<Position> getPositions(UserData user, Date from, Date to) throws ServiceGeneralErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Integer> calculateZoneStatistics() throws ServiceGeneralErrorException {
		// TODO Auto-generated method stub
		return null;
	}

}
