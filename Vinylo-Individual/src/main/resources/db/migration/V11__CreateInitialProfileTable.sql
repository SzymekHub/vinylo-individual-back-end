CREATE TABLE profile
(
    id          int     NOT NULL AUTO_INCREMENT,
    user_id     int     NOT NULL,
    bio   varchar(500),
    balance       int,
   PRIMARY KEY (id),
   UNIQUE (user_id),
   FOREIGN KEY (user_id) REFERENCES user (id)
);