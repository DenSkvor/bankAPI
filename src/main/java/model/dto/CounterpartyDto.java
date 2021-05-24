package model.dto;

import java.util.Objects;

public class CounterpartyDto {

    private String number;
    private String firstname;
    private String lastname;
    private boolean isActive;

    public CounterpartyDto() {
    }

    public CounterpartyDto(String number, String firstname, String lastname, boolean isActive) {
        this.number = number;
        this.firstname = firstname;
        this.lastname = lastname;
        this.isActive = isActive;
    }

    public String getNumber() {
        return number;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CounterpartyDto that = (CounterpartyDto) o;
        return isActive == that.isActive && Objects.equals(number, that.number) && Objects.equals(firstname, that.firstname) && Objects.equals(lastname, that.lastname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, firstname, lastname, isActive);
    }
}
