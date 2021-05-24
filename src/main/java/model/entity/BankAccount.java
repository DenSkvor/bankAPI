package model.entity;

import source.annotation.Table;

import java.math.BigDecimal;
import java.util.Objects;

@Table(name = "bank_account_tbl")
public class BankAccount {

    private Long id;
    private String number;
    private BigDecimal money;
    private Long userId;

    public BankAccount() {
    }

    public BankAccount(Long id, String number, BigDecimal money, Long userId) {
        this.id = id;
        this.number = number;
        this.money = money;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public Long getUserId() {
        return userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(id, that.id) && Objects.equals(number, that.number) && Objects.equals(money, that.money) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, money, userId);
    }
}
