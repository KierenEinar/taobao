CREATE TABLE `account` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `balance` decimal(10,0) unsigned NOT NULL DEFAULT '0',
  `lock_balance` decimal(10,0) unsigned NOT NULL DEFAULT '0',
  `status` varchar(32) NOT NULL DEFAULT 'active',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `account_trade_log` (
  `id` bigint(20) NOT NULL,
  `from_account_id` bigint(20) NOT NULL,
  `to_account_id` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `balance` decimal(10,0) NOT NULL DEFAULT '0',
  `remark` varchar(256) DEFAULT NULL,
  `channel` varchar(256) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `order_id` varchar(128) NOT NULL,
  `status` enum('payment','refund') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`order_id`,`status`) USING BTREE,
  KEY `idx_from_account_id` (`from_account_id`) USING BTREE,
  KEY `idx_to_account_id` (`to_account_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8