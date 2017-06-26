package uni.miskolc.ips.ilona.navigation.persist.ontology;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import uni.miskolc.ips.ilona.navigation.model.Gateway;
import uni.miskolc.ips.ilona.navigation.persist.NoSuchPersonException;
import uni.miskolc.ips.ilona.navigation.model.ZoneMap;
import uni.miskolc.ips.ilona.navigation.persist.OntologyDAO;

public class OntologyDAOImpl implements OntologyDAO {

	private final OWLOntology baseOntology;
	private final OWLOntology navigationOntology;

	/**
	 * The Constructor of the OntologyDAOImpl class
	 * @param baseOntologyPath The path to the raw ontology without individuals
	 * @param navigationOntologyPath path to the navigation ontology 
	 * @throws OWLOntologyCreationException Thrown when the path is wrong
	 */
	public OntologyDAOImpl(String baseOntologyPath, String navigationOntologyPath) throws OWLOntologyCreationException {
		super();
		File baseOntologyFile = new File(baseOntologyPath);
		File navigationOntologyFile = new File(navigationOntologyPath);
		this.baseOntology = readOntologyFromFile(baseOntologyFile);
		this.navigationOntology = navigationOntologyFile.exists() ? readOntologyFromFile(navigationOntologyFile) : null;
	}

	/**
	 * A convenience method for faster ontology loading.
	 * @param input The File from which we want to load the ontology
	 * @return The OWLOntology
	 * @throws OWLOntologyCreationException Thrown when the path is wrong
	 */
	private OWLOntology readOntologyFromFile(File input) throws OWLOntologyCreationException {
		OWLOntology result;
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		result = manager.loadOntologyFromOntologyDocument(input);
		return result;
	}

	@Override
	public OWLOntology getBaseOntology() {
		return baseOntology;
	}

	@Override
	public OWLOntology getNavigationOntology() {
		return navigationOntology;
	}

	/**
	 * 
	 * @param ontology
	 *            the ontology from which we want to create the graph
	 * @return the graph as a object
	 */
	@Override
	public ZoneMap createGraphWithoutRestrictions() {
		OWLOntology ontology = getNavigationOntology();
		Set<UUID> iDs = getAllZoneIDs(ontology);
		Set<Gateway> paths = getPaths(ontology);

		return new ZoneMap(iDs, paths);
	}

	/**
	 * Creates a graph from the NavigationOntology with the given restrictions
	 * @param gatewayRestrictions The restrictions for the gateways between the zones
	 * @param zoneRestrictions The restrictions for the zones
	 * @return A ZoneMap object ( a graph for navigation)
	 */
	@Override
	public ZoneMap createGraph(Set<GatewayRestriction> gatewayRestrictions, Set<ZoneRestriction> zoneRestrictions) {
		OWLOntology ontology = getNavigationOntology();
		Set<UUID> iDs = getZones(ontology, zoneRestrictions);
		Set<Gateway> gateways = getPaths(ontology, gatewayRestrictions);
		return new ZoneMap(iDs, gateways);
	}

	/**
	 * 
	 * @param ontology
	 *            the ontology from which we want to gain the information
	 * @return a set of the IDs of the zones in the ontology, later used as the
	 *         vertexes of the graph
	 */

