CREATE TABLE `bid` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `auction_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `bid_amount` DOUBLE NOT NULL,
  `bid_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`auction_id`) REFERENCES `auction`(`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);