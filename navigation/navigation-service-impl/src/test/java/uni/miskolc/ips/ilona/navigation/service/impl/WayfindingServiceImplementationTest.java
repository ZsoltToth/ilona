package uni.miskolc.ips.ilona.navigation.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.measurement.persist.ZoneDAO;
import uni.miskolc.ips.ilona.measurement.persist.exceptions.RecordNotFoundException;
import uni.miskolc.ips.ilona.navigation.model.Gateway;
import uni.miskolc.ips.ilona.navigation.model.ZoneMap;
import uni.miskolc.ips.ilona.navigation.persist.OntologyDAO;
import uni.miskolc.ips.ilona.navigation.persist.OntologyDAO.GatewayRestriction;
import uni.miskolc.ips.ilona.navigation.persist.OntologyDAO.ZoneRestriction;
import uni.miskolc.ips.ilona.navigation.service.NoRouteAvailableException;
import uni.miskolc.ips.ilona.navigation.service.WayfindingService.Restriction;

public class WayfindingServiceImplementationTest {
	private WayfindingServiceImpl testService;

	//The expected values of the tests
	private Zone zoneOne;
	private Zone zoneTwo;
	private Zone zoneThree;
	private String person;
	private Set<Restriction> restrictions;

	@Before
	public void setUp() throws RecordNotFoundException {
		restrictions = new HashSet<>();
		restrictions.add(Restriction.NO_ELEVATOR);
		restrictions.add(Restriction.NO_DOOR);
		restrictions.add(Restriction.DUMMY_ZONERESTRICTION);
		person = new String("Dummy");

		ZoneDAO zoneDAOMock = EasyMock.createMock(ZoneDAO.class);
		OntologyDAO ontoDAO = EasyMock.createMock(OntologyDAO.class);
		zoneOne = new Zone("za");
		zoneTwo = new Zone("zb");
		zoneThree = new Zone("zc");
		UUID a = zoneOne.getId();
		UUID b = zoneTwo.getId();
		UUID c = zoneThree.getId();
		Set<UUID> set = new HashSet<>();
		set.add(c);
		set.add(b);
		set.add(a);
		Set<Gateway> paths = new HashSet<>();
		paths.add(new Gateway(a, b));
		paths.add(new Gateway(b, c));
		ZoneMap map = new ZoneMap(set, paths);

		Set<GatewayRestriction> gatewayRestrictions = new HashSet<>();
		gatewayRestrictions.add(GatewayRestriction.NO_ELEVATOR);
		gatewayRestrictions.add(GatewayRestriction.NO_DOOR);

		Set<ZoneRestriction> zoneRestrictions = new HashSet<>();
		zoneRestrictions.add(ZoneRestriction.DUMMY_ZONERESTRICTION);

		EasyMock.expect(ontoDAO.createGraphWithoutRestrictions()).andReturn(map).anyTimes();
		EasyMock.expect(ontoDAO.createGraph(gatewayRestrictions, zoneRestrictions)).andReturn(map).anyTimes();
		EasyMock.expect(ontoDAO.getResidenceId(person)).andReturn(c);
		EasyMock.expect(zoneDAOMock.readZone(a)).andReturn(zoneOne);
		EasyMock.expect(zoneDAOMock.readZone(b)).andReturn(zoneTwo);
		EasyMock.expect(zoneDAOMock.readZone(c)).andReturn(zoneThree);
		EasyMock.replay(zoneDAOMock);
		EasyMock.replay(ontoDAO);

		testService = new WayfindingServiceImpl(ontoDAO, zoneDAOMock);
	}

	@Test
	public void testGenerateRoute() throws RecordNotFoundException, NoRouteAvailableException {
		List<Zone> results = testService.generateRoute(zoneOne, zoneThree);
		assertThat(results, hasItems(zoneOne, zoneTwo, zoneThree));
	}

	@Test
	public void testGenerateRouteWithRestrictions() throws RecordNotFoundException, NoRouteAvailableException {
		List<Zone> results = testService.generateRoute(zoneOne, zoneThree, restrictions);
		assertThat(results, hasItems(zoneOne, zoneTwo, zoneThree));
	}

	@Test
	public void testGenerateRouteWithEmptyRestrictionSet() throws RecordNotFoundException, NoRouteAvailableException {
		List<Zone> results = testService.generateRoute(zoneOne, zoneThree, new HashSet<>());

		assertThat(results, hasItems(zoneOne, zoneTwo, zoneThree));
	}

	@Test
	public void testGenerateRouteWithRestrictionsAndPerson() throws RecordNotFoundException, NoRouteAvailableException {
		List<Zone> results = testService.generateRoute(zoneOne, person, restrictions);
		assertThat(results, hasItems(zoneOne, zoneTwo, zoneThree));
	}

	@Test
	public void testGenerateRouteWithPersonAndEmptyRestrictionSet()
			throws RecordNotFoundException, NoRouteAvailableException {
		List<Zone> results = testService.generateRoute(zoneOne, person, new HashSet<>());
		assertThat(results, hasItems(zoneOne, zoneTwo, zoneThree));
	}

}
