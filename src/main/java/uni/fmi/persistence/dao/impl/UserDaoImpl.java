package uni.fmi.persistence.dao.impl;

import java.math.BigDecimal;
import uni.fmi.model.User;
import uni.fmi.persistence.DatabaseManager;
import uni.fmi.persistence.dao.UserDao;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import java.sql.*;
import javax.ws.rs.PathParam;

public class UserDaoImpl implements UserDao {

    private static final Logger LOG = Logger.getLogger(UserDaoImpl.class);

    @Inject
    private DatabaseManager databaseManager;

    private static final String ADD_USER_STATEMENT = "INSERT INTO users(username, password) "
            + "VALUES (?, ?)";

    private static final String VALIDATE_USER_STATEMENT = "SELECT id, username, password FROM users "
            + "WHERE username = ?";

    private static final String GET_USER_FOR_ID_STATEMENT =
            "SELECT id, username, email, overallExpensesAlertLimit, "
            + "categoryExpensesAlertLimit, overallExpensesAlert, categoryExpensesAlert "
            + "FROM users "
            + "WHERE id = ?";

    private static final String UPDATE_USER_FOR_ID_STATEMENT
            = "UPDATE users "
            + "SET username = ?, email = ?, "
            + "overallExpensesAlertLimit = ?, categoryExpensesAlertLimit = ?, "
            + "overallExpensesAlert = ?, categoryExpensesAlert = ? "
            + "WHERE id = ?";

    @Override
    public int createUser(User user) {
        int userId = -1;

        try (Connection conn = databaseManager.getDataSource().getConnection();
                PreparedStatement preparedStatement = conn
                        .prepareStatement(ADD_USER_STATEMENT, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();

            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                rs.next();
                userId = rs.getInt(1);
            }
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }
        return userId;
    }

    @Override
    public User getUserForUsername(String username) {
        User user = null;

        try (Connection conn = databaseManager.getDataSource().getConnection();
                PreparedStatement preparedStatement = conn
                        .prepareStatement(VALIDATE_USER_STATEMENT)) {

            preparedStatement.setString(1, username);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    user = buildUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }

        return user;
    }

    @Override
    public User getUserForId(int id) {
        User user = null;

        try (Connection conn = databaseManager.getDataSource().getConnection();
                PreparedStatement preparedStatement = conn
                        .prepareStatement(GET_USER_FOR_ID_STATEMENT)) {

            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    user = buildFullUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }

        return user;
    }
    
    @Override
    public User updateUserForId(@PathParam("id") int id, User user) {
        User updatedUser = null;
        LOG.info("Id " + id + " user " + user);
        try (Connection conn = databaseManager.getDataSource().getConnection();
                PreparedStatement preparedStatement = conn
                        .prepareStatement(UPDATE_USER_FOR_ID_STATEMENT)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setBigDecimal(3, user.getOverallExpensesAlertLimit());
            preparedStatement.setBigDecimal(4, user.getCategoryExpensesAlertLimit());
            preparedStatement.setBoolean(5, user.getOverallExpensesAlert());
            preparedStatement.setBoolean(6, user.getCategoryExpensesAlert());
            preparedStatement.setInt(7, id);
            preparedStatement.executeUpdate();
            
            updatedUser = getUserForId(id);
            
        } catch (SQLException e) {
            LOG.error("Exception was thrown", e);
        }
        
        LOG.info("Id " + id + " updatedUser " + updatedUser);
        return updatedUser;
    }

//    BigDecimal getOverallExpensesAlertLimit(int userId);
//    
//    BigDecimal getCategoryExpensesAlertLimit(int userId);
//    
//    void updateOverallExpensesAlertLimit(BigDecimal overallLimit);
//    
//    void updateCategoryExpensesAlertLimit(BigDecimal categoryLimit);
    private User buildUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"));
    }
    
    private User buildFullUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getBigDecimal("overallExpensesAlertLimit"),
                rs.getBigDecimal("categoryExpensesAlertLimit"),
                rs.getBoolean("overallExpensesAlert"),
                rs.getBoolean("categoryExpensesAlert"));
    }
}
