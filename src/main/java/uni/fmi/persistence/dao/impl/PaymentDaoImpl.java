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
import uni.fmi.model.Payment;
import uni.fmi.persistence.DatabaseManager;
import uni.fmi.persistence.dao.PaymentDao;

public class PaymentDaoImpl implements PaymentDao{
        
    private static final Logger LOG = Logger.getLogger(PaymentDaoImpl.class);
    
    @Inject
    private DatabaseManager databaseManager;

    private static final String ADD_PAYMENT_STATEMENT = "INSERT INTO payments(title, amount, categoryId) " +
                                                "VALUES (?, ?, ?)";
    private static final String GET_PAYMENT_STATEMENT = "";
    private static final String REMOVE_PAYMENT_STATEMENT = "DELETE FROM payments WHERE id=?";
    
     @Override
    public int createPayment(Payment payment) {
        int paymentId = -1;

        try (Connection conn = databaseManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn
                     .prepareStatement(ADD_PAYMENT_STATEMENT, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, payment.getTitle());
            preparedStatement.setBigDecimal(2, payment.getAmount());
            preparedStatement.setInt(3, payment.getCategory().getId());
            preparedStatement.executeUpdate();

            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                rs.next();
                paymentId = rs.getInt(1);
            }
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }
        return paymentId;
    }

    @Override
    public boolean removePayment(int id) {
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
}
