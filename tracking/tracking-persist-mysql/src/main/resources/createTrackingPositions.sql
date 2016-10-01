create table if not exists trackingpositions (
    posid varchar(40),
    coordx double,
    coordy double,
    coordz double,
    zoneid varchar(40),
    zonename varchar(30),
    constraint PK_trackingpositions primary key(posid),
    constraint FK_trackingpositions foreign key(posid) references trackinglocations(positionid) on delete cascade
)