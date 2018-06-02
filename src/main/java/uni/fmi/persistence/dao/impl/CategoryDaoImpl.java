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
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao{
    
    private static final Logger LOG = Logger.getLogger(CategoryDaoImpl.class);
    
    @Inject
    private DatabaseManager databaseManager;

    private static final String ADD_CATEGORY_STATEMENT = "INSERT INTO categories(name, plannedAmount, spentAmount, budgetId) " +
                                                "VALUES (?, ?, ?, ?)";
    
    private static final String GET_CATEGORIES_FOR_BUDGET_STATEMENT =
            "SELECT c.id, c.name, c.plannedAmount, c.spentAmount, " +
            "b.id, b.validForMonth, b.name, b.plannedAmount, b.spentAmount " +
                    "FROM categories AS c " +
                    "INNER JOIN budgets AS b " +
                    "ON c.budgetId = b.id " +
                    "WHERE c.budgetId = ?";
    
    private static final String GET_CATEGORIES_FOR_USER_AND_MONTH_STATEMENT =
            "SELECT c.id, c.name, c.plannedAmount, c.spentAmount, " +
            "b.id, b.validForMonth, b.name, b.plannedAmount, b.spentAmount " +
                    "FROM categories AS c " +
                    "INNER JOIN budgets AS b " +
                    "ON c.budgetId = b.id " +
                    "INNER JOIN users AS u " +
                    "ON b.userId = u.id " +
                    "WHERE u.id = ? AND b.validForMonth = ?";
    
    private static final String REMOVE_CATEGORY_STATEMENT = "DELETE FROM categories WHERE id=?";
    
    private static final String GET_CATEGORY_BY_ID_STATEMENT = 
            "SELECT c.id, c.name, c.plannedAmount, c.spentAmount, " +
            "b.id, b.validForMonth, b.name, b.plannedAmount, b.spentAmount " +
                    "FROM categories AS c " +
                    "INNER JOIN budgets AS b " +
                    "ON c.budgetId = b.id " +
                    "WHERE c.id = ?";

    private static final String CHANGE_BUDGET_SPENT_AMOUNT_ON_CATEGORY_REMOVAL_STATEMENT = 
            "UPDATE budgets " +
            "SET spentAmount = spentAmount - ? " +
            "WHERE id = ?";
    
    @Override
    public Category createCategory(Category category) {
        int categoryId = -1;
        
        // Checks if the categoy's panned amount is greater than its budget's planned amount
        List<Category> categories  = getCategoriesForBudget(category.getBudget().getId());
        LOG.info(categories);
        if (categories != null && !categories.isEmpty()) {
            BigDecimal budgetPlannedAmount = categories.get(0).getBudget().getPlannedAmount();
            BigDecimal categoryPlannedAmount = category.getPlannedAmount();
            if (categoryPlannedAmount.compareTo(budgetPlannedAmount) == 1) {
                LOG.error("The planned amount of this category = " + categoryPlannedAmount +
                        " is greater than its budget's planned amount = " + 
                        budgetPlannedAmount + " !");
                return null;
            }

            // Checks if the sum of the categoy's panned amount and the planned amounts of the previous categories
            // is greater than its budget's planned amount
            BigDecimal plannedAmountSum = categoryPlannedAmount;      
            for(Category item:categories){
                plannedAmountSum = plannedAmountSum.add(item.getPlannedAmount(), 
                        MathContext.DECIMAL32);
            }
            if (plannedAmountSum.compareTo(budgetPlannedAmount) == 1) {
                LOG.error("The sum of the planned amounts of this category and " +
                        "the previous ones = " + plannedAmountSum + 
                        " is greater than its budget's planned amount = " +
                        budgetPlannedAmount + " !");
                return null;
            }
        }
        

        try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(ADD_CATEGORY_STATEMENT, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, category.getName());
            preparedStatement.setBigDecimal(2, category.getPlannedAmount());
            preparedStatement.setBigDecimal(3, BigDecimal.ZERO);
            preparedStatement.setInt(4, category.getBudget().getId());
            preparedStatement.executeUpdate();

            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                rs.next();
                categoryId = rs.getInt(1);
            }
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }
        
        category.setId(categoryId);
        return category;
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
    public List<Category> getCategoriesForUserAndMonth(int userId, String month){
        List<Category> categories = new ArrayList<>();

        try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(GET_CATEGORIES_FOR_USER_AND_MONTH_STATEMENT)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, month);
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
    public List<Category> copyCategoriesForUserBudgetAndMonth(int userId, int budgetId, String month, Budget budget){
        List<Category> newCategories = new ArrayList<>();
        List<Category> categories = getCategoriesForUserAndMonth(userId, month);
        
        LOG.info("Categories to copy:" + categories.toString());
        LOG.info("budgetId " + budgetId);
        LOG.info("budget.getId() " + budget.getId());
        
        Object[] categoriesByBudgetId = categories
                .stream()
                .filter(a -> a.getBudget().getId() == budgetId)
                .toArray();

//        int maxCategoryId = categories
//                .stream()
//                .max(Comparator.comparing(Category::getId))
//                .get()
//                .getId();
        
//        LOG.info("Max category id = " + maxCategoryId);
        LOG.info("Categories by budget id = " + budgetId + " are: "+ categoriesByBudgetId);
        
        for (Object object : categoriesByBudgetId){
            Category category = ((Category)object);
            category.setBudget(budget);
            category.setSpentAmount(BigDecimal.ZERO);
//            maxCategoryId += 1;
            Category newCategoryReturned = createCategory(category);
            int newCategoryId = newCategoryReturned.getId();
            LOG.info("Created category with id = " + newCategoryId);
            category.setId(1);
            newCategories.add(category);
        }
        
        return newCategories;
    }

    @Override
    public boolean removeCategory(int id) {
        Category category = getCategoryForId(id);
        LOG.info("category to delete: " + category);
        if (category == null) {
            return false;
        }
        
        try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(REMOVE_CATEGORY_STATEMENT)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
            return false;
        }   
         
         try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(CHANGE_BUDGET_SPENT_AMOUNT_ON_CATEGORY_REMOVAL_STATEMENT)) {

            preparedStatement.setBigDecimal(1, category.getSpentAmount());
            preparedStatement.setInt(2, category.getBudget().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
            return false;
        }
         
        return true;
    }

    @Override
    public Category getCategoryForId(int id){
        Category category = null;
          try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(GET_CATEGORY_BY_ID_STATEMENT)) {

            preparedStatement.setInt(1, id);
          try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    category = buildCategoryFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }
          
        return category;
    }

    private Category buildCategoryFromResultSet(ResultSet rs) throws SQLException {
        Budget budget = new Budget(rs.getInt("b.id"),
                rs.getString("b.name"),
                rs.getBigDecimal("b.plannedAmount"),
                rs.getBigDecimal("b.spentAmount"),
                rs.getString("b.validForMonth"),
                null);

        return new Category(rs.getInt("c.id"),
                rs.getString("c.name"),
                rs.getBigDecimal("c.plannedAmount"),
                rs.getBigDecimal("c.spentAmount"),
                budget);
    }
}
