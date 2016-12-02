package uni.miskolc.ips.ilona.tracking.service;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.tracking.model.DeviceData;
import uni.miskolc.ips.ilona.tracking.model.TrackPosition;
import uni.miskolc.ips.ilona.tracking.service.exceptions.DeviceNotFoundException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.DuplicatedPositionException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.InvalidMeasurementException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.ServiceGeneralErrorException;

/**
 * This interface contains methods related to the tracking service.
 * 
 * @author Patrik
 *
 */
public interface TrackingService {

	/**
	 * Calculate the position of the measurement.
	 * 
	 * @param measurement
	 *            {@link Measurement}
	 * @return A calculated position in the system. {@link Position}
	 * @throws InvalidMeasurementException
	 *             If the details of the measruemnt is invalid.
	 * @throws ServiceGeneralErrorException
	 *             General error in the service.
	 */
	Position calculatePosition(Measurement measurement)
			throws InvalidMeasurementException, ServiceGeneralErrorException;

	/**
	 * Store the position for the current device.
	 * 
	 * @param device
	 *            The position will be binded to this device. {@link DeviceData}
	 * @param position
	 *            The position what is needs to be stored. {@link Position}
	 * @throws DuplicatedPositionException
	 *             If the current position is already exists. {@link Position}
	 * @throws ServiceGeneralErrorException
	 */
	void storePosition(DeviceData device, Position position)
			throws DeviceNotFoundException, DuplicatedPositionException, ServiceGeneralErrorException;

	/**
	 * Get the positions to the given device.
	 * 
	 * @param device
	 *            The device {@link DeviceData}
	 * @param from
	 *            Positions from the given date.
	 * @param to
	 *            Positions to the given date.
	 * @return A collection of positions in the given interval. {@link Position}
	 * @throws ServiceGeneralErrorException
	 *             General system error.
	 */
	Collection<TrackPosition> getPositions(DeviceData device, Date from, Date to)
			throws DeviceNotFoundException, ServiceGeneralErrorException;

	/**
	 * 
	 * @return
	 * @throws ServiceGeneralErrorException
	 */
	Map<String, Integer> calculateZoneStatistics() throws ServiceGeneralErrorException;

}
