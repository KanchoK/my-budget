package uni.fmi.persistence.dao.impl;

import uni.fmi.model.User;
import uni.fmi.persistence.DatabaseManager;
import uni.fmi.persistence.dao.UserDao;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import java.sql.*;

public class UserDaoImpl implements UserDao {

    private static final Logger LOG = Logger.getLogger(UserDaoImpl.class);

    @Inject
    private DatabaseManager databaseManager;

    private static final String ADD_USER_STATEMENT = "INSERT INTO users(username, password) " +
                                                    "VALUES (?, ?)";
    private static final String VALIDATE_USER_STATEMENT = "SELECT id, username, password FROM users " +
                                                    "WHERE username=?";

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

    private User buildUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"));
    }
}
