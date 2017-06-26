package uni.miskolc.ips.ilona.navigation.controller;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import com.mysql.jdbc.Driver;

import uni.miskolc.ips.ilona.measurement.persist.ZoneDAO;
import uni.miskolc.ips.ilona.measurement.persist.mysql.MySQLZoneDAO;
import uni.miskolc.ips.ilona.navigation.persist.OntologyDAO;
import uni.miskolc.ips.ilona.navigation.persist.ontology.OntologyDAOImpl;
import uni.miskolc.ips.ilona.navigation.service.OntologyGenerationService;
import uni.miskolc.ips.ilona.navigation.service.impl.OntologyGenerationServiceImpl;

public class OntologyGenerationControllerIntegrationTest {
	
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

	private OntologyGenerationService ontologyGenerationService;
	private File testFile;
	private OntologyGenerationController test;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
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

		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
		try {
			runSQLScript(TEST_DROP_TABLES);
		} catch (Exception e) {
			Assume.assumeNoException(e);
		}
		
	}

	@Before
	public void setUp() throws Exception {
		
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

		ontologyGenerationService = new OntologyGenerationServiceImpl(ontologyDAO, dao);
		test=new OntologyGenerationController(ontologyGenerationService);
		
	}


	@Test
	public void test() {
		fail("Not yet implemented");
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
