package uni.miskolc.ips.ilona.navigation.model;

import java.util.UUID;

import org.jgrapht.graph.DefaultEdge;

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

}
