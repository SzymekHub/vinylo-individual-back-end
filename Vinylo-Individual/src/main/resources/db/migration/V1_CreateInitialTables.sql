CREATE TABLE vinyl
(
    id   int     NOT NULL AUTO_INCREMENT,
    vinylType varchar(50) NOT NULL,
    title varchar(50) NOT NULL,
    description varchar(50) NOT NULL,
    isReleased varchar(50) NOT NULL,
    Artist varchar(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
);
