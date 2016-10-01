/**
 * This package domain contains the measurement service class hierarchy.
 */
package uni.miskolc.ips.ilona.measurement.service;

import java.util.Collection;
import java.util.UUID;

import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.measurement.service.exception.DatabaseUnavailableException;
import uni.miskolc.ips.ilona.measurement.service.exception.ZoneNotFoundException;

/**
 * This interface declares methods for zone specific tasks.
 * 
 * @author teket
 */

public interface ZoneService {

	/**
	 * Inserts the zone data into the database.
	 * 
	 * @param zone
	 *            Represents pars of buildings, in terms of rooms.
	 * @throws DatabaseUnavailableException
	 *             Triggers if the connection with the database cannot be
	 *             established.
	 * @throws ZoneNotFoundException
	 *             Occurs, when the zone does not exists.
	 */
	void createZone(Zone zone) throws DatabaseUnavailableException, ZoneNotFoundException;

	/**
	 * Retrieves a collection of zone data from the database.
	 * 
	 * @return Returns a collection of zone data.
	 * @throws DatabaseUnavailableException
	 *             Triggers if the connection with the database cannot be
	 *             established.
	 */
	Collection<Zone> getZones() throws DatabaseUnavailableException;

	/**
	 * Retrieves a collection of zone data from the database.
	 * 
	 * @param name
	 *            Name identifier for zone data query.
	 * @return Returns a collection of zone data.
	 * @throws DatabaseUnavailableException
	 *             Triggers if the connection with the database cannot be
	 *             established.
	 * @throws ZoneNotFoundException
	 *             Occurs, when the zone does not exists.
	 */
	Collection<Zone> getZones(String name) throws DatabaseUnavailableException, ZoneNotFoundException;

	/**
	 * Deletes a zone from the database.
	 * 
	 * @param zone
	 *            Represents pars of buildings, in terms of rooms.
	 * @throws DatabaseUnavailableException
	 *             Triggers if the connection with the database cannot be
	 *             established.
	 * @throws ZoneNotFoundException
	 *             Occurs, when the zone does not exists.
	 */
	void deleteZone(Zone zone) throws DatabaseUnavailableException, ZoneNotFoundException;

	Zone getZone(UUID value) throws DatabaseUnavailableException, ZoneNotFoundException;
}
