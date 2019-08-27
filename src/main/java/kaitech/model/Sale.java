package kaitech.model;

import kaitech.util.PaymentType;
import org.joda.money.Money;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Observable;

/**
 * A class that represents a sales record. A sales record is created whenever
 * an order is made. It contains important details about the sale that is
 * useful for data collection.
 */
public class Sale extends Observable {
    /**
     * Receipt number.
     */
    private int receiptNumber;

    /**
     * A map from the MenuItems ordered to the integer quantities.
     */
    private Map<MenuItem, Integer> itemsOrdered;

    /**
     * The date the order was taken.
     */
    private LocalDate date;

    /**
     * The time the order was taken.
     */
    private LocalTime time;

    /**
     * The method of payment used.
     */
    private PaymentType paymentType;

    /**
     * Any additional notes that the employee who took the order has left.
     */
    private String notes;

    /**
     * The total price of the order.
     */
    private Money totalPrice;

    /**
     * A run-of-the-mill constructor, except the Business is added as an observer and is notified with the Map of
     * MenuItems ordered.
     *
     * @param itemsOrdered The Map of MenuItems ordered
     * @param date         The Date of purchase
     * @param time         The Time of purchase
     * @param paymentType  The PaymentType value showing the method of payment
     * @param notes        A String where the cashier can leave any additional comments
     * @param totalPrice   An int representing the total cost of the sale
     */
    public Sale(Map<MenuItem, Integer> itemsOrdered, LocalDate date, LocalTime time, PaymentType paymentType,
                String notes, Money totalPrice, Business business) {
        this.receiptNumber = -1; // -1 implies the receipt number has not yet been assigned by the database
        this.itemsOrdered = itemsOrdered;
        this.date = date;
        this.time = time;
        this.paymentType = paymentType;
        this.notes = notes;
        this.totalPrice = totalPrice;
        addObserver(business);
        setChanged();
        notifyObservers(itemsOrdered);
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setTotalPrice(Money totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * Calculates the total cost of the order thus far, returning this amount. This is static such that the total can
     * be calculated before confirming the order and making it a Sale object, in case the order is changed or cancelled.
     * @param order A map of MenuItems to Integer quantities
     * @return A Money amount
     */
    public static Money calculateTotalCost(Map<MenuItem, Integer> order) {
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
     * @param total The total cost of the order
     * @param amountPaid The amount paid by the customer
     * @return A Money object showing the change
     * @throws IllegalArgumentException If the amount paid is insufficient
     */
    public static Money calculateChange(Money total, Money amountPaid) throws IllegalArgumentException{
        if (amountPaid.isGreaterThan(total) || amountPaid.isEqual(total)) {
            return amountPaid.minus(total);
        }
        else {
            throw new IllegalArgumentException("The amount paid is not enough.");
        }
    }
}
