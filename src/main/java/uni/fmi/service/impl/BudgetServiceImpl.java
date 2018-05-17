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
    public List<Budget> getBudgetsForUserAndMonth(int userId, String month) {
        return budgetDao.getBudgetsForUserAndMonth(userId, month);
    }

    @Override
    public boolean removeBudget(int id) {
        return budgetDao.removeBudget(id);
    }
}
