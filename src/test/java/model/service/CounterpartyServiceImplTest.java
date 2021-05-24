package model.service;

import exception.BadRequestException;
import model.dto.CounterpartyDto;
import model.entity.Counterparty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CounterpartyServiceImplTest {

    private static CounterpartyService counterpartyService;
    private Counterparty counterparty;

    @BeforeAll
    static void init(){
        counterpartyService = new CounterpartyServiceImpl();
    }

    @BeforeEach
    void setUp(){
        counterparty = new Counterparty(1L, 1L, 2L, true);
    }

    @Test
    void getAllByAccountIdNegativeIdTest() {
        Assertions.assertThrows(BadRequestException.class,  () -> {
            counterpartyService.getAllByAccountId(-10L);
        });
    }

    @Test
    void saveNullCounterpartyTest() {
        Assertions.assertThrows(BadRequestException.class,  () -> {
            counterpartyService.save(null);
        });
    }

    @Test
    void saveNullAccountFromIdTest() {
        counterparty.setAccountFromId(null);
        Assertions.assertThrows(BadRequestException.class,  () -> {
            counterpartyService.save(counterparty);
        });
    }

    @Test
    void saveNullAccountToIdTest() {
        counterparty.setAccountToId(null);
        Assertions.assertThrows(BadRequestException.class,  () -> {
            counterpartyService.save(counterparty);
        });
    }

    @Test
    void saveNegativeAccountFromIdTest() {
        counterparty.setAccountFromId(-10L);
        Assertions.assertThrows(BadRequestException.class,  () -> {
            counterpartyService.save(counterparty);
        });
    }

    @Test
    void saveNegativeAccountToIdTest() {
        counterparty.setAccountToId(-10L);
        Assertions.assertThrows(BadRequestException.class,  () -> {
            counterpartyService.save(counterparty);
        });
    }

    @Test
    void saveSameAccountFromAndToIdTest() {
        counterparty.setAccountFromId(1L);
        counterparty.setAccountToId(1L);
        Assertions.assertThrows(BadRequestException.class,  () -> {
            counterpartyService.save(counterparty);
        });
    }
}