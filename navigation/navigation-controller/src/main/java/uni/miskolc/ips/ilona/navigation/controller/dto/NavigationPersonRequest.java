package uni.miskolc.ips.ilona.navigation.controller.dto;

import java.util.Set;
import java.util.UUID;

import uni.miskolc.ips.ilona.navigation.service.WayfindingService.Restriction;

public class NavigationPersonRequest {

	private UUID startID;
	private String personName;
	private String startName;
	private Set<Restriction> restriction;

	public NavigationPersonRequest() {
		
	}

	public NavigationPersonRequest(UUID startID, String personName, String startName,
			Set<Restriction> restriction) {
		super();
		this.startID = startID;
		this.startName = startName;
		this.personName = personName;
		this.restriction = restriction;
	}

	public UUID getStartID() {
		return startID;
	}

	public void setStartID(UUID startID) {
		this.startID = startID;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getStartName() {
		return startName;
	}

	public void setStartName(String startName) {
		this.startName = startName;
	}

	public Set<Restriction> getRestriction() {
		return restriction;
	}

	public void setRestriction(Set<Restriction> restriction) {
		this.restriction = restriction;
	}

}
