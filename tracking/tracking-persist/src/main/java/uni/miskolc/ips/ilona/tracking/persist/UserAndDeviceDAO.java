package uni.miskolc.ips.ilona.tracking.persist;

/**
 * Interface segregation union interface.<br/>
 * Interfaces:<br/>
 * {@link UserManagementDAO} and {@link DeviceManagementDAO}
 * 
 * @author Patrik / A5USL0
 *
 */
public interface UserAndDeviceDAO extends UserManagementDAO, DeviceManagementDAO {

}
