/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.fmi.persistence.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import uni.fmi.model.Category;
import uni.fmi.persistence.DatabaseManager;
import uni.fmi.persistence.dao.CategoryDao;

public class CategoryDaoImpl implements CategoryDao{
    
    private static final Logger LOG = Logger.getLogger(CategoryDaoImpl.class);
    
    @Inject
    private DatabaseManager databaseManager;

    private static final String ADD_CATEGORY_STATEMENT = "INSERT INTO categories(name, plannedAmount, budgetId) " +
                                                "VALUES (?, ?, ?)";
    private static final String GET_CATEGORY_STATEMENT = "";
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
}
