package uni.miskolc.ips.ilona.tracking.persist.mysql;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class ServiceAvilabilityTest {

	@Test
	public void configFilAvailable() {
		
		File file = new File("src/main/resources/mybatis-configurationa.xml");
		Assert.assertTrue("The mybatis config file is not available in the resource folder!", file.exists());

	}
}
