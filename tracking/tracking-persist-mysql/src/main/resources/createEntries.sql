-- add users base details
insert into TrackingUsers values("admin", "adminname", "admin@email.com", "$2a$10$JVcljjbnUkq0CSG3VBoYouk0SgwN1mxE36qkWqATPUyuL/RvXzxMa",
1, now(), now(), 1, now());
insert into TrackingUsers values("user", "username", "user@email.com", "$2a$10$P5j1UrHAg5.hC2ytWwOXQOuH9mD21V.p7fke05UJqteen7W79NN8O",
1, now(), now(), 1, now());

-- add user roles

insert into TrackingUserRoles values("admin", "ROLE_USER");
insert into TrackingUserRoles values("admin", "ROLE_ADMIN");
insert into TrackingUserRoles values("user", "ROLE_USER");

-- login attempts

insert into TrackingLoginAttempts values ("user", now());

-- Add devices

insert into TrackingDevices values ("user", "device1", "userdevice1", "mobile", "type-001");
insert into TrackingDevices values ("user", "device2", "userdevice2", "tablet", "type-002");

-- Add persistent login

insert into persistent_logins values ("user", "series1", "value1", now());

-- Add position

insert into trackinglocations values("device1", "ac0d3531-4681-4254-840b-19332d854a48", now());
insert into trackingpositions values ("ac0d3531-4681-4254-840b-19332d854a48", 5, 10, 15, "ac0d3531-4681-4254-840b-19332d854a10", "zonename1");
