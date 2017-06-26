package uni.miskolc.ips.ilona.navigation.controller.dto;

import java.util.Set;
import java.util.UUID;

import uni.miskolc.ips.ilona.navigation.service.WayfindingService.Restriction;

public class NavigationZoneRequest {

	private UUID startID;
	private UUID destinationID;
	private String startName;
	private String destinationName;
	private Set<Restriction> restriction;

	public NavigationZoneRequest() {
	}

	public NavigationZoneRequest(UUID startID, UUID destinationID, String startName, String destinationName,
			Set<Restriction> restriction) {
		super();
		this.startID = startID;
		this.destinationID = destinationID;
		this.startName = startName;
		this.destinationName = destinationName;
		this.restriction = restriction;
	}

	public UUID getStartID() {
		return startID;
	}

	public void setStartID(UUID startID) {
		this.startID = startID;
	}

	public UUID getDestinationID() {
		return destinationID;
	}

	public void setDestinationID(UUID destinationID) {
		this.destinationID = destinationID;
	}

	public String getStartName() {
		return startName;
	}

	public void setStartName(String startName) {
		this.startName = startName;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public Set<Restriction> getRestriction() {
		return restriction;
	}

	public void setRestriction(Set<Restriction> restriction) {
		this.restriction = restriction;
	}

}
