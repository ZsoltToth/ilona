package security;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class A {

	public static void main(String[] Args) {
		BCryptPasswordEncoder enc = new BCryptPasswordEncoder(10);
		
		String encoded = enc.encode("patriku");
		System.out.println(encoded.length() + "  " + encoded);

		System.out.println(enc.matches("ilona", encoded));
		
		SecureRandom random = new SecureRandom();
		System.out.println((new BigInteger(120, random)).toString());
		
		System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));
		GrantedAuthority ga = new SimpleGrantedAuthority("ROLE_ADMIN");
		
		
		
		System.out.println(ga.getAuthority());
	}
}
