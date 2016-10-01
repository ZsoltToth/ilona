package uni.miskolc.ips.ilona.tracking.persist.mysql.model;

public class TrackPositionMapper {

	private String positionid;

	private double coordx;

	private double coordy;

	private double coordz;

	private String zoneid;

	private String zoneName;

	private long time;

	public TrackPositionMapper() {

	}

	public TrackPositionMapper(String positionid, double coordx, double coordy, double coordz, String zoneid,
			String zoneName, long time) {
		super();
		this.positionid = positionid;
		this.coordx = coordx;
		this.coordy = coordy;
		this.coordz = coordz;
		this.zoneid = zoneid;
		this.zoneName = zoneName;
		this.time = time;
	}

	public String getPositionid() {
		return positionid;
	}

	public void setPositionid(String positionid) {
		this.positionid = positionid;
	}

	public double getCoordx() {
		return coordx;
	}

	public void setCoordx(double coordx) {
		this.coordx = coordx;
	}

	public double getCoordy() {
		return coordy;
	}

	public void setCoordy(double coordy) {
		this.coordy = coordy;
	}

	public double getCoordz() {
		return coordz;
	}

	public void setCoordz(double coordz) {
		this.coordz = coordz;
	}

	public String getZoneid() {
		return zoneid;
	}

	public void setZoneid(String zoneid) {
		this.zoneid = zoneid;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "TrackPositionMapper [positionid=" + positionid + ", coordx=" + coordx + ", coordy=" + coordy
				+ ", coordz=" + coordz + ", zoneid=" + zoneid + ", zoneName=" + zoneName + ", time=" + time + "]";
	}

}
