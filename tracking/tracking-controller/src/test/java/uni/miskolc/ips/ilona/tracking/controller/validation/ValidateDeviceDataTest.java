package uni.miskolc.ips.ilona.tracking.controller.validation;

import org.junit.Assert;
import org.junit.Test;

import uni.miskolc.ips.ilona.tracking.controller.util.ValidateDeviceData;
import uni.miskolc.ips.ilona.tracking.controller.util.ValidityStatusHolder;

public class ValidateDeviceDataTest {

	@Test
	public void testValidateDeviceid() {
		String deviceid = "abc12235";

		ValidityStatusHolder holder = ValidateDeviceData.validateDeviceid(deviceid);
		Assert.assertTrue("The current pattern must be true: " + deviceid, holder.isValid());

		deviceid = "abcd";
		holder = ValidateDeviceData.validateDeviceid(deviceid);
		Assert.assertFalse("The deviceid muss be less than 5 characters!", holder.isValid());

		deviceid = "abcde";
		holder = ValidateDeviceData.validateDeviceid(deviceid);
		Assert.assertTrue("5 characters are enough!", holder.isValid());

		deviceid = "abc:565_ds22-4334";
		holder = ValidateDeviceData.validateDeviceid(deviceid);
		Assert.assertTrue("All possible characters test!", holder.isValid());

		deviceid = "loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo"
				+ "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnng";
		holder = ValidateDeviceData.validateDeviceid(deviceid);
		Assert.assertFalse("The length is more than 50 characters!", holder.isValid());

		deviceid = "loooooooooooooooooooooooooooo\noooooooooooooooooooooooooooooooooooooooooooooooooooooo\noo"
				+ "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn\nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnng";
		holder = ValidateDeviceData.validateDeviceid(deviceid);
		Assert.assertFalse("Special string characters test!", holder.isValid());

		deviceid = "_ddsadsad";
		holder = ValidateDeviceData.validateDeviceid(deviceid);
		Assert.assertFalse("The id cant begin with one of the special characters!", holder.isValid());
		
		deviceid = " ddsadsad";
		holder = ValidateDeviceData.validateDeviceid(deviceid);
		Assert.assertFalse("The id cant begin with one of the special characters!", holder.isValid());
		
		deviceid = "-ddsadsad";
		holder = ValidateDeviceData.validateDeviceid(deviceid);
		Assert.assertFalse("The id cant begin with one of the special characters!", holder.isValid());
		
		deviceid = "?ddsadsad";
		holder = ValidateDeviceData.validateDeviceid(deviceid);
		Assert.assertFalse("The id cant begin with one of the special characters!", holder.isValid());

		deviceid = "dsadasd--dsadasdsada";
		holder = ValidateDeviceData.validateDeviceid(deviceid);
		Assert.assertFalse("Two special characters cannot stand next to each other!", holder.isValid());

		deviceid = null;
		holder = ValidateDeviceData.validateDeviceid(deviceid);
		Assert.assertFalse("The id cant be null!", holder.isValid());

		deviceid = "abcde:";
		holder = ValidateDeviceData.validateDeviceid(deviceid);
		Assert.assertFalse("The id cant end with a special character!", holder.isValid());

		deviceid = "abcde9898989898";
		holder = ValidateDeviceData.validateDeviceid(deviceid);
		Assert.assertTrue("Valid id: " + deviceid, holder.isValid());

		deviceid = "212121212122121";
		holder = ValidateDeviceData.validateDeviceid(deviceid);
		Assert.assertTrue("Only numbers is valid!", holder.isValid());
	}

	@Test
	public void testValidateDeviceName() {
		String deviceName = "devname";
		ValidityStatusHolder holder = ValidateDeviceData.validateDeviceName(deviceName);
		Assert.assertTrue("Valid device name!", holder.isValid());

		deviceName = null;
		holder = ValidateDeviceData.validateDeviceName(deviceName);
		Assert.assertFalse("The name cant be null!", holder.isValid());

		deviceName = " dsdsdsss";
		holder = ValidateDeviceData.validateDeviceName(deviceName);
		Assert.assertFalse("White space start!", holder.isValid());

		deviceName = "4444 44444 4444";
		holder = ValidateDeviceData.validateDeviceName(deviceName);
		Assert.assertTrue("Valid number and space!", holder.isValid());

		deviceName = "dsadadas ";
		holder = ValidateDeviceData.validateDeviceName(deviceName);
		Assert.assertFalse("The name cannot end with whitespace", holder.isValid());

		deviceName = "dsadadas?";
		holder = ValidateDeviceData.validateDeviceName(deviceName);
		Assert.assertFalse("Illegal name end!", holder.isValid());
		
		deviceName = "ds_adada:s ";
		holder = ValidateDeviceData.validateDeviceName(deviceName);
		Assert.assertFalse("Not applicapable characters!", holder.isValid());

		deviceName = "dsadas   dsadad23323232";
		holder = ValidateDeviceData.validateDeviceName(deviceName);
		Assert.assertFalse("Too many white spaces!", holder.isValid());

		deviceName = "loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo"
				+ "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnng";
		holder = ValidateDeviceData.validateDeviceName(deviceName);
		Assert.assertFalse("Too long name!", holder.isValid());

		deviceName = "loooooooooooooooooooooooooooo\noooooooooooooooooooooooooooooooooooooooooooooo"
				+ "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn\nnnnnnnnnnnnnnnnnnnnnnnnnnng";
		holder = ValidateDeviceData.validateDeviceName(deviceName);
		Assert.assertFalse("Special command characters test!", holder.isValid());

		deviceName = "dsad";
		holder = ValidateDeviceData.validateDeviceName(deviceName);
		Assert.assertFalse("Too short name!", holder.isValid());
	}

