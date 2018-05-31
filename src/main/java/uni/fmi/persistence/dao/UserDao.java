package uni.fmi.persistence.dao;

import java.math.BigDecimal;
import uni.fmi.model.User;

public interface UserDao {
    int createUser(User user);

    User getUserForUsername(String username);
    
    User getUserForId(int id);
    
    User updateUserForId(int id, User user);
}
