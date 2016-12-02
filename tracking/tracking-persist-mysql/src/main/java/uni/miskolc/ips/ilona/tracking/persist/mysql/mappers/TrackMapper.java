package uni.miskolc.ips.ilona.tracking.persist.mysql.mappers;

import java.util.Collection;
import java.util.Date;

import org.apache.ibatis.annotations.Param;

import uni.miskolc.ips.ilona.tracking.persist.mysql.model.TrackPositionMapper;

public interface TrackMapper {

	int storePositionLocation(@Param(value = "deviceid") String deviceid,
			@Param(value = "positionid") String positionid, @Param(value = "time") double time);

	int storePosition(@Param(value = "positionid") String position, @Param("coordx") double coordx,
			@Param("coordy") double coordy, @Param("coordz") double coordz, @Param("zoneid") String zoneid,
			@Param("zoneName") String zoneName);

	TrackPositionMapper getTrackPosition(@Param(value = "positionid") String positionid);

	Collection<TrackPositionMapper> getTrackPositionsInterval(@Param(value = "deviceid") String deviceid,
			@Param(value = "from") Date from, @Param(value = "to") Date to);

	int checkDevice(@Param(value = "deviceid") String deiceid);
}
