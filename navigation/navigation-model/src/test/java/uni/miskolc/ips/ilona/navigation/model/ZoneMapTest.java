package uni.miskolc.ips.ilona.navigation.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import uni.miskolc.ips.ilona.measurement.model.position.Zone;

public class ZoneMapTest {
	static UUID zone1;
	static UUID zone2;
	static UUID zone3;
	static UUID zone4;
	static UUID zone5;
	static Gateway path1;
	static Gateway path2;
	static Gateway path3;
	static Gateway path4;
	static Gateway path5;
	static Gateway path6;
	static ZoneMap map;
	
	@BeforeClass
	public static void setUp(){
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
	public void testFindPathOnlyOne(){
		List<UUID> expected = new ArrayList<>();
		expected.add(zone1);
		expected.add(zone5);
		assertEquals(expected,map.findPath(zone1, zone5));
	}
	
	@Test
	public void testFindPathMore(){
		List<UUID> expected = new ArrayList<>();
		expected.add(zone1);
		expected.add(zone5);
		List<UUID> destinations = new ArrayList<>();
		destinations.add(zone4);
		destinations.add(zone5);
		assertEquals(expected, map.findPath(zone1, destinations));
	}
	
	

}
