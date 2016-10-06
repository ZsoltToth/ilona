package uni.miskolc.ips.ilona.tracking.controller.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.annotation.Resource;
import javax.sound.midi.Track;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uni.miskolc.ips.ilona.tracking.controller.exception.TrackingServiceErrorException;
import uni.miskolc.ips.ilona.tracking.controller.model.ExecutionResultDTO;
import uni.miskolc.ips.ilona.tracking.controller.model.PositionTrackDTO;
import uni.miskolc.ips.ilona.tracking.controller.model.UserSecurityDetails;
import uni.miskolc.ips.ilona.tracking.controller.track.GraphFunctions;
import uni.miskolc.ips.ilona.tracking.model.DeviceData;
import uni.miskolc.ips.ilona.tracking.model.TrackPosition;
import uni.miskolc.ips.ilona.tracking.model.UserData;
import uni.miskolc.ips.ilona.tracking.persist.TrackingDAO;
import uni.miskolc.ips.ilona.tracking.service.UserAndDeviceService;

@Controller
@RequestMapping(value = "/tracking/user")
public class UserTrackingDevicesController {

	private static Logger logger = LogManager.getLogger(UserTrackingDevicesController.class);

	@Resource(name = "UserAndDeviceService")
	private UserAndDeviceService userAndDeviceService;

	@Resource(name = "graphFunctions")
	private GraphFunctions graphFunctions;

	@Resource(name = "trackingDAO")
	private TrackingDAO trackingDAO;

	@RequestMapping(value = "/devicetracking", method = { RequestMethod.POST })
	public ModelAndView generateUserTrackingPageHandler() throws TrackingServiceErrorException {
		ModelAndView mav = new ModelAndView("tracking/user/tracking");
		try {
			UserSecurityDetails userDetails = (UserSecurityDetails) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			UserData user = userAndDeviceService.getUser(userDetails.getUserid());
			mav.addObject("devices", userAndDeviceService.readUserDevices(user));
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			throw new TrackingServiceErrorException();
		}

		return mav;
	}

	@RequestMapping(value = "/tracking/getpositions")
	@ResponseBody
	public Collection<PositionTrackDTO> getDevicePositionsHandler(@RequestParam(value = "deviceid") String deviceid,
			@RequestParam(value = "from") long from, @RequestParam(value = "to") long to)
			throws TrackingServiceErrorException {
		try {
			UserSecurityDetails userDetails = (UserSecurityDetails) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			UserData user = userAndDeviceService.getUser(userDetails.getUserid());
			DeviceData device = userAndDeviceService.readDevice(deviceid, user);
			Collection<TrackPosition> pos = trackingDAO.restorePositionsInterval(device, new Date(from), new Date(to));
			Collection<PositionTrackDTO> result = new ArrayList<PositionTrackDTO>(pos.size());
			for (TrackPosition position : pos) {
				result.add(PositionTrackDTO.convertToDTO(position));
			}
			return result;
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			throw new TrackingServiceErrorException();
		}
	}
	
	@RequestMapping(value = "/tracking/calculatepath", method = { RequestMethod.POST })
	@ResponseBody
	public Collection<String> calculatePath(@RequestParam(value = "start") String start,
			@RequestParam(value = "end") String end, @RequestParam("floor") int floor)
			throws TrackingServiceErrorException {
		try {
			return graphFunctions.generateShortestPath(start, end, floor);
		} catch (Exception e) {
			throw new TrackingServiceErrorException();
		}
	}

	@ExceptionHandler(TrackingServiceErrorException.class)
	@ResponseBody
	public ExecutionResultDTO serviceErrorHandler(TrackingServiceErrorException error) {
		ExecutionResultDTO result = new ExecutionResultDTO(400, new ArrayList<String>());
		result.addMessage("Service error!");
		return result;
	}
}
