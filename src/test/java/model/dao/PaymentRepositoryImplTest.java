package model.dao;

import exception.NotFoundException;
import model.DBConnection;
import model.entity.BankAccount;
import model.entity.Payment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import source.annotation.Table;
import web.Server;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static source.Constant.DB_TEST_INIT_SCRIPT_PATH;

class PaymentRepositoryImplTest {

    private static PaymentRepository paymentRepository;
    private static final String THIS_TABLE = Payment.class.getAnnotation(Table.class).name();
    private static final String ACCOUNT_TABLE = BankAccount.class.getAnnotation(Table.class).name();

    @BeforeAll
    static void init() throws IOException, ClassNotFoundException {
        Server.getInstance().startServer();
        DBConnection.getInstance().start();
        paymentRepository = new PaymentRepositoryImpl();
    }

    @BeforeEach
    public void setUp() throws ClassNotFoundException {
        DBConnection.getInstance().init(DB_TEST_INIT_SCRIPT_PATH);
    }

    @Test
    void insertSuccessTest() throws SQLException {
        Payment insertPayment = new Payment();
        insertPayment.setCounterpartyId(3L);
        insertPayment.setMoney(BigDecimal.valueOf(1000).setScale(2, RoundingMode.DOWN));
        Payment expectedPayment = new Payment(4L, 3L, BigDecimal.valueOf(1000).setScale(2, RoundingMode.DOWN), false);
        Connection dbConnection = DBConnection.getInstance().getConnection();
        ResultSet rs = dbConnection.createStatement().executeQuery("select count(*) from " + THIS_TABLE + ";");
        rs.next();
        int expectedPaymentCount = rs.getInt(1) + 1;
        dbConnection.close();
        Assertions.assertEquals(expectedPayment, paymentRepository.insert(insertPayment));
        Assertions.assertEquals(expectedPaymentCount, 4);
    }

    @Test
    void updateSuccessTest() throws SQLException {
        Payment updPayment = new Payment();
        updPayment.setId(3L);
        updPayment.setCounterpartyId(3L);
        updPayment.setMoney(BigDecimal.valueOf(2000));
        updPayment.setIsAccepted(true);
        Payment expectedPayment = new Payment(3L, 3L, BigDecimal.valueOf(2000).setScale(2, RoundingMode.DOWN), true);
        Assertions.assertEquals(expectedPayment,paymentRepository.update(updPayment));
        Connection dbConnection = DBConnection.getInstance().getConnection();
        ResultSet rs = dbConnection.createStatement().executeQuery("select money from " + ACCOUNT_TABLE + " where bank_account_id = 4");
        rs.next();
        Assertions.assertEquals(rs.getBigDecimal(1), BigDecimal.valueOf(2500).setScale(2, RoundingMode.DOWN));
        rs = dbConnection.createStatement().executeQuery("select money from " + ACCOUNT_TABLE + " where bank_account_id = 1");
        rs.next();
        Assertions.assertEquals(rs.getBigDecimal(1), BigDecimal.valueOf(3000).setScale(2, RoundingMode.DOWN));
        dbConnection.close();
    }

    @Test
    void updatePaymentNotFoundTest() throws SQLException {
        Payment updPayment = new Payment();
        updPayment.setId(10L);
        updPayment.setCounterpartyId(3L);
        updPayment.setMoney(BigDecimal.valueOf(2000));
        updPayment.setIsAccepted(true);

        Assertions.assertThrows(NotFoundException.class, () -> {
            paymentRepository.update(updPayment);
        });
    }

    @Test
    void updateCounterpartyNotFoundTest() throws SQLException {
        Payment updPayment = new Payment();
        updPayment.setId(3L);
        updPayment.setCounterpartyId(10L);
        updPayment.setMoney(BigDecimal.valueOf(2000));
        updPayment.setIsAccepted(true);

        Assertions.assertThrows(NotFoundException.class, () -> {
            paymentRepository.update(updPayment);
        });
    }
}