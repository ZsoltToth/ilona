create table if not exists trackinglocations(
	deviceid varchar(50),
    positionid varchar(40),
    meastime timestamp(3),
    constraint PK_trackinglocations primary key(positionid),
    constraint FK_trackinglocations foreign key(deviceid) references trackingdevices(deviceid) on delete cascade
)