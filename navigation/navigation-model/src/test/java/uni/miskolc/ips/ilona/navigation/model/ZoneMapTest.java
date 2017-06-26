package uni.miskolc.ips.ilona.navigation.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ZoneMapTest {
	//The vertices of the test map
	private UUID zone1;
	private UUID zone2;
	private UUID zone3;
	private UUID zone4;
	private UUID zone5;
	//the edges of the test map
	private Gateway path1;
	private Gateway path2;
	private Gateway path3;
	private Gateway path4;
	private Gateway path5;
	private Gateway path6;
	//The test map graph
	private ZoneMap map;
	private UUID unconnectedZone;
	
	@Before
	public void setUp(){
		Set<UUID> zones = new HashSet<>();
		Set<Gateway> paths = new HashSet<>();
		
		zone1 = UUID.randomUUID();
		zones.add(zone1);
		zone2 = UUID.randomUUID();
		zones.add(zone2);
		zone3 = UUID.randomUUID();
		zones.add(zone3);
		zone4 = UUID.randomUUID();
		zones.add(zone4);
		zone5 = UUID.randomUUID();
		zones.add(zone5);
		unconnectedZone = UUID.randomUUID();
		zones.add(unconnectedZone);
		
		path1= new Gateway(zone1, zone2);
		paths.add(path1);
		path2= new Gateway(zone2, zone3);
		paths.add(path2);
		path3= new Gateway(zone3, zone4);
		paths.add(path3);
		path4= new Gateway(zone4, zone5);
		paths.add(path4);
		path5= new Gateway(zone1, zone5);
		paths.add(path5);
		path6= new Gateway(zone2, zone4);
		paths.add(path6);
		
		map = new ZoneMap(zones, paths);
	}
	
	@Test
	public void testFindPathOnlyOneDestination(){
		List<UUID> expected = new ArrayList<>();
		expected.add(zone1);
		expected.add(zone5);
		assertEquals(expected,map.findPath(zone1, zone5));
	}
	
	@Test
	public void testFindPathSameStartAndDestination(){
		List<UUID> expected = new ArrayList<>();
		expected.add(zone1);
		assertEquals(expected,map.findPath(zone1, zone1));
	}
	
	@Test
	public void testFindPathMultiplePaths(){
		List<UUID> expected = new ArrayList<>();
		expected.add(zone1);
		expected.add(zone2);
		expected.add(zone4);
		assertEquals(expected,map.findPath(zone1, zone4));
	}
	
	@Test
	public void testFindPathOnlyOnePath(){
		List<UUID> expected = new ArrayList<>();
		expected.add(zone1);
		expected.add(zone2);
		expected.add(zone3);
		assertEquals(expected,map.findPath(zone1, zone3));
	}
	
	@Test(expected=NoPathAvailableException.class)
	public void testFindPathReversePath(){
		map.findPath(zone5, zone1);
	}
	
	@Test
	public void testFindPathMoreDestination(){
		List<UUID> expected = new ArrayList<>();
		expected.add(zone1);
		expected.add(zone5);
		List<UUID> destinations = new ArrayList<>();
		destinations.add(zone4);
		destinations.add(zone5);
		assertEquals(expected, map.findPath(zone1, destinations));
	}
	
	@Test(expected=NoPathAvailableException.class)
	public void testFindPathException(){
		map.findPath(zone1, unconnectedZone);
		
	}
	
	@Test(expected=NoPathAvailableException.class)
	public void testFindPathMoreException(){
		List<UUID> destinations = new ArrayList<>();
		destinations.add(unconnectedZone);
		map.findPath(zone1,destinations);
	}
	

}
