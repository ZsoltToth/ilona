package uni.miskolc.ips.ilona.tracking.controller.model;

import java.util.Date;

import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.tracking.model.TrackPosition;

public class PositionTrackDTO {

	private Position position;

	private Date date;

	public PositionTrackDTO() {

	}

	public static final PositionTrackDTO convertToDTO(TrackPosition tp) {
		return new PositionTrackDTO(tp.getPosition(), tp.getTrackTime());
	}
	
	public PositionTrackDTO(Position position, Date date) {
		super();
		this.position = position;
		this.date = date;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
