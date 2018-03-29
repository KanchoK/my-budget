CREATE DATABASE IF NOT EXISTS my_budget CHARACTER SET utf8 COLLATE utf8_general_ci;

grant all privileges on my_budget.* to mb_user@localhost identified by 'pass';
flush privileges;