package uni.miskolc.ips.ilona.tracking.controller.model;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;

public class MobileTransferAdminDTO {

	private String userid;

	private String deviceid;

	private Measurement measurement;

	public MobileTransferAdminDTO() {

	}

	public MobileTransferAdminDTO(String userid, String deviceid, Measurement measurement) {
		super();
		this.userid = userid;
		this.deviceid = deviceid;
		this.measurement = measurement;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public Measurement getMeasurement() {
		return measurement;
	}

	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}

	@Override
	public String toString() {
		return "MobileTransferAdminDTO [userid=" + userid + ", deviceid=" + deviceid + ", measurement=" + measurement
				+ "]";
	}

}