	@Test
	public void testValidateDeviceType() {
		String deviceType = "dsadadsad";
		ValidityStatusHolder holder = ValidateDeviceData.validateDeviceType(deviceType);
		Assert.assertTrue("Valid type name!", holder.isValid());

		deviceType = "dsada:a_dasda";
		holder = ValidateDeviceData.validateDeviceType(deviceType);
		Assert.assertFalse("Special characters test!", holder.isValid());

		deviceType = "";
		holder = ValidateDeviceData.validateDeviceType(deviceType);
		Assert.assertFalse("Too short type name!", holder.isValid());

		deviceType = "looooooooooooooooooooooooooooooooooooooooooooooooooooooooooong";
		holder = ValidateDeviceData.validateDeviceType(deviceType);
		Assert.assertFalse("Too long type name!", holder.isValid());

		deviceType = "looooooooooooooooooooooooooo\nooooooooooooooo\nooooooooooooooooong";
		holder = ValidateDeviceData.validateDeviceType(deviceType);
		Assert.assertFalse("Too long type name with command characters!", holder.isValid());

		deviceType = "aaa44dsada";
		holder = ValidateDeviceData.validateDeviceType(deviceType);
		Assert.assertFalse("Type name with numbers!", holder.isValid());
	}

	@Test
	public void testValidateDeviceTypeName() {
		String deviceTypeName = "dsadadsadasdsad";
		ValidityStatusHolder holder = ValidateDeviceData.validateDeviceTypeName(deviceTypeName);
		Assert.assertTrue("Valid type name !", holder.isValid());

		deviceTypeName = "";
		holder = ValidateDeviceData.validateDeviceTypeName(deviceTypeName);
		Assert.assertFalse("Too short!", holder.isValid());

		deviceTypeName = "loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong";
		holder = ValidateDeviceData.validateDeviceTypeName(deviceTypeName);
		Assert.assertFalse("Too long!", holder.isValid());

		deviceTypeName = "loooooooooooooooooooooooooooooooooooo\noooooooooooooooooooooooo\noooooooooooooong";
		holder = ValidateDeviceData.validateDeviceTypeName(deviceTypeName);
		Assert.assertFalse("Too long with commands!", holder.isValid());

		deviceTypeName = " dsadas_dsada4434";
		holder = ValidateDeviceData.validateDeviceTypeName(deviceTypeName);
		Assert.assertFalse("Illegal start character!", holder.isValid());
		
		deviceTypeName = "_sadas_dsada4434";
		holder = ValidateDeviceData.validateDeviceTypeName(deviceTypeName);
		Assert.assertFalse("Illegal start character!", holder.isValid());

		deviceTypeName = "-dsadas_dsada4434";
		holder = ValidateDeviceData.validateDeviceTypeName(deviceTypeName);
		Assert.assertFalse("Illegal start character!", holder.isValid());
		
		deviceTypeName = ":dsadas_dsada4434";
		holder = ValidateDeviceData.validateDeviceTypeName(deviceTypeName);
		Assert.assertFalse("Illegal start character!", holder.isValid());
		
		deviceTypeName = "dasda43434_";
		holder = ValidateDeviceData.validateDeviceTypeName(deviceTypeName);
		Assert.assertFalse("Illegal end character!", holder.isValid());
		
		deviceTypeName = "dsadas_dsada4434 ";
		holder = ValidateDeviceData.validateDeviceTypeName(deviceTypeName);
		Assert.assertFalse("Illegal end character!", holder.isValid());
		
		deviceTypeName = "dsadas_dsada4434-";
		holder = ValidateDeviceData.validateDeviceTypeName(deviceTypeName);
		Assert.assertFalse("Illegal end character!", holder.isValid());
		
		deviceTypeName = "dsadas__dsada4434";
		holder = ValidateDeviceData.validateDeviceTypeName(deviceTypeName);
		Assert.assertFalse("Illegal inner special characters occurence!", holder.isValid());
		
		deviceTypeName = "dsadas  dsada4434";
		holder = ValidateDeviceData.validateDeviceTypeName(deviceTypeName);
		Assert.assertFalse("Illegal inner special characters occurence!", holder.isValid());
		
		deviceTypeName = "dsadas--dsada4434";
		holder = ValidateDeviceData.validateDeviceTypeName(deviceTypeName);
		Assert.assertFalse("Illegal inner special characters occurence!", holder.isValid());

	}
}
