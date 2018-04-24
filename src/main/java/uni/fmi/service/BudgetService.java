package uni.fmi.service;

import uni.fmi.model.Budget;

import java.util.List;

public interface BudgetService {
    boolean createBudget(Budget budget);

    List<Budget> getBudgetsForUser(int userId);

    Budget getBudgetForUserAndMonth(String month, int userId);

    boolean removeBudget(int id);
}
