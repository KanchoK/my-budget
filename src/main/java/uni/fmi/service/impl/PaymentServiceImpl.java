/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.fmi.service.impl;

import uni.fmi.model.Payment;
import uni.fmi.persistence.dao.CategoryDao;
import uni.fmi.persistence.dao.PaymentDao;
import uni.fmi.service.PaymentService;

import javax.inject.Inject;
import java.util.List;

public class PaymentServiceImpl implements PaymentService {

    @Inject
    private PaymentDao paymentDao;

    @Inject
    private CategoryDao categoryDao;

    @Override
    public Payment createPayment(Payment payment) {
        Payment newPayment = paymentDao.createPayment(payment);
        newPayment.setCategory(categoryDao.getCategoryForId(newPayment.getCategory().getId()));
        return newPayment;
    }

    @Override
    public List<Payment> getPaymentsForCategory(int categoryId) {
        return paymentDao.getPaymentsForCategory(categoryId);
    }
    
    @Override
    public List<Payment> getPaymentsForUserAndMonth(int userId, String month){
        return paymentDao.getPaymentsForUserAndMonth(userId, month);
    }

    @Override
    public boolean removePayment(int id) {
        return paymentDao.removePayment(id);
    }
    
    @Override
    public Payment getPaymentForId(int id) {
        return paymentDao.getPaymentForId(id);
    }
    
    @Override
    public Payment updatePaymentById(int id, Payment payment){
        return paymentDao.updatePaymentById(id, payment);
    }
}
