create table of not exists TrackingUsers (
	userid varchar(45) primary key, -- arbitrary username
	password varchar(100) not null, -- hashed password
	enabled TINYINT(1) not null default 1, -- is this account enabled
	joinDate timestamp -- join date
);

