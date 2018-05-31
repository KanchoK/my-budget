package uni.fmi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private int id;
    private String username;
    private String password;
    private String email;
    private BigDecimal overallExpensesAlertLimit = new BigDecimal(80.00);
    private BigDecimal categoryExpensesAlertLimit =  new BigDecimal(80.00);
    private boolean overallExpensesAlert = true;
    private boolean categoryExpensesAlert = true;

    public User () {}

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
    
     public User(int id, String username, String email,
             BigDecimal overallExpensesAlertLimit,
             BigDecimal categoryExpensesAlertLimit,
             boolean overallExpensesAlert,
             boolean categoryExpensesAlert) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.overallExpensesAlertLimit =  overallExpensesAlertLimit;
        this.categoryExpensesAlertLimit = categoryExpensesAlertLimit;
        this.overallExpensesAlert =  overallExpensesAlert;
        this.categoryExpensesAlert = categoryExpensesAlert;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    
     public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public BigDecimal getOverallExpensesAlertLimit() {
        return overallExpensesAlertLimit;
    }

    public void setOverallExpensesAlertLimit(BigDecimal categoryExpensesAlertLimit) {
        this.overallExpensesAlertLimit = categoryExpensesAlertLimit;
    }
    
    public BigDecimal getCategoryExpensesAlertLimit() {
        return categoryExpensesAlertLimit;
    }

    public void setCategoryExpensesAlertLimit(BigDecimal categoryExpensesAlertLimit) {
        this.categoryExpensesAlertLimit = categoryExpensesAlertLimit;
    }
    
      public boolean getOverallExpensesAlert() {
        return overallExpensesAlert;
    }

    public void setOverallExpensesAlert(boolean categoryExpensesAlert) {
        this.overallExpensesAlert = categoryExpensesAlert;
    }
    
    public boolean getCategoryExpensesAlert() {
        return categoryExpensesAlert;
    }

    public void setCategoryExpensesAlert(boolean categoryExpensesAlert) {
        this.categoryExpensesAlert = categoryExpensesAlert;
    }
    

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + id;
        result = 31 * result + username.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        User user = (User) obj;
        return user.getId() == id
                && user.getUsername().equals(username)
                && user.getPassword().equals(password);
    }

    @Override
    public String toString() {
        
        return String.format("[Id: %s; Username: %s, Email: %s]",
                id, username, email);
    }
}
