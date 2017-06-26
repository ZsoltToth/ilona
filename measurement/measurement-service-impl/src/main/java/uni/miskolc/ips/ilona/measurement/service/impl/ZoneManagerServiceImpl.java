package uni.miskolc.ips.ilona.measurement.service.impl;

import java.util.Collection;
import java.util.UUID;

import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.measurement.persist.ZoneDAO;
import uni.miskolc.ips.ilona.measurement.persist.exceptions.InsertionException;
import uni.miskolc.ips.ilona.measurement.persist.exceptions.RecordNotFoundException;
import uni.miskolc.ips.ilona.measurement.service.ZoneService;
import uni.miskolc.ips.ilona.measurement.service.exception.DatabaseUnavailableException;
import uni.miskolc.ips.ilona.measurement.service.exception.ZoneNotFoundException;

/**
 * The PositionManagerServiceImpl class is used to Manage Zones.
 * 
 * @author tamas13
 *
 */
public class ZoneManagerServiceImpl implements ZoneService {
	/**
	 * PositionDAO provides an abstract interface to database of
	 * Zones.
	 */
	private ZoneDAO zoneDAO;
	/**
	 * The constructor of ZoneManagerServiceImpl with the zoneDAO parameter.
	 * @param zoneDAO provides an abstract interface to database of Zones.
	 */
	public ZoneManagerServiceImpl(final ZoneDAO zoneDAO) {
		super();
		this.zoneDAO = zoneDAO;
	}

	
	/**
	 * The createZone method insert the given Zone into the
	 * database.
	 * 
	 * @param zone
	 *            is the given Zone which should be recorded.
	 */
	public final void createZone(final Zone zone) {
		try {
			zoneDAO.createZone(zone);
		} catch (InsertionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * The readMeasurements method get the Zones from the database.
	 * 
	 * @return with the collection of the Zones read from the database.
	 */
	public final Collection<Zone> getZones() {
		return zoneDAO.readZones();
	}

	
	/**
	 * The getZones method get the Zone from the database, which
	 * fulfills the criterion that the name of the Zone is the given parameter. 
	 * 
	 * @param name   is the selection criterion.
	 * @return with the collection of Zones that fulfills the criterion
	 */
	public final Collection<Zone> getZones(final String name) {
		return zoneDAO.readZones(name);
	}

	
	/**
	 * The deleteZone method delete the given Zone from the database.
	 * @param zone which Zone need to be deleted.
	 */
	public final void deleteZone(final Zone zone) {
		try {
			zoneDAO.deleteZone(zone);
		} catch (RecordNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	@Override
	public Zone getZone(UUID value) throws DatabaseUnavailableException, ZoneNotFoundException {
		Zone result;
		try{
			result = zoneDAO.readZone(value);
		}catch(RecordNotFoundException e){
			result = Zone.UNKNOWN_POSITION;
		}
		return result;
	}

}
