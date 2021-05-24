package controller.employee_controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.DBConnection;
import model.entity.BankAccount;
import model.entity.User;
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

class EmployeeUserControllerTest {

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
    void successSaveUser() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/employee/user");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        User reqUser = new User();
        reqUser.setFirstName("Pavel");
        reqUser.setLastName("Pavlovich");
        OutputStream output = connection.getOutputStream();
        output.write(om.writeValueAsString(reqUser).getBytes());
        output.flush();
        output.close();
        Assertions.assertEquals(connection.getResponseCode(), 201);
        InputStream is = connection.getInputStream();
        User expectedUser = new User(5L, "Pavel", "Pavlovich");
        User respUser = om.readValue(is, User.class);
        is.close();
        Assertions.assertEquals(respUser, expectedUser);
    }

    @Test
    void saveUserEmptyBodyRequestTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/employee/user");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        Assertions.assertEquals(connection.getResponseCode(), 405);
    }

    @Test
    void saveUserBadRequestTest() throws IOException {
        URL url = new URL("http://localhost:8000/api/v1/employee/user/10");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        Assertions.assertEquals(connection.getResponseCode(), 400);
    }
}