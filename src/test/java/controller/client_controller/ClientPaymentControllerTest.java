package controller.client_controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.DBConnection;
import model.entity.Counterparty;
import model.entity.Payment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import web.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static source.Constant.DB_MAIN_INIT_SCRIPT_PATH;
import static source.Constant.DB_TEST_INIT_SCRIPT_PATH;

class ClientPaymentControllerTest {

    private static ObjectMapper om;

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
    void successSavePayment() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/client/payment");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        Payment reqPayment = new Payment();
        reqPayment.setCounterpartyId(3L);
        reqPayment.setMoney(BigDecimal.valueOf(1000).setScale(2, RoundingMode.DOWN));
        OutputStream output = connection.getOutputStream();
        output.write(om.writeValueAsString(reqPayment).getBytes());
        output.flush();
        output.close();
        Assertions.assertEquals(connection.getResponseCode(), 201);
        InputStream is = connection.getInputStream();
        Payment expectedPayment = new Payment(4L, 3L, BigDecimal.valueOf(1000).setScale(2, RoundingMode.DOWN), false);
        Payment respPayment = om.readValue(is, Payment.class);
        is.close();
        Assertions.assertEquals(respPayment, expectedPayment);
    }

    @Test
    void savePaymentEmptyBodyRequestTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/client/payment");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        Assertions.assertEquals(connection.getResponseCode(), 405);
    }

}