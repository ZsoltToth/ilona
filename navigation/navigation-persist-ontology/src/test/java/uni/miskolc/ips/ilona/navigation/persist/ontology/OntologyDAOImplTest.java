package uni.miskolc.ips.ilona.navigation.persist.ontology;


import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;


import uni.miskolc.ips.ilona.navigation.model.Gateway;
import uni.miskolc.ips.ilona.navigation.model.ZoneMap;
import uni.miskolc.ips.ilona.navigation.persist.NoSuchPersonException;
import uni.miskolc.ips.ilona.navigation.persist.OntologyDAO.GatewayRestriction;
import uni.miskolc.ips.ilona.navigation.persist.OntologyDAO.ZoneRestriction;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class OntologyDAOImplTest {
	private OWLOntology ontology;
	private OntologyDAOImpl test;

	@Before
	public void setUp() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		ontology = manager.loadOntologyFromOntologyDocument(new File("resources/dummy.owl"));
		test = new OntologyDAOImpl("resources/dummy.owl", "resources/dummy.owl");
	}

	@Test
	public void testBaseOntology() throws OWLOntologyCreationException {

		assertEquals(ontology, test.getBaseOntology());
	}

	@Test
	public void testNavigationOntology() throws OWLOntologyCreationException {
		assertEquals(ontology, test.getNavigationOntology());
	}

	@Test
	public void testGetZones() throws OWLOntologyCreationException {
		// The expected results
		Set<UUID> zoneIDs = new HashSet<UUID>();
		zoneIDs.add(UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee"));
		zoneIDs.add(UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56"));
		zoneIDs.add(UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d"));
		zoneIDs.add(UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c"));
		zoneIDs.add(UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88"));
		zoneIDs.add(UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3"));
		assertEquals(zoneIDs, test.getAllZoneIDs(ontology));
	}

	@Test
	public void testGetPaths() throws OWLOntologyCreationException {
		// the expected results
		Set<Gateway> gateways = new HashSet<Gateway>();
		gateways.add(new Gateway(UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56"),
				UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee")));
		gateways.add(new Gateway(UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d"),
				UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56")));
		gateways.add(new Gateway(UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c"),
				UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d")));
		gateways.add(new Gateway(UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88"),
				UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d")));
		gateways.add(new Gateway(UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88"),
				UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c")));
		gateways.add(new Gateway(UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3"),
				UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c")));
		gateways.add(new Gateway(UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3"),
				UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88")));

		gateways.add(new Gateway(UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee"),
				UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56")));
		gateways.add(new Gateway(UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56"),
				UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d")));
		gateways.add(new Gateway(UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d"),
				UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c")));
		gateways.add(new Gateway(UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d"),
				UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88")));
		gateways.add(new Gateway(UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c"),
				UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88")));
		gateways.add(new Gateway(UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c"),
				UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3")));
		gateways.add(new Gateway(UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88"),
				UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3")));
		assertThat(test.getPaths(ontology), containsInAnyOrder(gateways.toArray()));
	}

	@Test
	public void testGetPathWithoutRestrictionsIT() throws OWLOntologyCreationException {
		// The vertices of the expected graph
		Set<UUID> zoneIDs = new HashSet<UUID>();
		zoneIDs.add(UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee"));
		zoneIDs.add(UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56"));
		zoneIDs.add(UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d"));
		zoneIDs.add(UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c"));
		zoneIDs.add(UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88"));
		zoneIDs.add(UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3"));
		// the edges of the expected graph
		Set<Gateway> gateways = new HashSet<>();
		gateways.add(new Gateway(UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee"),
				UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56")));
		gateways.add(new Gateway(UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56"),
				UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d")));
		gateways.add(new Gateway(UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d"),
				UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c")));
		gateways.add(new Gateway(UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d"),
				UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88")));
		gateways.add(new Gateway(UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c"),
				UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88")));
		gateways.add(new Gateway(UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c"),
				UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3")));
		gateways.add(new Gateway(UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88"),
				UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3")));

		gateways.add(new Gateway(UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56"),
				UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee")));
		gateways.add(new Gateway(UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d"),
				UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56")));
		gateways.add(new Gateway(UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c"),
				UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d")));
		gateways.add(new Gateway(UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88"),
				UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d")));
		gateways.add(new Gateway(UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88"),
				UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c")));
		gateways.add(new Gateway(UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3"),
				UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c")));
		gateways.add(new Gateway(UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3"),
				UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88")));
		ZoneMap expected = new ZoneMap(zoneIDs, gateways);

		assertEquals(
				expected.findPath(UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee"),
						UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88")),
				test.createGraphWithoutRestrictions().findPath(UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee"),
						UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88")));
	}

	@Test
	public void testStringGetPathsWithRestriction() throws OWLOntologyCreationException {
		// The restrictions for the graph
		Set<GatewayRestriction> restrictions = new HashSet<>();
		restrictions.add(GatewayRestriction.NO_ESCALATOR);
		restrictions.add(GatewayRestriction.NO_STAIRS);

		Set<ZoneRestriction> zoneRestrictions = new HashSet<>();
		zoneRestrictions.add(ZoneRestriction.DUMMY_ZONERESTRICTION);

		// The expected vertices of the graph
		Set<UUID> zoneIDs = new HashSet<UUID>();
		zoneIDs.add(UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee"));
		zoneIDs.add(UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56"));
		zoneIDs.add(UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d"));
		zoneIDs.add(UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c"));
		zoneIDs.add(UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88"));
		zoneIDs.add(UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3"));

		// The expected gateways of the graph
		Set<Gateway> gateways = new HashSet<>();
		gateways.add(new Gateway(UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee"),
				UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56")));
		gateways.add(new Gateway(UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56"),
				UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d")));
		gateways.add(new Gateway(UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d"),
				UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c")));
		gateways.add(new Gateway(UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c"),
				UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88")));
		gateways.add(new Gateway(UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88"),
				UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3")));

		gateways.add(new Gateway(UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56"),
				UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee")));
		gateways.add(new Gateway(UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d"),
				UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56")));
		gateways.add(new Gateway(UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c"),
				UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d")));
		gateways.add(new Gateway(UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88"),
				UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c")));
		gateways.add(new Gateway(UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3"),
				UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88")));

		ZoneMap expected = new ZoneMap(zoneIDs, gateways);

		assertEquals(
				expected.findPath(UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee"),
						UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3")),
				test.createGraph(restrictions, zoneRestrictions).findPath(
						UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee"),
						UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3")));

	}

	@Test
	public void testGetPathWithoutRestrictionsbutWithRestrictionMethod() throws OWLOntologyCreationException {
		// The expected vertices of the graph
		Set<UUID> zoneIDs = new HashSet<UUID>();
		zoneIDs.add(UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee"));
		zoneIDs.add(UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56"));
		zoneIDs.add(UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d"));
		zoneIDs.add(UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c"));
		zoneIDs.add(UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88"));
		zoneIDs.add(UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3"));
		// the expected edges of the graph
		Set<Gateway> gateways = new HashSet<>();
		gateways.add(new Gateway(UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee"),
				UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56")));
		gateways.add(new Gateway(UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56"),
				UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d")));
		gateways.add(new Gateway(UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d"),
				UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c")));
		gateways.add(new Gateway(UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d"),
				UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88")));
		gateways.add(new Gateway(UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c"),
				UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88")));
		gateways.add(new Gateway(UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c"),
				UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3")));
		gateways.add(new Gateway(UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88"),
				UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3")));

		gateways.add(new Gateway(UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56"),
				UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee")));
		gateways.add(new Gateway(UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d"),
				UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56")));
		gateways.add(new Gateway(UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c"),
				UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d")));
		gateways.add(new Gateway(UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88"),
				UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d")));
		gateways.add(new Gateway(UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88"),
				UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c")));
		gateways.add(new Gateway(UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3"),
				UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c")));
		gateways.add(new Gateway(UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3"),
				UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88")));
		ZoneMap expected = new ZoneMap(zoneIDs, gateways);

		assertThat(
				expected.findPath(UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee"),
						UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3")),
				contains(test.createGraph(new HashSet<>(), new HashSet<>()).findPath(
						UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee"),
						UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3")).toArray()));
	}

	@Test
	public void testResidenceID() {
		assertEquals(UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88"), test.getResidenceId("Big Bad Wolf"));
	}
	
	@Test(expected=NoSuchPersonException.class)
	public void testResidenceIDWithoutActualResident(){
		test.getResidenceId("Little Red Riding Hood");
	}

}
