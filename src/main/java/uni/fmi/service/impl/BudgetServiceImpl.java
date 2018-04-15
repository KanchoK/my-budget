package uni.fmi.service.impl;

import uni.fmi.model.Budget;
import uni.fmi.persistence.dao.BudgetDao;
import uni.fmi.service.BudgetService;

import javax.inject.Inject;

public class BudgetServiceImpl implements BudgetService {

    @Inject
    private BudgetDao budgetDao;

    @Override
    public boolean createBudget(Budget budget) {
        int budgetId = budgetDao.createBudget(budget);
        return budgetId > 0;
    }

    @Override
    public Budget getBudgetForUserAndMonth(String month, int userId) {
        return budgetDao.getBudgetForMonthAndUserId(month, userId);
    }

    @Override
    public boolean removeBudget(int id) {
        return budgetDao.removeBudget(id);
    }
}