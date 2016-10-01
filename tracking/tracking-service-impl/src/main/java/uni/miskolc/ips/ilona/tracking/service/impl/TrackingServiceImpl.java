package uni.miskolc.ips.ilona.tracking.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.positioning.service.PositioningService;
import uni.miskolc.ips.ilona.tracking.model.DeviceData;
import uni.miskolc.ips.ilona.tracking.model.TrackPosition;
import uni.miskolc.ips.ilona.tracking.persist.TrackingDAO;
import uni.miskolc.ips.ilona.tracking.persist.exception.PositionAlreadyExistsException;
import uni.miskolc.ips.ilona.tracking.service.TrackingService;
import uni.miskolc.ips.ilona.tracking.service.exceptions.DuplicatedPositionException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.InvalidMeasurementException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.ServiceGeneralErrorException;

public class TrackingServiceImpl implements TrackingService {

	private static Logger logger = LogManager.getLogger(TrackingServiceImpl.class);

	@Resource(name = "positioningService")
	private PositioningService positionService;

	@Resource(name = "trackingDAO")
	private TrackingDAO trackingDAO;

	@Override
	public Position calculatePosition(Measurement measurement)
			throws InvalidMeasurementException, ServiceGeneralErrorException {
		try {
			return positionService.determinePosition(measurement);
		} catch (Exception e) {
			logger.error("Measurement error! Cause: " + e.getMessage());
			throw new InvalidMeasurementException("Invalid measurement", e);
		}
	}

	@Override
	public void storePosition(DeviceData device, Position position)
			throws DuplicatedPositionException, ServiceGeneralErrorException {
		try {
			trackingDAO.storePosition(device, position);
		} catch (Exception e) {
			if (e instanceof PositionAlreadyExistsException) {
				logger.info("Dupliated device");
				throw new DuplicatedPositionException("Duplicatd position", e);
			}
			logger.error("Error: " + e.getMessage());
			throw new ServiceGeneralErrorException("Error", e);
		}
	}

	@Override
	public Collection<TrackPosition> getPositions(DeviceData device, Date from, Date to)
			throws ServiceGeneralErrorException {
		try {
			return trackingDAO.restorePositionsInterval(device, from, to);
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			throw new ServiceGeneralErrorException("Error", e);
		}
	}

	@Override
	public Map<String, Integer> calculateZoneStatistics() throws ServiceGeneralErrorException {
		// TODO Auto-generated method stub
		return null;
	}

}
