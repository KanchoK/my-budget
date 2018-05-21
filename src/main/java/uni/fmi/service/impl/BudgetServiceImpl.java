package uni.fmi.service.impl;

import uni.fmi.model.Budget;
import uni.fmi.persistence.dao.BudgetDao;
import uni.fmi.service.BudgetService;

import javax.inject.Inject;
import java.util.List;

public class BudgetServiceImpl implements BudgetService {

    @Inject
    private BudgetDao budgetDao;

    @Override
    public boolean createBudget(Budget budget) {
        int budgetId = budgetDao.createBudget(budget);
        return budgetId > 0;
    }

    @Override
    public List<Budget> getBudgetsForUser(int userId) {
        return budgetDao.getBudgetsForUser(userId);
    }

    @Override
    public Budget getBudgetForId(int budgetId){
        return budgetDao.getBudgetForId(budgetId);
    }
    
    @Override
    public List<Budget> getBudgetsForUserAndMonth(int userId, String month) {
        return budgetDao.getBudgetsForUserAndMonth(userId, month);
    }
    
    @Override
    public Budget copyBudgetForUserBudgetAndMonth(int userId, int budgetId, String month){
        return budgetDao.copyBudgetForUserBudgetAndMonth(userId, budgetId, month);
    }

    @Override
    public boolean removeBudget(int id) {
        return budgetDao.removeBudget(id);
    }
}
