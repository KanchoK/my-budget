package uni.fmi.service.impl;

import uni.fmi.model.MonthlyIncome;
import uni.fmi.persistence.dao.MonthlyIncomeDao;
import uni.fmi.service.MonthlyIncomeService;

import javax.inject.Inject;
import java.util.List;

public class MonthlyIncomeServiceImpl implements MonthlyIncomeService {

    @Inject
    private MonthlyIncomeDao monthlyIncomeDao;

    @Override
    public MonthlyIncome createMonthlyIncome(MonthlyIncome monthlyIncome) {
        MonthlyIncome newMonthlyIncome = monthlyIncomeDao.createMonthlyIncome(monthlyIncome);
        return newMonthlyIncome;
    }

    @Override
    public List<MonthlyIncome> getMonthlyIncomesForUser(int userId) {
        return monthlyIncomeDao.getMonthlyIncomesForUser(userId);
    }

    @Override
    public MonthlyIncome getMonthlyIncomeForUserAndMonth(int userId, String month) {
        return monthlyIncomeDao.getMonthlyIncomeForUserAndMonth(userId, month);
    }

    @Override
    public boolean removeMonthlyIncome(int id) {
        return monthlyIncomeDao.removeMonthlyIncome(id);
    }
}
