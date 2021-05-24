package model.entity;

import source.annotation.Table;

import java.math.BigDecimal;
import java.util.Objects;

@Table(name = "payment_tbl")
public class Payment {

    private Long id;
    private Long counterpartyId;
    private BigDecimal money;
    private boolean isAccepted;

    public Payment() {
    }

    public Payment(Long id, Long counterpartyId, BigDecimal money, boolean isAccepted) {
        this.id = id;
        this.counterpartyId = counterpartyId;
        this.money = money;
        this.isAccepted = isAccepted;
    }


    public Long getId() {
        return id;
    }

    public Long getCounterpartyId() {
        return counterpartyId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public boolean getIsAccepted() {
        return isAccepted;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCounterpartyId(Long counterpartyId) {
        this.counterpartyId = counterpartyId;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public void setIsAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return isAccepted == payment.isAccepted && Objects.equals(id, payment.id) && Objects.equals(counterpartyId, payment.counterpartyId) && Objects.equals(money, payment.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, counterpartyId, money, isAccepted);
    }
}
