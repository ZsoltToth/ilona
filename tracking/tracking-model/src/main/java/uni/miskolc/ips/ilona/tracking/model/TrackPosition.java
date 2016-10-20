package uni.miskolc.ips.ilona.tracking.model;

import java.util.Date;

import uni.miskolc.ips.ilona.measurement.model.position.Position;

/**
 * This class holds the position object and when the positions measured.
 * 
 * @author Patrik
 *
 */
public class TrackPosition {

	private Position position;

	private Date trackTime;

	public TrackPosition() {

	}

	public TrackPosition(Position position, Date trackTime) {
		super();
		this.position = position;
		this.trackTime = trackTime;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Date getTrackTime() {
		return trackTime;
	}

	public void setTrackTime(Date trackTime) {
		this.trackTime = trackTime;
	}

	@Override
	public String toString() {
		return "TrackPosition [position=" + position + ", trackTime=" + trackTime + "]";
	}
}
