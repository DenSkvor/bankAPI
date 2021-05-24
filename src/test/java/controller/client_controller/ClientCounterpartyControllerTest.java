package controller.client_controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.DBConnection;
import model.dto.CounterpartyDto;
import model.entity.Card;
import model.entity.Counterparty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import web.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static source.Constant.DB_MAIN_INIT_SCRIPT_PATH;
import static source.Constant.DB_TEST_INIT_SCRIPT_PATH;

class ClientCounterpartyControllerTest {

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
    void getAllByAccountId() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/client/counterparty/1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        Assertions.assertEquals(connection.getResponseCode(), 200);
        InputStream is = connection.getInputStream();
        List<CounterpartyDto> listCounterpartyDto = om.readValue
                (is, om.getTypeFactory().constructCollectionType(List.class, CounterpartyDto.class));
        is.close();
        List<CounterpartyDto> expectedListCounterpartyDto = new ArrayList<>(){{
            add(new CounterpartyDto("12222", "Denis", "Skvortsov", true));
            add(new CounterpartyDto("21111", "Ivan", "Ivanov", true));
        }};
        Assertions.assertEquals(listCounterpartyDto, expectedListCounterpartyDto);
    }

    @Test
    void getAllByAccountIdNotFoundTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/client/counterparty/10");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        Assertions.assertEquals(connection.getResponseCode(), 404);
    }

    @Test
    void getAllByAccountIdIncorrectIdTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/client/counterparty/-10");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        Assertions.assertEquals(connection.getResponseCode(), 400);
    }

    @Test
    void successSaveCounterparty() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/client/counterparty");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        Counterparty reqCounterparty = new Counterparty();
        reqCounterparty.setAccountFromId(2L);
        reqCounterparty.setAccountToId(3L);
        OutputStream output = connection.getOutputStream();
        output.write(om.writeValueAsString(reqCounterparty).getBytes());
        output.flush();
        output.close();
        Assertions.assertEquals(connection.getResponseCode(), 201);
        InputStream is = connection.getInputStream();
        Counterparty expectedCounterparty = new Counterparty(4L, 2L, 3L, true);
        Counterparty respCounterparty = om.readValue(is, Counterparty.class);
        is.close();
        Assertions.assertEquals(respCounterparty, expectedCounterparty);
    }

    @Test
    void saveCardEmptyBodyRequestTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/client/counterparty");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        Assertions.assertEquals(connection.getResponseCode(), 405);
    }
}