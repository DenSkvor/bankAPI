package controller.employee_controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.DBConnection;
import model.entity.BankAccount;
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

class EmployeeBankAccountControllerTest {

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
    void successSaveBankAccount() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/employee/account");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        BankAccount reqBankAccount = new BankAccount();
        reqBankAccount.setNumber("112233445566");
        reqBankAccount.setMoney(BigDecimal.valueOf(0).setScale(2, RoundingMode.DOWN));
        reqBankAccount.setUserId(1L);
        OutputStream output = connection.getOutputStream();
        output.write(om.writeValueAsString(reqBankAccount).getBytes());
        output.flush();
        output.close();
        Assertions.assertEquals(connection.getResponseCode(), 201);
        InputStream is = connection.getInputStream();
        BankAccount expectedBankAccount = new BankAccount(7L, "112233445566", BigDecimal.valueOf(0).setScale(2, RoundingMode.DOWN), 1L);
        BankAccount respBankAccount = om.readValue(is, BankAccount.class);
        is.close();
        Assertions.assertEquals(respBankAccount, expectedBankAccount);
    }

    @Test
    void saveBankAccountEmptyBodyRequestTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/employee/account");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        Assertions.assertEquals(connection.getResponseCode(), 405);
    }

}