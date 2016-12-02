package uni.miskolc.ips.ilona.tracking.persist.mysql.integration;

import java.util.Collection;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import uni.miskolc.ips.ilona.tracking.model.DeviceData;
import uni.miskolc.ips.ilona.tracking.model.UserData;
import uni.miskolc.ips.ilona.tracking.persist.UserAndDeviceDAO;
import uni.miskolc.ips.ilona.tracking.persist.exception.DeviceAlreadyExistsException;
import uni.miskolc.ips.ilona.tracking.persist.exception.DeviceNotFoundException;
import uni.miskolc.ips.ilona.tracking.persist.exception.UserAlreadyExistsException;
import uni.miskolc.ips.ilona.tracking.persist.exception.UserNotFoundException;
import uni.miskolc.ips.ilona.tracking.persist.mysql.MySqlAndMybatisUserAndDeviceDAOImplementation;
import uni.miskolc.ips.ilona.tracking.persist.mysql.TestUtility;

public class UserAndDeviceDAOTest extends TestSetup {

	private static UserAndDeviceDAO dao;

	@BeforeClass
	public static void createDAoO() throws Exception {
		// System.out.println("!!! USER BEFORE CLASS!!!");
		dao = new MySqlAndMybatisUserAndDeviceDAOImplementation(MYBATIS_CONFIG_FILE, HOST, PORT, DATABASE, USER,
				PASSWORD);
	}

	@Test
	public void testCreateUser() {

		UserData user = TestUtility.generateGeneralUserWithoutDevices();

		try {
			dao.createUser(user);
		} catch (Exception e) {
			Assert.fail("User creation error!");
		}

	}

	@Test
	public void testCreateUserUserAlreadyExists() {
		// System.out.println("!!! USER TEST create exists !!!");
		UserData user = TestUtility.generateGeneralUserWithoutDevices();
		user.setUserid(USER_USERID);
		try {
			dao.createUser(user);
		} catch (UserAlreadyExistsException e) {
			return;
		} catch (Exception e) {
			Assert.fail("User already exists failed!");
		}
		Assert.fail("User already exists failed!");
	}

	@Test
	public void testGetUser() {
		String userid = ADMIN_USERID;
		try {
			dao.getUser(userid);
		} catch (Exception e) {
			Assert.fail("DAO execution failed!");
		}
	}

	@Test
	public void testGetUserUserNotFound() {
		String userid = NON_EXISTS_USERID;
		try {
			dao.getUser(userid);
		} catch (UserNotFoundException e) {
			return;
		} catch (Exception e) {
			Assert.fail("Not found test error!");
		}
		Assert.fail("Not found test error!");
	}

	@Test
	public void testGetAllUsers() {
		try {
			Collection<UserData> users = dao.getAllUsers();
			if (users.size() == 0) {
				Assert.fail("Get all users failed! There is no user in the system!");
			}
		} catch (Exception e) {
			Assert.fail("Execution failed!");
		}
	}

	@Test
	public void testUpdateUser() {
		UserData user = TestUtility.generateGeneralUserWithoutDevices();
		user.setUserid(USER_USERID);
		user.setUsername("ChangedValue");
		try {
			dao.updateUser(user);
		} catch (Exception e) {
			Assert.fail("UPDATE FAILED!");
		}
	}

	@Test
	public void testUpdateUserUserNotFound() {
		UserData user = TestUtility.generateGeneralUserWithoutDevices();
		user.setUserid(NON_EXISTS_USERID);
		try {
			dao.updateUser(null);
		} catch (UserNotFoundException e) {
			return;
		} catch (Exception e) {
			Assert.fail("Execution error!");
		}
		Assert.fail("Execution error!");
	}

	@Test
	public void testDeleteUser() {
		String userid = USER_USERID;
		try {
			dao.deleteUser(userid);
		} catch (Exception e) {
			Assert.fail("Execution error!");
		}
	}

	@Test
	public void testDeleteUserUserNotFound() {
		String userid = NON_EXISTS_USERID;
		try {
			dao.deleteUser(userid);
		} catch (UserNotFoundException e) {
			return;
		} catch (Exception e) {
			Assert.fail("Execution error!");
		}
		Assert.fail("Execution error!");
	}

	@Test
	public void storeDevice() {

		UserData user = TestUtility.generateGeneralUserWithoutDevices();
		user.setUserid(USER_USERID);

		DeviceData device = TestUtility.generateNewDevice();
		try {
			dao.storeDevice(device, user);
		} catch (Exception e) {
			Assert.fail("Storing device failed!");
		}

	}

	@Test
	public void storeDeviceUserNotFound() {
		UserData user = TestUtility.generateGeneralUserWithoutDevices();
		user.setUserid(NON_EXISTS_USERID);
		DeviceData device = TestUtility.generateNewDevice();
		try {
			dao.storeDevice(device, user);
		} catch (UserNotFoundException e) {
			return;
		} catch (Exception e) {

		}
		Assert.fail("User not found error!");
	}

