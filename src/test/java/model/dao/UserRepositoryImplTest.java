package model.dao;

import model.DBConnection;
import model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import source.annotation.Table;
import web.Server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static source.Constant.DB_TEST_INIT_SCRIPT_PATH;

class UserRepositoryImplTest {

    private static UserRepository userRepository;
    private static final String THIS_TABLE = User.class.getAnnotation(Table.class).name();

    @BeforeAll
    static void init() throws IOException, ClassNotFoundException {
        Server.getInstance().startServer();
        DBConnection.getInstance().start();
        userRepository = new UserRepositoryImpl();
    }

    @BeforeEach
    public void setUp() throws ClassNotFoundException {
        DBConnection.getInstance().init(DB_TEST_INIT_SCRIPT_PATH);
    }

    @Test
    void insertSuccessTest() throws SQLException {
        User insertUser = new User();
        insertUser.setFirstName("Pavel");
        insertUser.setLastName("Pavlovich");
        User expectedUser = new User(5L, "Pavel", "Pavlovich");
        Connection dbConnection = DBConnection.getInstance().getConnection();
        ResultSet rs = dbConnection.createStatement().executeQuery("select count(*) from " + THIS_TABLE + ";");
        rs.next();
        int expectedUserCount = rs.getInt(1) + 1;
        dbConnection.close();
        Assertions.assertEquals(expectedUser, userRepository.insert(insertUser));
        Assertions.assertEquals(expectedUserCount, 5);
    }
}