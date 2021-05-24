package model.dao;

import model.dto.PaymentAcceptDto;
import model.entity.Payment;

import java.util.List;

public interface PaymentRepository {

    Payment insert(Payment payment);
    Payment update(Payment payment);
}
