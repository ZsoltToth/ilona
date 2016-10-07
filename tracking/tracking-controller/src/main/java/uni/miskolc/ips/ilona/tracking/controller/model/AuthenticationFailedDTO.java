package uni.miskolc.ips.ilona.tracking.controller.model;

public class AuthenticationFailedDTO {

	private int responseState;

	private long timestamp;

	private String message;

	public AuthenticationFailedDTO() {

	}

	public AuthenticationFailedDTO(int responseState, long timestamp, String message) {
		super();
		this.responseState = responseState;
		this.timestamp = timestamp;
		this.message = message;
	}

	public int getResponseState() {
		return responseState;
	}

	public void setResponseState(int responseState) {
		this.responseState = responseState;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
