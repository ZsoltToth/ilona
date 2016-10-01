package uni.miskolc.ips.ilona.measurement.persist.mysql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uni.miskolc.ips.ilona.measurement.model.measurement.BluetoothTags;
import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.measurement.RFIDTags;
import uni.miskolc.ips.ilona.measurement.model.measurement.WiFiRSSI;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.persist.MeasurementDAO;
import uni.miskolc.ips.ilona.measurement.persist.PositionDAO;
import uni.miskolc.ips.ilona.measurement.persist.exceptions.InsertionException;
import uni.miskolc.ips.ilona.measurement.persist.exceptions.RecordNotFoundException;
import uni.miskolc.ips.ilona.measurement.persist.mappers.MeasurementMapper;
import uni.miskolc.ips.ilona.measurement.persist.mappers.PositionMapper;

/**
 * 
 * @author bogdandy
 *
 */
public class MySQLMeasurementDAO implements MeasurementDAO {
	/**
	 * 
	 */
	private final Logger LOG = LogManager.getLogger(MySQLMeasurementDAO.class);
	/**
	 * 
	 */
	private SqlSessionFactory sessionFactory;
	/**
	 * 
	 */
	private PositionDAO positionDAO;
/**
 * Constructor.
 * @param configFilePath The configuration path that needs to be initialized.
 * @param host The host name that needs to be initialized.
 * @param port the port number that needs to be initialized.
 * @param database the database string that needs to be initialized.
 * @param user the user string that needs to be initialized.
 * @param password the password string that needs to be initialized.
 * @throws FileNotFoundException is thrown if the initialization file is not found. 
 */
	protected MySQLMeasurementDAO(final String configFilePath, final String host, final int port, final String database, final String user,
			final String password) throws FileNotFoundException {
		File configFile = new File(configFilePath);
		// new File("src/main/resources/mybatis-configuration.xml");
		InputStream inputStream = new FileInputStream(configFile);
		String urlPattern = "jdbc:mysql://%s:%s/%s";
		String connectionURL = String.format(urlPattern, host, port, database);
		Properties props = new Properties();
		props.put("driver", "com.mysql.jdbc.Driver");
		props.put("url", connectionURL);
		props.put("username", user);
		props.put("password", password);

		this.sessionFactory = (new SqlSessionFactoryBuilder()).build(inputStream, props);

		this.positionDAO = new MySQLPositionDAO(configFilePath, host, port, database, user, password);
	}

	/**
	 * Constructor.
	 * @param configFilePath The configuration path that needs to be initialized.
	 * @param host The host name that needs to be initialized.
	 * @param port the port number that needs to be initialized.
	 * @param database the database string that needs to be initialized.
	 * @param user the user string that needs to be initialized.
	 * @param password the password string that needs to be initialized.
	 * @param positionDAO The positionDAO that needs to be initialized.
	 * @throws FileNotFoundException is thrown if the initialization file is not found.
	 */
	public MySQLMeasurementDAO(final String configFilePath, final String host, final int port, final String database,
			final String user, final String password, final PositionDAO positionDAO) throws FileNotFoundException {
		this(configFilePath, host, port, database, user, password);
		this.positionDAO = positionDAO;
	}

	/**
	 * 
	 */
	@Override
	public final void createMeasurement(final Measurement measurement) throws InsertionException {
		// TODO Auto-generated method stub
		SqlSession session = sessionFactory.openSession();
		try {
			this.positionDAO.createPosition(measurement.getPosition());
			MeasurementMapper mapper = session.getMapper(MeasurementMapper.class);
			mapper.insertMeasurement(measurement);
			final String measId = measurement.getId().toString();
			if (measurement.getBluetoothTags() != null) {
				for (String btDeviceId : measurement.getBluetoothTags().getTags()) {
					mapper.insertBTDevice4Measurement(btDeviceId, measId);
				}
			}
			if (measurement.getWifiRSSI() != null) {
				for (Map.Entry<String, Double> rssiEntry : measurement.getWifiRSSI().getRssiValues().entrySet()) {
					mapper.insertWiFiRSSI4Measurement(rssiEntry.getKey(), rssiEntry.getValue(), measId);
				}
			}
			if(measurement.getRfidtags() != null) {
				for(byte[] b : measurement.getRfidtags().getTags())
				mapper.insertRFID4Measurement(b, measId);
			}
		
			session.commit();
		} finally {
			session.close();
		}
	}

	/**
	 * 
	 */
	@Override
	public final Collection<Measurement> readMeasurements() {
		Collection<Measurement> result = new ArrayList<Measurement>();
		SqlSession session = sessionFactory.openSession();
		try {
			MeasurementMapper mapper = session.getMapper(MeasurementMapper.class);
			result = new ArrayList<Measurement>(mapper.selectMeasurements());
			for(Measurement m : result){
				String id = m.getId().toString();
				Map<String, Double>  wifi = new HashMap<String,Double>();
				List<Map<String,Double>> mysqlwifis = mapper.selectWiFiRSSIForMeasurement(id);
				for (Map<String, Double> map : mysqlwifis){
					String ssid =""+ map.get("ssid");
					wifi.put(ssid, map.get("rssi"));
				}
				m.setWifiRSSI(new WiFiRSSI(wifi));
				m.setBluetoothTags(new BluetoothTags(mapper.selectBTTagsForMeasurement(id)));
				m.setRfidtags(new RFIDTags(mapper.selectRFIDTagsForMeasurement(id)));
			}
		} finally {
			session.close();
		}
		return result;
	}

	/**
	 * 
	 */
	@Override
	public final void updateMeasurement(final Measurement measurement) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		SqlSession session = sessionFactory.openSession();
		try {
			MeasurementMapper mapper = session.getMapper(MeasurementMapper.class);
		} finally {
			session.close();
		}

	}

	/**
	 * 
	 */
	@Override
	public final void deleteMeasurement(final Date timestamp) throws RecordNotFoundException {
		SqlSession session = sessionFactory.openSession();
		try {
			MeasurementMapper mapper = session.getMapper(MeasurementMapper.class);
			mapper.deleteMeasurementByTimeStamp(timestamp);
		} finally {
			session.commit();
			session.close();
		}
	}

	/**
	 * 
	 */
	@Override
	public final void deleteMeasurement(final Measurement measurement) throws RecordNotFoundException {
		SqlSession session = sessionFactory.openSession();
		try {
			MeasurementMapper mapper = session.getMapper(MeasurementMapper.class);
			mapper.deleteMeasurementByMeasurement(measurement.getId().toString());
		} finally {
			session.commit();
			session.close();
		}
	}

}
