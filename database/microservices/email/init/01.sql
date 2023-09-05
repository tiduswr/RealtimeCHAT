CREATE DATABASE `webchat-email`;

USE `webchat-email`;

DROP TABLE IF EXISTS `tb_email`;
CREATE TABLE `tb_email` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email_from` varchar(255) NOT NULL,
  `email_send_at` datetime(6) NOT NULL,
  `email_status` enum('ERROR','SENT') NOT NULL,
  `email_subject` varchar(255) NOT NULL,
  `email_text` text,
  `email_to` varchar(255) NOT NULL,
  `owner_id` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;