create table if not exists TrackingLoginAttempts(
	userid varchar(20) not null,
    attempt timestamp(3) not null,
    constraint PK_TrackingLoginAttempts primary key(userid,attempt),
    constraint FK_TrackingLoginAttemptsUserid foreign key(userid) references TrackingUsers(userid)
    on delete cascade
);