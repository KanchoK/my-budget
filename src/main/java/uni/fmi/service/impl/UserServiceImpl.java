package uni.fmi.service.impl;

import java.math.BigDecimal;
import uni.fmi.model.User;
import uni.fmi.persistence.dao.UserDao;
import uni.fmi.service.PasswordService;
import uni.fmi.service.UserService;

import javax.inject.Inject;

public class UserServiceImpl implements UserService {

    @Inject
    private UserDao userDao;

    @Inject
    private PasswordService passwordService;

    @Override
    public boolean createUser(User user) {
        user.setPassword(passwordService.hashPassword(user.getPassword()));
        int userId = userDao.createUser(user);
        return userId >= 0;
    }

    @Override
    public User validateUser(String username, String password) {
        User user = userDao.getUserForUsername(username);
        if (user != null && passwordService.checkPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }
    
    @Override
    public User getUserForId(int id){
        User user = userDao.getUserForId(id);
        return user;
    }
    
    @Override
    public User updateUserForId(int id, User user){
        User updatedUser = userDao.updateUserForId(id, user);
        return updatedUser;
    }
}
