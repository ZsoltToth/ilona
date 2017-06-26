package uni.miskolc.ips.ilona.navigation.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.io.FileDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.parameters.OntologyCopy;
import org.semanticweb.owlapi.util.OWLEntityRemover;

import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.measurement.persist.ZoneDAO;
import uni.miskolc.ips.ilona.navigation.persist.OntologyDAO;
import uni.miskolc.ips.ilona.navigation.persist.ontology.IlonaIRIs;
import uni.miskolc.ips.ilona.navigation.service.OntologyGenerationService;

public class OntologyGenerationServiceImpl implements OntologyGenerationService {

	private OntologyDAO ontologyDAO;
	private ZoneDAO zoneDAO;

	public OntologyGenerationServiceImpl(OntologyDAO ontologyDAO, ZoneDAO zoneDAO) {
		super();
		this.ontologyDAO = ontologyDAO;
		this.zoneDAO = zoneDAO;
	}

	/**
	 * A method which returns the base ontology structure
	 * @return the base ontology as a file
	 */
	@Override
	public File baseOntology() {
		OWLOntology ontology = ontologyDAO.getBaseOntology();
		try {
			File tempFile = File.createTempFile("ontology", ".owl");
			FileWriter writer = new FileWriter(tempFile);
			writer.write(ontology.toString());
			writer.close();
			return tempFile;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * A method which generates a raw ontology, filled with only the zones from the Zone database of the system.
	 * @return the filled ontology as a file
	 */
	@Override
	public File generateRawOntology() {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		File result = baseOntology();
		//Copy the baseontology into a new ontology object and remove all possible individuals
		try {
			OWLOntology newOntology = manager.copyOntology(ontologyDAO.getBaseOntology(), OntologyCopy.DEEP);
			OWLEntityRemover remover = new OWLEntityRemover(newOntology);
			for (OWLNamedIndividual individual : newOntology.getIndividualsInSignature()) {
				remover.visit(individual);
			}
			manager.applyChanges(remover.getChanges());

			//Query all of the individuals from the Zone Database
			for (Zone zone : zoneDAO.readZones()) {
				List<AddAxiom> axioms = new ArrayList<AddAxiom>();
				String zoneName = zone.getName();
				zoneName=zoneName.trim();
				zoneName=zoneName.replaceAll(" ", "_");
				zoneName=zoneName.replaceAll("#", "");

				//Upload the queried data of the zone into the ontology
				OWLNamedIndividual zoneIndividual = factory
						.getOWLNamedIndividual(IRI.create(IlonaIRIs.PREFIX + zoneName));
				OWLClassAssertionAxiom classAssertion = factory
						.getOWLClassAssertionAxiom(factory.getOWLClass(IlonaIRIs.ZONE), zoneIndividual);
				axioms.add(new AddAxiom(newOntology, classAssertion));
				OWLDataPropertyAssertionAxiom nameAssertion = factory.getOWLDataPropertyAssertionAxiom(
						factory.getOWLDataProperty(IlonaIRIs.NAME), zoneIndividual, zone.getName());
				axioms.add(new AddAxiom(newOntology, nameAssertion));
				OWLDataPropertyAssertionAxiom iDAssertion = factory.getOWLDataPropertyAssertionAxiom(
						factory.getOWLDataProperty(IlonaIRIs.ID), zoneIndividual, zone.getId().toString());
				axioms.add(new AddAxiom(newOntology, iDAssertion));

				manager.applyChanges(axioms);

			}
			//try to save the ontology and catch the possible exception
			try {
				manager.saveOntology(newOntology, new OWLXMLDocumentFormat(), new FileDocumentTarget(result));
			} catch (OWLOntologyStorageException e) {
				e.printStackTrace();
			}
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		return result;
	}

}
