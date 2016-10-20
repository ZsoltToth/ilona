package uni.miskolc.ips.ilona.tracking.persist.mysql;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import uni.miskolc.ips.ilona.measurement.model.position.Coordinate;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.tracking.model.DeviceData;
import uni.miskolc.ips.ilona.tracking.model.TrackPosition;
import uni.miskolc.ips.ilona.tracking.persist.TrackingDAO;
import uni.miskolc.ips.ilona.tracking.persist.exception.OperationExecutionErrorException;
import uni.miskolc.ips.ilona.tracking.persist.exception.PositionAlreadyExistsException;
import uni.miskolc.ips.ilona.tracking.persist.exception.PositionNotFoundException;
import uni.miskolc.ips.ilona.tracking.persist.mysql.mappers.TrackMapper;
import uni.miskolc.ips.ilona.tracking.persist.mysql.model.TrackPositionMapper;

public class MySqlAndMybatisTrackingDAOImpl implements TrackingDAO {

	private static Logger logger = LogManager.getLogger(MySqlAndMybatisTrackingDAOImpl.class);

	private SqlSessionFactory sessionFactory;

	public MySqlAndMybatisTrackingDAOImpl() {
	}

	/**
	 * 
	 * @param host
	 *            Database host.
	 * @param port
	 *            Database port.
	 * @param database
	 *            Database name.
	 * @param user
	 *            Database username.
	 * @param password
	 *            Database password.
	 * @throws FileNotFoundException
	 *             Mybatis config file not found error.
	 */
	public MySqlAndMybatisTrackingDAOImpl(final String host, final int port, final String database, final String user,
			final String password) throws FileNotFoundException {
		ClassLoader loader = getClass().getClassLoader();
		InputStream inputStream = loader.getResourceAsStream("mybatis-configurationa.xml");

		String urlPattern = "jdbc:mysql://%s:%s/%s";
		String connectionURL = String.format(urlPattern, host, port, database);
		Properties props = new Properties();
		props.put("driver", "com.mysql.jdbc.Driver");
		props.put("url", connectionURL);
		props.put("username", user);
		props.put("password", password);

		this.sessionFactory = (new SqlSessionFactoryBuilder()).build(inputStream, props);
	}

	@Override
	public void storePosition(DeviceData device, Position position)
			throws PositionAlreadyExistsException, OperationExecutionErrorException {
		SqlSession session = sessionFactory.openSession();
		try {
			String deviceid = device.getDeviceid();
			UUID positionid = position.getUUID();
			Coordinate coord = position.getCoordinate();
			Zone zone = position.getZone();

			double trackTime = (new Date()).getTime() * 0.001;

			TrackMapper mapper = session.getMapper(TrackMapper.class);
			mapper.storePositionLocation(deviceid, positionid.toString(), trackTime);

			mapper.storePosition(position.getUUID().toString(), coord.getX(), coord.getY(), coord.getZ(),
					zone.getId().toString(), zone.getName());
			session.commit();
		} catch (Exception e) {
			if (e.getCause() instanceof MySQLIntegrityConstraintViolationException) {
				logger.info("Duplicated device with id: " + position.getUUID().toString());
				throw new PositionAlreadyExistsException("Duplicated device", e);
			}
			logger.error("Error: " + e.getMessage());
			throw new OperationExecutionErrorException();
		} finally {
			session.close();
		}
	}

	@Override
	public Collection<TrackPosition> restorePositionsInterval(DeviceData device, Date from, Date to)
			throws OperationExecutionErrorException {
		SqlSession session = sessionFactory.openSession();

		try {
			TrackMapper mapper = session.getMapper(TrackMapper.class);
			Collection<TrackPositionMapper> mapperColl = mapper.getTrackPositionsInterval(device.getDeviceid(), from,
					to);
			Collection<TrackPosition> positions = new ArrayList<>(mapperColl.size());
			for (TrackPositionMapper trackPosMapper : mapperColl) {
				positions.add(positionMapping(trackPosMapper));
			}
			return positions;
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			throw new OperationExecutionErrorException("Operation error", e);
		} finally {
			session.close();
		}
	}

	@Override
	public TrackPosition readPosition(String positionid)
			throws PositionNotFoundException, OperationExecutionErrorException {
		SqlSession session = sessionFactory.openSession();
		try {
			TrackMapper mapper = session.getMapper(TrackMapper.class);
			TrackPositionMapper trackPosMapper = mapper.getTrackPosition(positionid);
			return positionMapping(trackPosMapper);
		} catch (Exception e) {
			if (e instanceof NullPointerException) {
				logger.info("pos not found");
				throw new PositionNotFoundException("Position not found", e);
			}
			logger.error("Error: " + e.getMessage());
			throw new OperationExecutionErrorException("Operation error", e);
		} finally {
			session.close();
		}
	}

	/**
	 * The
	 * 
	 * @param mapper
	 * @return
	 */
	private TrackPosition positionMapping(TrackPositionMapper mapper) {
		TrackPosition trackPosition = new TrackPosition();
		// Position
		Position pos = new Position();

		// UUID
		UUID uuid = UUID.fromString(mapper.getPositionid());
		pos.setUUID(uuid);

		// Coordinate
		Coordinate coord = new Coordinate();
		coord.setX(mapper.getCoordx());
		coord.setY(mapper.getCoordy());
		coord.setZ(mapper.getCoordz());
		pos.setCoordinate(coord);

		// Zone
		Zone zone = new Zone();
		UUID zuuid = UUID.fromString(mapper.getZoneid());
		zone.setId(zuuid);
		zone.setName(mapper.getZoneName());
		pos.setZone(zone);

		// track time
		Date date = new Date(mapper.getTime());

		// Fill track position
		trackPosition.setTrackTime(date);
		trackPosition.setPosition(pos);
		return trackPosition;
	}

	/**
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SqlSessionFactory sessionFactory) {
		if (sessionFactory != null) {
			this.sessionFactory = sessionFactory;
		}
	}

}
