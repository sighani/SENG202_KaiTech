package kaitech.api.model;

import kaitech.api.model.MenuItem;
import kaitech.util.PaymentType;
import org.joda.money.Money;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

public interface Sale {
    /**
     * Calculates the total cost of the order thus far, returning this amount. This is static such that the total can
     * be calculated before confirming the order and making it a Sale object, in case the order is changed or cancelled.
     *
     * @param order A map of MenuItems to Integer quantities
     * @return A Money amount
     */
    static Money calculateTotalCost(Map<MenuItem, Integer> order) {
        Money total = Money.parse("NZD 0");
        for (Map.Entry<MenuItem, Integer> entry : order.entrySet()) {
            total = total.plus(entry.getKey().getPrice().multipliedBy(entry.getValue()));
        }
        return total;
    }

    /**
     * Calculates how much change should be given if the customer chooses to pay with cash. This is static such that
     * the change can be calculated before confirming the order and making it a Sale object. Throws an
     * IllegalArgumentException if the amount paid is insufficient.
     *
     * @param total      The total cost of the order
     * @param amountPaid The amount paid by the customer
     * @return A Money object showing the change
     * @throws IllegalArgumentException If the amount paid is insufficient
     */
    static Money calculateChange(Money total, Money amountPaid) throws IllegalArgumentException {
        if (amountPaid.isGreaterThan(total) || amountPaid.isEqual(total)) {
            return amountPaid.minus(total);
        } else {
            throw new IllegalArgumentException("The amount paid is not enough.");
        }
    }

    void setDate(LocalDate date);

    void setTime(LocalTime time);

    void setPaymentType(PaymentType paymentType);

    void setNotes(String notes);

    void setTotalPrice(Money totalPrice);

    PaymentType getPaymentType();

    Money getPrice();

    String getDate();

    String getTime();

    String getNotes();

}
