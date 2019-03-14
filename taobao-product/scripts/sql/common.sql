CREATE TABLE `product` (
  `product_id` bigint(20) NOT NULL,
  `name` varchar(512) NOT NULL,
  `title` varchar(512) NOT NULL,
  `html` text,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL,
  `status` enum('creating','sale','updating','shelves') DEFAULT NULL,
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品类别表';

CREATE TABLE `product_params_item` (
  `id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `param_data` text NOT NULL,
  `product_specs_id` bigint(20) NOT NULL,
  `type` enum('params','info') NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品参数';

CREATE TABLE `product_params_template` (
  `id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `template_json` text NOT NULL,
  `type` enum('params','info') NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品参数模板';

CREATE TABLE `product_specs` (
  `id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `attrs` varchar(128) NOT NULL,
  `stock` int(10) unsigned NOT NULL DEFAULT '0',
  `price` decimal(20,2) NOT NULL,
  `lock_num` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `product_specs_attribute_key` (
  `id` bigint(20) NOT NULL,
  `name` varchar(512) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品属性名称';

CREATE TABLE `product_specs_attribute_value` (
  `id` bigint(20) NOT NULL,
  `value` varchar(512) NOT NULL,
  `attr_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品属性值';

CREATE TABLE `product_specs_info` (
  `id` bigint(20) NOT NULL,
  `name` varchar(512) NOT NULL,
  `value` varchar(512) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `product_specs_id` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品详情参数';

CREATE TABLE `product_specs_param` (
  `id` bigint(20) NOT NULL,
  `name` varchar(512) NOT NULL,
  `value` varchar(512) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `product_specs_id` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品规格参数';

