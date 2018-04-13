package uni.fmi.persistence.dao;

import uni.fmi.model.Budget;

public interface BudgetDao {
    int createBudget(Budget budget);

    Budget getBudgetForMonthAndUserId(String month, int userId);

    boolean removeBudget(int id);
}
