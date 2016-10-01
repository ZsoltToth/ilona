package uni.miskolc.ips.ilona.tracking.service;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.tracking.model.UserData;
import uni.miskolc.ips.ilona.tracking.service.exceptions.DuplicatedPositionException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.InvalidMeasurementException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.ServiceGeneralErrorException;

public interface TrackingService {

	Position calculatePosition(Measurement measurement)
			throws InvalidMeasurementException, ServiceGeneralErrorException;

	void storePosition(Position position) throws DuplicatedPositionException, ServiceGeneralErrorException;

	Collection<Position> getPositions(UserData user, Date from, Date to) throws ServiceGeneralErrorException;
	
	Map<String, Integer> calculateZoneStatistics() throws ServiceGeneralErrorException;

}
