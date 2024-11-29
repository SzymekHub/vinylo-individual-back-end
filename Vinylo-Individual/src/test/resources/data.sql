CREATE TABLE IF NOT EXISTS artist
(
    id   int         NOT NULL AUTO_INCREMENT,
    name varchar(50) NOT NULL,
    bio  varchar(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (bio)
);

CREATE TABLE IF NOT EXISTS vinyl
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


INSERT INTO artist (id, name, bio) VALUES (1, 'Test Artist', 'Test BIO');
INSERT INTO artist (id, name, bio) VALUES (2, 'Test Artist2', 'Test BIO2');

INSERT INTO vinyl (id, title, description, isReleased, vinylType, artist_id)
VALUES (1, 'Test Vinyl', 'Test Description', true, 'LP', 1);

INSERT INTO vinyl (id, title, description, isReleased, vinylType, artist_id)
VALUES (2, 'Test Vinyl2', 'Test Description2', false, 'EP', 2);