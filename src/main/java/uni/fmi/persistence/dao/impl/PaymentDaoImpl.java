/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.fmi.persistence.dao.impl;

import java.math.BigDecimal;
import org.apache.log4j.Logger;
import uni.fmi.model.Category;
import uni.fmi.model.Payment;
import uni.fmi.persistence.DatabaseManager;
import uni.fmi.persistence.dao.PaymentDao;

import javax.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import uni.fmi.model.Budget;
import uni.fmi.model.User;

public class PaymentDaoImpl implements PaymentDao{
        
    private static final Logger LOG = Logger.getLogger(PaymentDaoImpl.class);
    
    @Inject
    private DatabaseManager databaseManager;

    private static final String ADD_PAYMENT_STATEMENT = "INSERT INTO payments(title, date, amount, categoryId) " +
                                                "VALUES (?, ?, ?, ?)";
    
    private static final String ADD_PAYMENT_WITH_COMMENT_STATEMENT = "INSERT INTO payments(title, date, amount, categoryId, comment) " +
                                                "VALUES (?, ?, ?, ?, ?)";
    
    private static final String GET_PAYMENTS_FOR_CATEGORY_STATEMENT =
            "SELECT p.id, p.title, p.date, p.amount, p.comment, c.id, c.name, " +
                    "c.plannedAmount, c.spentAmount, b.id, b.validForMonth, b.name, " +
                    "b.plannedAmount, b.spentAmount " +
                    "FROM payments AS p " +
                    "INNER JOIN categories AS c " +
                    "ON p.categoryId = c.id " +
                    "INNER JOIN budgets AS b " +
                    "ON c.budgetId = b.id " +
                    "WHERE p.categoryId = ?";
    
     private static final String GET_PAYMENTS_FOR_USER_AND_MONTH_STATEMENT =
            "SELECT p.id, p.title, p.date, p.amount, p.comment, c.id, c.name, " +
                    "c.plannedAmount, c.spentAmount, b.id, b.validForMonth, b.name, " +
                    "b.plannedAmount, b.spentAmount " +
                    "FROM payments AS p " +
                    "INNER JOIN categories AS c " +
                    "ON p.categoryId = c.id " +
                    "INNER JOIN budgets AS b " +
                    "ON c.budgetId = b.id " +
                    "INNER JOIN users AS u " +
                    "ON b.userId = u.id " +
                    "WHERE u.id = ? AND b.validForMonth = ?";
    
    private static final String REMOVE_PAYMENT_STATEMENT = "DELETE FROM payments WHERE id=?";
    
    private static final String ALTER_CATEGORY_SPEND_AMOUNT_FOR_CREATED_PAYMENT_STATEMENT =
            "UPDATE categories " +
            "SET spentAmount = spentAmount + ? " +
            "WHERE id = ?";
    
    private static final String ALTER_CATEGORY_SPEND_AMOUNT_FOR_DELETED_PAYMENT_STATEMENT =
            "UPDATE categories " +
            "SET spentAmount = spentAmount - ? " +
            "WHERE id = ?";
    
    private static final String ALTER_BUDGET_SPEND_AMOUNT_FOR_CREATED_PAYMENT_STATEMENT =
            "UPDATE budgets " +
            "SET spentAmount = spentAmount + ? " +
            "WHERE id = ?";
    
    private static final String ALTER_BUDGET_SPEND_AMOUNT_FOR_DELETED_PAYMENT_STATEMENT =
            "UPDATE budgets " +
            "SET spentAmount = spentAmount - ? " +
            "WHERE id = ?";
     
    private static final String GET_BUDGET_FOR_PAYMENT_STATEMENT =
            "SELECT c.budgetId " +
                    "FROM payments AS p " +
                    "INNER JOIN categories AS c " +
                    "ON p.categoryId = c.id " +
                    "WHERE p.id = ?";
     
    private static final String GET_BUDGET_BY_ID_STATEMENT =
            "SELECT p.id, p.categoryId, p.amount " +
                    "FROM payments AS p " +
                    "WHERE p.id = ?";
     
    private static final String GET_PATMENT_BY_ID_STATEMENT = 
             "SELECT p.id, p.title, p.date, p.amount, p.comment, c.id, c.name, " +
                    "c.plannedAmount, c.spentAmount, b.id, b.validForMonth, b.name, " +
                    "b.plannedAmount, b.spentAmount " +
                    "FROM payments AS p " +
                    "INNER JOIN categories AS c " +
                    "ON p.categoryId = c.id " +
                    "INNER JOIN budgets AS b " +
                    "ON c.budgetId = b.id " +
                    "INNER JOIN users AS u " +
                    "ON b.userId = u.id " +
                    "WHERE p.id = ?";
    
