/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.fmi.model;

import java.io.Serializable;
import java.math.BigDecimal;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;

/**
 *
 * @author icho9
 */
public class Category{
    private int id;
    private String name;
    private BigDecimal plannedAmount;
    private BigDecimal spentAmount;
    private Budget budget;

    public Category() {
    };

    public Category(int id, String name, BigDecimal plannedAmount,
                  BigDecimal spentAmount, Budget budget) {
        this.id = id;
        this.name = name;
        this.plannedAmount = plannedAmount;
        this.spentAmount = spentAmount;
        this.budget = budget;
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
    
    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + id;
        result = 31 * result + name.hashCode();
        result = 31 * result + plannedAmount.hashCode();
        result = 31 * result + spentAmount.hashCode();
        result = 31 * result + budget.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Category)) {
            return false;
        }
        Category category = (Category) obj;
        return category.getId() == id
                && category.getName().equals(name)
                && category.getBudget().equals(budget);
    }

    @Override
    public String toString() {
        return String.format("[Id: %s; Category Name: %s; " +
                        "Planned Amount: %s; Spent Amount: %s; Budget: %s]",
                id, name, plannedAmount, spentAmount, budget.getName());
    }
}
