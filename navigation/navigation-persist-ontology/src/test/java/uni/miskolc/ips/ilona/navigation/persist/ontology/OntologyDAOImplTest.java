package uni.miskolc.ips.ilona.navigation.persist.ontology;

import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import uni.miskolc.ips.ilona.navigation.model.Gateway;
import uni.miskolc.ips.ilona.navigation.model.ZoneMap;
import uni.miskolc.ips.ilona.navigation.persist.OntologyDAO.GatewayRestriction;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class OntologyDAOImplTest {
	
	@Test
	public void testBaseOntology() throws OWLOntologyCreationException{
		OntologyDAOImpl testImplementation = new OntologyDAOImpl("resources/ILONABASE.owl", "resources/ILONA.owl");
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology expected = manager.loadOntologyFromOntologyDocument(new File("resources/ILONABASE.owl"));
		assertEquals(expected, testImplementation.getBaseOntology());
	}

	@Test
	public void testNavigationOntology() throws OWLOntologyCreationException{
		OntologyDAOImpl testImplementation = new OntologyDAOImpl("resources/ILONABASE.owl", "resources/ILONA.owl");
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology expected = manager.loadOntologyFromOntologyDocument(new File("resources/ILONA.owl"));
		assertEquals(expected, testImplementation.getBaseOntology());
	}
	
	@Test
	public void testCreateGraphWithoutRestrictions() throws OWLOntologyCreationException{
		Set<UUID> iDs = new HashSet<>();
		UUID a =UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
		iDs.add(a);
		UUID b=UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
		iDs.add(b);
		UUID c =UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
		iDs.add(c);
		Set<Gateway> paths = new HashSet<>();
		paths.add(new Gateway(a, b));
		paths.add(new Gateway(b, a));
		paths.add(new Gateway(b, c));
		paths.add(new Gateway(c, b));
		paths.add(new Gateway(a, c));
		ZoneMap expected = new ZoneMap(iDs, paths);
		OntologyDAOImpl test = new OntologyDAOImpl("resources/ILONABASE.owl", "resources/ILONA.owl");
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
	//	assertEquals(expected.findPath(a, c), test.createGraphWithoutRestrictions(manager.loadOntologyFromOntologyDocument(new File("resources/DummyOntology.owl"))).findPath(a, c));
	}
	
	@Test
	public void testGetZones() throws OWLOntologyCreationException{
		Set<UUID> iDs = new HashSet<>();
		UUID a =UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
		iDs.add(a);
		UUID b=UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
		iDs.add(b);
		UUID c =UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
		iDs.add(c);
		OntologyDAOImpl test = new OntologyDAOImpl("resources/ILONABASE.owl", "resources/ILONA.owl");
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		assertEquals(iDs, test.getAllZoneIDs(manager.loadOntologyFromOntologyDocument(new File("resources/DummyOntology.owl"))));
	}
	
	@Test
	public void testGetPaths() throws OWLOntologyCreationException{
		Set<UUID> iDs = new HashSet<>();
		UUID a =UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
		iDs.add(a);
		UUID b=UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
		iDs.add(b);
		UUID c =UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
		iDs.add(c);
		Gateway p1 = new Gateway(a, b);
		Gateway p2 = new Gateway(b, a);
		Gateway p3 = new Gateway(b, c);
		Gateway p4 = new Gateway(c, b);
		Gateway p5 = new Gateway(a, c);
		OntologyDAOImpl test = new OntologyDAOImpl("resources/ILONABASE.owl", "resources/ILONA.owl");
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		Set<Gateway> actual = test.getPaths(manager.loadOntologyFromOntologyDocument(new File("resources/DummyOntology.owl")));
		Set<String> resultStrings = new HashSet<>();
		for(Gateway gate : actual){
			resultStrings.add(gate.toString());
		}
		assertThat(resultStrings, hasItems(p1.toString(),p2.toString(),p3.toString(),p4.toString(),p5.toString()));
	}
	
	@Test
	public void testGetZone() throws OWLOntologyCreationException {
		UUID expected = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
		OntologyDAOImpl test = new OntologyDAOImpl("resources/ILONABASE.owl", "resources/ILONA.owl");
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File("resources/DummyOntology.owl"));
		assertEquals(expected, test.getZone(expected, ontology));
	}
	
	@Ignore
	@Test
	public void testGetPathOnMap() throws OWLOntologyCreationException{
		UUID start = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
		UUID finish = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
		OntologyDAOImpl test = new OntologyDAOImpl("resources/ILONABASE.owl", "resources/ILONA.owl");
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File("resources/DummyOntology.owl"));
	//	List<UUID> actual = test.getPathOnMap(ontology, start.toString(), finish.toString());
	//	assertThat(actual, hasItems(start,finish));
	}
	
	@Test
	public void testStringGetPathsWithRestriction() throws OWLOntologyCreationException {
		Set<GatewayRestriction> restrictions = new HashSet<>();
		restrictions.add(GatewayRestriction.NO_DOOR);
		restrictions.add(GatewayRestriction.NO_STAIRS);
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File("resources/DummyOntology.owl"));
		OntologyDAOImpl test = new OntologyDAOImpl("resources/ILONABASE.owl", "resources/ILONA.owl");
		Set<Gateway> res = test.getPaths(ontology, restrictions);
	}
	
	@Test
	public void testGenerateUUID(){
		System.out.println(UUID.randomUUID());
	}
}
