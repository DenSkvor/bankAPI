package model.dao;

import model.dto.CardIsActiveDto;
import model.entity.Card;

import java.util.List;

public interface CardRepository {

    List<Card> findAllByAccountId(Long accountId);
    Card insert(Card card);
    Card update(CardIsActiveDto cardIsActiveDto);

}
