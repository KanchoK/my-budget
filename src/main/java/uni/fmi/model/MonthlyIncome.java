package uni.fmi.model;

import java.math.BigDecimal;

public class MonthlyIncome {

    private int id;
    private BigDecimal monthlyIncome;
    private String validForMonth;
    private User user;

    public MonthlyIncome() {
    }

    public MonthlyIncome(int id, BigDecimal monthlyIncome, String validForMonth,
            User user) {
        this.id = id;
        this.monthlyIncome = monthlyIncome;
        this.validForMonth = validForMonth;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public String getValidForMonth() {
        return validForMonth;
    }

    public void setValidForMonth(String validForMonth) {
        this.validForMonth = validForMonth;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + id;
        result = 31 * result + monthlyIncome.hashCode();
        result = 31 * result + validForMonth.hashCode();
        result = 31 * result + user.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MonthlyIncome)) {
            return false;
        }
        MonthlyIncome monthlyIncome = (MonthlyIncome) obj;
        return monthlyIncome.getId() == id
                && monthlyIncome.getValidForMonth().equals(validForMonth)
                && monthlyIncome.getUser().equals(user);
    }

    @Override
    public String toString() {
        return String.format("[Id: %s; Monthly Income: %s; " +
                        "Valid For Month: %s; User: %s]",
                id, monthlyIncome, validForMonth, user.getUsername());
    }
}
