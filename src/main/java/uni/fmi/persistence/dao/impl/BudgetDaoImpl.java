package uni.fmi.persistence.dao.impl;

import org.apache.log4j.Logger;
import uni.fmi.model.Budget;
import uni.fmi.model.User;
import uni.fmi.persistence.DatabaseManager;
import uni.fmi.persistence.dao.BudgetDao;

import javax.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetDaoImpl implements BudgetDao {

    private static final Logger LOG = Logger.getLogger(BudgetDaoImpl.class);

    @Inject
    private DatabaseManager databaseManager;

    private static final String ADD_BUDGET_STATEMENT = "INSERT INTO budgets(name, validForMonth, userId) " +
                                                "VALUES (?, ?, ?)";
    private static final String GET_BUDGETS_STATEMENT =
            "SELECT b.id, b.name, b.plannedAmount, b.spentAmount, b.validForMonth, u.id, u.username " +
                    "FROM budgets AS b " +
                    "INNER JOIN users AS u " +
                    "ON b.userId = u.id " +
                    "WHERE b.userId=?";
    private static final String GET_BUDGET_STATEMENT =
            "SELECT b.id, b.name, b.plannedAmount, b.spentAmount, b.validForMonth, u.id, u.username " +
            "FROM budgets AS b " +
            "INNER JOIN users AS u " +
                "ON b.userId = u.id " +
            "WHERE b.validForMonth=? AND b.userId=?";
    private static final String REMOVE_BUDGET_STATEMENT = "DELETE FROM budgets WHERE id=?";


    @Override
    public int createBudget(Budget budget) {
        int budgetId = -1;

        try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(ADD_BUDGET_STATEMENT, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, budget.getName());
            preparedStatement.setString(2, budget.getValidForMonth());
            preparedStatement.setInt(3, budget.getUser().getId());
            preparedStatement.executeUpdate();

            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                rs.next();
                budgetId = rs.getInt(1);
            }
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }
        return budgetId;
    }

    @Override
    public List<Budget> getBudgetsForUser(int userId) {
        List<Budget> budgets = new ArrayList<>();

        try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(GET_BUDGETS_STATEMENT)) {

            preparedStatement.setInt(1, userId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    budgets.add(buildBudgetFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }

        return budgets;
    }

    @Override
    public Budget getBudgetForMonthAndUserId(String month, int userId) {
        Budget budget = null;

        try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(GET_BUDGET_STATEMENT)) {

            preparedStatement.setString(1, month);
            preparedStatement.setInt(2, userId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    budget = buildBudgetFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }

        return budget;
    }

    @Override
    public boolean removeBudget(int id) {
        try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(REMOVE_BUDGET_STATEMENT)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
            return false;
        }
        return true;
    }

    private Budget buildBudgetFromResultSet(ResultSet rs) throws SQLException {
        User user = new User(rs.getInt("u.id"),
                rs.getString("u.username"));

        return new Budget(rs.getInt("b.id"),
                rs.getString("b.name"),
                rs.getBigDecimal("b.plannedAmount"),
                rs.getBigDecimal("b.spentAmount"),
                rs.getString("b.validForMonth"),
                user);
    }
}
