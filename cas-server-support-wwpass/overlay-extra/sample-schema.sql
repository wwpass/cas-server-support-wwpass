CREATE DATABASE wwpass_cas /*!40100 DEFAULT CHARACTER SET utf8 */;

CREATE USER 'cas'@'localhost' IDENTIFIED BY 'changeit';

GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP
	ON wwpass_cas.*
	TO 'cas'@'localhost';

CREATE TABLE wwpass_cas.cas_users (
  userid bigint(20) NOT NULL AUTO_INCREMENT,
  username varchar(50) NOT NULL,
  password varchar(50) NOT NULL,
  active bit(1) NOT NULL,
  PRIMARY KEY (userid),
  UNIQUE KEY username (username)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE TABLE wwpass_cas.user_roles (
  user_role_id int(11) NOT NULL AUTO_INCREMENT,
  uid bigint(20) NOT NULL,
  ROLE varchar(45) NOT NULL,
  PRIMARY KEY (user_role_id),
  UNIQUE KEY uni_username_role (ROLE,uid),
  KEY fk_username_idx (uid),
  CONSTRAINT fk_username FOREIGN KEY (uid) REFERENCES cas_users (userid) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

CREATE TABLE wwpass_cas.wwpass_users (
  puid varchar(50) NOT NULL,
  uid bigint(20) NOT NULL,
  PRIMARY KEY (puid),
  KEY uid (uid),
  CONSTRAINT wwpass_users_ibfk_1 FOREIGN KEY (uid) REFERENCES cas_users (userid) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
