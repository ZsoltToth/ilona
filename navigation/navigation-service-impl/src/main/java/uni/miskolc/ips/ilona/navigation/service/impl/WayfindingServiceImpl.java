package uni.miskolc.ips.ilona.navigation.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.measurement.persist.ZoneDAO;
import uni.miskolc.ips.ilona.measurement.persist.exceptions.RecordNotFoundException;
import uni.miskolc.ips.ilona.navigation.model.ZoneMap;
import uni.miskolc.ips.ilona.navigation.persist.OntologyDAO;
import uni.miskolc.ips.ilona.navigation.persist.OntologyDAO.GatewayRestriction;
import uni.miskolc.ips.ilona.navigation.persist.OntologyDAO.ZoneRestriction;
import uni.miskolc.ips.ilona.navigation.service.NoRouteAvailableException;
import uni.miskolc.ips.ilona.navigation.service.WayfindingService;

public class WayfindingServiceImpl implements WayfindingService {

	private final OntologyDAO ontologyDAO;
	private final ZoneDAO ZoneDAO;

	public WayfindingServiceImpl(OntologyDAO ontologyDAO, ZoneDAO zoneDAO) {
		super();
		this.ontologyDAO = ontologyDAO;
		ZoneDAO = zoneDAO;
	}

	/**
	 * A method which returns the shortest path between two zones
	 * @param from the current zone of the user
	 * @param to the destination of the user
	 * @param restrictions The restrictions given to our map
	 * @return the shortest path as a list of zones
	 * @throws NoRouteAvailableException if there is no route available
	 */
	@Override
	public List<Zone> generateRoute(Zone from, Zone to, Set<Restriction> restrictions)
			throws NoRouteAvailableException {
		List<Zone> result = new ArrayList<>();
		ZoneMap map = null;
		//create a map from all available data if there is no restriction, else create with the restrictions
		if (restrictions.isEmpty()) {
			map = ontologyDAO.createGraphWithoutRestrictions();
		} else {
			map = ontologyDAO.createGraph(getGatewayRestrictions(restrictions), getZoneRestrictions(restrictions));
		}
		//get the shortest path between the two zones and then transform the list of UUIDs into zones 
		for (UUID id : map.findPath(from.getId(), to.getId())) {
			try {
				result.add(ZoneDAO.readZone(id));
			} catch (RecordNotFoundException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * A method which finds the path between our current zone and a gicen person's residence.
	 * @param from the starting zone
	 * @param person the person which the user wants to find
	 * @param restrictions the given set of restrictions
	 * @return the shortest path as a list of zones
	 * @throws NoRouteAvailableException is there is no available route
	 */
	@Override
	public List<Zone> generateRoute(Zone from, String person, Set<Restriction> restrictions)
			throws NoRouteAvailableException {
		List<Zone> result = new ArrayList<>();
		ZoneMap map = null;
		//if there is no restriction given, simply create a graph with all information
		if (restrictions.isEmpty()) {
			map = ontologyDAO.createGraphWithoutRestrictions();
		} else {
			map = ontologyDAO.createGraph(getGatewayRestrictions(restrictions), getZoneRestrictions(restrictions));
		}
		//Get the path between the starting zone and the target person's residence, then transforming the UUIDs into zones
		for (UUID id : map.findPath(from.getId(), ontologyDAO.getResidenceId(person))) {
			try {
				result.add(ZoneDAO.readZone(id));
			} catch (RecordNotFoundException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * A method which creates a route between the given zones if there are no restrictions.
	 * @param from the starting zone
	 * @param to the destination
	 * @return The shortest Path as a list of zones
	 * @throws NoRouteAvailableException if there are no availablePaths
	 */
	@Override
	public List<Zone> generateRoute(Zone from, Zone to) throws NoRouteAvailableException {
		List<Zone> result = new ArrayList<>();
		ZoneMap map = ontologyDAO.createGraphWithoutRestrictions();
		//Get the path as UUIDs and then transform them into zones
		for (UUID id : map.findPath(from.getId(), to.getId())) {
			try {
				result.add(ZoneDAO.readZone(id));
			} catch (RecordNotFoundException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * An inner method to get the zoneRestrictions from the set of all restrictions
	 * @param restrictions all given restriction
	 * @return the zoneRestrictions
	 */
	private Set<ZoneRestriction> getZoneRestrictions(Set<Restriction> restrictions) {
		Set<ZoneRestriction> result = new HashSet<>();
		for (Restriction restriction : restrictions) {
			for (ZoneRestriction zoneRestriction : ZoneRestriction.values()) {
				if (restriction.name().equals(zoneRestriction.name())) {
					result.add(zoneRestriction);
				}
			}
		}
		return result;

	}

	/**
	 * An inner method to get the GatewayRestrictions from the set of all restrictions
	 * @param restrictions all given restriction
	 * @return the gatewayRestrictions
	 */
	private Set<GatewayRestriction> getGatewayRestrictions(Set<Restriction> restrictions) {
		Set<GatewayRestriction> result = new HashSet<>();
		for (Restriction restriction : restrictions) {
			for (GatewayRestriction gatewayRestriction : GatewayRestriction.values()) {
				if (restriction.name().equals(gatewayRestriction.name())) {
					result.add(gatewayRestriction);
				}
			}
		}
		return result;
	}

}
