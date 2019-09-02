package kaitech.model;

import kaitech.api.model.Business;
import kaitech.api.model.MenuItem;
import kaitech.api.model.Sale;
import kaitech.util.PaymentType;
import org.joda.money.Money;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Observable;

/**
 * SaleImpl implements the Sale interface, and is used to store details about a sale which has occurred.
 */
public class SaleImpl extends Observable implements Sale {
    /**
     * Receipt number.
     */
    protected int receiptNumber;

    /**
     * A map from the MenuItems ordered to the integer quantities.
     */
    protected Map<MenuItem, Integer> itemsOrdered;

    /**
     * The date the order was taken.
     */
    protected LocalDate date;

    /**
     * The time the order was taken.
     */
    protected LocalTime time;

    /**
     * The method of payment used.
     */
    protected PaymentType paymentType;

    /**
     * Any additional notes that the employee who took the order has left.
     */
    protected String notes;

    /**
     * The total price of the order.
     */
    protected Money totalPrice;

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
    public SaleImpl(Map<MenuItem, Integer> itemsOrdered, LocalDate date, LocalTime time, PaymentType paymentType,
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

    @Override
    public void setItemsOrdered(Map<MenuItem, Integer> itemsOrdered) {
        this.itemsOrdered = itemsOrdered;
    }

    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    @Override
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public void setTotalPrice(Money totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public int getReceiptNumber() {
        return receiptNumber;
    }

    @Override
    public Map<MenuItem, Integer> getItemsOrdered() {
        return itemsOrdered;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public LocalTime getTime() {
        return time;
    }

    @Override
    public PaymentType getPaymentType() {
        return paymentType;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public Money getTotalPrice() {
        return totalPrice;
    }
}
