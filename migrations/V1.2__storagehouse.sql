ALTER TABLE `stores`
ADD COLUMN `password` varchar (250) NOT NULL AFTER `email`,
ADD COLUMN `salt` varchar (250) NOT NULL AFTER `password`;