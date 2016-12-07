package uni.miskolc.ips.ilona.tracking.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import uni.miskolc.ips.ilona.tracking.controller.exception.PasswordRecoveryTokenValidityErrorException;
import uni.miskolc.ips.ilona.tracking.controller.exception.PasswordResetServiceErrorException;
import uni.miskolc.ips.ilona.tracking.controller.exception.TrackingServiceErrorException;
import uni.miskolc.ips.ilona.tracking.controller.exception.authentication.CredentialsExpiredException;
import uni.miskolc.ips.ilona.tracking.controller.model.ExecutionResultDTO;
import uni.miskolc.ips.ilona.tracking.controller.model.ExpiredPasswordChangeDTO;
import uni.miskolc.ips.ilona.tracking.controller.model.UserCreationDTO;
import uni.miskolc.ips.ilona.tracking.controller.model.UserSecurityDetails;
import uni.miskolc.ips.ilona.tracking.controller.passwordrecovery.PasswordRecoveryManager;
import uni.miskolc.ips.ilona.tracking.controller.security.CustomAuthenticationFailureHandler;
import uni.miskolc.ips.ilona.tracking.controller.util.ValidateUserData;
import uni.miskolc.ips.ilona.tracking.controller.util.ValidityStatusHolder;
import uni.miskolc.ips.ilona.tracking.controller.util.WebpageInformationProvider;
import uni.miskolc.ips.ilona.tracking.model.DeviceData;
import uni.miskolc.ips.ilona.tracking.model.UserData;
import uni.miskolc.ips.ilona.tracking.service.UserAndDeviceService;
import uni.miskolc.ips.ilona.tracking.service.exceptions.DuplicatedUserException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.ServiceGeneralErrorException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.UserNotFoundException;
import uni.miskolc.ips.ilona.tracking.util.TrackingModuleCentralManager;

/*
* Content:
* 
* 0) dependencies
* 1) Entrypoint mapper
* 2) mainpage mappers
* 3) exception handlers
* 4) setters/getters
*/

/**
 * This controller is responsible for the main page requests in the ILONA
 * System. This class can:<br>
 * - load the main page navigation bar<br>
 * - load the main page home page<br>
 * - load the main page login page<br>
 * - load the main page registration page
 * 
 * @author Patrik / A5USL0
 *
 */
@Controller(value = "trackingEntryPointController")
@RequestMapping(value = "/tracking")
public class TrackingEntryPointController {

	private static Logger logger = LogManager.getLogger(TrackingEntryPointController.class);

	@Resource(name = "UserAndDeviceService")
	private UserAndDeviceService userDeviceService;

	@Resource(name = "trackingPasswordEncoder")
	private PasswordEncoder passwordEncoder;

	@Resource(name = "trackingCentralManager")
	private TrackingModuleCentralManager centralManager;

	@Resource(name = "passwordRecoveryManager")
	private PasswordRecoveryManager passwordRecoveryManager;

