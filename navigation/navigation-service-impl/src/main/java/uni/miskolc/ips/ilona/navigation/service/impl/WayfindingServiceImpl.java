package uni.miskolc.ips.ilona.navigation.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.measurement.persist.ZoneDAO;
import uni.miskolc.ips.ilona.measurement.persist.exceptions.RecordNotFoundException;
import uni.miskolc.ips.ilona.measurement.persist.mysql.MySQLZoneDAO;
import uni.miskolc.ips.ilona.navigation.model.ZoneMap;
import uni.miskolc.ips.ilona.navigation.persist.OntologyDAO;
import uni.miskolc.ips.ilona.navigation.persist.OntologyDAO.GatewayRestriction;
import uni.miskolc.ips.ilona.navigation.persist.OntologyDAO.ZoneRestriction;
import uni.miskolc.ips.ilona.navigation.persist.ontology.OntologyDAOImpl;
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

	@Override
	public List<Zone> generateRoute(Zone from, Zone to, Set<Restriction> restrictions)
			throws NoRouteAvailableException {
		List<Zone> result = new ArrayList<>();
		ZoneMap map = null;
		if (restrictions.isEmpty()) {
			map = ontologyDAO.createGraphWithoutRestrictions();
		} else {
			if(getGatewayRestrictions(restrictions)==null){
				
			}
			map = ontologyDAO.createGraph(getGatewayRestrictions(restrictions), getZoneRestrictions(restrictions));
		}
		for (UUID id : map.findPath(from.getId(), to.getId())) {
			try {
				result.add(ZoneDAO.readZone(id));
			} catch (RecordNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(result.toString());
		return result;
	}
	
	@Override
	public List<Zone> generateRoute(Zone from, String person, Set<Restriction> restrictions)
			throws NoRouteAvailableException {
		List<Zone> result = new ArrayList<>();
		ZoneMap map = null;
		if (restrictions.isEmpty()) {
			map = ontologyDAO.createGraphWithoutRestrictions();
		} else {
			if(getGatewayRestrictions(restrictions)==null){
				
			}
			map = ontologyDAO.createGraph(getGatewayRestrictions(restrictions), getZoneRestrictions(restrictions));
		}
		for (UUID id : map.findPath(from.getId(), ontologyDAO.getResidenceId(person))) {
			try {
				result.add(ZoneDAO.readZone(id));
			} catch (RecordNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(result.toString());
		return result;
	}

	@Override
	public List<Zone> generateRoute(Zone from, Zone to) throws NoRouteAvailableException {
		List<Zone> result = new ArrayList<>();
		ZoneMap map = ontologyDAO.createGraphWithoutRestrictions();

		for (UUID id : map.findPath(from.getId(), to.getId())) {
			try {
				result.add(ZoneDAO.readZone(id));
			} catch (RecordNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(result.toString());
		return result;
	}

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
