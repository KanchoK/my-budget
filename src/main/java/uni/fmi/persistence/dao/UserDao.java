package uni.fmi.persistence.dao;

import uni.fmi.model.User;

public interface UserDao {
    int createUser(User user);

    User getUserForUsername(String username);
}
