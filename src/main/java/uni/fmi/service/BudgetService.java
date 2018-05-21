package uni.fmi.service;

import uni.fmi.model.Budget;

import java.util.List;

public interface BudgetService {
    boolean createBudget(Budget budget);

    List<Budget> getBudgetsForUser(int userId);
    
    Budget getBudgetForId(int budgetId);

    List<Budget> getBudgetsForUserAndMonth(int userId, String month);
    
    Budget copyBudgetForUserBudgetAndMonth(int userId, int budgetId, String month);

    boolean removeBudget(int id);
}
