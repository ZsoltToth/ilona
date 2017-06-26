package security;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
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
		
		System.out.println(UUID.randomUUID().toString().replaceAll("-", "").length());
		GrantedAuthority ga = new SimpleGrantedAuthority("ROLE_ADMIN");
		
		String regex = "^(?=.{5,50}$)([a-zA-Z0-9]{1}[_:-]{0,1}){1,49}[a-zA-Z0-9]{1}$"; // device id
		String regex2 = "^(?=.{5,50}$)([a-zA-Z0-9]{1}[ ]{0,1}){1,49}[a-zA-Z0-9]{1}$"; // device name
		String regex3 = "^(?=.{1,30}$)([a-zA-Z0-9]{1}[_- ]{0,1}){1,29}[a-zA-Z0-9]{1}$";
		System.out.println(new Date(1472667837L*1000L));
		
		
		
		System.out.println(ga.getAuthority());
	}
}
