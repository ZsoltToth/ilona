create table if not exists Trackings (
	trackingID int auto increment,
	positionID varchar(40)
	trackDate timestamp,
	primary key (trackingID),
	foreign key (positionID) references Position(posID)
);