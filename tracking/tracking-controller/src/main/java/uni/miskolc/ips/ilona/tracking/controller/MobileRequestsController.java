package uni.miskolc.ips.ilona.tracking.controller;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.tracking.controller.model.MobileTransferDTO;
import uni.miskolc.ips.ilona.tracking.controller.model.UserSecurityDetails;
import uni.miskolc.ips.ilona.tracking.model.DeviceData;
import uni.miskolc.ips.ilona.tracking.service.TrackingService;
import uni.miskolc.ips.ilona.tracking.service.UserAndDeviceService;

@Controller
@RequestMapping(value = "/tracking/mobile")
public class MobileRequestsController {

	@Resource(name = "trackService")
	private TrackingService trackingService;

	@Resource(name = "UserAndDeviceService")
	private UserAndDeviceService userAndDeviceService;

	public MobileRequestsController() {
	}

	@RequestMapping(value = "/trackposition")
	@ResponseBody
	public String mobileSendMeasurementHandler(@RequestBody(required = false) MobileTransferDTO dto) {

		Position position = null;
		try {
			position = trackingService.calculatePosition(dto.getMeasurement());
			DeviceData device = userAndDeviceService.readDevice(dto.getDeviceid());
			trackingService.storePosition(device, position);
		} catch (Exception e) {
			return e.getMessage();
		}

		UserSecurityDetails userDetails = (UserSecurityDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		return "OK!";
	}

}
