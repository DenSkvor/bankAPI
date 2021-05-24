package model.service;

import model.dto.CounterpartyDto;
import model.entity.Counterparty;

import java.util.List;

public interface CounterpartyService {

    List<CounterpartyDto> getAllByAccountId(Long accountId);
    Counterparty save(Counterparty counterparty);
}
