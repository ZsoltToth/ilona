package uni.miskolc.ips.ilona.tracking.persist.mysql.integration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;

import com.mysql.jdbc.Driver;

public class TestSetup {

	public static final String MYBATIS_CONFIG_FILE = "src/main/resources/mybatis-configurationa.xml";
	public static final String TEST_CREATE_TABLES = "src/main/resources/createTestTables.sql";
	public static final String TEST_DROP_TABLES = "src/main/resources/dropTestTables.sql";
	public static final String TEST_CREATE_ENTRIES = "src/main/resources/createEntries.sql";
	public static final String TEST_DELETE_ENTRIES = "src/main/resources/deleteEntries.sql";

	protected static String HOST = "localhost";
	protected static int PORT = 3306;
	protected static String DATABASE = "ilonatest";
	protected static String USER = "ilona";
	protected static String PASSWORD = "ilona";

	public static final String ADMIN_USERID = "admin";
	public static final String USER_USERID = "user";
	public static final String NON_EXISTS_USERID = "nonexists";

	public static final String USER_USERNAME = "username";
	public static final String ADMIN_USERNAME = "adminname";

	public static final String USER_DEVICE1_DEVICEID = "device1";
	public static final String USER_DEVICE2_DEVICEID = "device1";
	public static final String NON_EXISTS_DEVICEID = "devnonexists";

	public static final String PERSISTENT_TOKEN_SERIES = "series1";
	public static final String NON_EXISTS_TOKEN_SERIES = "seriesnonexists";

	public static final String USER_DEVICE1_POSITIONID1 = "ac0d3531-4681-4254-840b-19332d854a48";
	public static final String ZONEID1 = "ac0d3531-4681-4254-840b-19332d854a10";

	@BeforeClass
	public static void beforeClass() {
		System.out.println("!!! SETUP BEFORE CLASS !!!");
		File mybatisConfig = new File(MYBATIS_CONFIG_FILE);
		File createTablesSQL = new File(TEST_CREATE_TABLES);
		File dropTablesSQL = new File(TEST_DROP_TABLES);
		File setupTestSQL = new File(TEST_CREATE_ENTRIES);
		File teardownSQL = new File(TEST_DELETE_ENTRIES);

		Assume.assumeTrue(mybatisConfig.exists());
		Assume.assumeTrue(createTablesSQL.exists());
		Assume.assumeTrue(dropTablesSQL.exists());
		Assume.assumeTrue(setupTestSQL.exists());
		Assume.assumeTrue(teardownSQL.exists());
		/*
		 * String host = System.getProperty("database.host");
		 * System.out.println(host); int port = -1; try { port =
		 * Integer.parseInt(System.getProperty("database.port")); } catch
		 * (NumberFormatException ex) { port = -1; Assume.assumeNoException(ex);
		 * } String database = System.getProperty("database.db"); String user =
		 * System.getProperty("database.user"); String password =
		 * System.getProperty("database.password"); Assume.assumeNotNull(host,
		 * port, database, user, password); HOST = host; PORT = port; DATABASE =
		 * database; USER = user; PASSWORD = password;
		 */
		try {
			runSQLScript(TEST_CREATE_TABLES);
		} catch (Exception e) {
			Assume.assumeNoException(e);
		}
	}

	@AfterClass
	public static void afterClass() {
		System.out.println("!!! SETUP AFTER CLASS !!!");
		try {
			runSQLScript(TEST_DROP_TABLES);
		} catch (Exception e) {
			Assume.assumeNoException(e);
		}

	}

	@Before
	public void createEntries() {
		try {
			System.out.println("!!! SETUP BEFORE !!!");
			runSQLScript(TEST_CREATE_ENTRIES);
		} catch (Exception e) {
			Assume.assumeNoException(e);
		}
	}

	@After
	public void deleteEntries() {
		try {
			System.out.println("!!! SETUP AFTER !!!");
			runSQLScript(TEST_DELETE_ENTRIES);
		} catch (Exception e) {
			Assume.assumeNoException(e);
		}
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
