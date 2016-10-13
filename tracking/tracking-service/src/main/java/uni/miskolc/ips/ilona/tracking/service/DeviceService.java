package uni.miskolc.ips.ilona.tracking.service;

import java.util.Collection;

import uni.miskolc.ips.ilona.tracking.model.DeviceData;
import uni.miskolc.ips.ilona.tracking.model.UserData;
import uni.miskolc.ips.ilona.tracking.service.exceptions.DeviceNotFoundException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.DuplicatedDeviceException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.ServiceGeneralErrorException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.UserNotFoundException;

/**
 * This interface contains the methods related to the device management.
 * 
 * @author Patrik
 *
 */
public interface DeviceService {

	/**
	 * Store the current device for the user.
	 * 
	 * @param device
	 *            The device details. {@link DeviceData}
	 * @param user
	 *            The owner user. {@link UserData}
	 * @throws UserNotFoundException
	 *             If the current user is not in the system.
	 * @throws DuplicatedDeviceException
	 *             If the current device is already exists in the system.
	 * @throws ServiceGeneralErrorException
	 *             General error, database not available etc.
	 */
	void storeDevice(DeviceData device, UserData user)
			throws UserNotFoundException, DuplicatedDeviceException, ServiceGeneralErrorException;

	/**
	 * Load the device of the given user.
	 * 
	 * @param deviceid
	 *            The deviceid of the needed device.
	 * 
	 * @param user
	 *            The owner user. {@link UserData}
	 * @return The details of the device. {@link DeviceData}}
	 * @throws DeviceNotFoundException
	 *             If the device is not in the system.
	 * @throws ServiceGeneralErrorException
	 *             General service error in the subsystem.
	 */
	DeviceData readDevice(String deviceid, UserData user) throws DeviceNotFoundException, ServiceGeneralErrorException;

	/**
	 * Load the available devices of the given user.
	 * 
	 * @param user
	 *            The owner user. {@link UserData}
	 * @return A collection with details of the devices. {@link DeviceData}
	 *         <br/>
	 *         The collection has no elements if the user has no device.
	 * @throws UserNotFoundException
	 *             If the current user is not found in the system.
	 * @throws ServiceGeneralErrorException
	 *             General service error in the subsystem.
	 */
	Collection<DeviceData> readUserDevices(UserData user) throws UserNotFoundException, ServiceGeneralErrorException;

	/**
	 * Update the details of the current device.
	 * 
	 * @param device
	 *            The device what is needs to be updated. {@link DeviceData}
	 * @param user
	 *            The device owner. {@link UserData}
	 * @throws DeviceNotFoundException
	 *             If the current user has no such device.
	 * @throws UserNotFoundException
	 *             If the current user is not exists.
	 * @throws ServiceGeneralErrorException
	 *             General service error in the subsystem.
	 */
	void updateDevice(DeviceData device, UserData user)
			throws DeviceNotFoundException, UserNotFoundException, ServiceGeneralErrorException;

	/**
	 * Delete the current device.
	 * 
	 * @param device
	 *            The device what is needs to be updated. {@link DeviceData}
	 * @param user
	 *            The device owner. {@link UserData}
	 * @throws DeviceNotFoundException
	 *             If the current user has no such device.
	 * @throws UserNotFoundException
	 *             If the current user is not exists.
	 * @throws ServiceGeneralErrorException
	 *             General service error in the subsystem.
	 */
	void deleteDevice(DeviceData device, UserData user)
			throws DeviceNotFoundException, UserNotFoundException, ServiceGeneralErrorException;

}