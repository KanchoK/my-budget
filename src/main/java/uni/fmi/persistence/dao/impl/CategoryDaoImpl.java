/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.fmi.persistence.dao.impl;

import org.apache.log4j.Logger;
import uni.fmi.model.Budget;
import uni.fmi.model.Category;
import uni.fmi.persistence.DatabaseManager;
import uni.fmi.persistence.dao.CategoryDao;

import javax.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao{
    
    private static final Logger LOG = Logger.getLogger(CategoryDaoImpl.class);
    
    @Inject
    private DatabaseManager databaseManager;

    private static final String ADD_CATEGORY_STATEMENT = "INSERT INTO categories(name, plannedAmount, budgetId) " +
                                                "VALUES (?, ?, ?)";
    private static final String GET_CATEGORIES_FOR_BUDGET_STATEMENT =
            "SELECT c.id, c.name, c.plannedAmount, c.spentAmount, b.id, b.validForMonth, b.name " +
                    "FROM categories AS c " +
                    "INNER JOIN budgets AS b " +
                    "ON c.budgetId = b.id " +
                    "WHERE c.budgetId=?";
    private static final String REMOVE_CATEGORY_STATEMENT = "DELETE FROM categories WHERE id=?";
    
    @Override
    public int createCategory(Category category) {
        int categoryId = -1;

        try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(ADD_CATEGORY_STATEMENT, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, category.getName());
            preparedStatement.setBigDecimal(2, category.getPlannedAmount());
            preparedStatement.setInt(3, category.getBudget().getId());
            preparedStatement.executeUpdate();

            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                rs.next();
                categoryId = rs.getInt(1);
            }
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }
        return categoryId;
    }

    @Override
    public List<Category> getCategoriesForBudget(int budgetId) {
        List<Category> categories = new ArrayList<>();

        try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(GET_CATEGORIES_FOR_BUDGET_STATEMENT)) {

            preparedStatement.setInt(1, budgetId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    categories.add(buildCategoryFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }

        return categories;
    }

    @Override
    public boolean removeCategory(int id) {
        try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(REMOVE_CATEGORY_STATEMENT)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
            return false;
        }
        return true;
    }

    private Category buildCategoryFromResultSet(ResultSet rs) throws SQLException {
        Budget budget = new Budget(rs.getInt("b.id"),
                rs.getString("b.validForMonth"),
                rs.getString("b.name"));

        return new Category(rs.getInt("c.id"),
                rs.getString("c.name"),
                rs.getBigDecimal("c.plannedAmount"),
                rs.getBigDecimal("c.spentAmount"),
                budget);
    }
}
