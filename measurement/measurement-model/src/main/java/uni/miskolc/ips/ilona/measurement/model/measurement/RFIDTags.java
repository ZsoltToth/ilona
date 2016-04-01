package uni.miskolc.ips.ilona.measurement.model.measurement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RFIDTags {

	private Set<byte[]> tags;
	private static final Logger LOG= LogManager.getLogger(RFIDTags.class);

	public RFIDTags() {
		super();
	}

	public RFIDTags(Set<byte[]> tags) {
		super();
		this.tags = tags;
	}

	public Set<byte[]> getTags() {
		return tags;
	}

	public void setTags(Set<byte[]> tags) {
		this.tags = tags;
	}

	public void addTag(byte[] tag) {
		this.tags.add(tag);
	}

	public void removeTag(byte[] tag) {
		this.tags.remove(tag);
	}

	public double distance(RFIDTags other) {
		double result;

		if (this.getTags().isEmpty() && other.getTags().isEmpty()) {
			return 0.0;
		}
		if (this.getTags().isEmpty() || other.getTags().isEmpty()) {
			return 1.0;
		}

		Set<byte[]> intersection = this.intersection(other);
		Set<byte[]> union = this.union(other);


		result = 1 - ((double) intersection.size() / (double) union.size());
		LOG.info(String.format("Distance between %s and %s is %f",
				this.toString(), other.toString(), result));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RFIDTags == false) {
			return false;
		}
		return this.tags.equals(((RFIDTags) obj).tags);
	}

	protected Set<byte[]> union(RFIDTags other) {
		Set<byte[]> union = new HashSet<byte[]>();
		union.addAll(this.intersection(other));

		for (byte[] thiseach : this.tags) {
			for (byte[] each : other.tags) {
				if (!Arrays.equals(thiseach, each)) {

					if (this.contain(union, each)
							&& this.contain(union, thiseach)) {
						continue;
					} else if (this.contain(union, each)) {
						union.add(thiseach);
					} else if (this.contain(union, thiseach)) {
						union.add(each);
					} else {
						union.add(thiseach);
						union.add(each);
					}
				}

			}
		}
		return union;

	}

	protected Set<byte[]> intersection(RFIDTags other) {
		Set<byte[]> intersection = new HashSet<byte[]>();
		for (byte[] thiseach : this.tags) {
			for (byte[] each : other.tags) {
				if (Arrays.equals(thiseach, each)) {
					intersection.add(each);
				}
			}
		}
		return intersection;
	}

	private boolean contain(Set<byte[]> set, byte[] in) {
		for (byte[] each : set) {
			if (Arrays.equals(each, in)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		String result = "RFIDTags =";
		int i = 1, j = 1;
		for (byte[] each : tags) {
			j = 1;
			result = result + "[";
			for (byte eachbyte : each) {
				result = result + eachbyte;
				if (j < each.length) {
					result = result + ",";
				}
				j++;
			}

			result = result + "]";
			if (i < tags.size()) {
				result = result + ";";
			}
			i++;
		}
		return result;

	}

}
