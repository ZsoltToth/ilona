package uni.miskolc.ips.ilona.tracking.persist;

import java.util.Collection;

import uni.miskolc.ips.ilona.tracking.model.DeviceData;
import uni.miskolc.ips.ilona.tracking.model.UserData;
import uni.miskolc.ips.ilona.tracking.persist.exception.DeviceAlreadyExistsException;
import uni.miskolc.ips.ilona.tracking.persist.exception.DeviceNotFoundException;
import uni.miskolc.ips.ilona.tracking.persist.exception.OperationExecutionErrorException;
import uni.miskolc.ips.ilona.tracking.persist.exception.UserNotFoundException;

/**
 * General device management DAO interface.
 * 
 * @author Patrik / A5USL0
 *
 */
public interface DeviceManagementDAO {

	/**
	 * Stores the current device.
	 * 
	 * @param device
	 *            A device details object {@link DeviceData}
	 * @param user
	 *            The device owner user object {@link UserData}
	 * @throws DeviceAlreadyExistsException
	 *             If the device is already exists.
	 * @throws OperationExecutionErrorException
	 *             General execution error.
	 */
	void storeDevice(DeviceData device, UserData user)
			throws DeviceAlreadyExistsException, UserNotFoundException, OperationExecutionErrorException;

	/**
	 * Loads the current device.
	 * 
	 * @param deviceid
	 *            The deviceid of the selected device.
	 * @param user
	 *            The device owner user data. {@link UserData}
	 * @return The selected device details {@link DeviceData}
	 * @throws DeviceNotFoundException
	 *             The user doesn't have the selected device.
	 * @throws OperationExecutionErrorException
	 *             General error.
	 */
	DeviceData readDevice(String deviceid, UserData user)
			throws DeviceNotFoundException, UserNotFoundException, OperationExecutionErrorException;

	/**
	 * Loads the user devices.
	 * 
	 * @param user
	 *            The selected user {@link UserData}
	 * @return A collection with the devices {@link DeviceData}
	 * @throws OperationExecutionErrorException
	 *             General error.
	 */
	Collection<DeviceData> readUserDevices(UserData user)
			throws UserNotFoundException, OperationExecutionErrorException;

	/**
	 * Updates the selected device.
	 * 
	 * @param device
	 *            The device with the updated details. {@link DeviceData}
	 * @param user
	 *            The device owner. {@link UserData}
	 * @throws DeviceNotFoundException
	 *             The device is not selected.
	 * @throws OperationExecutionErrorException
	 *             General error.
	 */
	void updateDevice(DeviceData device, UserData user)
			throws DeviceNotFoundException, UserNotFoundException, OperationExecutionErrorException;

	/**
	 * Deletes the current device.
	 * 
	 * @param device
	 *            The selected device {@link DeviceData}
	 * @param user
	 *            The device owner data. {@link UserData}
	 * @throws DeviceNotFoundException
	 *             The device is not found, cannot be deleted.
	 * @throws OperationExecutionErrorException
	 *             General error.
	 */
	void deleteDevice(DeviceData device, UserData user)
			throws DeviceNotFoundException, UserNotFoundException, OperationExecutionErrorException;

}
