package uni.miskolc.ips.ilona.navigation.persist.ontology;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

public class queryEngineTest {
	private OWLOntology ontology;
	private QueryEngine engine;
	
	@Before
	public void setUp() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		ontology = manager.loadOntologyFromOntologyDocument(new File("resources/dummy.owl"));
		engine = new QueryEngine(new Reasoner.ReasonerFactory().createReasoner(ontology),
				new SimpleShortFormProvider());
	}
	
	
	@Test
	public void testGetSuperClasses() {
		assertEquals("[owl:Thing]", engine.getSuperClasses("Zone", true).toString());
	}

	@Test
	public void testGetSubClasses() {
		assertEquals("[owl:Nothing]", engine.getSubClasses("Zone", true).toString());
	}

	@Test
	public void testGetEquivalentClasses() {
		assertEquals("[]", engine.getEquivalentClasses("Zone").toString());
	}
	
	@Test
	public void testGetSuperClassesEmpty() {
		assertEquals(Collections.emptySet(), engine.getSuperClasses("", true));
	}
	
	@Test
	public void testGetSubClassesEmpty() {
		assertEquals(Collections.emptySet(), engine.getSubClasses("", true));
	}
	
	@Test
	public void testGetEquivalentClassesEmpty() {
		assertEquals(Collections.emptySet(), engine.getEquivalentClasses(""));
	}
	
	@Test
	public void testGetInstancesEmpty() {
		assertEquals(Collections.emptySet(), engine.getInstances("", true));
	}

}
