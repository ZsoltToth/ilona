package uni.miskolc.ips.ilona.measurement.persist.impl.integration;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.swing.text.DateFormatter;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import uni.miskolc.ips.ilona.measurement.model.measurement.BluetoothTags;
import uni.miskolc.ips.ilona.measurement.model.measurement.GPSCoordinate;
import uni.miskolc.ips.ilona.measurement.model.measurement.Magnetometer;
import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.measurement.MeasurementBuilder;
import uni.miskolc.ips.ilona.measurement.model.measurement.RFIDTags;
import uni.miskolc.ips.ilona.measurement.model.measurement.WiFiRSSI;
import uni.miskolc.ips.ilona.measurement.model.position.Coordinate;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.measurement.persist.PositionDAO;
import uni.miskolc.ips.ilona.measurement.persist.ZoneDAO;
import uni.miskolc.ips.ilona.measurement.persist.exceptions.InsertionException;
import uni.miskolc.ips.ilona.measurement.persist.exceptions.RecordNotFoundException;
import uni.miskolc.ips.ilona.measurement.persist.mappers.MeasurementMapper;
import uni.miskolc.ips.ilona.measurement.persist.mappers.PositionMapper;
import uni.miskolc.ips.ilona.measurement.persist.mappers.ZoneMapper;
import uni.miskolc.ips.ilona.measurement.persist.mysql.MySQLMeasurementDAO;
import uni.miskolc.ips.ilona.measurement.persist.mysql.MySQLPositionDAO;
import uni.miskolc.ips.ilona.measurement.persist.mysql.MySQLZoneDAO;


public class MeasurementDAOIntegrationTest extends SetupIntegrationTest {

	private static final Logger LOG = LogManager.getLogger("uni.miskolc.ips.ilona.persist");

	private MySQLMeasurementDAO dao;

	@Before
	public void setUp() {
		super.setUp();
		try {
			ZoneDAO zoneDAO = new MySQLZoneDAO(MYBATIS_CONFIG_FILE, HOST, PORT, DATABASE, USER, PASSWORD);
			PositionDAO positionDAO = new MySQLPositionDAO(MYBATIS_CONFIG_FILE, HOST, PORT, DATABASE, USER, PASSWORD,
					zoneDAO);
			// new MySQLPositionDAO(MYBATIS_CONFIG_FILE, HOST, PORT, DATABASE,
			// USER, PASSWORD, zoneDAO);
			this.dao = new MySQLMeasurementDAO(MYBATIS_CONFIG_FILE, HOST, PORT, DATABASE, USER, PASSWORD, positionDAO);
		} catch (FileNotFoundException e) {
			Assume.assumeNoException(e);
		}
	}

	@Test
	public void testMapperResult() throws Exception {
		InputStream inputStream = new FileInputStream(MYBATIS_CONFIG_FILE);
		String urlPattern = "jdbc:mysql://%s:%s/%s";
		String connectionURL = String.format(urlPattern, HOST, PORT, DATABASE);
		Properties props = new Properties();
		props.put("driver", "com.mysql.jdbc.Driver");
		props.put("url", connectionURL);
		props.put("username", USER);
		props.put("password", PASSWORD);

		SqlSessionFactory sessionFactory = (new SqlSessionFactoryBuilder()).build(inputStream, props);
		SqlSession session = sessionFactory.openSession();

		MeasurementMapper measurementMapper = session.getMapper(MeasurementMapper.class);

		Collection<Measurement> measurements = measurementMapper.selectMeasurements();
		for (Measurement meas : measurements) {
			meas.setRfidtags(new RFIDTags(measurementMapper.selectRFIDTagsForMeasurement(meas.getId().toString())));
			meas.setBluetoothTags(
					new BluetoothTags(measurementMapper.selectBTTagsForMeasurement(meas.getId().toString())));
			List<Map<String, Double>> mysqlwifis = measurementMapper
					.selectWiFiRSSIForMeasurement(meas.getId().toString());
			Map<String, Double> wifi = new HashMap<String, Double>();
			for (Map<String, Double> map : mysqlwifis) {
				String ssid = "" + map.get("ssid");
				wifi.put(ssid, map.get("rssi"));
			}
			meas.setWifiRSSI(new WiFiRSSI(wifi));
		}
		Collection<Measurement> actual = dao.readMeasurements();
		session.close();
		assertEquals(measurements, actual);
	}

