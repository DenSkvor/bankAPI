package model.dto;

import model.entity.BankAccount;

import java.math.BigDecimal;
import java.util.Objects;

public class BankAccountMoneyDto {

    private Long id;
    private BigDecimal money;

    public BankAccountMoneyDto() {
    }

    public BankAccountMoneyDto(Long id, BigDecimal money) {
        this.id = id;
        this.money = money;
    }

    public BankAccountMoneyDto(BankAccount bankAccount){
        this.id = bankAccount.getId();
        this.money = bankAccount.getMoney();
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccountMoneyDto that = (BankAccountMoneyDto) o;
        return id.equals(that.id) && money.equals(that.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, money);
    }
}
