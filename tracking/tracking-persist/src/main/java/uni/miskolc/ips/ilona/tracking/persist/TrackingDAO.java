package uni.miskolc.ips.ilona.tracking.persist;

import java.util.Collection;
import java.util.Date;

import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.tracking.model.DeviceData;
import uni.miskolc.ips.ilona.tracking.model.TrackPosition;
import uni.miskolc.ips.ilona.tracking.persist.exception.DeviceNotFoundException;
import uni.miskolc.ips.ilona.tracking.persist.exception.OperationExecutionErrorException;
import uni.miskolc.ips.ilona.tracking.persist.exception.PositionAlreadyExistsException;
import uni.miskolc.ips.ilona.tracking.persist.exception.PositionNotFoundException;

public interface TrackingDAO {

	/**
	 * Store a position in the system.
	 * 
	 * @param device
	 *            The position will belong to this device. {@link DeviceData}
	 * @param position
	 *            The new position object. {@link Position}
	 * @throws PositionAlreadyExistsException
	 *             The current position is already exists. {@link Position}
	 * @throws OperationExecutionErrorException
	 *             General system error.
	 * @throws DeviceNotFoundException
	 *             If the current device is not found in the system.
	 */
	void storePosition(DeviceData device, Position position)
			throws DeviceNotFoundException, PositionAlreadyExistsException, OperationExecutionErrorException;

	/**
	 * Load the positions for the selected device in the specified interval.
	 * 
	 * @param device
	 *            The selected device. {@link DeviceData}
	 * @param from
	 *            Positions from the current date.
	 * @param to
	 *            Positions until this date.
	 * @return A filtered collection of positions. {@link Position}
	 * @throws OperationExecutionErrorException
	 *             General error.
	 */
	Collection<TrackPosition> restorePositionsInterval(DeviceData device, Date from, Date to)
			throws DeviceNotFoundException, OperationExecutionErrorException;

	/**
	 * Load the selected position.
	 * 
	 * @param positionid
	 *            The id of the selected position. {@link Position}
	 * @return The selected position. {@link Position}
	 * @throws PositionNotFoundException
	 *             If the position is not found.
	 * @throws OperationExecutionErrorException
	 *             General error.
	 */
	TrackPosition readPosition(String positionid) throws PositionNotFoundException, OperationExecutionErrorException;
}
