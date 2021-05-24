package model.service;

import model.dto.CardIsActiveDto;
import model.entity.Card;

import java.util.List;

public interface CardService {

    List<Card> getAllByAccountId(Long accountId);
    Card save(Card card);
    Card update(CardIsActiveDto cardIsActiveDto);
}
