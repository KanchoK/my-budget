USE my_budget;

DROP TABLE IF EXISTS budgets;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(60) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table budgets
(
	id int auto_increment
		primary key,
	name varchar(50) not null,
	plannedAmount decimal null,
	spentAmount decimal null,
	validForMonth varchar(15) null,
	userId int not null,
	constraint name_UNIQUE
		unique (name),
	constraint user
		foreign key (userId) references my_budget.users (id)
)
;

create index id_idx
	on budgets (userId)
;

