package uni.miskolc.ips.ilona.navigation.service.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.easymock.EasyMock;

import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.measurement.persist.ZoneDAO;
import uni.miskolc.ips.ilona.navigation.persist.OntologyDAO;
import uni.miskolc.ips.ilona.navigation.persist.ontology.IlonaIRIs;

public class OntologyGenerationServiceImplTest {

	private ZoneDAO zoneDAOMock;
	private OntologyDAO ontologyDAOMock;

	@Before
	public void setUp() throws OWLOntologyCreationException {
		zoneDAOMock = EasyMock.createMock(ZoneDAO.class);
		ontologyDAOMock = EasyMock.createMock(OntologyDAO.class);
		Collection<Zone> zones = new ArrayList<Zone>();
		zones.add(new Zone("test #One"));
		zones.add(new Zone("test #Two"));
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		EasyMock.expect(ontologyDAOMock.getBaseOntology())
				.andReturn(manager.loadOntologyFromOntologyDocument(new File("src/resources/ILONABASE.owl")))
				.anyTimes();
		EasyMock.expect(zoneDAOMock.readZones()).andReturn(zones);
		EasyMock.replay(zoneDAOMock);
		EasyMock.replay(ontologyDAOMock);
	}

	@Test
	public void testGenerateRawOntology() throws OWLOntologyCreationException {
		//load the generated ontology
		OntologyGenerationServiceImpl test = new OntologyGenerationServiceImpl(ontologyDAOMock, zoneDAOMock);
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology generated = manager.loadOntologyFromOntologyDocument(test.generateRawOntology());
		OWLDataFactory factory = manager.getOWLDataFactory();

		//create a reasoner and query the zone individuals
		OWLReasoner reasoner = new Reasoner.ReasonerFactory().createReasoner(generated);
		NodeSet<OWLNamedIndividual> zoneQuery = reasoner.getInstances(factory.getOWLClass(IlonaIRIs.ZONE));
		List<String> result = new ArrayList<String>();
		for (Node<OWLNamedIndividual> individualNode : zoneQuery) {
			OWLNamedIndividual individual = individualNode.getRepresentativeElement();
			result.add(individual.getIRI().getShortForm());

		}
		//check the results
		List<String> expected = new ArrayList<String>();
		expected.add("test_One");
		expected.add("test_Two");
		assertEquals(expected, result);

	}

}
