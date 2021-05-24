package model.service;

import exception.BadRequestException;
import model.dao.BankAccountRepository;
import model.dao.BankAccountRepositoryImpl;
import model.dto.BankAccountMoneyDto;
import model.entity.BankAccount;

import java.math.BigDecimal;

import static util.UtilMethods.*;

public class BankAccountServiceImpl implements BankAccountService{

    private final BankAccountRepository bankAccountRepository;

    public BankAccountServiceImpl() {
        this.bankAccountRepository = new BankAccountRepositoryImpl();
    }

    public BankAccountMoneyDto getById(Long id){
        if(id <= 0) throw new BadRequestException();
        return new BankAccountMoneyDto(bankAccountRepository.findById(id));
    }

    public BankAccount save(BankAccount bankAccount){
        if(bankAccount == null
                || bankAccount.getMoney() == null
                || bankAccount.getMoney().compareTo(BigDecimal.valueOf(0L)) < 0
                || bankAccount.getNumber() == null
                || bankAccount.getUserId() == null
                || bankAccount.getUserId() <= 0) throw new  BadRequestException();
        return bankAccountRepository.insert(bankAccount);
    }

    public BankAccountMoneyDto update(BankAccountMoneyDto bankAccountMoneyDto){
        if(bankAccountMoneyDto == null
                || bankAccountMoneyDto.getId() <= 0) throw new BadRequestException();
        return new BankAccountMoneyDto(bankAccountRepository.update(bankAccountMoneyDto));
    }
}
