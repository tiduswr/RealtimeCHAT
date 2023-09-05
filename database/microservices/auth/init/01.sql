CREATE DATABASE `webchat-auth`;

USE `webchat-auth`;

DROP TABLE IF EXISTS `refresh_tokens`;
CREATE TABLE `refresh_tokens` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `token` varchar(200) NOT NULL,
  `username` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `tb_recover_password_code`;
CREATE TABLE `tb_recover_password_code` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(36) DEFAULT NULL,
  `username` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;