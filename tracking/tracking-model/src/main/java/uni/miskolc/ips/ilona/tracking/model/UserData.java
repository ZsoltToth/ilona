package uni.miskolc.ips.ilona.tracking.model;

import java.util.Date;

public class UserData {

	private String userID;
	private String password;
	private boolean enabled;
	private Date joinDate;
	private String role;

	public UserData(String userID, String password, boolean enabled, Date joinDate, String role) {
		super();
		this.userID = userID;
		this.password = password;
		this.enabled = enabled;
		this.joinDate = joinDate;
		this.role = role;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserData [userID=" + userID + ", enabled=" + enabled + ", joinDate=" + joinDate + ", role=" + role
				+ "]";
	}

}
