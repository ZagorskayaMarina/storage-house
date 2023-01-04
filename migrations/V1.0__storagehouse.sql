CREATE TABLE IF NOT EXISTS `stores` (
    `id` int not null auto_increment,
    `name` varchar(500) not null unique,
    `domain` varchar(250),
    `owner` varchar(250) not null,
    `email` varchar(100) not null,
    `country` varchar(100) not null,
    `province` varchar(100),
    `city` varchar(100) not null,
    `address` varchar(500) not null,
    `zip_code` varchar(50) not null,
    `currency` varchar(250) not null,
    `status` enum('ACTIVE', 'DELETED') not null,
    `created_at` datetime not null,
    `modified_at` datetime,
    PRIMARY KEY (`id`)
    );