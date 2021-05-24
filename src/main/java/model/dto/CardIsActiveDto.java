package model.dto;

import model.entity.Card;

import java.util.Objects;

public class CardIsActiveDto {

    private Long id;
    private Long accountId;
    private Boolean isActive;

    public CardIsActiveDto() {
    }

    public CardIsActiveDto(Long id, Long accountId, Boolean isActive) {
        this.id = id;
        this.accountId = accountId;
        this.isActive = isActive;
    }

    public CardIsActiveDto(Card card){
        this.id = card.getId();
        this.accountId = card.getAccountId();
        this.isActive = card.getIsActive();
    }

    public Long getId() {
        return id;
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
        CardIsActiveDto that = (CardIsActiveDto) o;
        return Objects.equals(id, that.id) && Objects.equals(accountId, that.accountId) && Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, isActive);
    }
}
