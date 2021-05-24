package model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import exception.BadRequestException;
import model.DBConnection;
import model.dto.BankAccountMoneyDto;
import model.entity.BankAccount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import web.Server;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountServiceImplTest {

    private static BankAccountService bankAccountService;
    private BankAccount bankAccount;

    @BeforeAll
    static void init() {
        bankAccountService = new BankAccountServiceImpl();
    }

    @BeforeEach
    void setUp(){
        bankAccount = new BankAccount(1L, "11111", BigDecimal.valueOf(1000).setScale(2, RoundingMode.DOWN), 1L);
    }

    @Test
    void getByIdIncorrectIdTest() {
        Assertions.assertThrows(BadRequestException.class,  () -> {
            bankAccountService.getById(-10L);
        });
    }

    @Test
    void saveNullBankAccountTest() {
        Assertions.assertThrows(BadRequestException.class,  () -> {
            bankAccountService.save(null);
        });
    }

    @Test
    void saveNullMoneyTest() {
        bankAccount.setMoney(null);
        Assertions.assertThrows(BadRequestException.class,  () -> {
            bankAccountService.save(bankAccount);
        });
    }

    @Test
    void saveNegativeMoneyTest() {
        bankAccount.setMoney(BigDecimal.valueOf(-1000).setScale(2, RoundingMode.DOWN));
        Assertions.assertThrows(BadRequestException.class,  () -> {
            bankAccountService.save(null);
        });
    }

    @Test
    void saveNullNumberTest() {
        bankAccount.setNumber(null);
        Assertions.assertThrows(BadRequestException.class,  () -> {
            bankAccountService.save(null);
        });
    }

    @Test
    void saveNullUserIdTest() {
        bankAccount.setUserId(null);
        Assertions.assertThrows(BadRequestException.class,  () -> {
            bankAccountService.save(null);
        });
    }

    @Test
    void saveNegativeUserIdTest() {
        bankAccount.setUserId(-1L);
        Assertions.assertThrows(BadRequestException.class,  () -> {
            bankAccountService.save(null);
        });
    }

    @Test
    void updateNullBankAccountDtoTest(){
        Assertions.assertThrows(BadRequestException.class,  () -> {
            bankAccountService.update(null);
        });
    }

    @Test
    void updateNegativeIdTest(){
        Assertions.assertThrows(BadRequestException.class,  () -> {
            bankAccountService.update(new BankAccountMoneyDto(-10L, BigDecimal.valueOf(1000).setScale(2, RoundingMode.DOWN)));
        });
    }
}