	@Test
	public void test() throws Exception {

		InputStream inputStream = new FileInputStream(MYBATIS_CONFIG_FILE);
		String urlPattern = "jdbc:mysql://%s:%s/%s";
		String connectionURL = String.format(urlPattern, HOST, PORT, DATABASE);
		Properties props = new Properties();
		props.put("driver", "com.mysql.jdbc.Driver");
		props.put("url", connectionURL);
		props.put("username", USER);
		props.put("password", PASSWORD);

		SqlSessionFactory sessionFactory = (new SqlSessionFactoryBuilder()).build(inputStream, props);
		SqlSession session = sessionFactory.openSession();
		try {
			PositionMapper positionMapper = session.getMapper(PositionMapper.class);
			MeasurementMapper measurementMapper = session.getMapper(MeasurementMapper.class);
			ZoneMapper zoneMapper = session.getMapper(ZoneMapper.class);
			BluetoothTags bttags = new BluetoothTags();
			bttags.setTags(measurementMapper.selectBTTagsForMeasurement("43c6270b-2d12-49f3-ac8f-0d09d55fcf61"));

			Set<byte[]> rfidTags = measurementMapper
					.selectRFIDTagsForMeasurement("43c6270b-2d12-49f3-ac8f-0d09d55fcf61");
			for (byte[] tag : rfidTags) {
				String tagStr = "";
				for (int i = 0; i < tag.length; i++) {
					tagStr += tag[i];
				}
			}
			System.out.println("-------------");

			Collection<Measurement> measuerements = dao.readMeasurements();
			for (Measurement meas : measuerements) {
				System.out.println(meas);
			}

			MeasurementBuilder measurementBuilder = new MeasurementBuilder();
			measurementBuilder.setMagnetometer(new Magnetometer(4, 4, 4, 4));
			measurementBuilder.setGPSCoordinates(new GPSCoordinate(3, 2, 1));

			Map<String, Double> wifirssi = new HashMap<String, Double>();
			wifirssi.put("testAP1", -1.0);
			wifirssi.put("testAP2", -2.0);
			wifirssi.put("testAP3", -3.0);
			measurementBuilder.setWifiRSSI(new WiFiRSSI(wifirssi));
			measurementBuilder.setbluetoothTags(new BluetoothTags(new HashSet<>(Arrays.asList("bt1", "bt2", "bt3"))));
			measurementBuilder.setPosition(new Position(new Zone("EZ")));
			RFIDTags rfid = new RFIDTags(new HashSet<byte[]>());
			rfid.addTag(new byte[] { (byte) 12 });
			;
			measurementBuilder.setRFIDTags(rfid);
			Measurement newMeas = measurementBuilder.build();

			zoneMapper.insertZone(newMeas.getPosition().getZone());
			measurementMapper.selectMeasurements();
			positionMapper.insertPosition(newMeas.getPosition());
			measurementMapper.insertMeasurement(newMeas);
			for (String wifi : wifirssi.keySet()) {
				measurementMapper.insertWiFiRSSI4Measurement(wifi, wifirssi.get(wifi), newMeas.getId().toString());
			}

			session.commit();

		} finally {
			session.close();
		}
	}

