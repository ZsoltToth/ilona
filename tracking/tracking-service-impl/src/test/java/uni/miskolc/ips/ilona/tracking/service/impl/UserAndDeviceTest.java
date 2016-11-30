package uni.miskolc.ips.ilona.tracking.service.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import uni.miskolc.ips.ilona.tracking.model.DeviceData;
import uni.miskolc.ips.ilona.tracking.model.UserData;
import uni.miskolc.ips.ilona.tracking.persist.UserAndDeviceDAO;
import uni.miskolc.ips.ilona.tracking.persist.exception.DeviceAlreadyExistsException;
import uni.miskolc.ips.ilona.tracking.persist.exception.DeviceNotFoundException;
import uni.miskolc.ips.ilona.tracking.persist.exception.OperationExecutionErrorException;
import uni.miskolc.ips.ilona.tracking.persist.exception.UserAlreadyExistsException;
import uni.miskolc.ips.ilona.tracking.persist.exception.UserNotFoundException;
import uni.miskolc.ips.ilona.tracking.service.UserAndDeviceService;
import uni.miskolc.ips.ilona.tracking.service.exceptions.DuplicatedDeviceException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.DuplicatedUserException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.ServiceGeneralErrorException;

public class UserAndDeviceTest {

	@Test()
	public void testCreateUser() {

		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user");
			EasyMock.replay(user);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			dao.createUser(user);
			EasyMock.expectLastCall().andVoid();
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.createUser(user);

		} catch (Exception e) {
			Assert.fail();
		}

	}

	@Test(expected = DuplicatedUserException.class)
	public void testCreateUserUserAlreadyExists() throws Exception {

		UserAndDeviceService service = new UserAndDeviceServiceImpl();
		UserData user = EasyMock.createMock(UserData.class);
		EasyMock.expect(user.getUserid()).andReturn("user");
		EasyMock.replay(user);
		UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
		dao.createUser(user);
		EasyMock.expectLastCall().andThrow(new UserAlreadyExistsException());
		EasyMock.replay(dao);
		((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
		service.createUser(user);

	}

	@Test(expected = ServiceGeneralErrorException.class)
	public void testCreateUserGeneralError() throws Exception {
		UserAndDeviceService service = new UserAndDeviceServiceImpl();
		UserData user = EasyMock.createMock(UserData.class);
		EasyMock.expect(user.getUserid()).andReturn("user");
		EasyMock.replay(user);
		UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
		dao.createUser(user);
		EasyMock.expectLastCall().andThrow(new OperationExecutionErrorException());
		EasyMock.replay(dao);
		((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
		service.createUser(user);
	}

	@Test
	public void testGetUser() throws Exception {
		UserAndDeviceService service = new UserAndDeviceServiceImpl();
		UserData user = EasyMock.createMock(UserData.class);
		EasyMock.expect(user.getUserid()).andReturn("user");
		EasyMock.replay(user);
		UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
		final String userid = "user";
		EasyMock.expect(dao.getUser(userid)).andReturn(user);
		EasyMock.replay(dao);
		((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
		service.getUser(userid);
	}

	@Test
	public void testGetUserUserNotFound() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user");
			EasyMock.replay(user);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			final String userid = "user";
			EasyMock.expect(dao.getUser(userid)).andThrow(new UserNotFoundException());
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.getUser(userid);
		} catch (uni.miskolc.ips.ilona.tracking.service.exceptions.UserNotFoundException e) {
			return;
		} catch (Exception e) {
			Assert.fail("Execution error: " + e.getMessage());
		}
		Assert.fail("No UserNotFoundException!");
	}

	@Test
	public void testGetUserGeneralError() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user");
			EasyMock.replay(user);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			final String userid = "user";
			EasyMock.expect(dao.getUser(userid)).andThrow(new OperationExecutionErrorException());
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.getUser(userid);
		} catch (ServiceGeneralErrorException e) {
			return;
		} catch (Exception e) {
			Assert.fail("Execution error: " + e.getMessage());
		}
		Assert.fail("No ServiceGeneralErrorException!");
	}

	@Test
	public void testGetAllUsers() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user");
			EasyMock.replay(user);
			Collection<UserData> users = new ArrayList<UserData>();
			users.add(user);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			EasyMock.expect(dao.getAllUsers()).andReturn(users);
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.getAllUsers();
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}
	}

	@Test
	public void testGetAllUsersGeneralError() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user");
			EasyMock.replay(user);
			Collection<UserData> users = new ArrayList<UserData>();
			users.add(user);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			EasyMock.expect(dao.getAllUsers()).andThrow(new OperationExecutionErrorException());
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.getAllUsers();
		} catch (ServiceGeneralErrorException e) {
			return;
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}
		Assert.fail("No ServiceGeneralErrorException!");
	}

	@Test
	public void testUpdateUser() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user");
			EasyMock.replay(user);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			dao.updateUser(user);
			EasyMock.expectLastCall().andVoid();
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.updateUser(user);
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}

	}

	@Test
	public void testUpdateUserUserNotFound() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user");
			EasyMock.replay(user);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			dao.updateUser(user);
			EasyMock.expectLastCall().andThrow(new UserNotFoundException());
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.updateUser(user);
		} catch (uni.miskolc.ips.ilona.tracking.service.exceptions.UserNotFoundException e) {
			return;
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}
		Assert.fail("No UserNotFoundException!");
	}

	@Test
	public void testUpdateUserGeneralError() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user");
			EasyMock.replay(user);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			dao.updateUser(user);
			EasyMock.expectLastCall().andThrow(new OperationExecutionErrorException());
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.updateUser(user);
		} catch (ServiceGeneralErrorException e) {
			return;
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}
		Assert.fail("No UserNotFoundException!");
	}

	@Test
	public void testDeleteUser() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user");
			EasyMock.replay(user);
			dao.deleteUser("user");
			EasyMock.expectLastCall().andVoid();
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.deleteUser(user);
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}
	}

	@Test
	public void testDeleteUserUserNotFound() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user").anyTimes();
			EasyMock.replay(user);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			dao.deleteUser("user");
			EasyMock.expectLastCall().andThrow(new UserNotFoundException());
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.deleteUser(user);
		} catch (uni.miskolc.ips.ilona.tracking.service.exceptions.UserNotFoundException e) {
			return;
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}
		Assert.fail("No UserNotFoundException!");
	}

	@Test
	public void testDeleteUserGeneralError() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user").anyTimes();
			EasyMock.replay(user);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			dao.updateUser(user);
			EasyMock.expectLastCall().andThrow(new OperationExecutionErrorException());
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.updateUser(user);
		} catch (ServiceGeneralErrorException e) {
			return;
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}
		Assert.fail("No UserNotFoundException!");
	}

	@Test
	public void testStoreDevice() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user").anyTimes();
			DeviceData device = EasyMock.createMock(DeviceData.class);
			EasyMock.expect(device.getDeviceid()).andReturn("device").anyTimes();
			EasyMock.replay(user, device);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			dao.storeDevice(device, user);
			EasyMock.expectLastCall().andVoid();
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.storeDevice(device, user);
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}
	}

	@Test
	public void testStoreDeviceDuplacatedDevice() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user").anyTimes();
			DeviceData device = EasyMock.createMock(DeviceData.class);
			EasyMock.expect(device.getDeviceid()).andReturn("device").anyTimes();
			EasyMock.replay(user, device);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			dao.storeDevice(device, user);
			EasyMock.expectLastCall().andThrow(new DeviceAlreadyExistsException());
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.storeDevice(device, user);
		} catch (DuplicatedDeviceException e) {
			return;
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}
		Assert.fail("No DuplicatedDeviceException!");
	}

	@Test
	public void testStoreDeviceGeneralError() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user").anyTimes();
			DeviceData device = EasyMock.createMock(DeviceData.class);
			EasyMock.expect(device.getDeviceid()).andReturn("device").anyTimes();
			EasyMock.replay(user, device);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			dao.storeDevice(device, user);
			EasyMock.expectLastCall().andThrow(new OperationExecutionErrorException());
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.storeDevice(device, user);
		} catch (ServiceGeneralErrorException e) {
			return;
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}
		Assert.fail("No DuplicatedDeviceException!");
	}

	@Test
	public void testReadDevice() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user").anyTimes();
			DeviceData device = EasyMock.createMock(DeviceData.class);
			EasyMock.expect(device.getDeviceid()).andReturn("device").anyTimes();
			EasyMock.replay(user, device);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			EasyMock.expect(dao.readDevice("device", user)).andReturn(device);
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.readDevice("device", user);
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}
	}

	@Test
	public void testReadDeviceDeviceNotFound() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user").anyTimes();
			DeviceData device = EasyMock.createMock(DeviceData.class);
			EasyMock.expect(device.getDeviceid()).andReturn("device").anyTimes();
			EasyMock.replay(user, device);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			EasyMock.expect(dao.readDevice("device", user)).andThrow(new DeviceNotFoundException());
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.readDevice("device", user);
		} catch (uni.miskolc.ips.ilona.tracking.service.exceptions.DeviceNotFoundException e) {
			return;
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}
		Assert.fail("No DeviceNotFoundException!");
	}

	@Test
	public void testReadDeviceGeneralError() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user").anyTimes();
			DeviceData device = EasyMock.createMock(DeviceData.class);
			EasyMock.expect(device.getDeviceid()).andReturn("device").anyTimes();
			EasyMock.replay(user, device);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			EasyMock.expect(dao.readDevice("device", user)).andThrow(new OperationExecutionErrorException());
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.readDevice("device", user);
		} catch (ServiceGeneralErrorException e) {
			return;
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}
		Assert.fail("No DeviceNotFoundException!");
	}

	@Test
	public void testReadUserDevices() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user").anyTimes();
			DeviceData device = EasyMock.createMock(DeviceData.class);
			EasyMock.expect(device.getDeviceid()).andReturn("device").anyTimes();
			EasyMock.replay(user, device);
			Collection<DeviceData> devices = new ArrayList<DeviceData>();
			devices.add(device);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			EasyMock.expect(dao.readUserDevices(user)).andReturn(devices);
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.readUserDevices(user);
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}
	}

	@Test
	public void testReadUserDevicesGeneralError() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user").anyTimes();
			DeviceData device = EasyMock.createMock(DeviceData.class);
			EasyMock.expect(device.getDeviceid()).andReturn("device").anyTimes();
			EasyMock.replay(user, device);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			EasyMock.expect(dao.readUserDevices(user)).andThrow(new OperationExecutionErrorException());
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.readUserDevices(user);
		} catch (ServiceGeneralErrorException e) {
			return;
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}
		Assert.fail("No UserNotFoundException!");
	}

	@Test
	public void testUpdateDevice() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user").anyTimes();
			DeviceData device = EasyMock.createMock(DeviceData.class);
			EasyMock.expect(device.getDeviceid()).andReturn("device").anyTimes();
			EasyMock.replay(user, device);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			dao.updateDevice(device, user);
			EasyMock.expectLastCall().andVoid();
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.updateDevice(device, user);
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}
	}

	@Test
	public void testUpdateDeviceDeviceNotFound() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user").anyTimes();
			DeviceData device = EasyMock.createMock(DeviceData.class);
			EasyMock.expect(device.getDeviceid()).andReturn("device").anyTimes();
			EasyMock.replay(user, device);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			dao.updateDevice(device, user);
			EasyMock.expectLastCall().andThrow(new DeviceNotFoundException());
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.updateDevice(device, user);
		} catch(uni.miskolc.ips.ilona.tracking.service.exceptions.DeviceNotFoundException e) {
			return;
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}
		Assert.fail("No DeviceNotFoundException!");
	}
	
	@Test
	public void testUpdateDeviceGeneralError() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user").anyTimes();
			DeviceData device = EasyMock.createMock(DeviceData.class);
			EasyMock.expect(device.getDeviceid()).andReturn("device").anyTimes();
			EasyMock.replay(user, device);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			dao.updateDevice(device, user);
			EasyMock.expectLastCall().andThrow(new OperationExecutionErrorException());
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.updateDevice(device, user);
		} catch(ServiceGeneralErrorException e) {
			return;
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}
		Assert.fail("No DeviceNotFoundException!");
	}
	
	@Test
	public void testDeleteDevice() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user").anyTimes();
			DeviceData device = EasyMock.createMock(DeviceData.class);
			EasyMock.expect(device.getDeviceid()).andReturn("device").anyTimes();
			EasyMock.replay(user, device);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			dao.deleteDevice(device, user);
			EasyMock.expectLastCall().andVoid();
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.deleteDevice(device, user);;
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}
	}
	
	@Test
	public void testDeleteDeviceDeviceNotFound() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user").anyTimes();
			DeviceData device = EasyMock.createMock(DeviceData.class);
			EasyMock.expect(device.getDeviceid()).andReturn("device").anyTimes();
			EasyMock.replay(user, device);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			dao.deleteDevice(device, user);
			EasyMock.expectLastCall().andThrow(new DeviceNotFoundException());
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.deleteDevice(device, user);
		} catch(uni.miskolc.ips.ilona.tracking.service.exceptions.DeviceNotFoundException e) {
			return;
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}
		Assert.fail("No DeviceNotFoundException!");
	}
	
	@Test
	public void testDeleteDeviceGeneralError() {
		try {
			UserAndDeviceService service = new UserAndDeviceServiceImpl();
			UserData user = EasyMock.createMock(UserData.class);
			EasyMock.expect(user.getUserid()).andReturn("user").anyTimes();
			DeviceData device = EasyMock.createMock(DeviceData.class);
			EasyMock.expect(device.getDeviceid()).andReturn("device").anyTimes();
			EasyMock.replay(user, device);
			UserAndDeviceDAO dao = EasyMock.createMock(UserAndDeviceDAO.class);
			dao.deleteDevice(device, user);
			EasyMock.expectLastCall().andThrow(new OperationExecutionErrorException());
			EasyMock.replay(dao);
			((UserAndDeviceServiceImpl) service).setUserDeviceDAO(dao);
			service.deleteDevice(device, user);
		} catch(ServiceGeneralErrorException e) {
			return;
		} catch (Exception e) {
			Assert.fail("Error: " + e.getMessage());
		}
		Assert.fail("No DeviceNotFoundException!");
	}
}