	@Test
	public void storeDeviceDeviceNotFound() {
		UserData user = TestUtility.generateGeneralUserWithoutDevices();
		user.setUserid(USER_USERID);
		DeviceData device = TestUtility.generateNewDevice();
		device.setDeviceid(USER_DEVICE1_DEVICEID);
		try {
			dao.storeDevice(device, user);
		} catch (DeviceAlreadyExistsException e) {
			return;
		} catch (Exception e) {

		}
		Assert.fail("Device already exists failed!");
	}

	@Test
	public void testReadDevice() {
		UserData user = TestUtility.generateGeneralUserWithoutDevices();
		user.setUserid(USER_USERID);
		String deviceid = USER_DEVICE1_DEVICEID;
		try {
			dao.readDevice(deviceid, user);
		} catch (Exception e) {
			Assert.fail("Read device failed!");
		}
	}

	@Test
	public void testReadDeviceUserNotFound() {
		UserData user = TestUtility.generateGeneralUserWithoutDevices();
		user.setUserid(NON_EXISTS_USERID);
		String deviceid = USER_DEVICE1_DEVICEID;
		try {
			dao.readDevice(deviceid, user);
		} catch (UserNotFoundException e) {
			return;
		} catch (Exception e) {

		}
		Assert.fail("User not found error!");
	}

	@Test
	public void testReadDeviceDeviceNotFound() {
		UserData user = TestUtility.generateGeneralUserWithoutDevices();
		user.setUserid(USER_USERID);
		String deviceid = NON_EXISTS_DEVICEID;
		try {
			dao.readDevice(deviceid, user);
		} catch (DeviceNotFoundException e) {
			return;
		} catch (Exception e) {

		}
		Assert.fail("Device not found error!");
	}

	@Test
	public void testReadUserDevices() {
		UserData user = TestUtility.generateGeneralUserWithoutDevices();
		user.setUserid(USER_USERID);
		try {
			Collection<DeviceData> devices = dao.readUserDevices(user);
			if (devices.size() == 0) {
				Assert.fail("There is no device in the system! Expected at least one!");
			}
		} catch (Exception e) {
			Assert.fail("Read devices error!");
		}
	}

	@Test
	public void testReadUserDevicesUserNotFound() {
		UserData user = TestUtility.generateGeneralUserWithoutDevices();
		user.setUserid(NON_EXISTS_USERID);
		try {
			dao.readUserDevices(user);
		} catch (UserNotFoundException e) {
			return;
		} catch (Exception e) {

		}
		Assert.fail("User not exists failure!");
	}

	@Test
	public void testUpdateDevice() {
		UserData user = TestUtility.generateGeneralUserWithoutDevices();
		user.setUserid(USER_USERID);
		DeviceData device = TestUtility.generateNewDevice();
		device.setDeviceid(USER_DEVICE1_DEVICEID);
		try {
			dao.updateDevice(device, user);
		} catch (Exception e) {
			Assert.fail("Update device execution error!");
		}
	}

	@Test
	public void testUpdateDeviceUserNotFound() {
		UserData user = TestUtility.generateGeneralUserWithoutDevices();
		user.setUserid(NON_EXISTS_USERID);
		DeviceData device = TestUtility.generateNewDevice();
		device.setDeviceid(USER_DEVICE1_DEVICEID);
		try {
			dao.updateDevice(device, user);
		} catch (UserNotFoundException e) {
			return;
		} catch (Exception e) {

		}
		Assert.fail("Update device execution error!");
	}

	@Test
	public void testUpdateDeviceDeviceNotFound() {
		UserData user = TestUtility.generateGeneralUserWithoutDevices();
		user.setUserid(USER_USERID);
		DeviceData device = TestUtility.generateNewDevice();
		device.setDeviceid(NON_EXISTS_DEVICEID);
		try {
			dao.updateDevice(device, user);
		} catch (DeviceNotFoundException e) {
			return;
		} catch (Exception e) {

		}
		Assert.fail("Update device execution error!");
	}

	@Test
	public void testDeleteDevice() {
		UserData user = TestUtility.generateGeneralUserWithoutDevices();
		user.setUserid(USER_USERID);
		DeviceData device = TestUtility.generateNewDevice();
		device.setDeviceid(USER_DEVICE1_DEVICEID);
		try {
			dao.deleteDevice(device, user);
		} catch (Exception e) {
			Assert.fail("Delete device execution error!");
		}
	}

	@Test
	public void testDeleteDeviceUserNotFound() {
		UserData user = TestUtility.generateGeneralUserWithoutDevices();
		user.setUserid(NON_EXISTS_USERID);
		DeviceData device = TestUtility.generateNewDevice();
		device.setDeviceid(USER_DEVICE1_DEVICEID);
		try {
			dao.deleteDevice(device, user);
		} catch (UserNotFoundException e) {
			return;
		} catch (Exception e) {

		}
		Assert.fail("Delete device execution error!");
	}

	@Test
	public void testDeleteDeviceDeviceNotFound() {
		UserData user = TestUtility.generateGeneralUserWithoutDevices();
		user.setUserid(USER_USERID);
		DeviceData device = TestUtility.generateNewDevice();
		device.setDeviceid(NON_EXISTS_DEVICEID);
		try {
			dao.deleteDevice(device, user);
		} catch (DeviceNotFoundException e) {
			return;
		} catch (Exception e) {

		}
		Assert.fail("Delete device execution error!");
	}

}
