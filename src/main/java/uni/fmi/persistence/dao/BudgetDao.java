package uni.fmi.persistence.dao;

import uni.fmi.model.Budget;

import java.util.List;

public interface BudgetDao {
    int createBudget(Budget budget);

    List<Budget> getBudgetsForUser(int userId);
    
    List<Budget> getBudgetsForUserAndMonth(int userId, String month);

    boolean removeBudget(int id);
}
