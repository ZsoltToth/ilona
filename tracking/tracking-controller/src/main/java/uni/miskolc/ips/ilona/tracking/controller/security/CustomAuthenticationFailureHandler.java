package uni.miskolc.ips.ilona.tracking.controller.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import uni.miskolc.ips.ilona.tracking.controller.exception.authentication.AccountExpiredException;
import uni.miskolc.ips.ilona.tracking.controller.exception.authentication.AccountIsNotEnabledException;
import uni.miskolc.ips.ilona.tracking.controller.exception.authentication.AccountLockedException;
import uni.miskolc.ips.ilona.tracking.controller.exception.authentication.CredentialsExpiredException;
import uni.miskolc.ips.ilona.tracking.controller.exception.authentication.InvalidUsernamePasswordException;
import uni.miskolc.ips.ilona.tracking.controller.exception.authentication.UserNotFoundException;
import uni.miskolc.ips.ilona.tracking.controller.model.AuthenticationFailedDTO;
import uni.miskolc.ips.ilona.tracking.controller.model.ExecutionResultDTO;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	public final static int RES_INVALID_USERNAME_PASSWORD = 100;
	public final static int RES_ACCOUNT_NOT_ENABLED = 200;
	public final static int RES_ACCOUNT_EXPIRED = 300;
	public final static int RES_ACCOUNT_LOCKED = 400;
	public final static int RES_CREDENTIALS_EXPIRED = 500;
	public final static int RES_SERVICE_ERROR = 600;

	public final static String USERID = "AuthFaild_Userid";

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException ex) throws IOException, ServletException {

		if (ex instanceof InvalidUsernamePasswordException || ex instanceof UserNotFoundException) {
			InitializeResponse(response, RES_INVALID_USERNAME_PASSWORD, "Invalid username or password!",
					System.currentTimeMillis());
			return;
		}

		if (ex instanceof AccountIsNotEnabledException) {
			InitializeResponse(response, RES_ACCOUNT_NOT_ENABLED, "The account is not enabled!",
					System.currentTimeMillis());
			return;
		}

		if (ex instanceof AccountExpiredException) {
			InitializeResponse(response, RES_ACCOUNT_EXPIRED, "Account Expired", System.currentTimeMillis());
			return;
		}

		if (ex instanceof AccountLockedException) {
			AccountLockedException e = (AccountLockedException) ex;
			InitializeResponse(response, RES_ACCOUNT_LOCKED, "AccountLocked", e.getLockedUntil());
			return;
		}

		if (ex instanceof CredentialsExpiredException) {
			InitializeResponse(response, RES_CREDENTIALS_EXPIRED, "Passsoword expired", System.currentTimeMillis());
			return;
		}

		InitializeResponse(response, RES_SERVICE_ERROR, "Service error!", System.currentTimeMillis());
	}

	private void InitializeResponse(HttpServletResponse response, int responseState, String message, long timestamp)
			throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		AuthenticationFailedDTO dto = new AuthenticationFailedDTO();
		dto.setResponseState(responseState);
		dto.setMessage(message);
		dto.setTimestamp(timestamp);
		PrintWriter writer = response.getWriter();
		writer.write(mapper.writeValueAsString(dto));
	}

}
