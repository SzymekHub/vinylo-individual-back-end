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
    speed       varchar(50) NOT NULL,
    title       varchar(50) NOT NULL,
    description varchar(155) NOT NULL,
    state       varchar(50) NOT NULL,
    color       varchar(50) NOT NULL,
    isReleased  BOOLEAN NOT NULL,
    artist_id   int,
    PRIMARY KEY (id),
    UNIQUE (description),
    FOREIGN KEY (artist_id) REFERENCES artist (id)
);


INSERT INTO artist (id, name, bio) VALUES (1, 'Test Artist', 'Test BIO');
INSERT INTO artist (id, name, bio) VALUES (2, 'Test Artist2', 'Test BIO2');

INSERT INTO vinyl (id, vinylType, speed, title, description, state, color, isReleased, artist_id)
VALUES (1,'LP_12_INCH', 'RPM_45', 'Test Vinyl', 'Test Description', 'NEW', 'COLORED', true, 1);

INSERT INTO vinyl (id, vinylType, speed, title, description, state, color, isReleased, artist_id)
VALUES (2,'EP', 'RPM_33_1_3', 'Test Vinyl2', 'Test Description2', 'REMASTERED', 'BLACK', false, 2);
