USE my_budget;

DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS budgets;
DROP TABLE IF EXISTS monthly_incomes;
DROP TABLE IF EXISTS users;


CREATE TABLE users (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(60) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table monthly_incomes
(
	id int auto_increment
		primary key,
	monthlyIncome decimal not null,
	validForMonth varchar(15) not null,
	userId int not null,
	constraint userId_validForMonth_UNIQUE
		unique (userId, validForMonth),
	constraint user_monthly_incomes
		foreign key (userId) references my_budget.users (id)
)
;

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
	constraint user_budgets
		foreign key (userId) references my_budget.users (id)
)
;

create table categories
(
	id int auto_increment
		primary key,
	name varchar(50) not null,
	plannedAmount decimal null,
	spentAmount decimal null,
	budgetId int not null,
	constraint name_UNIQUE
		unique (name),
	constraint budget
		foreign key (budgetId) references my_budget.budgets (id)
)
;

create table payments
(
	id int auto_increment
		primary key,
	title varchar(50) not null,
    `comment`  varchar(150) null,
	`date` varchar(15) not null,
	amount decimal not null,
	categoryId int not null,
	constraint title_UNIQUE
		unique (title),
	constraint category
		foreign key (categoryId) references my_budget.categories (id)
)
;

create index id_idx
	on budgets (userId)
;

create index id_idx
	on monthly_incomes (userId)
;

create index id_idx
	on categories (budgetId)
;

create index id_idx
	on payments (categoryId)
;