    @Override
    public Payment createPayment(Payment payment) {
        int paymentId = -1;

        if (payment.getComment() == null || "".equals(payment.getComment())){
            try (Connection conn = databaseManager.getDataSource().getConnection();
                 PreparedStatement preparedStatement = conn
                         .prepareStatement(ADD_PAYMENT_STATEMENT, Statement.RETURN_GENERATED_KEYS)) {

                preparedStatement.setString(1, payment.getTitle());
                preparedStatement.setString(2, payment.getDate());
                preparedStatement.setBigDecimal(3, payment.getAmount());
                preparedStatement.setInt(4, payment.getCategory().getId());
                preparedStatement.executeUpdate();

                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    rs.next();
                    paymentId = rs.getInt(1);
                }

                addPaymentAmountToCategory(payment.getCategory().getId(),
                        payment.getAmount());
                LOG.info("payment id"  + paymentId);
                addPaymentAmountToBudget(paymentId, payment.getAmount());


            } catch (SQLException e) {
                LOG.error("Exception was thrown", e);
            }
        } else {
              try (Connection conn = databaseManager.getDataSource().getConnection();
                 PreparedStatement preparedStatement = conn
                         .prepareStatement(ADD_PAYMENT_WITH_COMMENT_STATEMENT, Statement.RETURN_GENERATED_KEYS)) {

                preparedStatement.setString(1, payment.getTitle());
                preparedStatement.setString(2, payment.getDate());
                preparedStatement.setBigDecimal(3, payment.getAmount());
                preparedStatement.setInt(4, payment.getCategory().getId());
                preparedStatement.setString(5, payment.getComment());
                preparedStatement.executeUpdate();

                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    rs.next();
                    paymentId = rs.getInt(1);
                }

                addPaymentAmountToCategory(payment.getCategory().getId(),
                        payment.getAmount());
                LOG.info("payment id"  + paymentId);
                addPaymentAmountToBudget(paymentId, payment.getAmount());


            } catch (SQLException e) {
                LOG.error("Exception was thrown", e);
            }
        }
        
        payment.setId(paymentId);
        return payment;
    }

    @Override
    public List<Payment> getPaymentsForCategory(int categoryId) {
        List<Payment> payments = new ArrayList<>();

        try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(GET_PAYMENTS_FOR_CATEGORY_STATEMENT)) {

            preparedStatement.setInt(1, categoryId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    payments.add(buildPaymentFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }

        return payments;
    }
    
    @Override
    public List<Payment> getPaymentsForUserAndMonth(int userId, String month){
    List<Payment> payments = new ArrayList<>();

        try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(GET_PAYMENTS_FOR_USER_AND_MONTH_STATEMENT)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, month);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    payments.add(buildPaymentFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }

        return payments;
    }

    @Override
    public boolean removePayment(int id) {
          
         try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(GET_BUDGET_BY_ID_STATEMENT)) {
            preparedStatement.setInt(1, id);   
            
             try (ResultSet rs = preparedStatement.executeQuery()) {
                rs.next();
             
                removePaymentAmountFromCategory(rs.getInt("p.categoryId"),
                rs.getBigDecimal("p.amount"));
//                LOG.info("payment id"  + paymentId);
                removePaymentAmountFromBudget(rs.getInt("p.id"), rs.getBigDecimal("p.amount"));
            }
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }
        
        
        try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(REMOVE_PAYMENT_STATEMENT)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();      
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
            return false;
        }
        return true;
    }

    private Payment buildPaymentFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        
        Budget budget = new Budget(rs.getInt("b.id"),
                rs.getString("b.name"),
                rs.getBigDecimal("b.plannedAmount"),
                rs.getBigDecimal("b.spentAmount"),
                rs.getString("b.validForMonth"),
                user);
        
        Category category = new Category(rs.getInt("c.id"),
                rs.getString("c.name"),
                rs.getBigDecimal("c.plannedAmount"),
                rs.getBigDecimal("c.spentAmount"),
                budget);

        return new Payment(rs.getInt("p.id"),
                rs.getString("p.title"),
                rs.getString("p.date"),
                rs.getBigDecimal("p.amount"),
                category,
                rs.getString("p.comment"));
    }
    
    private void addPaymentAmountToCategory(int categoryId, BigDecimal amount){
         try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(ALTER_CATEGORY_SPEND_AMOUNT_FOR_CREATED_PAYMENT_STATEMENT)) {

            preparedStatement.setBigDecimal(1, amount);
            preparedStatement.setInt(2, categoryId);   
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }
    }
    
     private void removePaymentAmountFromCategory(int categoryId, BigDecimal amount){
         try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(ALTER_CATEGORY_SPEND_AMOUNT_FOR_DELETED_PAYMENT_STATEMENT)) {

            preparedStatement.setBigDecimal(1, amount);
            preparedStatement.setInt(2, categoryId);   
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }
    }

    private void addPaymentAmountToBudget(int paymentId, BigDecimal amount) {
         int budgetId = -1;
         
         try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(GET_BUDGET_FOR_PAYMENT_STATEMENT)) {
            preparedStatement.setInt(1, paymentId);   
            
             try (ResultSet rs = preparedStatement.executeQuery()) {
                rs.next();
              
                budgetId = rs.getInt("c.budgetId");
                LOG.info("budgetId: " + budgetId);
            }
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }
         
       try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(ALTER_BUDGET_SPEND_AMOUNT_FOR_CREATED_PAYMENT_STATEMENT)) {

            preparedStatement.setBigDecimal(1, amount);
            preparedStatement.setInt(2, budgetId);   
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }
    }
    
     private void removePaymentAmountFromBudget(int paymentId, BigDecimal amount) {
         int budgetId = -1;
         
         try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(GET_BUDGET_FOR_PAYMENT_STATEMENT)) {
            preparedStatement.setInt(1, paymentId);   
            
             try (ResultSet rs = preparedStatement.executeQuery()) {
                rs.next();
              
                budgetId = rs.getInt("c.budgetId");
                LOG.info("budgetId: " + budgetId);
            }
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }
         
       try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(ALTER_BUDGET_SPEND_AMOUNT_FOR_DELETED_PAYMENT_STATEMENT)) {

            preparedStatement.setBigDecimal(1, amount);
            preparedStatement.setInt(2, budgetId);   
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }
    }
     
    @Override
    public Payment getPaymentForId(int id){
        Payment payment = null;
          try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(GET_PATMENT_BY_ID_STATEMENT)) {

            preparedStatement.setInt(1, id);
          try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    payment = buildPaymentFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }
          
        return payment;
    }
}
