package uni.miskolc.ips.ilona.navigation.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import com.mysql.jdbc.Driver;

import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.measurement.persist.ZoneDAO;
import uni.miskolc.ips.ilona.measurement.persist.mysql.MySQLZoneDAO;
import uni.miskolc.ips.ilona.navigation.persist.OntologyDAO;
import uni.miskolc.ips.ilona.navigation.persist.ontology.OntologyDAOImpl;
import uni.miskolc.ips.ilona.navigation.service.NoRouteAvailableException;
import uni.miskolc.ips.ilona.navigation.service.WayfindingService;
import uni.miskolc.ips.ilona.navigation.service.WayfindingService.Restriction;

public class WayfindingServiceImplementationIntegrationTest {

	public static final String MYBATIS_CONFIG_FILE = "src/resources/mybatis-configuration.xml";
	public static final String TEST_CREATE_TABLES = "src/resources/createTables.sql";
	public static final String TEST_DROP_TABLES = "src/resources/dropTables.sql";
	public static final String TEST_SETUP_SQL = "src/resources/setupTestDB.sql";
	public static final String TEST_TEAR_DOWN_SQL = "src/resources/teardownTestDB.sql";
	public static final String TEST_DUMMY_ONTOLOGY = "src/resources/dummy.owl";
	public static final String TEST_BASE_ONTOLOGY = "src/resources/ILONABASE.owl";

	protected static String HOST;
	protected static int PORT;
	protected static String DATABASE;
	protected static String USER;
	protected static String PASSWORD;

	// The zones of the ontology
	private static Zone zone1;
	private static Zone zone2;
	private static Zone zone3;
	private static Zone zone4A;
	private static Zone zone4B;
	private static Zone zone5;

	private WayfindingService wayfindingImpl;

