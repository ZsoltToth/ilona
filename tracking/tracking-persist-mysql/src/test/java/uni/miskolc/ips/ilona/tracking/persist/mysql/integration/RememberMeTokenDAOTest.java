package uni.miskolc.ips.ilona.tracking.persist.mysql.integration;

import java.util.Date;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import uni.miskolc.ips.ilona.tracking.model.UserData;
import uni.miskolc.ips.ilona.tracking.persist.mysql.MySqlAndMybatisRememberMeTokenRepository;
import uni.miskolc.ips.ilona.tracking.persist.mysql.TestUtility;

public class RememberMeTokenDAOTest extends TestSetup {

	private static PersistentTokenRepository dao;

	@BeforeClass
	public static void createDAoO() throws Exception {
		dao = new MySqlAndMybatisRememberMeTokenRepository(HOST, PORT, DATABASE, USER, PASSWORD);
	}

	@Test
	public void testCreateToken() {
		PersistentRememberMeToken token = TestUtility.generateMockedRememberMeTokenToken(USER_USERID, "newseries",
				"valueee", new Date());
		try {
			dao.createNewToken(token);
		} catch (Exception e) {
			Assert.fail("Token store error!");
		}
	}

}
