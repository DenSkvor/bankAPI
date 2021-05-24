package model.dao;

import exception.NotFoundException;
import model.DBConnection;
import model.dto.CounterpartyDto;
import model.entity.Counterparty;
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
import java.util.ArrayList;
import java.util.List;

import static source.Constant.DB_TEST_INIT_SCRIPT_PATH;

class CounterpartyRepositoryImplTest {

    private static CounterpartyRepository counterpartyRepository;
    private static final String THIS_TABLE = Counterparty.class.getAnnotation(Table.class).name();


    @BeforeAll
    static void init() throws IOException, ClassNotFoundException {
        Server.getInstance().startServer();
        DBConnection.getInstance().start();
        counterpartyRepository = new CounterpartyRepositoryImpl();
    }

    @BeforeEach
    public void setUp() throws ClassNotFoundException {
        DBConnection.getInstance().init(DB_TEST_INIT_SCRIPT_PATH);
    }

    @Test
    void findAllByAccountIdSuccessTest() {
        List<CounterpartyDto> expectedListCounterpartyDto = new ArrayList<>(){{
            add(new CounterpartyDto("12222", "Denis", "Skvortsov", true));
            add(new CounterpartyDto("21111", "Ivan", "Ivanov", true));
        }};
        Assertions.assertEquals(counterpartyRepository.findAllByAccountId(1L), expectedListCounterpartyDto);
    }

    @Test
    void findAllByAccountIdNotFoundTest() {
        Assertions.assertThrows(NotFoundException.class, () -> {
            counterpartyRepository.findAllByAccountId(10L);
        });
    }

    @Test
    void insert() throws SQLException {
        Counterparty insertCounterparty = new Counterparty();
        insertCounterparty.setAccountFromId(2L);
        insertCounterparty.setAccountToId(3L);
        Counterparty expectedCounterparty = new Counterparty(4L, 2L, 3L, true);
        Connection dbConnection = DBConnection.getInstance().getConnection();
        ResultSet rs = dbConnection.createStatement().executeQuery("select count(*) from " + THIS_TABLE + ";");
        rs.next();
        int expectedCounterpartyCount = rs.getInt(1) + 1;
        dbConnection.close();
        Assertions.assertEquals(expectedCounterparty, counterpartyRepository.insert(insertCounterparty));
        Assertions.assertEquals(expectedCounterpartyCount, 4);
    }
}