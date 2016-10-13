package uni.miskolc.ips.ilona.tracking.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * This class represent the device details in the tracking module. <br />
 * 
 * Contains every details(except the ownerid) what is needed to manage the
 * current device.
 * 
 * @author Patrik / A5USL0
 *
 */
public class DeviceData implements Serializable, Comparable<DeviceData>, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This id is unique amongst the devices.
	 */
	private String deviceid;

	/**
	 * An arbitrary device name,, given by the user.
	 */
	private String deviceName;

	/**
	 * An arbitrary devicetype, given by the user. <br />
	 * Example: mobile, tablet, etc.
	 */
	private String deviceType;

	/**
	 * An arbitrary device type name. <br/>
	 * Example: Manufacturer-type-series. Brand-Serices HD555
	 */
	private String deviceTypeName;

	/**
	 * Parameterless constructor for serialization, cloning and others.
	 */
	public DeviceData() {

	}

	/**
	 * Parameter with the details.
	 * 
	 * @param deviceid
	 *            A unique id. This id is unique amongst the devices.
	 * @param deviceName
	 *            An arbitrary device name.
	 * @param deviceType
	 *            An arbitrary devicetype, given by the user. <br />
	 *            Example: mobile, tablet, etc.
	 * @param deviceTypeName
	 *            An arbitrary device type name. <br/>
	 *            Example: Manufacturer-type-series. Brand-Services HD555
	 */
	public DeviceData(String deviceid, String deviceName, String deviceType, String deviceTypeName) {
		super();
		this.deviceid = deviceid;
		this.deviceName = deviceName;
		this.deviceType = deviceType;
		this.deviceTypeName = deviceTypeName;
	}

	/**
	 * This id is unique amongst the devices.
	 * 
	 * @return The current deviceid.
	 */
	public String getDeviceid() {
		return deviceid;
	}

	/**
	 * This id is unique amongst the devices.
	 * 
	 * @param deviceid
	 *            The new device id.
	 */
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	/**
	 * An arbitrary device name, given by the user.
	 * 
	 * @return The current device name value.
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * An arbitrary device name, given by the user.
	 * 
	 * @param deviceName
	 *            The new device name value.
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * An arbitrary devicetype, given by the user. <br />
	 * Example: mobile, tablet, etc.
	 * 
	 * @return The current device type value.
	 */
	public String getDeviceType() {
		return deviceType;
	}

	/**
	 * An arbitrary devicetype, given by the user. <br />
	 * Example: mobile, tablet, etc.
	 * 
	 * @param deviceType
	 *            The new devie type value.
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	/**
	 * An arbitrary device type name. <br/>
	 * Example: Manufacturer-type-series. Brand-Services HD555
	 * 
	 * @return The current device type name.
	 */
	public String getDeviceTypeName() {
		return deviceTypeName;
	}

	/**
	 * An arbitrary device type name. <br/>
	 * Example: Manufacturer-type-series. Brand-Services HD555
	 * 
	 * @param deviceTypeName
	 *            The new device type name value.
	 */
	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}

	@Override
	public String toString() {
		return "DeviceData [deviceid=" + deviceid + ", deviceName=" + deviceName + ", deviceType=" + deviceType
				+ ", deviceTypeName=" + deviceTypeName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deviceName == null) ? 0 : deviceName.hashCode());
		result = prime * result + ((deviceType == null) ? 0 : deviceType.hashCode());
		result = prime * result + ((deviceTypeName == null) ? 0 : deviceTypeName.hashCode());
		result = prime * result + ((deviceid == null) ? 0 : deviceid.hashCode());
		return result;
	}

	/**
	 * Two device objects are equal, if the deviceids of the devices are equal!
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeviceData other = (DeviceData) obj;
		if (deviceName == null) {
			if (other.deviceName != null)
				return false;
		} else if (!deviceName.equals(other.deviceName))
			return false;
		if (deviceType == null) {
			if (other.deviceType != null)
				return false;
		} else if (!deviceType.equals(other.deviceType))
			return false;
		if (deviceTypeName == null) {
			if (other.deviceTypeName != null)
				return false;
		} else if (!deviceTypeName.equals(other.deviceTypeName))
			return false;
		if (deviceid == null) {
			if (other.deviceid != null)
				return false;
		} else if (!deviceid.equals(other.deviceid))
			return false;
		return true;
	}

	/**
	 * Creates a shallow copy.
	 * 
	 * @return
	 */
	public DeviceData shallowCopy() {
		try {
			return (DeviceData) this.clone();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Creates a deep copy.
	 * 
	 * @return
	 */
	public DeviceData deepCopy() {
		ByteArrayOutputStream bos = null;
		ObjectOutputStream out = null;
		ObjectInputStream ois = null;
		ByteArrayInputStream bis = null;
		DeviceData deviceData = null;

		try {
			/*
			 * Serialization part.
			 */
			bos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bos);
			out.writeObject(this);
			out.flush();
			// out.close();

			/*
			 * Deserialization part.
			 */
			bis = new ByteArrayInputStream(bos.toByteArray());
			ois = new ObjectInputStream(bis);
			deviceData = (DeviceData) ois.readObject();

		} catch (Exception e) {
			deviceData = null;

		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception a) {

				}
			}

			if (out != null) {
				try {
					out.close();
				} catch (Exception a) {

				}
			}

			if (ois != null) {
				try {
					ois.close();
				} catch (Exception a) {

				}
			}

			if (bis != null) {
				try {
					bis.close();
				} catch (Exception a) {

				}
			}
		}

		return deviceData;
	}

	@Override
	public int compareTo(DeviceData o) {
		if (o == null) {
			return 1;
		}

		if (!(o instanceof DeviceData)) {
			return 1;
		}
		return this.deviceid.compareTo(o.deviceid);
	}

}
