package uni.fmi.persistence.dao.impl;

import java.math.BigDecimal;
import org.apache.log4j.Logger;
import uni.fmi.model.Budget;
import uni.fmi.model.User;
import uni.fmi.persistence.DatabaseManager;
import uni.fmi.persistence.dao.BudgetDao;

import javax.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BudgetDaoImpl implements BudgetDao {

    private static final Logger LOG = Logger.getLogger(BudgetDaoImpl.class);

    @Inject
    private DatabaseManager databaseManager;

    private static final String ADD_BUDGET_EXTENDED_STATEMENT = "INSERT INTO budgets(name, validForMonth, plannedAmount, spentAmount, userId) "
            + "VALUES (?, ?, ?, ?, ?)";
    private static final String ADD_BUDGET_STATEMENT = "INSERT INTO budgets(name, validForMonth, userId) "
            + "VALUES (?, ?, ?)";
    private static final String GET_BUDGETS_FOR_USER_STATEMENT
            = "SELECT b.id, b.name, b.plannedAmount, b.spentAmount, b.validForMonth, u.id, u.username "
            + "FROM budgets AS b "
            + "INNER JOIN users AS u "
            + "ON b.userId = u.id "
            + "WHERE b.userId=?";
    private static final String GET_BUDGET_FOR_USER_AND_MONTH_STATEMENT
            = "SELECT b.id, b.name, b.plannedAmount, b.spentAmount, b.validForMonth, u.id, u.username "
            + "FROM budgets AS b "
            + "INNER JOIN users AS u "
            + "ON b.userId = u.id "
            + "WHERE b.validForMonth=? AND b.userId=?";
     private static final String GET_BUDGET_FOR_ID_STATEMENT
            = "SELECT b.id, b.name, b.plannedAmount, b.spentAmount, b.validForMonth, u.id, u.username "
            + "FROM budgets AS b "
            + "INNER JOIN users AS u "
            + "ON b.userId = u.id "
            + "WHERE b.id = ?";
    private static final String REMOVE_BUDGET_STATEMENT = "DELETE FROM budgets WHERE id=?";

    @Override
    public int createBudget(Budget budget) {
        int budgetId = -1;
        if (budget.getPlannedAmount() != BigDecimal.ZERO) {
            try (Connection conn = databaseManager.getDataSource().getConnection();
                    PreparedStatement preparedStatement = conn
                            .prepareStatement(ADD_BUDGET_EXTENDED_STATEMENT, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, budget.getName());
                preparedStatement.setString(2, budget.getValidForMonth());
                preparedStatement.setBigDecimal(3, budget.getPlannedAmount());
                preparedStatement.setBigDecimal(4, budget.getSpentAmount());
                preparedStatement.setInt(5, budget.getUser().getId());
                preparedStatement.executeUpdate();

                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    rs.next();
                    budgetId = rs.getInt(1);
                }
            } catch (SQLException e) {
                LOG.error("Exception was thrown", e);
            }
        } else {
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
        }
        return budgetId;
    }

    @Override
    public List<Budget> getBudgetsForUser(int userId) {
        List<Budget> budgets = new ArrayList<>();

        try (Connection conn = databaseManager.getDataSource().getConnection();
                PreparedStatement preparedStatement = conn
                        .prepareStatement(GET_BUDGETS_FOR_USER_STATEMENT)) {

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
    public List<Budget> getBudgetsForUserAndMonth(int userId, String month) {
        List<Budget> budgets = new ArrayList<>();

        try (Connection conn = databaseManager.getDataSource().getConnection();
                PreparedStatement preparedStatement = conn
                        .prepareStatement(GET_BUDGET_FOR_USER_AND_MONTH_STATEMENT)) {

            preparedStatement.setString(1, month);
            preparedStatement.setInt(2, userId);
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
    public Budget getBudgetForId(int budgetId){
        Budget budget = null;

        try (Connection conn = databaseManager.getDataSource().getConnection();
                PreparedStatement preparedStatement = conn
                        .prepareStatement(GET_BUDGET_FOR_ID_STATEMENT)) {

            preparedStatement.setInt(1, budgetId);
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
    public Budget copyBudgetForUserBudgetAndMonth(int userId, int budgetId, String month) {
        List<Budget> budgets = getBudgetsForUserAndMonth(userId, month);
        LOG.info(budgets.toString());
        Budget budgetById = getBudgetForId(budgetId);

//        int maxBudgetId = budgets
//                .stream()
//                .max(Comparator.comparing(Budget::getId))
//                .get()
//                .getId();
//        
//        LOG.info("maxBudgetId: " + maxBudgetId);

//        String[] monthParts = month.split("-");
//        String newMonth = (Integer.parseInt(monthParts[0]) + 1      

        Budget newBudget = new Budget(1, budgetById.getName(),
                budgetById.getPlannedAmount(), BigDecimal.ZERO, month,
                budgetById.getUser());

        int newBudgetId = createBudget(newBudget);
        newBudget.setId(newBudgetId);

        return newBudget;
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

//     private Budget buildAllBudgetFromResultSet(ResultSet rs) throws SQLException {
//        return new Budget(rs.getInt("b.id"),
//                rs.getString("b.name"),
//                rs.getBigDecimal("b.plannedAmount"),
//                rs.getBigDecimal("b.spentAmount"),
//                rs.getString("b.validForMonth"),
//                user);
//    }
}
