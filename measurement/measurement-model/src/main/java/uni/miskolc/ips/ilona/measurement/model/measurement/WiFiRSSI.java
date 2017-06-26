package uni.miskolc.ips.ilona.measurement.model.measurement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WiFiRSSI {

	private Map<String, Double> rssiValues;

	public WiFiRSSI() {
		super();
		this.rssiValues = new HashMap<String, Double>();
	}

	public WiFiRSSI(Map<String, Double> rssiValues) {
		super();
		this.rssiValues = rssiValues;
	}

	
	public void setRSSI(String ssid, double rssi) {
		this.rssiValues.put(ssid, rssi);
	}

	public double getRSSI(String ssid) {
		return this.rssiValues.get(ssid);
	}

	public void removeSSID(String ssid) {
		this.rssiValues.remove(ssid);
	}

	public Map<String, Double> getRssiValues() {
		return rssiValues;
	}
	
	

	public void setRssiValues(Map<String, Double> rssiValues) {
		this.rssiValues = rssiValues;
	}

	@Override
	public String toString() {
		return "WiFiRSSI rssiValues = " + rssiValues;
	}

	
	
}
