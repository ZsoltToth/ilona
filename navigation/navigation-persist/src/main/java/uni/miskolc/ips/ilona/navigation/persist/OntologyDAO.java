package uni.miskolc.ips.ilona.navigation.persist;

import java.util.Set;
import java.util.UUID;

import org.semanticweb.owlapi.model.OWLOntology;

import uni.miskolc.ips.ilona.navigation.model.ZoneMap;


public interface OntologyDAO {
	
	public enum GatewayRestriction {
		NO_STAIRS("not Stairway"),NO_DOOR("not Door"),NO_ELEVATOR("not Elevator"),NO_ESCALATOR("not Escalator");
		
		private String stringForm;

		private GatewayRestriction(String stringForm) {
			this.stringForm = stringForm;
		}
		
		public String getStringForm(){
			return stringForm;
		}
	}
	
	public enum ZoneRestriction {
		
		DUMMY_ZONERESTRICTION("Zone");
		
		private String stringForm;

		private ZoneRestriction(String stringForm) {
			this.stringForm = stringForm;
		}
		
		public String getStringForm(){
			return stringForm;
		}
		
	}
	
	public OWLOntology getBaseOntology();
	public OWLOntology getNavigationOntology();
	public ZoneMap createGraphWithoutRestrictions();
	public ZoneMap createGraph(Set<GatewayRestriction> gatewayRestrictions, Set<ZoneRestriction> zoneRestrictions);
	public UUID getResidenceId(String person) throws NoSuchPersonException;
	

}
