package controller.employee_controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.DBConnection;
import model.dto.CardIsActiveDto;
import model.entity.BankAccount;
import model.entity.Card;
import model.entity.Payment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import source.annotation.Table;
import web.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static source.Constant.DB_MAIN_INIT_SCRIPT_PATH;
import static source.Constant.DB_TEST_INIT_SCRIPT_PATH;

class EmployeePaymentControllerTest {

    private static ObjectMapper om;
    public static String ACCOUNT_TABLE = BankAccount.class.getAnnotation(Table.class).name();


    @BeforeAll
    public static void init() throws ClassNotFoundException, IOException {
        Server.getInstance().startServer();
        DBConnection.getInstance().start();
        om = new ObjectMapper();
    }

    @BeforeEach
    public void setUp() throws ClassNotFoundException {
        DBConnection.getInstance().init(DB_TEST_INIT_SCRIPT_PATH);
    }

    @Test
    void updatePaymentSuccessTest() throws IOException, SQLException {
        URL url = new URL("http://localhost:8000/api/v1/employee/payment");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        Payment reqPayment = new Payment();
        reqPayment.setId(3L);
        reqPayment.setCounterpartyId(3L);
        reqPayment.setMoney(BigDecimal.valueOf(2000));
        reqPayment.setIsAccepted(true);
        OutputStream output = connection.getOutputStream();
        output.write(om.writeValueAsString(reqPayment).getBytes());
        output.flush();
        output.close();
        Assertions.assertEquals(connection.getResponseCode(), 200);
        InputStream is = connection.getInputStream();
        Payment expectedPayment = new Payment(3L, 3L, BigDecimal.valueOf(2000).setScale(2, RoundingMode.DOWN), true);
        Payment respPayment = om.readValue(is, Payment.class);
        is.close();
        Assertions.assertEquals(respPayment, expectedPayment);
        Connection bdConnection = DBConnection.getInstance().getConnection();
        ResultSet rs = bdConnection.createStatement().executeQuery("select money from " + ACCOUNT_TABLE + " where bank_account_id = 4");
        rs.next();
        Assertions.assertEquals(rs.getBigDecimal(1), BigDecimal.valueOf(2500).setScale(2, RoundingMode.DOWN));
        rs = bdConnection.createStatement().executeQuery("select money from " + ACCOUNT_TABLE + " where bank_account_id = 1");
        rs.next();
        Assertions.assertEquals(rs.getBigDecimal(1), BigDecimal.valueOf(3000).setScale(2, RoundingMode.DOWN));
        bdConnection.close();
    }

    @Test
    void updatePaymentNotFoundTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/employee/payment");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        Payment reqPayment = new Payment();
        reqPayment.setId(10L);
        reqPayment.setCounterpartyId(3L);
        reqPayment.setMoney(BigDecimal.valueOf(2000));
        reqPayment.setIsAccepted(true);
        OutputStream output = connection.getOutputStream();
        output.write(om.writeValueAsString(reqPayment).getBytes());
        output.flush();
        output.close();
        Assertions.assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void updatePaymentAccountNotFoundTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/employee/payment");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        Payment reqPayment = new Payment();
        reqPayment.setId(3L);
        reqPayment.setCounterpartyId(10L);
        reqPayment.setMoney(BigDecimal.valueOf(2000));
        reqPayment.setIsAccepted(true);
        OutputStream output = connection.getOutputStream();
        output.write(om.writeValueAsString(reqPayment).getBytes());
        output.flush();
        output.close();
        Assertions.assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void updatePaymentEmptyBodyRequestTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/employee/payment");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        Assertions.assertEquals(connection.getResponseCode(), 405);
    }

    @Test
    void updatePaymentBadRequestTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/employee/payment/10");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        Assertions.assertEquals(connection.getResponseCode(), 400);
    }

}