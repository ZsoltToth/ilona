package uni.miskolc.ips.ilona.tracking.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uni.miskolc.ips.ilona.tracking.model.UserData;
import uni.miskolc.ips.ilona.tracking.service.TrackingLoginService;
import uni.miskolc.ips.ilona.tracking.service.exceptions.DatabaseUnavailableException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.DuplicatedUserException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.InvalidDataException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.NoSuchUserException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.UnenabledUserException;

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

	@Autowired
	private TrackingLoginService trackingLoginService;

	/**
	 * This method gives back the index page of the tracking modul. Login or
	 * registration.
	 * 
	 * @return The tracking index jsp page.
	 */
	@RequestMapping(value = "/index", method=RequestMethod.GET)
	public ModelAndView loadingStartpage() {
		logger.info("Tracking index page request.");
		ModelAndView back = new ModelAndView("tracking_index");
		back.addObject("message", "");
		return back;
	}

	/**
	 * This method gives back the registration page. New user registration.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ModelAndView loadLoginPage() {
		logger.info("Tracking login page request.");
		ModelAndView mav = new ModelAndView("tracking_registration");
		mav.addObject("message", "");
		return mav;
	}

	@RequestMapping(value = "/getUser/{userID}", method = RequestMethod.GET)
	@ResponseBody
	public UserData getUser(@RequestParam(value="userID", required=true)String userID) {
		UserData data = null;

		try {
			trackingLoginService.getUser(userID);
		} catch (DatabaseUnavailableException e) {

			e.printStackTrace();
		} catch (NoSuchUserException e) {

			e.printStackTrace();
		} catch (UnenabledUserException e) {

			e.printStackTrace();
		}
		return data;
	}
	
	@RequestMapping(value = "/createUser", method = RequestMethod.POST)
	public ModelAndView createUser(@RequestBody(required = true) UserData userData) {
		ModelAndView mav = null;
		try {
			trackingLoginService.createUser(userData);
		} catch (DatabaseUnavailableException e) {
			mav = new ModelAndView("tracking_registration");
			mav.addObject("message", "Az adatbazis nem elerheto!");
			return mav;
			//e.printStackTrace();
		} catch (InvalidDataException e) {
			mav = new ModelAndView("tracking_registration");
			mav.addObject("message", "Hibas adat(ok)!");
			return mav;
			//e.printStackTrace();
		} catch (DuplicatedUserException e) {
			mav = new ModelAndView("tracking_registration");
			mav.addObject("message", "Mar van ilyen felhasznalo!");
			return mav;
			//e.printStackTrace();
		}
		// after successful registration tracking main page?
		mav = new ModelAndView("tracking_index");
		mav.addObject("message", "Sikeres regisztracio");
		return mav;
	}

}
