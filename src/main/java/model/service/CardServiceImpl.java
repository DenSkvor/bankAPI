package model.service;

import exception.BadRequestException;
import model.dao.CardRepository;
import model.dao.CardRepositoryImpl;
import model.dto.CardIsActiveDto;
import model.entity.Card;

import java.util.List;

public class CardServiceImpl implements CardService{

    private final CardRepository cardRepository;

    public CardServiceImpl(){
        cardRepository = new CardRepositoryImpl();
    }

    @Override
    public List<Card> getAllByAccountId(Long accountId) {
        if(accountId <= 0) throw new BadRequestException();
        return cardRepository.findAllByAccountId(accountId);
    }

    @Override
    public Card save(Card card) {
        if(card == null || card.getAccountId() <= 0) throw new BadRequestException();
        return cardRepository.insert(card);
    }

    @Override
    public Card update(CardIsActiveDto cardIsActiveDto) {
        if(cardIsActiveDto == null
                || cardIsActiveDto.getId() == null
                || cardIsActiveDto.getAccountId() == null
                || cardIsActiveDto.getId() <= 0
                || cardIsActiveDto.getAccountId() <= 0
                || cardIsActiveDto.getIsActive() == null) throw new BadRequestException();
        return cardRepository.update(cardIsActiveDto);
    }
}
