package uni.miskolc.ips.ilona.measurement.persist.mysql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.UUID;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.persist.PositionDAO;
import uni.miskolc.ips.ilona.measurement.persist.ZoneDAO;
import uni.miskolc.ips.ilona.measurement.persist.exceptions.InsertionException;
import uni.miskolc.ips.ilona.measurement.persist.exceptions.RecordNotFoundException;
import uni.miskolc.ips.ilona.measurement.persist.mappers.PositionMapper;

import com.mysql.jdbc.PreparedStatement;

public class MySQLPositionDAO implements PositionDAO {

	/**
	 * This is the central interface in the log4j package. 
	 * Most logging operations, except configuration, are done through this interface.
	 */	
	private final Logger log = LogManager.getLogger(MySQLPositionDAO.class);

	/**
	 * Interface for identification zone.
	 */
	private ZoneDAO zoneDAO;
	
	/**
	 * Creates an SqlSesion out of a connection or a DataSource.
	 */
	private SqlSessionFactory sessionFactory;	
	
	/**
	 * Created for Unit Testing.
	 * 
	 * @param sessionFactory Facotry method for instanceing.
	 * 				
	 */
	protected MySQLPositionDAO(final SqlSessionFactory sessionFactory) {
		super();
		this.sessionFactory = sessionFactory;
	}

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
	protected MySQLPositionDAO(final String configFilePath, final String host, final int port,
			final String database, final String user, final String password)
			throws FileNotFoundException {
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

		this.sessionFactory = (new SqlSessionFactoryBuilder()).build(
				inputStream, props);
	}
	
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
	public MySQLPositionDAO(final String configFilePath, final String host, final int port,
			final String database, final String user, final String password, final ZoneDAO zoneDAO)
			throws FileNotFoundException {
		this(configFilePath, host, port, database, user, password);
		this.zoneDAO = zoneDAO;
	}

	@Override
	public final void createPosition(final Position position) throws InsertionException {
		SqlSession session = sessionFactory.openSession();
		try {
			if (position.getZone() != null) {
				try {
					this.zoneDAO.readZone(position.getZone().getId());
				} catch (RecordNotFoundException ex) {
					this.zoneDAO.createZone(position.getZone());
				}
			}
			PositionMapper mapper = session.getMapper(PositionMapper.class);
			mapper.insertPosition(position);
			session.commit();
		} catch (PersistenceException ex) {
			log.error(ex.getMessage());
			throw new InsertionException();
		} finally {
			session.close();
		}

	}

	@Override
	public final Position getPosition(final UUID id) throws RecordNotFoundException {
		Position result = null;
		SqlSession session = sessionFactory.openSession();
		try {
			PositionMapper mapper = session.getMapper(PositionMapper.class);
			result = mapper.selectPositionByUUID(id);
		} finally {
			session.close();
		}

		if (result == null) {
			throw new RecordNotFoundException();
		}
		return result;
	}

	@Override
	public final Collection<Position> readPositions() {
		Collection<Position> result = new ArrayList<Position>();
		SqlSession session = sessionFactory.openSession();

		try {
			PositionMapper mapper = session.getMapper(PositionMapper.class);
			result = mapper.selectPositions();
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public final void updatePosition(final Position position)
			throws RecordNotFoundException {
		SqlSession session = sessionFactory.openSession();
		try {
			PositionMapper mapper = session.getMapper(PositionMapper.class);
			if (mapper.selectPositionByUUID(position.getUUID()) == null) {
				throw new RecordNotFoundException();
			}
			mapper.updatePosition(position);
			session.commit();
		} catch (PersistenceException ex) {
			throw new RecordNotFoundException();
		} finally {
			session.close();
		}
	}

	@Override
	public final void deletePosition(final Position position)
			throws RecordNotFoundException {
		SqlSession session = sessionFactory.openSession();
		try {
			PositionMapper mapper = session.getMapper(PositionMapper.class);
			if (mapper.selectPositionByUUID(position.getUUID()) == null) {
				throw new RecordNotFoundException();
			}
			mapper.deletePosition(position);
			session.commit();
		} finally {
			session.close();
		}

	}

}
