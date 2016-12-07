package uni.miskolc.ips.ilona.tracking.controller.validation;

import org.junit.Assert;
import org.junit.Test;

import uni.miskolc.ips.ilona.tracking.controller.util.ValidateUserData;
import uni.miskolc.ips.ilona.tracking.controller.util.ValidityStatusHolder;

public class ValidateUserDataTest {

	@Test
	public void testValidateUserid() {
		String userid = "dsdadsa";
		ValidityStatusHolder holder = ValidateUserData.validateUserid(userid);
		Assert.assertTrue("Valid userid", holder.isValid());

		userid = "aaa";
		holder = ValidateUserData.validateUserid(userid);
		Assert.assertFalse("Too short!", holder.isValid());

		userid = null;
		holder = ValidateUserData.validateUserid(userid);
		Assert.assertFalse("NULL!", holder.isValid());

		userid = "aaadsaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		holder = ValidateUserData.validateUserid(userid);
		Assert.assertFalse("Too long!", holder.isValid());

		userid = "9dddaaa";
		holder = ValidateUserData.validateUserid(userid);
		Assert.assertFalse("Starts with a number!", holder.isValid());

		userid = "aaa?dsda3 dsada:dddddd";
		holder = ValidateUserData.validateUserid(userid);
		Assert.assertFalse("Format is invalid!", holder.isValid());
	}

	@Test
	public void testValidateUsername() {
		String username = "dsdadsad";
		ValidityStatusHolder holder = ValidateUserData.validateUsername(username);
		Assert.assertTrue("Valid username!", holder.isValid());

		username = "aaa";
		holder = ValidateUserData.validateUsername(username);
		Assert.assertFalse("Too short!", holder.isValid());

		username = null;
		holder = ValidateUserData.validateUsername(username);
		Assert.assertFalse("NULL!", holder.isValid());

		username = "aadsadasdasdsaasdaddsdasdsadasdsadsadasdasdssadda";
		holder = ValidateUserData.validateUsername(username);
		Assert.assertFalse("Too long!", holder.isValid());

		username = "aadsadasdasdsaasdaddsdasdsadasds\ndsdadads\nadasdasdssadda";
		holder = ValidateUserData.validateUsername(username);
		Assert.assertFalse("Too long!", holder.isValid());

		username = "aaaééőőáéüüó";
		holder = ValidateUserData.validateUsername(username);
		Assert.assertTrue("Valid format!", holder.isValid());

		username = "aaa";
		holder = ValidateUserData.validateUsername(username);
		Assert.assertFalse("Too short!", holder.isValid());

		username = "aaa7676é:dasdas_dasdas";
		holder = ValidateUserData.validateUsername(username);
		Assert.assertFalse("Invalid format!", holder.isValid());
	}

	@Test
	public void testValidateEmail() {
		String email = "dsadas@dsadad.com";
		ValidityStatusHolder holder = ValidateUserData.validateEmail(email);
		Assert.assertTrue("Valid email!", holder.isValid());

		email = null;
		holder = ValidateUserData.validateEmail(email);
		Assert.assertFalse("NULL!", holder.isValid());

		email = "@dsada.com";
		holder = ValidateUserData.validateEmail(email);
		Assert.assertFalse("NULL!", holder.isValid());

		email = "dsada.com";
		holder = ValidateUserData.validateEmail(email);
		Assert.assertFalse("NULL!", holder.isValid());

		email = "646546456546456456456@dsadasd6556:_dasdsadsada.com";
		holder = ValidateUserData.validateEmail(email);
		Assert.assertFalse("NULL!", holder.isValid());

	}
}
