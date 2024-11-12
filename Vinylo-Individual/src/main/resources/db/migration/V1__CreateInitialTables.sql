CREATE TABLE artist
(
    id   int         NOT NULL AUTO_INCREMENT,
    name varchar(50) NOT NULL,
    bio  varchar(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (bio)
);

CREATE TABLE vinyl
(
    id          int     NOT NULL AUTO_INCREMENT,
    vinylType   varchar(50) NOT NULL,
    title       varchar(50) NOT NULL,
    description varchar(155) NOT NULL,
    isReleased  BOOLEAN NOT NULL,
    artist_id   int,
    PRIMARY KEY (id),
    UNIQUE (description),
    FOREIGN KEY (artist_id) REFERENCES artist (id)
);
