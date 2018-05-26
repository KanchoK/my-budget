package uni.fmi.model;

import java.math.BigDecimal;

public class Budget {

    private int id;
    private String name;
    private BigDecimal plannedAmount;
    private BigDecimal spentAmount;
    private String validForMonth;
    private User user;

    public Budget() {
    }

    public Budget(int id, String name, BigDecimal plannedAmount,
                  BigDecimal spentAmount, String validForMonth, User user) {
        this.id = id;
        this.name = name;
        this.plannedAmount = plannedAmount;
        this.spentAmount = spentAmount;
        this.validForMonth = validForMonth;
        this.user = user;
    }

    public Budget(int id, String validForMonth, String name) {
        this.id = id;
        this.validForMonth = validForMonth;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPlannedAmount() {
        return plannedAmount;
    }

    public void setPlannedAmount(BigDecimal plannedAmount) {
        this.plannedAmount = plannedAmount;
    }

    public BigDecimal getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount;
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
        result = 31 * result + name.hashCode();
        result = 31 * result + plannedAmount.hashCode();
        result = 31 * result + spentAmount.hashCode();
        result = 31 * result + validForMonth.hashCode();
        result = 31 * result + user.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Budget)) {
            return false;
        }
        Budget budget = (Budget) obj;
        return budget.getId() == id
                && budget.getName().equals(name)
                && budget.getValidForMonth().equals(validForMonth)
                && budget.getUser().equals(user);
    }

    @Override
    public String toString() {
        return String.format("[Id: %s; Budget Name: %s; " +
                        "Planned Amount: %s; Spent Amount: %s; " +
                        "Valid For Month: %s; User: %s]",
                id, name, plannedAmount, spentAmount,
                validForMonth, user.getUsername());
    }
}
