CREATE DATABASE `webchat-image`;

USE `webchat-image`;

DROP TABLE IF EXISTS `profile_image`;
CREATE TABLE `profile_image` (
  `id` binary(16) NOT NULL,
  `username` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;