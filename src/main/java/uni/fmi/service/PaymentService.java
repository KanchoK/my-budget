/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.fmi.service;

import uni.fmi.model.Payment;

public interface PaymentService {
    
    boolean createPayment(Payment payment);

    boolean removePayment(int id);
}
