package uni.miskolc.ips.ilona.tracking.service;

/**
 * Interface segregation union interface.<br/>
 * Implementations: <br/>
 * {@link UserService} <br/>
 * {@link DeviceService}
 * 
 * @author Patrik
 *
 */
public interface UserAndDeviceService extends UserService, DeviceService {

}