	@BeforeClass
	public static void beforeClass() {
		/* Check SQL Scripts */
		File mybatisConfig = new File(MYBATIS_CONFIG_FILE);
		File createTablesSQL = new File(TEST_CREATE_TABLES);
		File dropTablesSQL = new File(TEST_DROP_TABLES);
		File setupTestSQL = new File(TEST_SETUP_SQL);
		File teardownSQL = new File(TEST_TEAR_DOWN_SQL);

		Assume.assumeTrue(mybatisConfig.exists());
		Assume.assumeTrue(createTablesSQL.exists());
		Assume.assumeTrue(dropTablesSQL.exists());
		Assume.assumeTrue(setupTestSQL.exists());
		Assume.assumeTrue(teardownSQL.exists());

		/* Check System Properties */
		String host = System.getProperty("database.host");

		int port = -1;
		try {
			System.out.println(System.getProperty("database.port"));
			port = Integer.parseInt(System.getProperty("database.port"));
		} catch (NumberFormatException ex) {
			port = -1;
			Assume.assumeNoException(ex);
		}
		String database = System.getProperty("database.db");
		String user = System.getProperty("database.user");
		String password = System.getProperty("database.password");
		Assume.assumeNotNull(host, port, database, user, password);
		HOST = host;
		PORT = port;
		DATABASE = database;
		USER = user;
		PASSWORD = password;

		try {
			runSQLScript(TEST_CREATE_TABLES);
		} catch (Exception e) {
			Assume.assumeNoException(e);
		}

		// The Zones of the ontology. These values should only be used as
		// exoected values.
		zone1 = new Zone("Zone # 1");
		zone2 = new Zone("Zone # 2");
		zone3 = new Zone("Zone # 3");
		zone4A = new Zone("Zone # 4A");
		zone4B = new Zone("Zone # 4B");
		zone5 = new Zone("Zone # 5");

		zone1.setId(UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee"));
		zone2.setId(UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56"));
		zone3.setId(UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d"));
		zone4A.setId(UUID.fromString("9f71cbac-14eb-45ce-9e0f-b5757ad4cc5c"));
		zone4B.setId(UUID.fromString("76f33f88-0568-4058-8b3e-4435f636bf88"));
		zone5.setId(UUID.fromString("18364962-7390-4b22-9dab-22283a01dbc3"));

	}

	/**
	 * It drops the created tables.
	 */
	@AfterClass
	public static void afterClass() {
		try {
			runSQLScript(TEST_DROP_TABLES);
		} catch (Exception e) {
			Assume.assumeNoException(e);
		}

	}

	@Before
	public void setUp() {

		/* INSERT TEST RECORDS */
		try {
			runSQLScript(TEST_SETUP_SQL);
		} catch (Exception e) {
			Assume.assumeNoException(e);
		}

		ZoneDAO dao = null;
		try {
			dao = new MySQLZoneDAO(MYBATIS_CONFIG_FILE, HOST, PORT, DATABASE, USER, PASSWORD);
		} catch (FileNotFoundException ex) {
			Assume.assumeNoException(ex);
		}
		OntologyDAO ontologyDAO = null;
		try {
			ontologyDAO = new OntologyDAOImpl(TEST_BASE_ONTOLOGY, TEST_DUMMY_ONTOLOGY);
		} catch (OWLOntologyCreationException e) {
			Assume.assumeNoException(e);
		}

		wayfindingImpl = new WayfindingServiceImpl(ontologyDAO, dao);

	}

	@After
	public void tearDown() {
		try {
			runSQLScript(TEST_TEAR_DOWN_SQL);
		} catch (Exception e) {
			Assume.assumeNoException(e);
		}
	}

	@Test
	public void testGenerateRouteWithoutRestrictions() throws NoRouteAvailableException {
		// first possible route
		List<Zone> firstSolution = new ArrayList<>();
		firstSolution.add(zone1);
		firstSolution.add(zone2);
		firstSolution.add(zone3);
		firstSolution.add(zone4A);
		firstSolution.add(zone5);

		// second possible route
		List<Zone> secondSolution = new ArrayList<>();
		secondSolution.add(zone1);
		secondSolution.add(zone2);
		secondSolution.add(zone3);
		secondSolution.add(zone4B);
		secondSolution.add(zone5);

		if (firstSolution.equals(wayfindingImpl.generateRoute(zone1, zone5)) == true
				|| secondSolution.equals(wayfindingImpl.generateRoute(zone1, zone5)) == true) {
			assertTrue(true);
		} else {
			assertTrue(false);
		}
	}

	@Test
	public void testGenerateRouteWithRestrictions() throws NoRouteAvailableException {
		// the possible route for the method
		List<Zone> solution = new ArrayList<>();
		solution.add(zone1);
		solution.add(zone2);
		solution.add(zone3);
		solution.add(zone4A);
		solution.add(zone4B);
		solution.add(zone5);

		Set<Restriction> restrictions = new HashSet<>();
		restrictions.add(Restriction.DUMMY_ZONERESTRICTION);
		restrictions.add(Restriction.NO_ESCALATOR);
		restrictions.add(Restriction.NO_STAIRS);
		
		System.out.println(solution.size()+wayfindingImpl.generateRoute(zone1, zone5, restrictions).size());

		assertEquals(solution, wayfindingImpl.generateRoute(zone1, zone5, restrictions));
	}

	@Test
	public void testGenerateRouteToPersonWithoutRestriction() throws NoRouteAvailableException {
		List<Zone> solution = new ArrayList<>();
		solution.add(zone1);
		solution.add(zone2);
		solution.add(zone3);
		solution.add(zone4B);

		assertEquals(solution, wayfindingImpl.generateRoute(zone1, "Big Bad Wolf", new HashSet<>()));
	}

	@Test
	public void testGenerateRouteToPersonWithtRestriction() throws NoRouteAvailableException {
		List<Zone> solution = new ArrayList<>();
		solution.add(zone1);
		solution.add(zone2);
		solution.add(zone3);
		solution.add(zone4A);
		solution.add(zone4B);

		Set<Restriction> restrictions = new HashSet<>();
		restrictions.add(Restriction.DUMMY_ZONERESTRICTION);
		restrictions.add(Restriction.NO_ESCALATOR);
		restrictions.add(Restriction.NO_STAIRS);

		assertEquals(solution, wayfindingImpl.generateRoute(zone1, "Big Bad Wolf", restrictions));
	}

	private static void runSQLScript(String scriptFile)
			throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {

		Class.forName(Driver.class.getName());
		final String connectionURL = String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, DATABASE);
		Connection connection = DriverManager.getConnection(connectionURL, USER, PASSWORD);
		ScriptRunner runner = new ScriptRunner(connection);
		Reader reader = new FileReader(scriptFile);
		runner.runScript(reader);
		connection.close();
		reader.close();
	}
}
