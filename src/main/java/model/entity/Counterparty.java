package model.entity;

import source.annotation.Table;

import java.util.Objects;

@Table(name = "counterparty_tbl")
public class Counterparty {

    private Long id;
    private Long accountFromId;
    private Long accountToId;
    private Boolean isActive;

    public Counterparty() {
    }

    public Counterparty(Long id, Long accountFromId, Long accountToId, Boolean isActive) {
        this.id = id;
        this.accountFromId = accountFromId;
        this.accountToId = accountToId;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public Long getAccountFromId() {
        return accountFromId;
    }

    public Long getAccountToId() {
        return accountToId;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccountFromId(Long accountFromId) {
        this.accountFromId = accountFromId;
    }

    public void setAccountToId(Long accountToId) {
        this.accountToId = accountToId;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Counterparty that = (Counterparty) o;
        return Objects.equals(id, that.id) && Objects.equals(accountFromId, that.accountFromId) && Objects.equals(accountToId, that.accountToId) && Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountFromId, accountToId, isActive);
    }
}
