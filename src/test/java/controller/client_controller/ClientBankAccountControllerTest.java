package controller.client_controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.DBConnection;
import model.dao.BankAccountRepository;
import model.dao.BankAccountRepositoryImpl;
import model.dto.BankAccountMoneyDto;
import model.entity.BankAccount;
import org.junit.jupiter.api.*;
import web.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static source.Constant.DB_MAIN_INIT_SCRIPT_PATH;
import static source.Constant.DB_TEST_INIT_SCRIPT_PATH;

class ClientBankAccountControllerTest {

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
    void getAccountByIdSuccessTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/client/account/1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        Assertions.assertEquals(connection.getResponseCode(), 200);
        InputStream is = connection.getInputStream();
        BankAccountMoneyDto bankAccountMoneyDto = om.readValue(is, BankAccountMoneyDto.class);
        is.close();
        Assertions.assertEquals(bankAccountMoneyDto.getId(), 1);
        Assertions.assertEquals(bankAccountMoneyDto.getMoney(), BigDecimal.valueOf(1000).setScale(2, RoundingMode.DOWN));
    }

    @Test
    void getAccountByIdAccountNotFoundTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/client/account/10");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        Assertions.assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void getAccountByIdIncorrectIdTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/client/account/-10");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        Assertions.assertEquals(connection.getResponseCode(), 400);
    }

    @Test
    void updateAccountSuccessTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/client/account");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        BankAccountMoneyDto reqBankAccountMoneyDto = new BankAccountMoneyDto(1L, BigDecimal.valueOf(1000).setScale(2, RoundingMode.DOWN));
        OutputStream output = connection.getOutputStream();
        output.write(om.writeValueAsString(reqBankAccountMoneyDto).getBytes());
        output.flush();
        output.close();
        Assertions.assertEquals(connection.getResponseCode(), 200);
        InputStream is = connection.getInputStream();
        BankAccountMoneyDto expectedBankAccountMoneyDto = new BankAccountMoneyDto(1L, BigDecimal.valueOf(2000).setScale(2, RoundingMode.DOWN));
        BankAccountMoneyDto respBankAccountMoneyDto = om.readValue(is, BankAccountMoneyDto.class);
        is.close();
        Assertions.assertEquals(respBankAccountMoneyDto, expectedBankAccountMoneyDto);
    }

    @Test
    void updateAccountNotFoundTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/client/account");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        BankAccountMoneyDto reqBankAccountMoneyDto = new BankAccountMoneyDto(10L, BigDecimal.valueOf(1000).setScale(2, RoundingMode.DOWN));
        OutputStream output = connection.getOutputStream();
        output.write(om.writeValueAsString(reqBankAccountMoneyDto).getBytes());
        output.flush();
        output.close();
        Assertions.assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void updateAccountEmptyBodyRequestTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/client/account");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        Assertions.assertEquals(connection.getResponseCode(), 405);
    }

    @Test
    void updateAccountBadRequestTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/client/account/10");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        Assertions.assertEquals(connection.getResponseCode(), 400);
    }

}