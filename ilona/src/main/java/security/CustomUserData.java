package security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;

public class CustomUserData implements UserDetails, Cloneable, Comparable<CustomUserData> {

	private String username;
	private String password;
	private Collection<GrantedAuthority> authorities;
	private boolean isAccountExpired;
	private boolean isAccountNonLocked;
	private boolean isEnabled;
	private boolean isCredentialsNonExpired;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parameterless constructor for serialization and spring IoC creation and for others.
	 */
	public CustomUserData() {

	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @param authorities
	 * @param isAccountExpire
	 * @param isAccountNonLocked
	 * @param isenabled
	 * @param isCredentialsNonExpired
	 */
	public CustomUserData(String username, String password, List<GrantedAuthority> authorities, boolean isAccountExpire,
			boolean isAccountNonLocked, boolean isenabled, boolean isCredentialsNonExpired) {
		super();
		this.username = username;
		this.password = password;
		this.authorities = authorities;
		this.isAccountExpired = isAccountExpire;
		this.isAccountNonLocked = isAccountNonLocked;
		this.isEnabled = isenabled;
		this.isCredentialsNonExpired = isCredentialsNonExpired;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAccountExpired() {
		return isAccountExpired;
	}

	public void setAccountExpired(boolean isAccountExpire) {
		this.isAccountExpired = isAccountExpire;
	}

	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}

	public void setAccountNonLocked(boolean isAccountNonLocked) {
		this.isAccountNonLocked = isAccountNonLocked;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(boolean isenabled) {
		this.isEnabled = isenabled;
	}

	public boolean isCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
		this.isCredentialsNonExpired = isCredentialsNonExpired;
	}

	public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authorities == null) ? 0 : authorities.hashCode());
		result = prime * result + (isAccountExpired ? 1231 : 1237);
		result = prime * result + (isAccountNonLocked ? 1231 : 1237);
		result = prime * result + (isCredentialsNonExpired ? 1231 : 1237);
		result = prime * result + (isEnabled ? 1231 : 1237);
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomUserData other = (CustomUserData) obj;
		if (authorities == null) {
			if (other.authorities != null)
				return false;
		} else if (!authorities.equals(other.authorities))
			return false;
		if (isAccountExpired != other.isAccountExpired)
			return false;
		if (isAccountNonLocked != other.isAccountNonLocked)
			return false;
		if (isCredentialsNonExpired != other.isCredentialsNonExpired)
			return false;
		if (isEnabled != other.isEnabled)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CustomUserData [username=" + username + ", password=" + "[NoPasswordForYou]" + ", authorities="
				+ authorities + ", isAccountExpired=" + isAccountExpired + ", isAccountNonLocked=" + isAccountNonLocked
				+ ", isEnabled=" + isEnabled + ", isCredentialsNonExpired=" + isCredentialsNonExpired + "]";
	}

	/**
	 * 
	 * @return
	 */
	public final CustomUserData createShallowCopy() {
		try {
			return (CustomUserData) this.clone();
		} catch (CloneNotSupportedException e) {
			/*
			 * 
			 */
			return null;
		}
	}

	/**
	 * 
	 * @return
	 */
	public final CustomUserData createDeepCopy() {

		CustomUserData userdata = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;

		try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			
		} catch(Exception e) {
			
		}
		
		try {
			/*
			 * Serialize part
			 */
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);

			oos.writeObject(this);
			oos.flush();
			oos.close();

			/*
			 * Deserialize part
			 */
			bais = new ByteArrayInputStream(bos.toByteArray());
			ois = new ObjectInputStream(bais);
			userdata = (CustomUserData) ois.readObject();

		} catch (Exception e) {
		} finally {
			
		}
		return userdata;
	}
	
	/**
	 * Ordered by the username.compareto
	 */
	@Override
	public int compareTo(CustomUserData o) {
		return username.compareTo(o.username);
	}
}
