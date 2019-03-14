CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `first_name` varchar(32) DEFAULT NULL,
  `last_name` varchar(32) DEFAULT NULL,
  `member_id` bigint(20) NOT NULL,
  `credit_id` bigint(20) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL,
  `gender` enum('男','女') DEFAULT '男',
  `user_id` bigint(20) NOT NULL,
  `phone` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `phone` (`phone`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8

drop table if EXISTS user_receive_config ;
create table `user_receive_config` (
	id bigint not null primary key
	user_id bigint(20) not null,
	receiver_name varchar(64) not null,
	receiver_phone varchar(128) not null,
	receiver_detail varchar (256) not null,
	create_time datetime not null default now(),
	update_time datetime,
	key(`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

