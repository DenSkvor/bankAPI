package model.service;

import exception.BadRequestException;
import model.dao.PaymentRepository;
import model.dao.PaymentRepositoryImpl;
import model.entity.Payment;

import java.math.BigDecimal;
import java.util.List;

public class PaymentServiceImpl implements PaymentService {

    private PaymentRepository paymentRepository;

    public PaymentServiceImpl(){
        this.paymentRepository = new PaymentRepositoryImpl();
    }

    @Override
    public Payment save(Payment payment) {
        if(payment == null
                || payment.getCounterpartyId() == null
                || payment.getCounterpartyId() <= 0
                || payment.getMoney().compareTo(BigDecimal.valueOf(0L)) <= 0) throw new BadRequestException();
        return paymentRepository.insert(payment);
    }

    @Override
    public Payment update(Payment payment) {
        if(payment == null
                || payment.getCounterpartyId() == null
                || payment.getCounterpartyId() <= 0
                || payment.getMoney().compareTo(BigDecimal.valueOf(0L)) <= 0
                || !payment.getIsAccepted()) throw new BadRequestException();
        return paymentRepository.update(payment);
    }
}
