/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.fmi.model;

import java.math.BigDecimal;

public class Payment {
    private int id;
    private String title;
    private BigDecimal amount;
    private Category category;


    public Payment() {
    }

    public Payment(int id, String title, BigDecimal amount,
            Category category) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

     public Category getCategory() {
        return category;
    }

    public void setCaegory(Category caegory) {
        this.category = caegory;
    }
    
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + id;
        result = 31 * result + title.hashCode();
        result = 31 * result + amount.hashCode();
        result = 31 * result + category.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Payment)) {
            return false;
        }
        Payment payment = (Payment) obj;
        return payment.getId() == id
                && payment.getTitle().equals(title)
                && payment.getAmount().equals(amount)
                && payment.getCategory().equals(category);
    }

    @Override
    public String toString() {
        return String.format("[Id: %s; Title: %s; Amount: %s; " +
                "Category: %s]",
                id, title, amount, category.getName());
    }
}
