package uni.fmi.service;

import uni.fmi.model.MonthlyIncome;

import java.util.List;

public interface MonthlyIncomeService {
    
    MonthlyIncome createMonthlyIncome(MonthlyIncome monthlyIncome);

    List<MonthlyIncome> getMonthlyIncomesForUser(int userId);

    MonthlyIncome getMonthlyIncomeForUserAndMonth(int userId, String month);

    boolean removeMonthlyIncome(int id);
}
