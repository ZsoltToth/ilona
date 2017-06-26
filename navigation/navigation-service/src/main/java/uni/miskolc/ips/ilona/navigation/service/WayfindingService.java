package uni.miskolc.ips.ilona.navigation.service;

import java.util.List;
import java.util.Set;

import uni.miskolc.ips.ilona.measurement.model.position.Zone;

public interface WayfindingService {

	public enum Restriction {
		NO_STAIRS ("no stairs" ), NO_DOOR ("no doors"), NO_ELEVATOR ("no elevators"), NO_ESCALATOR ("no escalators"), DUMMY_ZONERESTRICTION("Dummy");
		
		private String stringForm;

		private Restriction(String stringForm) {
			this.stringForm = stringForm;
		}
		
		
	}

	public List<Zone> generateRoute(Zone from, Zone to) throws NoRouteAvailableException;

	public List<Zone> generateRoute(Zone from, Zone to, Set<Restriction> restrictions) throws NoRouteAvailableException;

	public List<Zone> generateRoute(Zone from, String person, Set<Restriction> restrictions) throws NoRouteAvailableException;


}
