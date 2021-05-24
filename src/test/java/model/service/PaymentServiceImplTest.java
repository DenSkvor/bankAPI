package model.service;

import exception.BadRequestException;
import model.entity.Payment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceImplTest {

    private static PaymentService paymentService;
    private Payment payment;

    @BeforeAll
    static void init() {
        paymentService = new PaymentServiceImpl();
    }

    @BeforeEach
    void setUp(){
        payment = new Payment(1L, 1L, BigDecimal.valueOf(1000).setScale(2, RoundingMode.DOWN), true);
    }

    @Test
    void saveNullPaymentTest() {
        Assertions.assertThrows(BadRequestException.class,  () -> {
            paymentService.save(null);
        });
    }

    @Test
    void saveNullCounterpartyIdTest() {
        payment.setCounterpartyId(null);
        Assertions.assertThrows(BadRequestException.class,  () -> {
            paymentService.save(payment);
        });
    }

    @Test
    void saveNegativeCounterpartyIdTest() {
        payment.setCounterpartyId(-10L);
        Assertions.assertThrows(BadRequestException.class,  () -> {
            paymentService.save(payment);
        });
    }

    @Test
    void saveNegativeManyTest() {
        payment.setMoney(BigDecimal.valueOf(-1000).setScale(2, RoundingMode.DOWN));
        Assertions.assertThrows(BadRequestException.class,  () -> {
            paymentService.save(payment);
        });
    }

    @Test
    void updateNullPaymentTest() {
        Assertions.assertThrows(BadRequestException.class,  () -> {
            paymentService.update(null);
        });
    }

    @Test
    void updateNullCounterpartyIdTest() {
        payment.setCounterpartyId(null);
        Assertions.assertThrows(BadRequestException.class,  () -> {
            paymentService.update(payment);
        });
    }

    @Test
    void updateNegativeCounterpartyIdTest() {
        payment.setCounterpartyId(-10L);
        Assertions.assertThrows(BadRequestException.class,  () -> {
            paymentService.update(payment);
        });
    }

    @Test
    void updateNegativeManyTest() {
        payment.setMoney(BigDecimal.valueOf(-1000).setScale(2, RoundingMode.DOWN));
        Assertions.assertThrows(BadRequestException.class,  () -> {
            paymentService.update(payment);
        });
    }

    @Test
    void updateFalseAcceptedTest() {
        payment.setIsAccepted(false);
        Assertions.assertThrows(BadRequestException.class,  () -> {
            paymentService.update(payment);
        });
    }
}