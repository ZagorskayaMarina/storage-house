CREATE TABLE IF NOT EXISTS `tokens` (
    `id` int NOT NULL AUTO_INCREMENT,
    `storage_id` int NOT NULL,
    `access_token` varchar(1000) NOT NULL,
    `created_date` timestamp NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `tokens_to_stores` FOREIGN KEY(`storage_id`) REFERENCES `stores`(`id`)
    );