/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.fmi.service;

import uni.fmi.model.Payment;

import java.util.List;

public interface PaymentService {
    
    Payment createPayment(Payment payment);

    List<Payment> getPaymentsForCategory(int categoryId);
    
    List<Payment> getPaymentsForUserAndMonth(int userId, String month);

    boolean removePayment(int id);
    
    Payment getPaymentForId(int id);
    
    Payment updatePaymentById(int id, Payment payment);
}
