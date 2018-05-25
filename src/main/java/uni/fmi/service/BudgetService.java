package uni.fmi.service;

import java.math.BigDecimal;
import uni.fmi.model.Budget;

import java.util.List;

public interface BudgetService {
    boolean createBudget(Budget budget);

    List<Budget> getBudgetsForUser(int userId);
    
    Budget getBudgetForId(int budgetId);

    List<Budget> getBudgetsForUserAndMonth(int userId, String month);
    
    Budget copyBudgetForUserBudgetAndMonth(int userId, int budgetId, String month);
    
    BigDecimal getBudgetsPlannedAmountForUserAndMonth(int userId, String month);
         
    BigDecimal getBudgetsSpentAmountForUserAndMonth(int userId, String month);

    boolean removeBudget(int id);
}
