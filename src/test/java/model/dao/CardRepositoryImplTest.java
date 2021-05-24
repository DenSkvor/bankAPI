package model.dao;

import exception.NotFoundException;
import model.DBConnection;
import model.dto.CardIsActiveDto;
import model.entity.Card;
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

import static org.junit.jupiter.api.Assertions.*;
import static source.Constant.DB_TEST_INIT_SCRIPT_PATH;

class CardRepositoryImplTest {

    private static CardRepository cardRepository;
    private static final String THIS_TABLE = Card.class.getAnnotation(Table.class).name();

    @BeforeAll
    static void init() throws IOException, ClassNotFoundException {
        Server.getInstance().startServer();
        DBConnection.getInstance().start();
        cardRepository = new CardRepositoryImpl();
    }

    @BeforeEach
    public void setUp() throws ClassNotFoundException {
        DBConnection.getInstance().init(DB_TEST_INIT_SCRIPT_PATH);
    }

    @Test
    void findAllByAccountIdSuccessTest() {
        List<Card> expectedListCard = new ArrayList<>(){{
            add(new Card(1L, "1111111111111111", 1L, false));
            add(new Card(2L, "1111111122222222", 1L, true));
        }};
        Assertions.assertEquals(cardRepository.findAllByAccountId(1L), expectedListCard);
    }

    @Test
    void findAllByAccountIdNotFoundTest() {
        Assertions.assertThrows(NotFoundException.class, () -> {
            cardRepository.findAllByAccountId(10L);
        });
    }

    @Test
    void insertSuccessTest() throws SQLException {
        Card insertCard = new Card();
        insertCard.setNumber("00000");
        insertCard.setAccountId(1L);
        Card expectedCard = new Card(8L, "00000", 1L, false);
        Connection dbConnection = DBConnection.getInstance().getConnection();
        ResultSet rs = dbConnection.createStatement().executeQuery("select count(*) from " + THIS_TABLE + ";");
        rs.next();
        int expectedCardCount = rs.getInt(1) + 1;
        dbConnection.close();
        Assertions.assertEquals(expectedCard, cardRepository.insert(insertCard));
        Assertions.assertEquals(expectedCardCount, 8);

    }

    @Test
    void updateSuccessTest() {
        CardIsActiveDto updCardIsActiveDto = new CardIsActiveDto(4L, 3L, true);
        Card expectedCard = new Card(4L, "3333333333333333", 3L, true);
        Assertions.assertEquals(expectedCard, cardRepository.update(updCardIsActiveDto));
    }

    @Test
    void updateCardNotFoundTest() {
        CardIsActiveDto updCardIsActiveDto = new CardIsActiveDto(10L, 3L, true);
        Assertions.assertThrows(NotFoundException.class, () -> {
            cardRepository.update(updCardIsActiveDto);
        });
    }
}