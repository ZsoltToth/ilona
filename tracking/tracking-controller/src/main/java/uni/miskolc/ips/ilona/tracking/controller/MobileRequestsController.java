package uni.miskolc.ips.ilona.tracking.controller;

import java.util.Collection;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.tracking.controller.exception.rest.BadRequestException;
import uni.miskolc.ips.ilona.tracking.controller.exception.rest.DeviceNotExistsException;
import uni.miskolc.ips.ilona.tracking.controller.exception.rest.PositionIsAlreadyExistsException;
import uni.miskolc.ips.ilona.tracking.controller.exception.rest.ServiceErrorException;
import uni.miskolc.ips.ilona.tracking.controller.model.ExecutionResultDTO;
import uni.miskolc.ips.ilona.tracking.controller.model.MobileTransferAdminDTO;
import uni.miskolc.ips.ilona.tracking.controller.model.MobileTransferDTO;
import uni.miskolc.ips.ilona.tracking.controller.model.UserSecurityDetails;
import uni.miskolc.ips.ilona.tracking.model.DeviceData;
import uni.miskolc.ips.ilona.tracking.model.UserData;
import uni.miskolc.ips.ilona.tracking.service.TrackingService;
import uni.miskolc.ips.ilona.tracking.service.UserAndDeviceService;
import uni.miskolc.ips.ilona.tracking.service.exceptions.DeviceNotFoundException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.DuplicatedPositionException;

@Controller
@RequestMapping(value = "/tracking/mobile")
public class MobileRequestsController {

	private Logger logger = LogManager.getLogger(MobileRequestsController.class);

	@Resource(name = "trackService")
	private TrackingService trackingService;

	@Resource(name = "UserAndDeviceService")
	private UserAndDeviceService userAndDeviceService;

	public MobileRequestsController() {
	}

	/**
	 * Calculate and store the current position.
	 * 
	 * @param dto
	 *            A fully populated {@link MobileTransferDTO} object in json.
	 * @return The current position in json.
	 * @throws ServiceErrorException
	 *             General service error! <br/>
	 *             Error code: 400<br/>
	 *             Response status: Server error!
	 * @throws DeviceNotExistsException
	 *             The current user has no such device! <br/>
	 *             Error code: 200. <br/>
	 *             Response status: Bad request!
	 * @throws PositionIsAlreadyExistsException
	 *             The current position is already exists<br/>
	 *             Error code: 300.<br/>
	 *             Response status: Bad request!
	 */
	@RequestMapping(value = "/trackposition", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Position mobileSendMeasurementHandler(@RequestBody(required = false) MobileTransferDTO dto)
			throws ServiceErrorException, DeviceNotExistsException, PositionIsAlreadyExistsException {

		Position position = null;
		try {
			position = trackingService.calculatePosition(dto.getMeasurement());
			UserSecurityDetails userDetails = (UserSecurityDetails) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();

			UserData user = userAndDeviceService.getUser(userDetails.getUserid());
			DeviceData device = userAndDeviceService.readDevice(dto.getDeviceid(), user);
			trackingService.storePosition(device, position);
			return position;
		} catch (DeviceNotFoundException e) {
			throw new DeviceNotExistsException("Device not found with id: " + dto.getDeviceid());
		} catch (DuplicatedPositionException e) {
			throw new PositionIsAlreadyExistsException();
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			throw new ServiceErrorException();
		}
	}

	/**
	 * 
	 * @param measurement
	 *            A fully populated measurement {@link Measurement}
	 * @return A calculated position {@link Position}
	 * @throws ServiceErrorException
	 *             General service error!<br/>
	 *             Error code: 400. <br/>
	 *             Response status: Internal server error!
	 */
	@RequestMapping(value = "/calculateposition", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Position calculatePositionHandler(@RequestBody() Measurement measurement) throws ServiceErrorException {
		try {
			return trackingService.calculatePosition(measurement);
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			throw new ServiceErrorException();
		}
	}

	/**
	 * 
	 * @param request
	 *            {@link MobileTransferAdminDTO}
	 * @return The calculated position.
	 * @throws BadRequestException
	 *             Bad request error! <br/>
	 *             Error code: 100<br/>
	 *             Response status: Bad request!
	 * @throws PositionIsAlreadyExistsException
	 *             The current position is already exists<br/>
	 *             Error code: 300.<br/>
	 *             Response status: Bad request!
	 * @throws DeviceNotExistsException
	 *             The current user has no such device! <br/>
	 *             Error code: 200. <br/>
	 *             Response status: Bad request!
	 * @throws ServiceErrorException
	 *             General service error! <br/>
	 *             Error code: 400<br/>
	 *             Response status: Server error!
	 */
	@RequestMapping(value = "/trackingpositionadmin", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Position trackingPositionAdminHandler(@RequestBody MobileTransferAdminDTO request)
			throws BadRequestException, PositionIsAlreadyExistsException, DeviceNotExistsException,
			ServiceErrorException {
		if (request.getDeviceid() == null || request.getUserid() == null) {
			throw new BadRequestException();
		}
		UserSecurityDetails userDetails = (UserSecurityDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		try {

			if (!userDetails.getUserid().equals(request.getUserid())) {
				if (!isAdmin(userDetails)) {
					throw new BadRequestException();
				}
			}
			Position position = trackingService.calculatePosition(request.getMeasurement());
			UserData user = userAndDeviceService.getUser(request.getUserid());
			DeviceData device = userAndDeviceService.readDevice(request.getDeviceid(), user);
			trackingService.storePosition(device, position);
			return position;
		} catch (BadRequestException e) {
			logger.error("Authority violation: Userid: " + userDetails.getUserid() + " wanted to modifity this user: "
					+ request.getUserid());
			throw new BadRequestException();
		} catch (DeviceNotFoundException e) {
			throw new DeviceNotExistsException();
		} catch (DuplicatedPositionException e) {
			throw new PositionIsAlreadyExistsException();
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			throw new ServiceErrorException();
		}
	}

	private boolean isAdmin(UserSecurityDetails userDetails) {
		Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
		if (roles == null) {
			return false;
		}

		for (GrantedAuthority role : roles) {
			if ("ROLE_ADMIN".equals(role.getAuthority())) {
				return true;
			}
		}
		return false;
	}

	@ExceptionHandler(BadRequestException.class)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	private int badRequestErrorHandler() {
		return 100;
	}

	@ExceptionHandler(ServiceErrorException.class)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	private int ServiceErrorExceptionHandler() {
		return 400;
	}

	@ExceptionHandler(DeviceNotExistsException.class)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	private int deviceNotFoundErrorHandler() {
		return 200;
	}

	@ExceptionHandler(PositionIsAlreadyExistsException.class)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	private int positionAlreadyExistsErrorHandler() {
		return 300;
	}
}
