package uni.miskolc.ips.ilona.tracking.persist.mysql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import org.easymock.EasyMock;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import uni.miskolc.ips.ilona.measurement.model.position.Coordinate;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.tracking.model.DeviceData;
import uni.miskolc.ips.ilona.tracking.model.UserData;

public class TestUtility {

	/*
	 * ROLES
	 */

	public static final String ROLES_ROLE_USER = "ROLE_USER";
	public static final String ROLES_ROLE_ADMIN = "ROLE_ADMIN";

	/*
	 * General user and device skeleton
	 */

	public static final DeviceData generateNewDevice() {
		String deviceid = "newdevice";
		String devicename = "newdevname";
		String devicetype = "newdevtype";
		String devicetypename = "newdevtypename";
		return new DeviceData(deviceid, devicename, devicetype, devicetypename);
	}

	public static final UserData generateGeneralUserWithoutDevices() {
		Collection<String> roles = new ArrayList<String>();
		roles.add(TestUtility.ROLES_ROLE_USER);
		return new UserData("newuser", "newusername", "newuser@mail.com", "$2a10dsa654ddada\3343", true, roles,
				new Date(), new Date(), new Date(), true, new ArrayList<Date>(), null);
	}

	public static final PersistentRememberMeToken generateMockedRememberMeTokenToken(String username, String series,
			String value, Date timestamp) {
		PersistentRememberMeToken token = EasyMock.createMock(PersistentRememberMeToken.class);
		EasyMock.expect(token.getUsername()).andReturn(username).anyTimes();
		EasyMock.expect(token.getSeries()).andReturn(series).anyTimes();
		EasyMock.expect(token.getTokenValue()).andReturn(value).anyTimes();
		EasyMock.expect(token.getDate()).andReturn(timestamp).anyTimes();
		EasyMock.replay(token);
		return token;
	}

	public static final DeviceData generateMockedDeviceData(String deviceid, String deviceName, String deviceType,
			String deviceTypeName) {
		DeviceData device = EasyMock.createMock(DeviceData.class);
		EasyMock.expect(device.getDeviceid()).andReturn(deviceid).anyTimes();
		EasyMock.expect(device.getDeviceName()).andReturn(deviceName).anyTimes();
		EasyMock.expect(device.getDeviceType()).andReturn(deviceType).anyTimes();
		EasyMock.expect(device.getDeviceTypeName()).andReturn(deviceTypeName).anyTimes();
		EasyMock.replay(device);
		return device;
	}

	public static final Position generateMockedPosition(String positionid, double valx, double valy, double valz,
			UUID zoneid, String zoneName) {
		Position pos = EasyMock.createMock(Position.class);
		EasyMock.expect(pos.getUUID()).andReturn(UUID.fromString(positionid)).anyTimes();
		EasyMock.expect(pos.getCoordinate()).andReturn(generateMockedCoordinate(valx, valy, valz)).anyTimes();
		EasyMock.expect(pos.getZone()).andReturn(generateMockedZone(zoneid, zoneName)).anyTimes();
		EasyMock.replay(pos);
		return pos;
	}

	public static final Coordinate generateMockedCoordinate(double valx, double valy, double valz) {
		Coordinate coord = EasyMock.createMock(Coordinate.class);
		EasyMock.expect(coord.getX()).andReturn(valx).anyTimes();
		EasyMock.expect(coord.getY()).andReturn(valy).anyTimes();
		EasyMock.expect(coord.getZ()).andReturn(valz).anyTimes();
		EasyMock.replay(coord);
		return coord;
	}

	public static final Zone generateMockedZone(UUID zoneid, String zoneName) {
		Zone zone = EasyMock.createMock(Zone.class);
		EasyMock.expect(zone.getId()).andReturn(zoneid).anyTimes();
		EasyMock.expect(zone.getName()).andReturn(zoneName).anyTimes();
		EasyMock.replay(zone);
		return zone;
	}

	public static final Position generateRandomMockedPosition() {
		return generateMockedPosition(UUID.randomUUID().toString(), 5.0, 10.0, 15.0,
				UUID.randomUUID(), "zonename");
	}

}
