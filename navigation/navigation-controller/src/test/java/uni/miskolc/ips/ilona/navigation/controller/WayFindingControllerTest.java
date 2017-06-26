package uni.miskolc.ips.ilona.navigation.controller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.easymock.EasyMock;

import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.navigation.controller.dto.NavigationPersonRequest;
import uni.miskolc.ips.ilona.navigation.controller.dto.NavigationZoneRequest;
import uni.miskolc.ips.ilona.navigation.service.WayfindingService;
import uni.miskolc.ips.ilona.navigation.service.WayfindingService.Restriction;

public class WayFindingControllerTest {

	private WayfindingService serviceMock;
	private WayfindingController test;
	private Zone from;
	private Zone to;
	private Set<Restriction> restrictions;
	private String person;
	
	
	@Before
	public void setUp() throws Exception {
		serviceMock = EasyMock.createMock(WayfindingService.class);
		test = new WayfindingController(serviceMock);
		
		from = new Zone("start");
		to = new Zone("finish");
		restrictions = new HashSet<>();
		person = "Big Bad wolf";
		
		List<Zone> result= new ArrayList<>();
		result.add(from);
		result.add(to);
		
		EasyMock.expect(serviceMock.generateRoute(from, to, restrictions)).andReturn(result).anyTimes();
		EasyMock.expect(serviceMock.generateRoute(from, person, restrictions)).andReturn(result).anyTimes();
		EasyMock.replay(serviceMock);
		
	}

	@Test
	public void testGetNavigationPath() {
		
		NavigationZoneRequest request = new NavigationZoneRequest(from.getId(), to.getId(), from.getName(), to.getName(), restrictions);
		
		List<Zone> result= new ArrayList<>();
		result.add(from);
		result.add(to);
		
		assertEquals(result, test.getNavigationPath(request));
	}
	
	@Test
	public void testNavigationPathToPerson(){
		
		NavigationPersonRequest request = new NavigationPersonRequest(from.getId(), person, from.getName(), restrictions);
		
		List<Zone> result= new ArrayList<>();
		result.add(from);
		result.add(to);
		
		assertEquals(result, test.getNavigationPathToPerson(request));
	}

}
