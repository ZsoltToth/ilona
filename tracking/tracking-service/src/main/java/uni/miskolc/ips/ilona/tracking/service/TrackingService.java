package uni.miskolc.ips.ilona.tracking.service;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.tracking.model.DeviceData;
import uni.miskolc.ips.ilona.tracking.model.TrackPosition;
import uni.miskolc.ips.ilona.tracking.service.exceptions.DuplicatedPositionException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.InvalidMeasurementException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.ServiceGeneralErrorException;

public interface TrackingService {

	Position calculatePosition(Measurement measurement)
			throws InvalidMeasurementException, ServiceGeneralErrorException;

	void storePosition(DeviceData device, Position position)
			throws DuplicatedPositionException, ServiceGeneralErrorException;

	Collection<TrackPosition> getPositions(DeviceData device, Date from, Date to) throws ServiceGeneralErrorException;

	Map<String, Integer> calculateZoneStatistics() throws ServiceGeneralErrorException;

}
