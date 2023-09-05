CREATE DATABASE `webchat-message`;

USE `webchat-message`;

DROP TABLE IF EXISTS `messages`;
CREATE TABLE `messages` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `message` text NOT NULL,
  `readed` bit(1) DEFAULT NULL,
  `status` tinyint NOT NULL,
  `receiver` varchar(30) DEFAULT NULL,
  `sender` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;