package uni.miskolc.ips.ilona.navigation.model;

import java.util.UUID;

public class Gateway {

	private UUID from;
	private UUID to;

	public Gateway(UUID from, UUID to) {
		super();
		this.from = from;
		this.to = to;
	}

	public UUID getFrom() {
		return from;
	}

	public void setFrom(UUID from) {
		this.from = from;
	}

	public UUID getTo() {
		return to;
	}

	public void setTo(UUID to) {
		this.to = to;
	}

	@Override
	public String toString() {
		return "Gateway [from=" + from + ", to=" + to + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
	        return true;
	    if(obj == null)
	        return false;
	    if(getClass() != obj.getClass())
	        return false;
	    Gateway other = (Gateway) obj;
	    if(from == null){
	        if(other.from != null)
	            return false;
	    }
	    else if(!from.equals(other.from))
	        return false;
	    if(to == null){
	        if(other.to != null)
	            return false;
	    }
	    else if(!to.equals(other.to))
	        return false;
	    return true;
	}
	
	

}
