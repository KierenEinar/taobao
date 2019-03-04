alter table users add column user_id bigint not null;
alter table users add column phone varchar(128);
alter table users add index (phone);
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

