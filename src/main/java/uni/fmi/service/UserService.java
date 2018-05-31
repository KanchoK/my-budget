package uni.fmi.service;

import java.math.BigDecimal;
import uni.fmi.model.User;

public interface UserService {
    boolean createUser(User user);

    User validateUser(String username, String password);
    
    User getUserForId(int id);
    
    User updateUserForId(int id, User user);
}
