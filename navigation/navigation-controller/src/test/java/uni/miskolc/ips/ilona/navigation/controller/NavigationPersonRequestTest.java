package uni.miskolc.ips.ilona.navigation.controller;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import uni.miskolc.ips.ilona.navigation.controller.dto.NavigationPersonRequest;
import uni.miskolc.ips.ilona.navigation.service.WayfindingService.Restriction;

public class NavigationPersonRequestTest {
	private String person;
	private UUID startID;
	private String startName;
	private Set<Restriction> restrictions;
	private NavigationPersonRequest request;
	
	
	@Before
	public void setUp() throws Exception {
		person = "Big Bad Wolf";
		startID = UUID.randomUUID();
		startName = "startZone";
		restrictions=new HashSet<>();
		restrictions.add(Restriction.DUMMY_ZONERESTRICTION);
		restrictions.add(Restriction.NO_DOOR);
		
		request = new NavigationPersonRequest(startID, person, startName, restrictions);
	}
	
	@Test
	public void testEmptyConstructor(){
		NavigationPersonRequest emptyRequest = new NavigationPersonRequest();
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
	public void testGetPerson() {
		assertEquals(person, request.getPersonName());
	}
	
	@Test
	public void testSetPerson(){
		request.setPersonName("changedName");
		assertEquals("changedName", request.getPersonName());
	}
	
	
}
