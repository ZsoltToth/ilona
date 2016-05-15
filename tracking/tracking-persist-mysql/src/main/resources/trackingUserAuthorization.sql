create table if not exists TrackingRoles(
	user_role_id int(10) not null auto_increment
	username varchar(45) not null,
	role varchar(10) not null,
	
	primary key (user_role_id)
);

CREATE TABLE if not exists user_roles (
  user_role_id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(45) NOT NULL,
  role varchar(45) NOT NULL,
  PRIMARY KEY (user_role_id),
  UNIQUE KEY uni_username_role (role,username),
  KEY fk_username_idx (username),
  CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (username)
  );