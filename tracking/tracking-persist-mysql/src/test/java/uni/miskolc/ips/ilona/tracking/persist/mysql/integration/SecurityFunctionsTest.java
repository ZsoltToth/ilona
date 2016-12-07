package uni.miskolc.ips.ilona.tracking.persist.mysql.integration;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import uni.miskolc.ips.ilona.tracking.persist.SecurityFunctionsDAO;
import uni.miskolc.ips.ilona.tracking.persist.exception.UserNotFoundException;
import uni.miskolc.ips.ilona.tracking.persist.mysql.MySqlAndMybatisSecurityFunctionsDAOImpl;

public class SecurityFunctionsTest extends TestSetup {

	private static SecurityFunctionsDAO securityDAO;

	@BeforeClass
	public static void initializeDAO() throws Exception {
		securityDAO = new MySqlAndMybatisSecurityFunctionsDAOImpl(HOST, PORT, DATABASE, USER, PASSWORD);
	}

	@Test
	public void testUpdatePassword() {
		try {
			securityDAO.updatePassword(USER_USERID, "Dummmy password", new Date());
		} catch (Exception e) {
			Assert.fail("Password update failed!");
		}
	}

	@Test
	public void testUpdatePasswordUserNotFound() {
		try {
			securityDAO.updatePassword(NON_EXISTS_USERID, "dummy passsword", new Date());
		} catch (UserNotFoundException e) {
			return;
		} catch (Exception e) {

		}
		Assert.fail("Another error!");
	}

	@Test
	public void testUpdateEnabled() {
		try {
			securityDAO.updateEnabled(USER_USERID, true);
		} catch (Exception e) {
			Assert.fail("Enabled update failed!");
		}
	}

	@Test
	public void testUpdateEnabledUserNotFound() {
		try {
			securityDAO.updateEnabled(NON_EXISTS_USERID, true);
		} catch (UserNotFoundException e) {
			return;
		} catch (Exception e) {

		}
		Assert.fail("The dao hast thrown an user not found error!");
	}

	@Test
	public void testUpdateAccountExpiration() {
		try {
			securityDAO.updateAccountExpiration(USER_USERID, new Date());
		} catch (Exception e) {
			Assert.fail("Account expiration update failed!F");
		}
	}

	@Test
	public void testUpdateAccountExpirationUserNotFound() {
		try {
			securityDAO.updateAccountExpiration(NON_EXISTS_USERID, new Date());
		} catch (UserNotFoundException e) {
			return;
		} catch (Exception e) {

		}
		Assert.fail("User not found exception test error!");
	}

	@Test
	public void testUpdateRoles() {
		try {
			securityDAO.updateRoles(USER_USERID, new ArrayList<String>());
		} catch (Exception e) {
			Assert.fail("Security error!");
		}
	}

	@Test
	public void testUpdateRolesWithRoles() {
		try {
			ArrayList<String> roles = new ArrayList<>();
			roles.add("ROLE_USER");
			securityDAO.updateRoles(USER_USERID, roles);
		} catch (Exception e) {
			Assert.fail("Security error!");
		}
	}

	@Test
	public void testUpdateRolesUserNotFound() {
		try {
			securityDAO.updateRoles(NON_EXISTS_USERID, new ArrayList<String>());
		} catch (UserNotFoundException e) {
			return;
		} catch (Exception e) {

		}
		Assert.fail("User not found exception test error!");
	}

	@Test
	public void testEraseBadLogins() {
		try {
			securityDAO.eraseBadLogins(USER_USERID);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Erase roles error!");
		}
	}

	@Test
	public void testEraseBadLoginsUserNotFound() {
		try {
			securityDAO.eraseBadLogins(NON_EXISTS_USERID);
		} catch (UserNotFoundException e) {
			return;
		} catch (Exception e) {
		}
		Assert.fail("User not found test error!");
	}

	@Test
	public void testLoadBadLogins() {
		try {
			securityDAO.loadBadLogins(USER_USERID);
		} catch (Exception e) {
			Assert.fail("Bad logins load has failed!");
		}
	}

	@Test
	public void testLoadBadLoginsUserNotFound() {
		try {
			securityDAO.loadBadLogins(NON_EXISTS_USERID);
		} catch (UserNotFoundException e) {
			return;
		} catch (Exception e) {

		}
		Assert.fail("User not found test error!");
	}

	@Test
	public void testUpdateBadLogins() {
		try {
			securityDAO.updateBadLogins(USER_USERID, new ArrayList<Date>());
		} catch (Exception e) {
			Assert.fail("Update bad logins error!");
		}
	}

	@Test
	public void testUpdateBadLoginsWithDatas() {
		try {
			ArrayList<Date> badLogins = new ArrayList<Date>();
			badLogins.add(new Date(343243333));
			badLogins.add(new Date());
			securityDAO.updateBadLogins(USER_USERID, badLogins);
		} catch (Exception e) {
			Assert.fail("Update bad logins error!");
		}
	}

	@Test
	public void testUpdateBadLoginsUserNotFound() {
		try {
			securityDAO.updateBadLogins(NON_EXISTS_USERID, new ArrayList<Date>());
		} catch (UserNotFoundException e) {
			return;
		} catch (Exception e) {

		}
		Assert.fail("User not found test error!");
	}

	@Test
	public void testUpdateLockedAndUntilLocked() {
		try {
			securityDAO.updateLockedAndUntilLocked(USER_USERID, false, new Date(), false);
		} catch (Exception e) {
			Assert.fail("Locked and Until locked update failed!");
		}
	}

	@Test
	public void testUpdateLockedAndUntilLockedUserNotFound() {
		try {
			securityDAO.updateLockedAndUntilLocked(NON_EXISTS_USERID, false, new Date(), false);
		} catch (UserNotFoundException e) {
			return;
		} catch (Exception e) {
			
		}
		Assert.fail("Locked and Until locked update failed!");
	}
}
