package uni.miskolc.ips.ilona.tracking.persist.mysql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uni.miskolc.ips.ilona.tracking.model.UserData;
import uni.miskolc.ips.ilona.tracking.persist.TrackingLoginDAO;

public class MySQLTrackingLoginDAO implements TrackingLoginDAO {

	/**
	 * 
	 */
	private final Logger LOG = LogManager.getLogger(MySQLTrackingLoginDAO.class);
	/**
	 * 
	 */
	private SqlSessionFactory sessionFactory;

	public MySQLTrackingLoginDAO(final String configFilePath, final String host, final int port, final String database,
			final String user, final String password) throws FileNotFoundException {
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

	}

	@Override
	public void createUser(UserData user) {
		SqlSession session = sessionFactory.openSession();
		try {
			TrackingLoginMapper mapper = session.getMapper(TrackingLoginMapper.class);
			mapper.createUser(user);
			session.commit();
		} finally {
			session.close();
		}
	}

	@Override
	public UserData getUser(String userID) {
		UserData result = null;
		SqlSession session = sessionFactory.openSession();
		try {
			TrackingLoginMapper mapper = session.getMapper(TrackingLoginMapper.class);
			result = mapper.getUser(userID);
			// session.commit();
		} finally {
			session.close();
		}
		return result;
	}

	@Override
	public void updateUser(UserData user) {
		SqlSession session = sessionFactory.openSession();
		try {
			TrackingLoginMapper mapper = session.getMapper(TrackingLoginMapper.class);
			mapper.updateUser(user);
			session.commit();
		} finally {
			session.close();
		}
	}

	@Override
	public void deleteUser(String userID) {
		SqlSession session = sessionFactory.openSession();
		try {
			TrackingLoginMapper mapper = session.getMapper(TrackingLoginMapper.class);
			mapper.deleteUser(userID);
			session.commit();
		} finally {
			session.close();
		}
	}

}
