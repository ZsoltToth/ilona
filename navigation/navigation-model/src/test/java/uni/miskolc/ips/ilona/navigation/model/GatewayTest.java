package uni.miskolc.ips.ilona.navigation.model;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class GatewayTest {

	private Gateway testGateway;

	@Before
	public void setUp() {
		testGateway = new Gateway(UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee"),
				UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56"));
	}
	
	@Test
	public void testGetFrom(){
		assertEquals(UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee"), testGateway.getFrom());
	}
	
	@Test
	public void testGetTo(){
		assertEquals(UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56"), testGateway.getTo());
	}
	
	@Test
	public void testSetFrom(){
		testGateway.setFrom(UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d"));
		assertEquals(UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d"), testGateway.getFrom());
	}
	
	@Test
	public void testSetTo(){
		testGateway.setTo(UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d"));
		assertEquals(UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d"), testGateway.getTo());
	}
	
	@Test
	public void testToString(){
		assertEquals("Gateway [from=07a25de0-a013-486d-9463-404a348e05ee, to=14fc835a-ee28-4b78-9c59-9ee0f759ce56]", testGateway.toString());
	}
}
