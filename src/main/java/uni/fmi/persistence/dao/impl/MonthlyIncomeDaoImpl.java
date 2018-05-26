package uni.fmi.persistence.dao.impl;

import java.math.BigDecimal;
import org.apache.log4j.Logger;
import uni.fmi.model.MonthlyIncome;
import uni.fmi.model.User;
import uni.fmi.persistence.DatabaseManager;

import javax.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import uni.fmi.persistence.dao.MonthlyIncomeDao;

public class MonthlyIncomeDaoImpl implements MonthlyIncomeDao {

    private static final Logger LOG = Logger.getLogger(MonthlyIncomeDaoImpl.class);

    @Inject
    private DatabaseManager databaseManager;

    private static final String ADD_MONTHLY_INCOME_STATEMENT = "INSERT INTO monthly_incomes(monthlyIncome, validForMonth, userId) " +
                                                "VALUES (?, ?, ?)";
    private static final String GET_MONTHLY_INCOMES_STATEMENT =
            "SELECT m.id, m.monthlyIncome, m.validForMonth, u.id, u.username " +
                    "FROM monthly_incomes AS m " +
                    "INNER JOIN users AS u " +
                    "ON m.userId = u.id " +
                    "WHERE m.userId = ?";
    private static final String GET_MONTHLY_INCOME_STATEMENT =
            "SELECT m.id, m.monthlyIncome, m.validForMonth, u.id, u.username " +
                "FROM monthly_incomes AS m " +
                "INNER JOIN users AS u " +
                "ON m.userId = u.id " +
                "WHERE m.validForMonth = ? AND m.userId = ?";
    private static final String REMOVE_MONTHLY_INCOME_STATEMENT = "DELETE FROM monthly_incomes WHERE id=?";


    @Override
    public MonthlyIncome createMonthlyIncome(MonthlyIncome monthlyIncome) {
        int monthlyIncomeId = -1;

        try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(ADD_MONTHLY_INCOME_STATEMENT, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setBigDecimal(1, monthlyIncome.getMonthlyIncome());
            preparedStatement.setString(2, monthlyIncome.getValidForMonth());
            preparedStatement.setInt(3, monthlyIncome.getUser().getId());
            preparedStatement.executeUpdate();

            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                rs.next();
                monthlyIncomeId = rs.getInt(1);
            }
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }
        
        monthlyIncome.setId(monthlyIncomeId);
        return monthlyIncome;
    }

    @Override
    public List<MonthlyIncome> getMonthlyIncomesForUser(int userId) {
        List<MonthlyIncome> monthlyIncomes = new ArrayList<>();

        try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(GET_MONTHLY_INCOMES_STATEMENT)) {

            preparedStatement.setInt(1, userId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    monthlyIncomes.add(buildMonthlyIncomeFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }

        return monthlyIncomes;
    }

    @Override
    public MonthlyIncome getMonthlyIncomeForUserAndMonth(int userId, String month) {
        MonthlyIncome monthlyIncome = null;

        try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(GET_MONTHLY_INCOME_STATEMENT)) {

            preparedStatement.setString(1, month);
            preparedStatement.setInt(2, userId);           
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    monthlyIncome = buildMonthlyIncomeFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }
        
//        if (monthlyIncome == null) {
//            monthlyIncome = new MonthlyIncome(0, BigDecimal.ZERO, "", new User());
//        }

        return monthlyIncome;
    }

    @Override
    public boolean removeMonthlyIncome(int id) {
        try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(REMOVE_MONTHLY_INCOME_STATEMENT)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
            return false;
        }
        return true;
    }

    private MonthlyIncome buildMonthlyIncomeFromResultSet(ResultSet rs) throws SQLException {
        User user = new User(rs.getInt("u.id"),
                rs.getString("u.username"));

        return new MonthlyIncome(rs.getInt("m.id"),
                rs.getBigDecimal("m.monthlyIncome"),
                rs.getString("m.validForMonth"),
                user);
    }
}
