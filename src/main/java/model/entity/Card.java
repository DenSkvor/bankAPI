package model.entity;

import source.annotation.Table;

import java.util.Objects;

@Table(name = "card_tbl")
public class Card {

    private Long id;
    private String number;
    private Long accountId;
    private Boolean isActive;

    public Card(){}

    public Card(Long id, String number, Long accountId, Boolean isActive) {
        this.id = id;
        this.number = number;
        this.accountId = accountId;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(id, card.id) && Objects.equals(number, card.number) && Objects.equals(accountId, card.accountId) && Objects.equals(isActive, card.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, accountId, isActive);
    }
}