	public Set<UUID> getAllZoneIDs(OWLOntology ontology) {
		Set<UUID> result = new HashSet<UUID>();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLReasoner reasoner = new Reasoner.ReasonerFactory().createReasoner(ontology);
		OWLDataFactory factory = manager.getOWLDataFactory();
		reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

		// get all the zone individuals
		for (Node<OWLNamedIndividual> node : reasoner.getInstances(factory.getOWLClass(IlonaIRIs.ZONE))) {
			OWLNamedIndividual zone = node.getRepresentativeElement();
			// get the IDs of the zone
			Set<OWLLiteral> UUIDs = reasoner.getDataPropertyValues(zone, factory.getOWLDataProperty(IlonaIRIs.ID));
			for (OWLLiteral literal : UUIDs) {
				UUID uUID = UUID.fromString(literal.getLiteral());
				result.add(uUID);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param ontology
	 *            the ontology from which we want to gain the information
	 * @return A set of the gateways found in the ontology, needed for
	 *         instancing the edges of the model graph
	 */
	public Set<Gateway> getPaths(OWLOntology ontology) {
		Set<Gateway> result = new HashSet<>();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLReasoner reasoner = new Reasoner.ReasonerFactory().createReasoner(ontology);
		OWLDataFactory factory = manager.getOWLDataFactory();
		reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

		// get the subclasses of the gateway class
		Set<OWLClass> gateWaySubClasses = new HashSet<OWLClass>();
		for (Node<OWLClass> subclass : reasoner.getSubClasses(factory.getOWLClass(IlonaIRIs.GATEWAY))) {
			gateWaySubClasses.add(subclass.getRepresentativeElement());
		}

		for (OWLClass subClass : gateWaySubClasses) {
			// get the gateway instances
			for (Node<OWLNamedIndividual> gatewayNode : reasoner.getInstances(subClass)) {
				OWLNamedIndividual gateway = gatewayNode.getRepresentativeElement();
				UUID from = null;
				UUID to = null;
				// get fromzone
				for (Node<OWLNamedIndividual> individual : reasoner.getObjectPropertyValues(gateway,
						factory.getOWLObjectProperty(IlonaIRIs.FROMZONE))) {
					OWLNamedIndividual fromZone = individual.getRepresentativeElement();
					for (OWLLiteral id : reasoner.getDataPropertyValues(fromZone,
							factory.getOWLDataProperty(IlonaIRIs.ID))) {
						from = UUID.fromString(id.getLiteral());
					}
				}
				// get tozone
				for (Node<OWLNamedIndividual> individual : reasoner.getObjectPropertyValues(gateway,
						factory.getOWLObjectProperty(IlonaIRIs.TOZONE))) {
					OWLNamedIndividual fromZone = individual.getRepresentativeElement();
					for (OWLLiteral id : reasoner.getDataPropertyValues(fromZone,
							factory.getOWLDataProperty(IlonaIRIs.ID))) {
						to = UUID.fromString(id.getLiteral());
					}
				}
				// check if oneway
				if (reasoner.getDataPropertyValues(gateway, factory.getOWLDataProperty(IlonaIRIs.ONEWAY)).isEmpty()) {
					// if not oneway, add the gateway to both ways
					result.add(new Gateway(from, to));
					result.add(new Gateway(to, from));
				} else {
					// if oneway, add only the appropiate edge
					result.add(new Gateway(from, to));
				}
			}
		}
		return result;
	}

	/**
	 * The method used to get the paths of the graphs
	 * @param ontology The ontology from which the method queries the information
	 * @param restrictions The set of restrictions used during querying
	 * @return a set of gateway objects used during the creation of the navigation graph
	 */
	public Set<Gateway> getPaths(OWLOntology ontology, Set<GatewayRestriction> restrictions) {
		Set<Gateway> result = new HashSet<>();
		if (restrictions.isEmpty()) {
			result = getPaths(ontology);
		} else {
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory factory = manager.getOWLDataFactory();
			OWLReasoner reasoner = new Reasoner.ReasonerFactory().createReasoner(ontology);
			reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
			QueryEngine engine = new QueryEngine(reasoner, new SimpleShortFormProvider());
			String query = new String("Gateway and (");

			// constructing the query
			for (GatewayRestriction restriction : restrictions) {
				query = query.concat(restriction.getStringForm() + " and ");
			}
			query = query.substring(0, query.length() - 5);
			query = query.concat(")");
			Set<OWLNamedIndividual> individuals = engine.getInstances(query, false);

			for (OWLNamedIndividual individual : individuals) {
				UUID from = null;
				UUID to = null;
				// get fromzone
				for (Node<OWLNamedIndividual> zoneIndividual : reasoner.getObjectPropertyValues(individual,
						factory.getOWLObjectProperty(IlonaIRIs.FROMZONE))) {
					OWLNamedIndividual fromZone = zoneIndividual.getRepresentativeElement();
					for (OWLLiteral id : reasoner.getDataPropertyValues(fromZone,
							factory.getOWLDataProperty(IlonaIRIs.ID))) {
						from = UUID.fromString(id.getLiteral());
					}
				}
				// get tozone
				for (Node<OWLNamedIndividual> zoneIndividual : reasoner.getObjectPropertyValues(individual,
						factory.getOWLObjectProperty(IlonaIRIs.TOZONE))) {
					OWLNamedIndividual fromZone = zoneIndividual.getRepresentativeElement();
					for (OWLLiteral id : reasoner.getDataPropertyValues(fromZone,
							factory.getOWLDataProperty(IlonaIRIs.ID))) {
						to = UUID.fromString(id.getLiteral());
					}
				}
				// check if oneway
				if (reasoner.getDataPropertyValues(individual, factory.getOWLDataProperty(IlonaIRIs.ONEWAY))
						.isEmpty()) {
					// if not oneway, add the gateway to both ways
					result.add(new Gateway(from, to));
					result.add(new Gateway(to, from));
				} else {
					// if oneway, add only the appropiate edge
					result.add(new Gateway(from, to));
				}

			}
		}
		return result;
	}

	/**
	 * The method used to get the UUIDs for the creation of the graph
	 * @param ontology The ontology from which the method queries the information
	 * @param restrictions The set of restrictions used during querying
	 * @return a set of UUIDs used during the creation of the navigation graph
	 */
	public Set<UUID> getZones(OWLOntology ontology, Set<ZoneRestriction> restrictions) {
		Set<UUID> result = new HashSet<>();
		if (restrictions.isEmpty()) {
			result = getAllZoneIDs(ontology);
		} else {
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory factory = manager.getOWLDataFactory();
			OWLReasoner reasoner = new Reasoner.ReasonerFactory().createReasoner(ontology);
			reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
			QueryEngine engine = new QueryEngine(reasoner, new SimpleShortFormProvider());

			String query = new String("Zone and (");
			// constructing the query
			for (ZoneRestriction restriction : restrictions) {
				query = query.concat(restriction.getStringForm() + " and ");
			}
			query = query.substring(0, query.length() - 5);
			query = query.concat(")");
			Set<OWLNamedIndividual> individuals = engine.getInstances(query, true);
			for (OWLNamedIndividual zone : individuals) {
				// get the IDs of the zone
				Set<OWLLiteral> UUIDs = reasoner.getDataPropertyValues(zone, factory.getOWLDataProperty(IlonaIRIs.ID));
				for (OWLLiteral literal : UUIDs) {
					UUID uUID = UUID.fromString(literal.getLiteral());
					result.add(uUID);
				}
			}

		}
		return result;
	}

	/**
	 * A method used to get zone of residence for a given person
	 * @param person The name attribute of the person
	 * @return The UUID of the zone in which the person resides
	 */
	@Override
	public UUID getResidenceId(String person) throws NoSuchPersonException {
		UUID result = null;
		OWLOntology ontology = getNavigationOntology();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		OWLReasoner reasoner = new Reasoner.ReasonerFactory().createReasoner(ontology);
		reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

		for (Node<OWLNamedIndividual> node : reasoner.getInstances(factory.getOWLClass(IlonaIRIs.RESIDENT))) {
			OWLNamedIndividual individual = node.getRepresentativeElement();
			for (OWLLiteral name : reasoner.getDataPropertyValues(individual,
					factory.getOWLDataProperty(IlonaIRIs.NAME))) {
				if(name.getLiteral().equals(person)){
				for (Node<OWLNamedIndividual> zoneNode : reasoner.getObjectPropertyValues(individual,
						factory.getOWLObjectProperty(IlonaIRIs.RESIDENCE))) {
					OWLNamedIndividual zone = zoneNode.getRepresentativeElement();
					for(OWLLiteral id : reasoner.getDataPropertyValues(zone, factory.getOWLDataProperty(IlonaIRIs.ID))){
						result = UUID.fromString(id.getLiteral());
					}

				}}
			}
		}
		if(result==null){
			throw new NoSuchPersonException();
		}
		return result;
	}
}
