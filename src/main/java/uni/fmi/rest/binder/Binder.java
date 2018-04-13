package uni.fmi.rest.binder;

import uni.fmi.persistence.DatabaseManager;
import uni.fmi.persistence.dao.BudgetDao;
import uni.fmi.persistence.dao.UserDao;
import uni.fmi.persistence.dao.impl.BudgetDaoImpl;
import uni.fmi.persistence.dao.impl.UserDaoImpl;
import uni.fmi.service.BudgetService;
import uni.fmi.service.PasswordService;
import uni.fmi.service.TokenService;
import uni.fmi.service.UserService;
import uni.fmi.service.impl.BudgetServiceImpl;
import uni.fmi.service.impl.PasswordServiceImpl;
import uni.fmi.service.impl.TokenServiceImpl;
import uni.fmi.service.impl.UserServiceImpl;
import org.glassfish.jersey.internal.inject.AbstractBinder;

import javax.inject.Singleton;

public class Binder extends AbstractBinder {


    @Override
    protected void configure() {
        bind(PasswordServiceImpl.class).to(PasswordService.class);
        bind(UserServiceImpl.class).to(UserService.class);
        bind(BudgetServiceImpl.class).to(BudgetService.class);
        bind(TokenServiceImpl.class).to(TokenService.class);
        bind(UserDaoImpl.class).to(UserDao.class);
        bind(BudgetDaoImpl.class).to(BudgetDao.class);
        bind(DatabaseManager.class).to(DatabaseManager.class).in(Singleton.class);
    }
}
