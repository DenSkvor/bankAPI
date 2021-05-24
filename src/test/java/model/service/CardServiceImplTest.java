package model.service;

import exception.BadRequestException;
import model.dto.CardIsActiveDto;
import model.entity.Card;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardServiceImplTest {

    private static CardService cardService;
    private CardIsActiveDto cardIsActiveDto;

    @BeforeAll
    static void init(){
        cardService = new CardServiceImpl();
    }

    @BeforeEach
    void setUp(){
        cardIsActiveDto = new CardIsActiveDto(1L, 1L, true);
    }

    @Test
    void getAllByAccountIdNegativeIdTest() {
        Assertions.assertThrows(BadRequestException.class, () -> {
           cardService.getAllByAccountId(-10L);
        });
    }

    @Test
    void saveNullCardTest() {
        Assertions.assertThrows(BadRequestException.class, () -> {
            cardService.save(null);
        });
    }

    @Test
    void saveNegativeAccountIdTest() {
        Card card = new Card();
        card.setAccountId(-10L);
        Assertions.assertThrows(BadRequestException.class, () -> {
            cardService.save(card);
        });
    }

    @Test
    void updateNullCardIsActiveDtoTest() {
        Assertions.assertThrows(BadRequestException.class, () -> {
            cardService.update(null);
        });
    }

    @Test
    void updateNullIdTest() {
        cardIsActiveDto.setId(null);
        Assertions.assertThrows(BadRequestException.class, () -> {
            cardService.update(cardIsActiveDto);
        });
    }

    @Test
    void updateNullAccountIdTest() {
        cardIsActiveDto.setAccountId(null);
        Assertions.assertThrows(BadRequestException.class, () -> {
            cardService.update(cardIsActiveDto);
        });
    }

    @Test
    void updateNegativeIdTest() {
        cardIsActiveDto.setId(-10L);
        Assertions.assertThrows(BadRequestException.class, () -> {
            cardService.update(cardIsActiveDto);
        });
    }

    @Test
    void updateNegativeAccountIdTest() {
        cardIsActiveDto.setAccountId(-10L);
        Assertions.assertThrows(BadRequestException.class, () -> {
            cardService.update(cardIsActiveDto);
        });
    }

    @Test
    void updateNullIsActiveTest() {
        cardIsActiveDto.setIsActive(null);
        Assertions.assertThrows(BadRequestException.class, () -> {
            cardService.update(cardIsActiveDto);
        });
    }
}