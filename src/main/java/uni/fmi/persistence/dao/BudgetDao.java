package uni.fmi.persistence.dao;

import java.math.BigDecimal;
import uni.fmi.model.Budget;

import java.util.List;

public interface BudgetDao {
    Budget createBudget(Budget budget);

    List<Budget> getBudgetsForUser(int userId);
    
    List<Budget> getBudgetsForUserAndMonth(int userId, String month);
    
    Budget getBudgetForId(int budgetId);
    
    Budget copyBudgetForUserBudgetAndMonth(int userId,int budgetId, String month);
    
    BigDecimal getBudgetsPlannedAmountForUserAndMonth(int userId, String month);
    
    BigDecimal getBudgetsSpentAmountForUserAndMonth(int userId, String month);

    boolean removeBudget(int id);
}
