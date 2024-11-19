CREATE TABLE auction (
    id INT NOT NULL AUTO_INCREMENT,
    title VARCHAR(50) NOT NULL,
    vinyl_id INT,
    user_id INT,
    description VARCHAR(155),
    startingPrice DOUBLE NOT NULL,
    currentPrice DOUBLE NOT NULL,
    startTime DATE NOT NULL,
    endTime DATE NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (vinyl_id) REFERENCES vinyl(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);
