package uni.miskolc.ips.ilona.tracking.controller.security;

import java.util.ArrayList;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import uni.miskolc.ips.ilona.tracking.controller.exception.authentication.AccountExpiredException;
import uni.miskolc.ips.ilona.tracking.controller.exception.authentication.AccountIsNotEnabledException;
import uni.miskolc.ips.ilona.tracking.controller.exception.authentication.AccountLockedException;
import uni.miskolc.ips.ilona.tracking.controller.exception.authentication.CredentialsExpiredException;
import uni.miskolc.ips.ilona.tracking.controller.exception.authentication.InvalidUsernamePasswordException;
import uni.miskolc.ips.ilona.tracking.controller.exception.authentication.ServiceErrorException;
import uni.miskolc.ips.ilona.tracking.controller.exception.authentication.UserNotFoundException;
import uni.miskolc.ips.ilona.tracking.controller.model.UserSecurityDetails;
import uni.miskolc.ips.ilona.tracking.controller.passwordrecovery.PasswordGenerator;
import uni.miskolc.ips.ilona.tracking.controller.passwordrecovery.PasswordTokenSender;
import uni.miskolc.ips.ilona.tracking.persist.SecurityFunctionsDAO;
import uni.miskolc.ips.ilona.tracking.util.TrackingModuleCentralManager;

public class CustomAuthenticationProvider implements AuthenticationProvider {

	private static Logger logger = LogManager.getLogger(CustomAuthenticationProvider.class);

	private UserDetailsService userService;

	private PasswordEncoder passwordEncoder;

	private TrackingModuleCentralManager centralManager;

	private SecurityFunctionsDAO securityDAO;

	private PasswordGenerator passwordGenerator;

	private PasswordTokenSender passwordSender;

	public CustomAuthenticationProvider() {

	}

	public CustomAuthenticationProvider(UserDetailsService userService, PasswordEncoder passwordEncoder,
			TrackingModuleCentralManager centralManager, SecurityFunctionsDAO securityDAO,
			PasswordGenerator passwordGenerator, PasswordTokenSender passwordSender) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.centralManager = centralManager;
		this.securityDAO = securityDAO;
		this.passwordGenerator = passwordGenerator;
		this.passwordSender = passwordSender;
	}

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {

		/*
		 * Checked by the ProviderManager?! if
		 * (!auth.getClass().equals(UsernamePasswordAuthenticationToken.class))
		 * { return null; }
		 */

		// Get the username and password
		String userid = null;
		String password = null;
		try {
			userid = auth.getPrincipal().toString();
			password = auth.getCredentials().toString();
		} catch (Exception e) {

		}
		if (userid == null || password == null) {
			throw new InvalidUsernamePasswordException("Invalid username or password!");
		}

		if (userid.equals("") || password.equals("")) {
			throw new InvalidUsernamePasswordException("Invalid username or password!");
		}
		UserSecurityDetails user = null;

		try {
			user = (UserSecurityDetails) userService.loadUserByUsername(userid);

		} catch (UsernameNotFoundException e) {
			throw new UserNotFoundException("User not found!", userid);
		}

		if (centralManager.isEnabledCheckEnabled()) {
			if (!user.isEnabled()) {
				throw new AccountIsNotEnabledException("Account is not enabled!");
			}
		}

		if (centralManager.isAccountExpirationCheckEnabled()) {
			long ExpirationTime = centralManager.getAccountExpirationTime();
			long currentTime = System.currentTimeMillis();
			long lastLogin = user.getLastLoginDate().getTime();
			if ((currentTime - ExpirationTime) > lastLogin) {
				throw new AccountExpiredException("Account expired!");
			}
		}

		if (centralManager.isLockedCheckEnabled()) {
			if (!user.isNonLocked()) {

				// the account is locked but the lock time is expired check and
				// restore
				if (System.currentTimeMillis() > user.getLockedUntil().getTime()) {
					try {
						securityDAO.updateLockedAndUntilLocked(userid, true, user.getLockedUntil(), false);
					} catch (Exception e) {
						logger.error("Error: " + e.getMessage());
						throw new ServiceErrorException("Service error!");
					}
				} else {
					throw new AccountLockedException("Account locked!", user.getLockedUntil().getTime());
				}
			}

			if (user.getBadLogins().size() > centralManager.getBadLoginsUpperBound()) {
				// user.setBadLogins(new ArrayList<Date>());
				// user.setNonLocked(false);
				Date lockUntil = new Date(System.currentTimeMillis() + centralManager.getLockedTimeAfterBadLogins());
				// user.setLockedUntil(lockUntil);
				// securityDAO.eraseBadLogins(userid);
				try {
					securityDAO.updateLockedAndUntilLocked(userid, false, lockUntil, true);
				} catch (Exception e) {
					logger.error("Error: " + e.getMessage());
					throw new ServiceErrorException("Service error!");
				}
				throw new AccountLockedException("Account locked", lockUntil.getTime());
			}
		}

		if (System.currentTimeMillis()  > user.getCredentialNonExpiredUntil().getTime()) {
			String newPassword = passwordGenerator.generatePassword(8);
			String hashedPassword = passwordEncoder.encode(newPassword);
			try {

				securityDAO.updatePassword(userid, hashedPassword,
						new Date(System.currentTimeMillis() + centralManager.getCredentialsValidityPeriod()));

				passwordSender.sendNewPassword(userid, newPassword, user.getEmail());
			} catch (Exception e) {
				logger.error("Error: " + e.getMessage());
				throw new ServiceErrorException("Service error!");
			}
			throw new CredentialsExpiredException("Password expired!");
		}

		if (!passwordEncoder.matches(password, user.getPassword())) {
			user.getBadLogins().add(new Date());
			try {
				securityDAO.updateBadLogins(userid, user.getBadLogins());
			} catch (Exception e) {
				logger.error("Error: " + e.getMessage());
				throw new ServiceErrorException("Service error!");
			}
			throw new InvalidUsernamePasswordException("Invalid password!");
		}
		try {
			securityDAO.updateAccountExpiration(userid, new Date());
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			throw new ServiceErrorException("Service error!");
		}

		Authentication successAuth = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
				user.getAuthorities());
		return successAuth;

	}

	@Override
	public boolean supports(Class<?> arg0) {
		if (arg0.equals(UsernamePasswordAuthenticationToken.class)) {
			return true;
		}
		return false;
	}
}
