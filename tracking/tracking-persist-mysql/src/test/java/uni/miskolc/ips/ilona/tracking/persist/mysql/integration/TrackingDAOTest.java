package uni.miskolc.ips.ilona.tracking.persist.mysql.integration;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.tracking.model.DeviceData;
import uni.miskolc.ips.ilona.tracking.model.TrackPosition;
import uni.miskolc.ips.ilona.tracking.persist.TrackingDAO;
import uni.miskolc.ips.ilona.tracking.persist.exception.DeviceNotFoundException;
import uni.miskolc.ips.ilona.tracking.persist.exception.PositionAlreadyExistsException;
import uni.miskolc.ips.ilona.tracking.persist.exception.PositionNotFoundException;
import uni.miskolc.ips.ilona.tracking.persist.mysql.MySqlAndMybatisTrackingDAOImpl;
import uni.miskolc.ips.ilona.tracking.persist.mysql.TestUtility;

public class TrackingDAOTest extends TestSetup {

	private static TrackingDAO dao;

	@BeforeClass
	public static void createDAoO() throws Exception {
		System.out.println(UUID.randomUUID());
		dao = new MySqlAndMybatisTrackingDAOImpl(HOST, PORT, DATABASE, USER, PASSWORD);
	}

	@Test
	public void testStorePosition() {
		DeviceData device = TestUtility.generateMockedDeviceData(USER_DEVICE1_DEVICEID, "devname", "devtype",
				"devtypename");
		Position pos = TestUtility.generateRandomMockedPosition();
		try {
			dao.storePosition(device, pos);
		} catch (Exception e) {
			Assert.fail("Position store failed!");
		}
	}

	@Test
	public void testStorePositionDeviceNotFound() {
		DeviceData device = TestUtility.generateMockedDeviceData(NON_EXISTS_DEVICEID, "devname", "devtype",
				"devtypename");
		Position pos = TestUtility.generateRandomMockedPosition();
		try {
			dao.storePosition(device, pos);
		} catch (DeviceNotFoundException e) {
			return;
		} catch (Exception e) {
			Assert.fail("Position store failed!");
		}
		Assert.fail("Position store failed!");
	}

	@Test
	public void testStorePositionPositionAlreadyExists() {
		DeviceData device = TestUtility.generateMockedDeviceData(USER_DEVICE1_DEVICEID, "devname", "devtype",
				"devtypename");
		Position position = TestUtility.generateMockedPosition(USER_DEVICE1_POSITIONID1, 5.0, 10.0, 15.0,
				UUID.randomUUID(), "zonename1");
		try {
			dao.storePosition(device, position);
		} catch (PositionAlreadyExistsException e) {
			return;
		} catch (Exception e) {

		}
		Assert.fail("Position storage failed!");
	}

	@Test
	public void testRestorePositionsInterval() {
		DeviceData device = TestUtility.generateMockedDeviceData(USER_DEVICE1_DEVICEID, "devname", "devtype",
				"devtypename");
		Date from = new Date(new Date().getTime() - 100000000000L);
		Date to = new Date(new Date().getTime() + 5000000L);
		try {
			Collection<TrackPosition> positions = dao.restorePositionsInterval(device, from, to);
			if (positions.size() == 0) {
				Assert.fail("Invalid position collection size! Expected at least one element!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed!");
		}
	}

	@Test
	public void testRestorePositionsIntervalDeviceNotFound() {
		DeviceData device = TestUtility.generateMockedDeviceData(NON_EXISTS_DEVICEID, "devname", "devtype",
				"devtypename");
		Date from = new Date(new Date().getTime() - 100000000000L);
		Date to = new Date(new Date().getTime() + 5000000L);
		try {
			dao.restorePositionsInterval(device, from, to);

		} catch (DeviceNotFoundException e) {
			return;
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed!");
		}
		Assert.fail("Failed!");
	}

	@Test
	public void testReadPosition() {
		try {
			dao.readPosition(USER_DEVICE1_POSITIONID1);
		} catch (Exception e) {
			Assert.fail("Position recovery error!");
		}
	}

	@Test
	public void testReadPositionPositionNotFound() {
		try {
			dao.readPosition(NON_EXISTS_DEVICEID);
		} catch (PositionNotFoundException e) {
			return;
		} catch (Exception e) {
			Assert.fail("Position recovery error!");
		}
		Assert.fail("Position recovery error!");
	}
}
