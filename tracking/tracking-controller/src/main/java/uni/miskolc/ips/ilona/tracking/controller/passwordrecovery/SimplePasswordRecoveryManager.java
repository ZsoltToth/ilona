package uni.miskolc.ips.ilona.tracking.controller.passwordrecovery;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;

import uni.miskolc.ips.ilona.tracking.controller.exception.PasswordRecoveryException;
import uni.miskolc.ips.ilona.tracking.controller.exception.PasswordRecoveryTokenValidityErrorException;
import uni.miskolc.ips.ilona.tracking.model.PasswordRecoveryToken;
import uni.miskolc.ips.ilona.tracking.model.UserData;
import uni.miskolc.ips.ilona.tracking.service.UserAndDeviceService;
import uni.miskolc.ips.ilona.tracking.util.TrackingModuleCentralManager;

public class SimplePasswordRecoveryManager implements PasswordRecoveryManager {

	private static Logger logger = LogManager.getLogger(SimplePasswordRecoveryManager.class);

	private PasswordTokenDAO tokenDAO;

	private PasswordTokenGenerator tokenGenerator;

	private PasswordTokenSender tokenSender;

	private UserAndDeviceService userDeviceService;

	private PasswordEncoder passwordEncoder;

	private TrackingModuleCentralManager centralManager;

	private PasswordGenerator passwordGenerator;

	public SimplePasswordRecoveryManager() {

	}

	public SimplePasswordRecoveryManager(PasswordTokenDAO tokenDAO, PasswordTokenGenerator tokenGenerator,
			PasswordTokenSender tokenSender, UserAndDeviceService userDeviceService, PasswordEncoder passwordEncoder,
			TrackingModuleCentralManager centralManager, PasswordGenerator passwordGenerator) {
		this.tokenDAO = tokenDAO;
		this.tokenGenerator = tokenGenerator;
		this.tokenSender = tokenSender;
		this.userDeviceService = userDeviceService;
		this.passwordEncoder = passwordEncoder;
		this.centralManager = centralManager;
		this.passwordGenerator = passwordGenerator;
	}

	@Override
	public void handlePasswordRecoveryRequest(String userid) throws PasswordRecoveryException {
		try {
			String passwordToken = tokenGenerator.generateToken();
			long tokenValidityLength = centralManager.getPasswordRecoveryTokenValidityTime();
			Date validUntil = new Date((new Date().getTime()) + tokenValidityLength);
			PasswordRecoveryToken token = new PasswordRecoveryToken(userid, passwordToken, validUntil);
			this.tokenDAO.storePasswordToken(token);
			UserData user = userDeviceService.getUser(userid);
			this.tokenSender.sendToken(userid, passwordToken, user.getEmail());
		} catch (Exception e) {
			logger.error("Token storage request failed! userid: " + userid + " Error: " + e.getMessage());
			throw new PasswordRecoveryException(
					"Token storage request failed! userid: " + userid + " Error: " + e.getMessage(), e);
		}
	}

	@Override
	public void handlePasswordRestore(String userid, String token)
			throws PasswordRecoveryTokenValidityErrorException, PasswordRecoveryException {
		try {
			token = token.trim();
			PasswordRecoveryToken requestToken = new PasswordRecoveryToken(userid, token, null);
			PasswordRecoveryToken tok = tokenDAO.loadPasswordToken(requestToken);
			Date until = tok.getTokenValidUntil();
			if (until.getTime() < new Date().getTime()) {
				throw new PasswordRecoveryTokenValidityErrorException("Validity error!");
			}
			if (!tok.getToken().equals(token)) {
				throw new PasswordRecoveryTokenValidityErrorException("validity error!");
			}
			UserData user = userDeviceService.getUser(userid);
			String oldPassword = user.getPassword();
			String newPassword = passwordGenerator.generatePassword();
			String hashedPassword = passwordEncoder.encode(newPassword);
			user.setPassword(hashedPassword);
			userDeviceService.updateUser(user);
			try {
				tokenSender.sendNewPassword(userid, newPassword, user.getEmail());
			} catch (Exception e) {
				user.setPassword(oldPassword);
				userDeviceService.updateUser(user);
				throw new PasswordRecoveryException("Email client is unreacheable!");
			}

		} catch (Exception e) {
			if (e instanceof PasswordRecoveryTokenValidityErrorException) {
				logger.error("Token restore request failed, token validity expired! userid: " + userid + " token:"
						+ token + " Error: " + e.getMessage());
				throw new PasswordRecoveryTokenValidityErrorException(
						"Token restore request failed, token validity expired! userid: " + userid + " token:" + token
								+ " Error: " + e.getMessage(),
						e);
			}

			logger.error("Token restore request failed! userid: " + userid + " token:" + token + " Error: "
					+ e.getMessage());
			throw new PasswordRecoveryException(
					"Token storage request failed! userid: " + userid + " token:" + token + " Error: " + e.getMessage(),
					e);
		}

	}

}
