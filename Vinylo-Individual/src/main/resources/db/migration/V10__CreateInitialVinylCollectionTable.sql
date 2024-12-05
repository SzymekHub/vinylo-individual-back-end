CREATE TABLE vinyl_collection (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT NOT NULL,
    vinyl_id    INT NOT NULL,
    UNIQUE KEY unique_user_vinyl (user_id, vinyl_id),
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
    FOREIGN KEY (vinyl_id) REFERENCES vinyl (id) ON DELETE CASCADE
);
