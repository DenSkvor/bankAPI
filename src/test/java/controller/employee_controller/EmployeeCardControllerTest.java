package controller.employee_controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.DBConnection;
import model.dto.BankAccountMoneyDto;
import model.dto.CardIsActiveDto;
import model.entity.Card;
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

class EmployeeCardControllerTest {

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
    void updateCardSuccessTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/employee/card");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        CardIsActiveDto reqCardIsActiveDto = new CardIsActiveDto(4L, 3L, true);
        OutputStream output = connection.getOutputStream();
        output.write(om.writeValueAsString(reqCardIsActiveDto).getBytes());
        output.flush();
        output.close();
        Assertions.assertEquals(connection.getResponseCode(), 200);
        InputStream is = connection.getInputStream();
        Card expectedCard = new Card(4L, "3333333333333333", 3L, true);
        Card respCard = om.readValue(is, Card.class);
        is.close();
        Assertions.assertEquals(respCard, expectedCard);
    }

    @Test
    void updateCardNotFoundTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/employee/card");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        CardIsActiveDto reqCardIsActiveDto = new CardIsActiveDto(10L, 10L, true);
        OutputStream output = connection.getOutputStream();
        output.write(om.writeValueAsString(reqCardIsActiveDto).getBytes());
        output.flush();
        output.close();
        Assertions.assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void updateCardEmptyBodyRequestTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/employee/card");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        Assertions.assertEquals(connection.getResponseCode(), 405);
    }

    @Test
    void updateCardBadRequestTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/employee/card/10");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        Assertions.assertEquals(connection.getResponseCode(), 400);
    }

}