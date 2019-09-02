package kaitech.api.model;

import kaitech.util.PaymentType;
import org.joda.money.Money;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

/**
 * An interface that represents a sales record. A sales record is created whenever an order is made. It contains
 * important details about the sale that is useful for data collection.
 *
 * @author Julia Harrison
 */
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

    /**
     * Sets the map of MenuItem to quantity, representing the items ordered and their quantity for that sale.
     *
     * @param itemsOrdered A map of MenuItem to integer.
     */
    void setItemsOrdered(Map<MenuItem, Integer> itemsOrdered);

    /**
     * Sets the date of the recorded sale.
     *
     * @param date The {@link LocalDate} of the sale.
     */
    void setDate(LocalDate date);

    /**
     * Sets the time of the recorded sale.
     *
     * @param time the {@link LocalTime} of the sale.
     */
    void setTime(LocalTime time);

    /**
     * Sets the payment type of the recorded sale.
     *
     * @param paymentType The {@link PaymentType} of the sale.
     */
    void setPaymentType(PaymentType paymentType);

    /**
     * Sets the notes of the recorded sale.
     *
     * @param notes Notes regarding the sale, as a String.
     */
    void setNotes(String notes);

    /**
     * Sets the total price of the sale.
     *
     * @param totalPrice The total price of the sale, as a {@link Money} object.
     */
    void setTotalPrice(Money totalPrice);

    /**
     * @return The integer receipt number.
     */
    int getReceiptNumber();

    /**
     * @return A map of MenuItem to Integer, representing how many of which item was ordered in that sale.
     */
    Map<MenuItem, Integer> getItemsOrdered();

    /**
     * @return The {@link LocalDate} of the sale.
     */
    LocalDate getDate();

    /**
     * @return The {@link LocalTime} of the sale.
     */
    LocalTime getTime();

    /**
     * @return The {@link PaymentType} of the sale.
     */
    PaymentType getPaymentType();

    /**
     * @return The String of notes relating to the sale.
     */
    String getNotes();

    /**
     * @return The total price of the sale, as a {@link Money} object.
     */
    Money getTotalPrice();
}
