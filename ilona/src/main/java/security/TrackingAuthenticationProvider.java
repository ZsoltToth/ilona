package security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import uni.miskolc.ips.ilona.tracking.model.UserDetails;

public class TrackingAuthenticationProvider implements AuthenticationProvider {

	/*
	@Autowired
	private TrackingUserDAO2 trackingDAO;

	@Autowired
	private TrackingUserDAO trackingUserDao;
	 */
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String username = authentication.getName();
		String userpassword = authentication.getCredentials().toString();
		Collection<GrantedAuthority> grantedAuths = null;
		UserDetails userDetails = null;
		/*
		try {
			userDetails = trackingUserDao.getUser(username);

			if (userDetails == null) {
				throw new UserNotFoundException();
			}

			

			if (passwordEncoder.matches(userpassword, userDetails.getPassword()) == false) {
				return null;
			}

			grantedAuths = new ArrayList<GrantedAuthority>();

			for (String role : userDetails.getRoles()) {
				grantedAuths.add(new SimpleGrantedAuthority(role));
			}

		} catch (UserNotFoundException e) {
			return null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		return new UsernamePasswordAuthenticationToken(username, userpassword, grantedAuths);
		/*
		 * String name = authentication.getName(); try { TrackingLoginUserData
		 * userdata = trackingDAO.getUser(name); if (userdata == null) { throw
		 * new TrackingUserNotFoundException(); }
		 * 
		 * for(String s : userdata.getRoles()) { System.out.println("role: " +
		 * s); } System.out.println(userdata.isEnabled()); String password =
		 * authentication.getCredentials().toString(); List<GrantedAuthority>
		 * grantedAuths = new ArrayList<>(); grantedAuths.add(new
		 * SimpleGrantedAuthority("ROLE_ADMIN")); String hashedPassword =
		 * userdata.getPassword(); if (passwordEncoder.matches(password,
		 * hashedPassword)) { //if (enc.matches(password, enc.encode("ilona")))
		 * { return new UsernamePasswordAuthenticationToken(name, password,
		 * grantedAuths); }
		 * 
		 * } catch(TrackingUserNotFoundException e) { return null; } return
		 * null;
		 */
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return arg0.equals(UsernamePasswordAuthenticationToken.class);
	}

	/**
	 * Autowire
	 * 
	 * @param trackingDAO
	 */
	/*
	public void setTrackingDAO(TrackingUserDAO2 trackingDAO) {
		this.trackingDAO = trackingDAO;
	}
	
	public void setTrackingUserDao(TrackingUserDAO trackingUserDao) {
		this.trackingUserDao = trackingUserDao;
	}
	*/
	public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	

}
