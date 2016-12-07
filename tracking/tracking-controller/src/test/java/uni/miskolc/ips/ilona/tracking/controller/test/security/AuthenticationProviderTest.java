package uni.miskolc.ips.ilona.tracking.controller.test.security;

import java.lang.reflect.Field;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import uni.miskolc.ips.ilona.tracking.controller.exception.authentication.AccountIsNotEnabledException;
import uni.miskolc.ips.ilona.tracking.controller.exception.authentication.InvalidUsernamePasswordException;
import uni.miskolc.ips.ilona.tracking.controller.exception.authentication.UserNotFoundException;
import uni.miskolc.ips.ilona.tracking.controller.model.UserSecurityDetails;
import uni.miskolc.ips.ilona.tracking.controller.security.CustomAuthenticationProvider;
import uni.miskolc.ips.ilona.tracking.util.TrackingModuleCentralManager;

public class AuthenticationProviderTest {

	private AuthenticationProvider provider;

	private final String USERID = "userid1";
	private final String USERID_NOT_EXISTS = "useridnonexists";
	private final String USERID_ENABLED_FALSE = "userid001";

	public AuthenticationProviderTest() throws Exception {
		this.provider = new CustomAuthenticationProvider();
		Field field = null;

		// centralManager injection
		Class<?> provClass = this.provider.getClass();
		field = provClass.getDeclaredField("centralManager");
		field.setAccessible(true);
		field.set(provider, generateCentralManager());

		// userDetailsService injection
		field = provClass.getDeclaredField("userService");
		field.setAccessible(true);
		field.set(provider, generateMockedUserService());

	}

	public Authentication generateMockedAuthentication(String principal, String credential) {
		Authentication auth = EasyMock.createMock(Authentication.class);
		EasyMock.expect(auth.getPrincipal()).andReturn(principal).anyTimes();
		EasyMock.expect(auth.getCredentials()).andReturn(credential).anyTimes();
		EasyMock.replay(auth);
		return auth;
	}

	public UserDetailsService generateMockedUserService() {
		UserDetailsService userService = EasyMock.createMock(UserDetailsService.class);
		EasyMock.expect(userService.loadUserByUsername(USERID)).andReturn(generateUserDetails(USERID, "dsadada", true))
				.anyTimes();
		EasyMock.expect(userService.loadUserByUsername(USERID_ENABLED_FALSE))
				.andReturn(generateUserDetails(USERID_ENABLED_FALSE, "dsadasdas", false)).anyTimes();
		EasyMock.expect(userService.loadUserByUsername(USERID_NOT_EXISTS))
				.andThrow(new UserNotFoundException("", USERID_NOT_EXISTS));
		EasyMock.replay(userService);
		return userService;
	}

	public UserDetails generateUserDetails(String userid, String password, boolean enabled) {
		UserSecurityDetails user = new UserSecurityDetails();
		user.setUserid(userid);
		user.setPassword(password);
		user.setEnabled(enabled);
		return user;
	}

	public TrackingModuleCentralManager generateCentralManager() {
		TrackingModuleCentralManager manager = new TrackingModuleCentralManager();
		manager.setEnabledCheckEnabled(true);
		manager.setAccountExpirationCheckEnabled(true);
		return manager;
	}

	@Test
	public void testPrincipalIsNull() {
		try {
			provider.authenticate(generateMockedAuthentication(null, "password1"));
		} catch (InvalidUsernamePasswordException e) {
			return;
		} catch (Exception e) {

		}
		Assert.fail("user principal null failed!");
	}

	@Test
	public void testCredentialsAreNull() {
		try {
			provider.authenticate(generateMockedAuthentication("user", null));
		} catch (InvalidUsernamePasswordException e) {
			return;
		} catch (Exception e) {

		}
		Assert.fail("user principal null failed!");
	}

	@Test
	public void testPrincipalAndCredentialsAreNull() {
		try {
			provider.authenticate(generateMockedAuthentication(null, null));
		} catch (InvalidUsernamePasswordException e) {
			return;
		} catch (Exception e) {

		}
		Assert.fail("user principal null failed!");
	}

	@Test
	public void testUserNotFound() {
		try {
			provider.authenticate(generateMockedAuthentication(USERID_NOT_EXISTS, "dsdasdas"));
		} catch (UserNotFoundException e) {
			return;
		} catch (Exception e) {

		}
		Assert.fail("FAIL!");
	}
	
	@Test
	public void testEnabledFalse() {
		try {
			this.provider.authenticate(generateMockedAuthentication(USERID_ENABLED_FALSE, "dsdsdsd"));
		} catch(AccountIsNotEnabledException e) {
			return;
		} catch(Exception e) {
			
		}
		Assert.fail("Enabled check is failed!");
	}
}
