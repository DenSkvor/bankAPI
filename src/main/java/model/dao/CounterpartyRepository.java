package model.dao;

import model.dto.CounterpartyDto;
import model.entity.Counterparty;

import java.util.List;

public interface CounterpartyRepository {

    List<CounterpartyDto> findAllByAccountId(Long accountId);
    Counterparty insert(Counterparty counterparty);

}
