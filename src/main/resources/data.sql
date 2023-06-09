DROP TABLE IF EXISTS `refresh_tokens`;
CREATE TABLE `refresh_tokens` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_mapping_id` bigint(20) NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_mapping_id_token` (`user_mapping_id`,`token`) USING BTREE,
  KEY `idx_token` (`token`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tax_information`;
CREATE TABLE `tax_information`
(
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_mapping_id` bigint(20) NOT NULL,
  `calculated_tax` bigint(20) NOT NULL,
  `insurance_premium` bigint(20) NOT NULL,
  `education_expense` bigint(20) NOT NULL,
  `donation` bigint(20) NOT NULL,
  `medical_expense` bigint(20) NOT NULL,
  `retirement_pension` bigint(20) NOT NULL,
  `total_salary` bigint(20) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_mapping_id` (`user_mapping_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles` (
  `user_mapping_id` bigint(20) NOT NULL,
  `roles` varchar(255) NOT NULL,
  PRIMARY KEY (`user_mapping_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) NOT NULL,
  `gender` varchar(50) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `reg_no` varchar(255) NOT NULL,
  `nickname` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone_number` varchar(50) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
