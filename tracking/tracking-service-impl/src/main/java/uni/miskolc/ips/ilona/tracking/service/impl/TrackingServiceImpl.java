package uni.miskolc.ips.ilona.tracking.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.service.PositionService;
import uni.miskolc.ips.ilona.positioning.service.PositioningService;
import uni.miskolc.ips.ilona.tracking.model.DeviceData;
import uni.miskolc.ips.ilona.tracking.model.TrackPosition;
import uni.miskolc.ips.ilona.tracking.persist.TrackingDAO;
import uni.miskolc.ips.ilona.tracking.persist.exception.PositionAlreadyExistsException;
import uni.miskolc.ips.ilona.tracking.service.TrackingService;
import uni.miskolc.ips.ilona.tracking.service.exceptions.DuplicatedPositionException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.InvalidMeasurementException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.ServiceGeneralErrorException;

/**
 * This class is an implementation of the interface {@link TrackingService}
 * 
 * @author Patrik / A5USL0
 *
 */
public class TrackingServiceImpl implements TrackingService {

	private static Logger logger = LogManager.getLogger(TrackingServiceImpl.class);

	/**
	 * Position service injected by the IOC.
	 */
	@Resource(name = "positioningService")
	private PositioningService positionService;

	/**
	 * TrackingDAO injected by the IOC.
	 */
	@Resource(name = "trackingDAO")
	private TrackingDAO trackingDAO;

	/**
	 * 
	 */
	public TrackingServiceImpl() {

	}

	/**
	 * Constructor with the two dependencies.
	 * 
	 * @param positioningService
	 *            Class what implemenets the {@link PositioningService}
	 *            interface.
	 * @param trackingDAO
	 *            Class what implements the {@link TrackingDAO} interface.
	 */
	public TrackingServiceImpl(PositioningService positioningService, TrackingDAO trackingDAO) {
		this.positionService = positioningService;
		this.trackingDAO = trackingDAO;
	}

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

		return null;
	}

	/**
	 * Returns the inner position service.
	 * 
	 * @return Implements {@link PositioningService} interface.
	 */
	public PositioningService getPositionService() {
		return positionService;
	}

	/**
	 * Sets the inner position service.
	 * 
	 * @param positionService
	 *            Implements the {@link PositionService} interface.
	 */
	public void setPositionService(PositioningService positionService) {
		this.positionService = positionService;
	}

	/**
	 * Return the inner tracking DAO.
	 * 
	 * @return Implements {@link TrackingDAO} interface.
	 */
	public TrackingDAO getTrackingDAO() {
		return trackingDAO;
	}

	/**
	 * Sets the inner trackingDAO.
	 * 
	 * @param trackingDAO
	 *            Implements the {@link TrackingDAO} interface.
	 */
	public void setTrackingDAO(TrackingDAO trackingDAO) {
		this.trackingDAO = trackingDAO;
	}

}
