package uni.miskolc.ips.ilona.navigation.service;

import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.navigation.service.WayfindingService.Restriction;

public interface WayfindingService {

	public enum Restriction {
		NO_STAIRS ("no stairs" ), NO_DOOR ("no doors"), NO_ELEVATOR ("no elevators"), NO_ESCALATOR ("no escalators");
		
		private String stringForm;

		private Restriction(String stringForm) {
			this.stringForm = stringForm;
		}
		
		
	}

	public List<Zone> generateRoute(Zone from, Zone to) throws NoRouteAvailableException;

	public List<Zone> generateRoute(Zone from, Zone to, Set<Restriction> restrictions) throws NoRouteAvailableException;

	List<Zone> generateRoute(Zone from, String person, Set<Restriction> restrictions) throws NoRouteAvailableException;


}
