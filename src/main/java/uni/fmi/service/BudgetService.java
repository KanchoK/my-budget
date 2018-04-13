package uni.fmi.service;

import uni.fmi.model.Budget;

public interface BudgetService {
    boolean createBudget(Budget budget);

    Budget getBudgetForUserAndMonth(String month, int userId);

    boolean removeBudget(int id);
}
