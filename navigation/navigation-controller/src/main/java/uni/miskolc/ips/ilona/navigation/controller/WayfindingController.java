package uni.miskolc.ips.ilona.navigation.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.navigation.controller.dto.NavigationPersonRequest;
import uni.miskolc.ips.ilona.navigation.controller.dto.NavigationZoneRequest;
import uni.miskolc.ips.ilona.navigation.service.NoRouteAvailableException;
import uni.miskolc.ips.ilona.navigation.service.WayfindingService;


@Controller
public class WayfindingController {
	
	private WayfindingService wayfindingService;

	public WayfindingController(WayfindingService wayfindingService) {
		super();
		this.wayfindingService = wayfindingService;
		}

	@RequestMapping(value = "/navigation/ontology/getpath", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody List<Zone> getNavigationPath(@RequestBody NavigationZoneRequest parameters) {
		List<Zone> result = new ArrayList<>();
	
		Zone start = new Zone();
		start.setId(parameters.getStartID());
		start.setName(parameters.getStartName());
		Zone destination = new Zone();
		destination.setId(parameters.getDestinationID());
		destination.setName(parameters.getDestinationName());
		try {
			result = wayfindingService.generateRoute(start,destination,parameters.getRestriction());
		} catch (NoRouteAvailableException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	@RequestMapping(value = "/navigation/ontology/getperson", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody List<Zone> getNavigationPathToPerson(@RequestBody NavigationPersonRequest parameters) {
		List<Zone> result = new ArrayList<>();
		
		Zone start = new Zone();
		start.setId(parameters.getStartID());
		start.setName(parameters.getStartName());
		try {
			result = wayfindingService.generateRoute(start,parameters.getPersonName(),parameters.getRestriction());
		} catch (NoRouteAvailableException e) {
			e.printStackTrace();
		}

		return result;
	}
	

	
	/* Helping method to get the JSON template
	 @RequestMapping(value="/navigation/getdummyobject")
	@ResponseBody
	public NavigationZoneRequest getTemplate(){
		Set<Restriction> res = new HashSet<>();
		res.add(Restriction.NO_ESCALATOR);
		res.add(Restriction.NO_STAIRS);
		return new NavigationZoneRequest(UUID.randomUUID(),UUID.randomUUID(),new String("fogy a"), new String("türelmem"), res);
		
	}*/

}
