/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.fmi.service.impl;

import javax.inject.Inject;
import uni.fmi.model.Payment;
import uni.fmi.persistence.dao.PaymentDao;
import uni.fmi.service.PaymentService;

public class PaymentServiceImpl implements PaymentService {

    @Inject
    private PaymentDao paymentDao;

    @Override
    public boolean createPayment(Payment payment) {
        int paymentId = paymentDao.createPayment(payment);
        return paymentId > 0;
    }

    @Override
    public boolean removePayment(int id) {
        return paymentDao.removePayment(id);
    }
}
