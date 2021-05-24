package model.service;

import model.dto.BankAccountMoneyDto;
import model.entity.BankAccount;

public interface BankAccountService {

    BankAccountMoneyDto getById(Long id);
    BankAccount save(BankAccount bankAccount);
    BankAccountMoneyDto update(BankAccountMoneyDto bankAccountMoneyDto);
}
