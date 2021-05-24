package controller.client_controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.DBConnection;
import model.dto.BankAccountMoneyDto;
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
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static source.Constant.DB_MAIN_INIT_SCRIPT_PATH;
import static source.Constant.DB_TEST_INIT_SCRIPT_PATH;

class ClientCardControllerTest {

    private static ObjectMapper om;

    @BeforeAll
    static void init() throws ClassNotFoundException, IOException {
        Server.getInstance().startServer();
        DBConnection.getInstance().start();
        om = new ObjectMapper();
    }

    @BeforeEach
    public void setUp() throws ClassNotFoundException {
        DBConnection.getInstance().init(DB_TEST_INIT_SCRIPT_PATH);
    }

    @Test
    void successGetAllByAccountId() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/client/card/1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        Assertions.assertEquals(connection.getResponseCode(), 200);
        InputStream is = connection.getInputStream();
        List<Card> listCard = om.readValue
                (is, om.getTypeFactory().constructCollectionType(List.class, Card.class));
        is.close();
        List<Card> expectedListCard = new ArrayList<>(){{
            add(new Card(1L, "1111111111111111", 1L, false));
            add(new Card(2L, "1111111122222222", 1L, true));
        }};
        Assertions.assertEquals(listCard, expectedListCard);
    }

    @Test
    void getAllByAccountIdNotFoundTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/client/card/10");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        Assertions.assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void getAllByAccountIdIncorrectIdTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/client/card/-10");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        Assertions.assertEquals(connection.getResponseCode(), 400);
    }

    @Test
    void successSaveCardTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/client/card");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        Card reqCard = new Card();
        reqCard.setNumber("1111111122222333");
        reqCard.setAccountId(1L);
        OutputStream output = connection.getOutputStream();
        output.write(om.writeValueAsString(reqCard).getBytes());
        output.flush();
        output.close();
        Assertions.assertEquals(connection.getResponseCode(), 201);
        InputStream is = connection.getInputStream();
        Card expectedCard = new Card(8L, "1111111122222333", 1L, false);
        Card respCard = om.readValue(is, Card.class);
        is.close();
        Assertions.assertEquals(respCard, expectedCard);
    }

    @Test
    void saveCardEmptyBodyRequestTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/client/card");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        Assertions.assertEquals(connection.getResponseCode(), 405);
    }
}