	/**
	 * This method sends back the actual tracking content.<br>
	 * <dl>
	 * <dt>If the user is not authenticated:</dt>
	 * <dd>- Sends back the start page of the tracking modul.</dd>
	 * <dd>- This page contains the login and the sign up functions.</dd>
	 * <dt>If the user is authenticated</dt>
	 * <dd>- Sends back the logged in user main page</dd>
	 * <dd>- If the user has the authority admin, the return page is the admin
	 * main page</dd>
	 * <dd>- If the user has the authority user, the return page is the user
	 * main page</dd>
	 * </dl>
	 * 
	 * @return The actual tracking content depends on the actual login status
	 *         and/or the actual authority of the current user.
	 */
	@RequestMapping(value = "/maincontentdecision", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView createTrackingContentpage(Authentication authentication) throws InterruptedException {
		/*
		 * 
		 * RETURN PAGE DEPENDS ON THE ACTUAL AUTHENTICATION! anonymousUser
		 */

		/*
		 * Get the actual authenticaton.
		 */
		authentication = SecurityContextHolder.getContext().getAuthentication();

		/*
		 * Check if the current user is anonymous user? Theoretically the
		 * authentication cannot be null.
		 */

		if (authentication == null) {
			logger.info("Anonymus authentication request!");
			return new ModelAndView("tracking/mainpageHome");
		}

		/*
		 * If the current user is not autthenticated (anonymus) the returned
		 * page will be the tracking login page.
		 */
		for (GrantedAuthority role : authentication.getAuthorities()) {
			if (role.getAuthority().equals("ROLE_ANONYMOUS")) {
				return new ModelAndView("tracking/mainpageHome");
			}
		}

		/**
		 * Chech for admin rights
		 */
		boolean isAdmin = false;
		for (GrantedAuthority auth : authentication.getAuthorities()) {
			if (auth.getAuthority().equals("ROLE_ADMIN")) {
				logger.info("Admin page authentication request!" + authentication.getName());
				// return new ModelAndView("tracking/admin/mainpage");
				isAdmin = true;
			}
		}

		/**
		 * Check for user rights
		 */
		for (GrantedAuthority auth : authentication.getAuthorities()) {
			if (auth.getAuthority().equals("ROLE_USER")) {
				logger.info("User page authentication request!" + authentication.getName());
				// return new ModelAndView("tracking/user/userMainpage");
			}
		}

		UserSecurityDetails userDetails = (UserSecurityDetails) authentication.getPrincipal();

		if (System.currentTimeMillis() > userDetails.getCredentialNonExpiredUntil().getTime()) {
			ModelAndView mav = new ModelAndView("tracking/passwordReset");
			mav.addObject("passwordRestriction", WebpageInformationProvider.getPasswordRestrictionMessage());
			mav.addObject("userid", userDetails.getUserid());
			return mav;
		}

		if (isAdmin == true) {
			return new ModelAndView("tracking/admin/mainpage");
		} else {
			return new ModelAndView("tracking/user/userMainpage");
		}
		/**
		 * Other role?
		 */
		// return new ModelAndView("tracking/mainpageHome");
	}

	/**
	 * 
	 * @return The login page.
	 */
	@RequestMapping(value = "/getmainpagehome", method = { RequestMethod.POST })
	public ModelAndView generateMainpageHome() {
		return new ModelAndView("tracking/mainpageHome");
	}

	/**
	 * 
	 * @return the user signup page
	 */
	@RequestMapping(value = "/getmainpagesignup", method = { RequestMethod.POST })
	public ModelAndView generateMainpageSignup() {
		ModelAndView mav = new ModelAndView("tracking/mainpageSignup");
		mav.addObject("useridRestriction", WebpageInformationProvider.getUseridRestrictionMessage());
		mav.addObject("usernameRestriction", WebpageInformationProvider.getUsernameRestrictionMessage());
		mav.addObject("passwordRestriction", WebpageInformationProvider.getPasswordRestrictionMessage());
		mav.addObject("emailRestriction", WebpageInformationProvider.getEmailRestrictionMessage());

		return mav;
	}

	/**
	 * 
	 * @param error
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getloginpage", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView generateLoginpage(@RequestParam(value = "error", required = false) String error) {
		ModelAndView mav = new ModelAndView("tracking/loginpage");
		mav.addObject("error", error);
		return mav;
	}

	/**
	 * 
	 * @param user
	 * @return {@link ExecutionResultDTO}<br>
	 *         <ul>
	 *         <li>100: OK</li>
	 *         <li>200: Parameter error</li>
	 *         <li>300: Validity error</li>
	 *         <li>400: Service error</li>
	 *         <li>500: Server error(timeout)</li>
	 *         <li>600: Duplicated user error</li>
	 *         </ul>
	 */
	@RequestMapping(value = "/registeruser", method = { RequestMethod.POST })
	@ResponseBody
	public ExecutionResultDTO registerUser(@ModelAttribute(name = "user") UserCreationDTO user) {

		/*
		 * Input validation.
		 */
		ExecutionResultDTO result = new ExecutionResultDTO(100, new ArrayList<String>());
		try {
			ValidityStatusHolder errors = new ValidityStatusHolder();
			errors.appendValidityStatusHolder(ValidateUserData.validateUserid(user.getUserid()));
			errors.appendValidityStatusHolder(ValidateUserData.validateUsername(user.getUsername()));
			errors.appendValidityStatusHolder(ValidateUserData.validateEmail(user.getEmail()));
			errors.appendValidityStatusHolder(ValidateUserData.validateRawPassword(user.getPassword()));

			if (!errors.isValid()) {
				result.setResponseState(300);
				result.setMessages(errors.getErrors());
				return result;
			}
		} catch (Exception e) {
			result.setResponseState(400);
			result.addMessage(e.getMessage());
			return result;
		}
		try {

			/*
			 * Password encoding.
			 */
			String encodedPassword = passwordEncoder.encode(user.getPassword());
			/*
			 * ROLES setup.
			 */
			ArrayList<String> roles = new ArrayList<String>();
			roles.add("ROLE_USER");

			/*
			 * Credentials validity setup.
			 */
			Date passwordExpired = new Date(new Date().getTime() + centralManager.getCredentialsValidityPeriod());

			/*
			 * Setup other still null fields.
			 */
			Collection<Date> badLogins = new ArrayList<Date>();
			Collection<DeviceData> devices = new ArrayList<DeviceData>();
			/*
			 * Create user with the given details. The account will be enabled
			 * and non locked and the last login date is the account creation
			 * time.
			 */
			UserData userDB = new UserData(user.getUserid(), user.getUsername(), user.getEmail(), encodedPassword, true,
					roles, new Date(), passwordExpired, new Date(), true, badLogins, devices);
			/*
			 * 
			 */
			this.userDeviceService.createUser(userDB);
		} catch (DuplicatedUserException e) {
			logger.error("Duplicated userid: " + user.getUserid());
			result.addMessage("Duplicated user with this userid: " + user.getUserid());
			result.setResponseState(600);
			return result;
		} catch (ServiceGeneralErrorException e) {
			logger.error("Error: " + e.getMessage());
			result.addMessage("There has been an error with the service, the account is not created!");
			result.setResponseState(400);
			return result;
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			result.addMessage("There has been an error with the service, the account is not created!");
			result.setResponseState(400);
			return result;
		}

		result.addMessage("Account has been created!");
		return result;
	}

	/**
	 * 
	 * @param userid
	 * @return {@link ExecutionResultDTO}<br>
	 *         <ul>
	 *         <li>100: OK</li>
	 *         <li>200: Parameter error</li>
	 *         <li>400: Service error</li>
	 *         <li>600: Duplicated user error</li>
	 *         </ul>
	 */
	@RequestMapping(value = "/resetpassword", method = { RequestMethod.POST })
	@ResponseBody
	public ExecutionResultDTO trackingResetPasswordHandler(
			@RequestParam(value = "userid", required = false) String userid) {
		ExecutionResultDTO result = new ExecutionResultDTO(100, new ArrayList<String>());
		if (userid == null) {
			result.setResponseState(200);
			result.addMessage("Invalid parameter!");
			return result;
		}
		try {
			UserData user = userDeviceService.getUser(userid);
			passwordRecoveryManager.handlePasswordRecoveryRequest(user);
		} catch (UserNotFoundException e) {
			logger.info("Password recovery has failed, user not found! userid: " + userid);
			result.addMessage("User not found with id: " + userid);
			result.setResponseState(600);
			return result;
		} catch (Exception e) {
			logger.info("Password recovery has failed! userid: " + userid);
			result.setResponseState(400);
			result.addMessage("Password recovery has failed!");
			return result;
		}

		result.addMessage("Password recovery has been sent!");
		return result;
	}

	/**
	 * 
	 * @param userid
	 * @param token
	 *            requested token id
	 * @return {@link ExecutionResultDTO}<br>
	 *         <ul>
	 *         <li>100: OK</li>
	 *         <li>200: Parameter error</li>
	 *         <li>300: Validity error</li>
	 *         <li>400: Service error</li>
	 *         <li>600: Duplicated user error</li>
	 *         </ul>
	 */
	@RequestMapping(value = "/passwordrequestwithtoken", method = { RequestMethod.POST })
	@ResponseBody
	public ExecutionResultDTO resetPasswordWithTokenHandler(
			@RequestParam(value = "userid", required = false) String userid,
			@RequestParam(value = "token", required = false) String token) {
		ExecutionResultDTO result = new ExecutionResultDTO(100, new ArrayList<String>());

		if (userid == null || token == null) {
			result.addMessage("Invalid paramter: userid: " + userid + " Token: " + token);
			result.setResponseState(200);
			return result;
		}
		try {
			UserData user = userDeviceService.getUser(userid);
			passwordRecoveryManager.handlePasswordRestore(user, token);
		} catch (UserNotFoundException e) {
			logger.info("user not found with id: " + userid);
			result.addMessage("user not found with id: " + userid);
			result.setResponseState(600);
			return result;
		} catch (PasswordRecoveryTokenValidityErrorException e) {
			logger.info("Password recovery token validity error! userid: " + userid + " token: " + token);
			result.addMessage("Password token validity error!");
			result.setResponseState(300);
			return result;
		} catch (Exception e) {
			logger.info("Password recovery error! userid: " + userid + " token: " + token);
			result.addMessage("Service error!");
			result.setResponseState(400);
			return result;
		}
		result.addMessage("The new password has been sent!");
		return result;
	}

	/**
	 * 
	 * @return {@link ExecutionResultDTO}<br>
	 *         <ul>
	 *         <li>100: OK</li>
	 *         <li>200: The given password doenst match the old password!</li>
	 *         <li>300: Password format is invalid!</li>
	 *         <li>400: Service error!</li>
	 *         <li>600: The new password arent equals!</li>
	 *         </ul>
	 */
	@RequestMapping(value = "/expiredpasswordchange", method = { RequestMethod.POST })
	public ModelAndView generatePasswordChangePageHandler(@ModelAttribute() ExpiredPasswordChangeDTO password)
			throws PasswordResetServiceErrorException {

		UserSecurityDetails user = (UserSecurityDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String actualPassword = password.getOldPassword();
		if (actualPassword == null || actualPassword.equals("")) {
			throw new PasswordResetServiceErrorException("Error!", 200);
		}

		if (!passwordEncoder.matches(actualPassword, user.getPassword())) {
			throw new PasswordResetServiceErrorException("Error!", 200);
		}

		String newPassword1 = password.getNewPassword1();
		String newPassword2 = password.getNewPassword2();

		ValidityStatusHolder valid = ValidateUserData.validateRawPassword(newPassword1);
		if (!valid.isValid()) {
			throw new PasswordResetServiceErrorException("Error!", 300);
		}

		if (!newPassword1.equals(newPassword2)) {
			throw new PasswordResetServiceErrorException("Error!", 600);
		}

		String hashedPassword = passwordEncoder.encode(newPassword1);
		Date credentialsValidity;
		try {
			UserData userDetail = userDeviceService.getUser(user.getUserid());
			userDetail.setPassword(hashedPassword);
			credentialsValidity = new Date(new Date().getTime() + this.centralManager.getCredentialsValidityPeriod());
			userDetail.setCredentialNonExpiredUntil(credentialsValidity);
			userDeviceService.updateUser(userDetail);
		} catch (Exception e) {
			throw new PasswordResetServiceErrorException("Error!", 400);
		}
		user.setPassword(hashedPassword);
		user.setCredentialNonExpiredUntil(credentialsValidity);
		return new ModelAndView("forward:/tracking/maincontentdecision");
	}

	@ExceptionHandler(PasswordResetServiceErrorException.class)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ExecutionResultDTO handlePasswordResetErrors(PasswordResetServiceErrorException ex) {
		int code = ex.getErrorCode();
		System.out.println("aaa" + code);
		ExecutionResultDTO result = new ExecutionResultDTO();
		switch (code) {
		case 200:
			result.setResponseState(200);
			result.addMessage("The old password doesnt match!");
			return result;
		case 300:
			result.setResponseState(300);
			result.addMessage("The password format is invalid!");
			return result;
		case 600:
			result.setResponseState(600);
			result.addMessage("The new password and the check password are not equal!");
			return result;
		default:
			result.setResponseState(400);
			result.addMessage("Service error!");
			return result;
		}
	}

	@ExceptionHandler(TrackingServiceErrorException.class)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.CREATED)
	public String dadsa() {
		return "";
	}

	public void setUserDeviceService(UserAndDeviceService userDeviceService) {
		if (userDeviceService != null) {
			this.userDeviceService = userDeviceService;
		}
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		if (passwordEncoder != null) {
			this.passwordEncoder = passwordEncoder;
		}
	}

	public void setCentralManager(TrackingModuleCentralManager centralManager) {
		if (centralManager != null) {
			this.centralManager = centralManager;
		}
	}

	public void setPasswordRecoveryManager(PasswordRecoveryManager passwordRecoveryManager) {
		if (passwordRecoveryManager != null) {
			this.passwordRecoveryManager = passwordRecoveryManager;
		}
	}

}
