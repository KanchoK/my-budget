package uni.fmi.persistence;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DatabaseManager {
    private DataSource dataSource;

    public DatabaseManager() {
        try {
            Context ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/my_budget");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