	@Test()
	public void testReadMeasurementFromArray() throws RecordNotFoundException, InsertionException {
		UUID zoneId1 = UUID.fromString("9ff78a6a-2216-4f38-bfeb-5fa189b6421b");
		Zone bathroom = new Zone("bathroom");
		bathroom.setId(zoneId1);
		UUID zoneId2 = UUID.fromString("743d2365-2eaa-412f-8324-6b6b1361ba5b");
		Zone kitchen = new Zone("kitchen");
		kitchen.setId(zoneId2);
		UUID zoneId3 = UUID.fromString("183f0204-5029-4b33-a128-404ba5c68fa8");
		Zone bedroom = new Zone("bedroom");
		bedroom.setId(zoneId3);

		UUID posId1 = UUID.fromString("eb264eea-4106-46a3-9992-70f16f283a15");
		Position pos1 = new Position(new Coordinate(0, 0, 0), bathroom);
		pos1.setUUID(posId1);
		UUID posId2 = UUID.fromString("5f484241-6dcc-4731-846c-7fc3e4f0fafb");
		Position pos2 = new Position(new Coordinate(0, 0, 0), kitchen);
		pos2.setUUID(posId2);
		UUID posId3 = UUID.fromString("c36e7f61-ba7b-408f-b113-c528980e7131");
		Position pos3 = new Position(new Coordinate(0, 0, 0), bathroom);
		pos3.setUUID(posId3);

		UUID mesID1 = UUID.fromString("59d46ae9-e0c8-48d0-b14a-503ed414b7cc");
		MeasurementBuilder mesbuild1 = new MeasurementBuilder();
		mesbuild1.setPosition(pos1);
		mesbuild1.setMagnetometer(new Magnetometer(0, 0, 0, 0));
		mesbuild1.setGPSCoordinates(new GPSCoordinate(00, 00, 00));
		Measurement mes1 = mesbuild1.build();
		mes1.setTimestamp(Timestamp.valueOf("2015-07-10 15:00:00"));
		mes1.setId(mesID1);

		UUID mesID2 = UUID.fromString("59d46ae9-e0c8-48d0-b14a-503ed414b7cc");
		MeasurementBuilder mesbuild2 = new MeasurementBuilder();
		mesbuild2.setPosition(pos2);
		mesbuild2.setMagnetometer(new Magnetometer(1, 1, 1, 1));
		mesbuild2.setGPSCoordinates(new GPSCoordinate(11, 11, 11));
		Measurement mes2 = mesbuild2.build();
		mes2.setTimestamp(Timestamp.valueOf("2015-07-11 20:00:00"));
		mes2.setId(mesID2);

		UUID mesID3 = UUID.fromString("59d46ae9-e0c8-48d0-b14a-503ed414b7cc");
		MeasurementBuilder mesbuild3 = new MeasurementBuilder();
		mesbuild3.setPosition(pos3);
		mesbuild3.setMagnetometer(new Magnetometer(2, 2, 2, 2));
		mesbuild3.setGPSCoordinates(new GPSCoordinate(22, 22, 22));
		Measurement mes3 = mesbuild3.build();
		mes3.setTimestamp(Timestamp.valueOf("2015-07-12 12:00:00"));
		mes3.setId(mesID3);

		Collection<Measurement> expected = Arrays.asList(mes1, mes2, mes3);
		Collection<Measurement> actual = dao.readMeasurements();
		assertThat(actual, hasItems(expected.toArray(new Measurement[expected.size()])));
	}

	@Test
	public void testCreateMeasurement() throws InsertionException {
		MeasurementBuilder measurementBuilder = new MeasurementBuilder();
		measurementBuilder.setMagnetometer(new Magnetometer(4, 4, 4, 4));
		measurementBuilder.setGPSCoordinates(new GPSCoordinate(3, 2, 1));
		Map<String, Double> wifirssi = new HashMap<String, Double>();
		wifirssi.put("testAP1", -1.0);
		wifirssi.put("testAP2", -2.0);
		wifirssi.put("testAP3", -3.0);
		measurementBuilder.setWifiRSSI(new WiFiRSSI(wifirssi));
		measurementBuilder.setbluetoothTags(new BluetoothTags(new HashSet<>(Arrays.asList("bt1", "bt2", "bt3"))));
		measurementBuilder.setPosition(new Position(new Coordinate(3, 3, 3), new Zone("EZ")));
		RFIDTags rfid = new RFIDTags(new HashSet<byte[]>());
		rfid.addTag(new byte[] { (byte) 12 });
		rfid.addTag(new byte[] { (byte) -82, (byte) 34 });
		measurementBuilder.setRFIDTags(rfid);
		Measurement measurement = measurementBuilder.build();
		// System.out.println(measurement.getPosition());
		dao.createMeasurement(measurement);
		Collection<Measurement> measuerements = dao.readMeasurements();
		assertEquals(true, measuerements.contains(measurement));

	}

	@Ignore
	public void testDeleteMeasurementByMeasurement() throws RecordNotFoundException {
		UUID zoneId1 = UUID.fromString("9ff78a6a-2216-4f38-bfeb-5fa189b6421b");
		Zone bathroom = new Zone("bathroom");
		bathroom.setId(zoneId1);

		UUID posId1 = UUID.fromString("eb264eea-4106-46a3-9992-70f16f283a15");
		Position pos1 = new Position(new Coordinate(0, 0, 0), bathroom);
		pos1.setUUID(posId1);

		UUID mesID1 = UUID.fromString("59d46ae9-e0c8-48d0-b14a-503ed414b7cc");
		MeasurementBuilder mesbuild1 = new MeasurementBuilder();
		mesbuild1.setPosition(pos1);
		mesbuild1.setMagnetometer(new Magnetometer(0, 0, 0, 0));
		mesbuild1.setGPSCoordinates(new GPSCoordinate(00, 00, 00));
		Measurement mes1 = mesbuild1.build();
		mes1.setTimestamp(Timestamp.valueOf("2015-07-10 15:00:00"));
		mes1.setId(mesID1);

		dao.deleteMeasurement(mes1);

		assertEquals(2, dao.readMeasurements().size());
	}

}
