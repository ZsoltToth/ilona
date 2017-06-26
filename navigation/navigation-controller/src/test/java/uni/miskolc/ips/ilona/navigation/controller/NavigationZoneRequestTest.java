package uni.miskolc.ips.ilona.navigation.controller;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import uni.miskolc.ips.ilona.navigation.controller.dto.NavigationZoneRequest;
import uni.miskolc.ips.ilona.navigation.service.WayfindingService.Restriction;

public class NavigationZoneRequestTest {
	
	private UUID startID;
	private String startName;
	private UUID destinationID;
	private String destinationName;
	private Set<Restriction> restrictions;
	private NavigationZoneRequest request;
	

	@Before
	public void setUp() throws Exception {
		startID = UUID.randomUUID();
		startName = "startZone";
		destinationID=UUID.randomUUID();
		destinationName="destination";
		restrictions=new HashSet<>();
		restrictions.add(Restriction.DUMMY_ZONERESTRICTION);
		restrictions.add(Restriction.NO_DOOR);
		
		request=new NavigationZoneRequest(startID, destinationID, startName, destinationName, restrictions);
	}

	@Test
	public void testEmptyConstructor(){
		NavigationZoneRequest emptyRequest = new NavigationZoneRequest();
		assertTrue(emptyRequest!=null);
	}

	@Test
	public void testGetStartID() {
		assertEquals(startID, request.getStartID());
	}
	
	@Test
	public void testGetStartName() {
		assertEquals(startName, request.getStartName());
	}
	
	@Test
	public void testRestrictions() {
		assertEquals(restrictions.toString(), request.getRestriction().toString());
	}
	
	@Test
	public void testSetRestriction(){
	 Set<Restriction> changedRestrictions=new HashSet<>();
	 changedRestrictions.add(Restriction.NO_ESCALATOR);
	 request.setRestriction(changedRestrictions);
	 assertEquals(changedRestrictions, request.getRestriction());
	}

	@Test
	public void testSetStartName(){
		request.setStartName("changedName");
		assertEquals("changedName", request.getStartName());
	}
	
	@Test
	public void testSetStartID(){
		UUID newUUID=UUID.randomUUID();
		request.setStartID(newUUID);
		assertEquals(newUUID, request.getStartID());
	}
	
	@Test
	public void testGetDestinationID() {
		assertEquals(startID, request.getStartID());
	}
	
	@Test
	public void testGetDestinationName() {
		assertEquals(startName, request.getStartName());
	}
	
	@Test
	public void testSetDestinationName(){
		request.setStartName("changedName");
		assertEquals("changedName", request.getStartName());
	}
	
	@Test
	public void testSetDestinationID(){
		UUID newUUID=UUID.randomUUID();
		request.setStartID(newUUID);
		assertEquals(newUUID, request.getStartID());
	}

}
