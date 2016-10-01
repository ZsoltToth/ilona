package uni.miskolc.ips.ilona.measurement.persist.mappers;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.measurement.WiFiRSSI;
import uni.miskolc.ips.ilona.measurement.model.position.Position;

/**
 * 
 * @author bogdandy
 *
 */
public interface MeasurementMapper {
	/**
	 * Selects all measurements from the database. It returns an empty
	 * collection, if no measurement has been found.
	 * 
	 * 
	 * @return a collection of measurements, if there is no measurements then it
	 *         returns with an empty collection.
	 */
	Collection<Measurement> selectMeasurements();

	/**
	 * Selects a position by an ID string from the database. Returs an empty
	 * collection if no measurement has been found.
	 * 
	 * @param id
	 *            A string of the id wanted position.
	 * 
	 * @return a collection of measurements, if there is no measurements that
	 *         fit the selection, then returns with an empty collection.
	 */
	Position selectPositionById(String id);

	/**
	 * Selects a set of bluetooth tags for the measurements.
	 * 
	 * @param measId
	 *            An ID string of the measurement.
	 * @return A set of strings of the bluetooth tags.
	 */
	Set<String> selectBTTagsForMeasurement(String measId);

	/**
	 * Selects a set of RFID tags for measurement.
	 * 
	 * @param measId
	 *            An ID string of the measurement.
	 * @return A set of bytes of the RFID tags.
	 */
	Set<byte[]> selectRFIDTagsForMeasurement(String measId);

	/**
	 * Selects a map from the data of the WIFI-RSSI.
	 * 
	 * @param measId
	 *            An ID string of the measurement.
	 * @return A map based on the measurement ID string.
	 */

	@MapKey("ssid")
	List<Map<String, Double>> selectWiFiRSSIForMeasurement(@Param("measId") String measId);

	/**
	 * Deletes a measurement by a measurement ID string.
	 * 
	 * @param measId
	 *            An ID string of the measurement.
	 */
	void deleteMeasurementByMeasurement(String measId);

	/**
	 * Deletes a measurement by a timestamp.
	 * 
	 * @param timestamp
	 *            A timestamp of the measurement that needs to be deleted.
	 */
	void deleteMeasurementByTimeStamp(Date timestamp);

	// TODO implement RFID Tag mapper for myBatis
	// public Set<byte[]> selectRFIDTagsForMeasurement(String measId);
	/**
	 * Inserts data into a measurement object, based on the measuring process.
	 * 
	 * @param measurement
	 *            The saved measurement.
	 */
	void insertMeasurement(Measurement measurement);

	/**
	 * Inserts data into a measurement object, based on a measurement from
	 * bluetooth.
	 * 
	 * @param btDeviceId
	 *            The ID of the measuring bluetooth object.
	 * @param measId
	 *            The ID sting of the measurement.
	 */
	void insertBTDevice4Measurement(@Param("btDeviceId") String btDeviceId, @Param("measId") String measId);

	/**
	 * Inserts data into a measurement object, based on a meaurement from
	 * WIFI-RSSI.
	 * 
	 * @param ssid
	 *            Id of the measuring RSSI object.
	 * @param rssi
	 *            A negative double in dB.
	 * @param measId
	 *            The ID string of the measurement.
	 */
	void insertWiFiRSSI4Measurement(@Param("ssid") String ssid, @Param("rssi") double rssi,
			@Param("measId") String measId);
	
	
	void insertRFID4Measurement(@Param("rfid") byte[] rfid, @Param("measId") String measId);
	

}
