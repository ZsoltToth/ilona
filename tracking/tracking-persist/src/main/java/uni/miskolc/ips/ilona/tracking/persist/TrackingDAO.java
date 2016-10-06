package uni.miskolc.ips.ilona.tracking.persist;

import java.util.Collection;
import java.util.Date;

import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.tracking.model.DeviceData;
import uni.miskolc.ips.ilona.tracking.model.TrackPosition;
import uni.miskolc.ips.ilona.tracking.model.UserData;
import uni.miskolc.ips.ilona.tracking.persist.exception.OperationExecutionErrorException;
import uni.miskolc.ips.ilona.tracking.persist.exception.PositionAlreadyExistsException;
import uni.miskolc.ips.ilona.tracking.persist.exception.PositionNotFoundException;

public interface TrackingDAO {

	void storePosition(DeviceData device, Position position)
			throws PositionAlreadyExistsException, OperationExecutionErrorException;

	Collection<TrackPosition> restorePositionsInterval(DeviceData device, Date from, Date to)
			throws OperationExecutionErrorException;

	TrackPosition readPosition(String positionid) throws PositionNotFoundException, OperationExecutionErrorException;
}
