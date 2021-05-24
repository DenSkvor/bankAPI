package model.dto;


public class PaymentAcceptDto {

    private Long paymentId;
    private boolean isAccepted;

    public PaymentAcceptDto() {
    }

    public PaymentAcceptDto(Long paymentId, boolean isAccepted) {
        this.paymentId = paymentId;
        this.isAccepted = isAccepted;
    }


    public Long getPaymentId() {
        return paymentId;
    }

    public boolean getIsAccepted() {
        return isAccepted;
    }

    public void setPaymentId(Long id) {
        this.paymentId = id;
    }

    public void setIsAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
