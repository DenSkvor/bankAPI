package model.service;

import model.entity.Payment;

import java.util.List;

public interface PaymentService {

    Payment save(Payment payment);
    Payment update(Payment payment);
}
