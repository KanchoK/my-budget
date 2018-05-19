INSERT INTO users (username, `password`)
VALUES ("testUser", "123");

INSERT INTO monthly_incomes (monthlyIncome, validForMonth, userId)
VALUES (1200, "05-2018", 1),
       (1400, "06-2018", 1);

INSERT INTO budgets (`name`, plannedAmount, spentAmount, validForMonth, userId)
VALUES ("Main Budget", 500, 22.60, "05-2018", 1),
	   ("Holiday Budget", 500, 154.90, "05-2018", 1);

INSERT INTO categories (`name`, plannedAmount, spentAmount, budgetId)
VALUES ("Food", 100.50, 14.80, 1),
	   ("Drinks", 50.50, 7.80, 1),
	   ("Hotel", 300, 150, 2),
	   ("Food", 100, 4.90, 2);

INSERT INTO payments (title, `date`, `comment`, amount, categoryId)
VALUES ("Pizza Margarita", "05-02-2018", "from Pizza Victoria", 11.90, 1),
       ("Shopska Salad", "05-02-2018", "from Happy", 2.90, 1),
       ("Beer Shumensko", "05-03-2018", "500 ml, pretty expensive", 5.30, 2),
       ("Coca-Cola", "05-03-2018", "500 ml", 2.50, 2),
	   ("Hotel Paradise", "05-10-2018", "for 3 day and for 2 people", 150, 2),
       ("Tarator", "05-11-2018", "from Tavern Misho, pretty expensive", 4.90, 1);
