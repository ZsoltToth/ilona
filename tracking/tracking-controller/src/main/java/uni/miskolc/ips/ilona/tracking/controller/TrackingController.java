package uni.miskolc.ips.ilona.tracking.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import uni.miskolc.ips.ilona.tracking.persist.UserAndDeviceDAO;

/**
 * Tracking module
 * 
 * @author Patrik
 *
 */
@Controller
@RequestMapping(value = "/tracking")
public class TrackingController {

	private static Logger logger = LogManager.getLogger(TrackingController.class);

	@RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
	public String logoutHandler() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("trackingIndex");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			mav.addObject("message", auth.getPrincipal().toString());
		} else {
			mav.addObject("message", "Nincs felhasznalo!");
		}
		mav.addObject("message", auth.getPrincipal().toString());
		return "redirect:/tracking/index";
	}

}