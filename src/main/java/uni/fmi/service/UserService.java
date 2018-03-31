package uni.fmi.service;

import uni.fmi.model.User;

public interface UserService {
    boolean createUser(User user);

    User validateUser(String username, String password);
}
