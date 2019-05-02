CREATE TABLE `user` (
  `id` varchar(10) NOT NULL,
  `name` varchar(20) NOT NULL,
  `password` varchar(1000) NOT NULL,
  `level` tinyint(2) DEFAULT NULL,
  `login` int(8) DEFAULT NULL,
  `recommend` int(8) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `recid` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `confirm` (
  `id` varchar(10) NOT NULL,
  `confirm_date` int(8) NOT NULL,
  `confirm_seq` int(3) NOT NULL,
  `confirm_time` varchar(6) DEFAULT NULL,
  `content` longtext DEFAULT NULL,
  `solve_content` longtext DEFAULT NULL,
  `checkflagad` varchar(1) DEFAULT 'N',
  `checkflagus` varchar(1) DEFAULT 'N',
  `solve_timestamp` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`,`confirm_date`,`confirm_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `board` (
  `board_no` int(8) not null auto_increment PRIMARY KEY,
  `writer_id` varchar(10) NOT NULL,  
  `content` longtext DEFAULT NULL,
  `board_gubun` int(1) Default 1 NOT NULL ,  
  `write_time` timestamp ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;