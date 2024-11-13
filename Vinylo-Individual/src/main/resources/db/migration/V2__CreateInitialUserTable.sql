CREATE TABLE user
(
    id          int     NOT NULL AUTO_INCREMENT,
    username   varchar(50) NOT NULL,
    email       varchar(100) NOT NULL,
    password varchar(250) NOT NULL,
    isPremium  BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (email)
);