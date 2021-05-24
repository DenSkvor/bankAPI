package model.service;

import exception.BadRequestException;
import model.dao.CounterpartyRepository;
import model.dao.CounterpartyRepositoryImpl;
import model.dto.CounterpartyDto;
import model.entity.Counterparty;

import java.util.List;

public class CounterpartyServiceImpl implements CounterpartyService{

    private final CounterpartyRepository counterpartyRepository;

    public CounterpartyServiceImpl() {
        this.counterpartyRepository = new CounterpartyRepositoryImpl();
    }

    @Override
    public List<CounterpartyDto> getAllByAccountId(Long accountId) {
        if(accountId <= 0) throw new BadRequestException();
        return counterpartyRepository.findAllByAccountId(accountId);
    }

    @Override
    public Counterparty save(Counterparty counterparty) {
        if(counterparty == null
                || counterparty.getAccountFromId() == null
                || counterparty.getAccountToId() == null
                || counterparty.getAccountFromId() <= 0
                || counterparty.getAccountToId() <= 0
                || counterparty.getAccountFromId().equals(counterparty.getAccountToId())) throw new BadRequestException();
        return counterpartyRepository.insert(counterparty);
    }
}
