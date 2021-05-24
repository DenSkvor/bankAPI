package model.dao;

import model.dto.BankAccountMoneyDto;
import model.entity.BankAccount;
public interface BankAccountRepository {

    BankAccount findById(Long id);
    BankAccount insert(BankAccount bankAccount);
    BankAccount update(BankAccountMoneyDto bankAccountMoneyDto);
}
