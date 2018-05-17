INSERT INTO users (username, `password`)
VALUES ("testUser", "1");

INSERT INTO monthly_incomes (monthlyIncome, validForMonth, userId)
VALUES (1200, "05-2018", 1),
       (1400, "06-2018", 1);

INSERT INTO budgets (`name`, plannedAmount, validForMonth, userId)
VALUES ("Main Budget", 500, "05-2018", 1),
	   ("Holiday Budget", 500, "05-2018", 1);

INSERT INTO categories (`name`, plannedAmount, budgetId)
VALUES ("Food", 100.50, 1),
	   ("Drinks", 50.50, 1),
	   ("Hotel", 100, 2),
	   ("Food", 100, 2);

INSERT INTO payments (title, `date`, `comment`, amount, categoryId)
VALUES ("Pizza Margarita", "05-02-2018", "from Pizza Victoria", 11.90, 1),
       ("Shopska Salad", "05-02-2018", "from Happy", 2.90, 1),
       ("Beer Shumensko", "05-03-2018", "500 ml, pretty expensive", 5.30, 2),
       ("Coca-Cola", "05-03-2018", "500 ml", 2.50, 2),
	   ("Hotel Paradise", "05-10-2018", "for 3 day and for 2 people", 150, 2),
       ("Tarator", "from Tavern Misho, pretty expensive", "05-11-2018", 4.90, 1);

