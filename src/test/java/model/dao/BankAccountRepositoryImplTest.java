package model.dao;

import exception.NotFoundException;
import model.DBConnection;
import model.dto.BankAccountMoneyDto;
import model.entity.BankAccount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import source.annotation.Table;
import web.Server;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static source.Constant.DB_TEST_INIT_SCRIPT_PATH;

class BankAccountRepositoryImplTest {

    private static BankAccountRepository bankAccountRepository;
    private static final String THIS_TABLE = BankAccount.class.getAnnotation(Table.class).name();


    @BeforeAll
    static void init() throws IOException, ClassNotFoundException {
        Server.getInstance().startServer();
        DBConnection.getInstance().start();
        bankAccountRepository = new BankAccountRepositoryImpl();
    }

    @BeforeEach
    public void setUp() throws ClassNotFoundException {
        DBConnection.getInstance().init(DB_TEST_INIT_SCRIPT_PATH);
    }

    @Test
    void findByIdSuccessTest() {
        BankAccount expectedBankAccount = new BankAccount(4L, "31111", BigDecimal.valueOf(4500).setScale(2, RoundingMode.DOWN), 3L);
        BankAccount testBankAccount = bankAccountRepository.findById(4L);
        Assertions.assertEquals(testBankAccount, expectedBankAccount);
    }

    @Test
    void findByIdNotFoundTest() {
        Assertions.assertThrows(NotFoundException.class, () -> {
            bankAccountRepository.findById(10L);
        });
    }

    @Test
    void insertSuccessTest() throws SQLException {
        BankAccount insertBankAccount = new BankAccount();
        insertBankAccount.setNumber("000000");
        insertBankAccount.setMoney(BigDecimal.valueOf(1000).setScale(2, RoundingMode.DOWN));
        insertBankAccount.setUserId(1L);
        BankAccount expectedBankAccount = new BankAccount(7L, "000000", BigDecimal.valueOf(1000).setScale(2, RoundingMode.DOWN), 1L);
        Connection dbConnection = DBConnection.getInstance().getConnection();
        ResultSet rs = dbConnection.createStatement().executeQuery("select count(*) from " + THIS_TABLE + ";");
        rs.next();
        int expectedBankAccountCount = rs.getInt(1) + 1;
        dbConnection.close();
        Assertions.assertEquals(expectedBankAccountCount, 7);
        Assertions.assertEquals(expectedBankAccount, bankAccountRepository.insert(insertBankAccount));
    }

    @Test
    void updateSuccessTest() {
        BankAccountMoneyDto bankAccountMoneyDto = new BankAccountMoneyDto(1L, BigDecimal.valueOf(1000).setScale(2, RoundingMode.DOWN));
        BankAccount expectedBankAccount = new BankAccount(1L, "11111", BigDecimal.valueOf(2000).setScale(2, RoundingMode.DOWN), 1L);
        Assertions.assertEquals(expectedBankAccount, bankAccountRepository.update(bankAccountMoneyDto));
    }

    @Test
    void updateNotFoundTest() {
        BankAccountMoneyDto bankAccountMoneyDto = new BankAccountMoneyDto(10L, BigDecimal.valueOf(1000).setScale(2, RoundingMode.DOWN));
        Assertions.assertThrows(NotFoundException.class, () -> {
            bankAccountRepository.update(bankAccountMoneyDto);
        });
    }
}