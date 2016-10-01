package uni.miskolc.ips.ilona.navigation.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.math.analysis.interpolation.NevilleInterpolator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.MediaType;

import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.navigation.service.NoRouteAvailableException;
import uni.miskolc.ips.ilona.navigation.service.WayfindingService;
import uni.miskolc.ips.ilona.navigation.service.WayfindingService.Restriction;

@Controller
public class WayfindingController {
	//NavigationZoneRequest parameters
	@Autowired
	private WayfindingService wayfindingService;

	@RequestMapping(value = "/navigation/ontology/getpath", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody List<Zone> getNavigationPath(@RequestBody NavigationZoneRequest parameters) {
		List<Zone> result = new ArrayList<>();
		
		//System.out.println(parameters.getStartName());
		Zone start = new Zone();
		start.setId(parameters.getStartID());
		start.setName(parameters.getStartName());
		Zone destination = new Zone();
		destination.setId(parameters.getDestinationID());
		destination.setName(parameters.getDestinationName());
		try {
			result = wayfindingService.generateRoute(start,destination,parameters.getRestriction());
		} catch (NoRouteAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	@RequestMapping(value = "/navigation/ontology/getperson", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody List<Zone> getNavigationPathToPerson(@RequestBody NavigationPersonRequest parameters) {
		List<Zone> result = new ArrayList<>();
		
		//System.out.println(parameters.getStartName());
		Zone start = new Zone();
		start.setId(parameters.getStartID());
		start.setName(parameters.getStartName());
		try {
			result = wayfindingService.generateRoute(start,parameters.getPersonName(),parameters.getRestriction());
		} catch (NoRouteAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	

	
	@RequestMapping(value="/navigation/getdummyobject")
	@ResponseBody
	public NavigationZoneRequest getTemplate(){
		Set<Restriction> res = new HashSet<>();
		res.add(Restriction.NO_ESCALATOR);
		res.add(Restriction.NO_STAIRS);
		return new NavigationZoneRequest(UUID.randomUUID(),UUID.randomUUID(),new String("fogy a"), new String("türelmem"), res);
		
	}

}
